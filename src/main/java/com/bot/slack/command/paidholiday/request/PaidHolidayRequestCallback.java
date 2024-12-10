package com.bot.slack.command.paidholiday.request;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bot.slack.command.base.CommandCallback;
import com.bot.slack.entity.paidholiday.HolidayRquestStatus;
import com.bot.slack.entity.paidholiday.PaidHoliday;
import com.bot.slack.entity.user.AppUser;
import com.bot.slack.service.AppUserService;
import com.bot.slack.service.PaidHolidayService;
import com.slack.api.bolt.context.builtin.ViewSubmissionContext;
import com.slack.api.bolt.request.builtin.ViewSubmissionRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.users.UsersInfoRequest;
import com.slack.api.methods.response.users.UsersInfoResponse;
import com.slack.api.model.view.ViewState.Value;


@Component
public class PaidHolidayRequestCallback extends CommandCallback {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PaidHolidayService paidHolidayService;

    public Response handle(ViewSubmissionRequest req, ViewSubmissionContext ctx) {
        ctx.logger.info("/modal command start.");
        ctx.logger.debug("Get user email addr by slack user id.");

        String userId = req.getPayload().getUser().getId();

        Optional<AppUser> userData = appUserService.getUserBySlackId(userId);

        if (userData.isEmpty()) {
            ctx.logger.info("The applying user is not registered in the Slack application.");
            Optional<UsersInfoResponse> slackUser = getUserByDB(ctx, userId);

            if (slackUser.isEmpty()) {
                ctx.logger.error(String.format("slack user is not found. Slack User ID: ", userId));
                return ctx.ack();
            }
            
            String employeeId = "123456789";
            String userEmail = slackUser.get().getUser().getProfile().getEmail();
            String realName = slackUser.get().getUser().getProfile().getRealName();
            userData = setUserOnDB(userId, employeeId, userEmail, realName);
            ctx.logger.info("User data has been successfully registered.");
        }

        System.out.println(userData.get());

        Map<String, Map<String, Value>> requestValues = req.getPayload().getView().getState().getValues();
        Optional<LocalDateTime> requestDate = convertLocalDateTime(requestValues.get("requestDate").get("date").getSelectedDate());
        String requestReason = requestValues.get("requestReason").get("reason").getValue();

        if (requestDate.isEmpty()) {
            ctx.logger.error("Failed to parse LocalDateTime.");
            return ctx.ack();
        } 

        Optional<PaidHoliday> paidHoliday = setPaidHolidayOnDB(userData.get(), requestDate.get(), requestReason);
        paidHoliday.ifPresentOrElse(
            value -> ctx.logger.info("Data insertion was successful."),
            () -> ctx.logger.error("Failed to insert data.")
        );

        ctx.logger.info("/modal command end.");
        return ctx.ack();
    }
    
    private Optional<UsersInfoResponse> getUserByDB(ViewSubmissionContext ctx, String slackId) {
        UsersInfoRequest usersInfoRequest = UsersInfoRequest.builder().user(slackId).build();

        try {
            UsersInfoResponse usersInfoResponse = ctx.client().usersInfo(usersInfoRequest);
            return Optional.ofNullable(usersInfoResponse);

        } catch (IOException e) {
            ctx.logger.error("throw IoException.", e);

        } catch (SlackApiException e) {
            ctx.logger.error("throw IoException.", e);
        }

        return Optional.empty();
    }


    private Optional<AppUser> setUserOnDB(String slackId, String employeeId, String userEmail, String displayName) {
        AppUser userData = new AppUser();
        userData.setSlackId(slackId);
        userData.setEmployeeId(employeeId);
        userData.setEmail(userEmail);
        userData.setRealName(displayName);
        return Optional.of(appUserService.createUser(userData));
    }

    private Optional<PaidHoliday> setPaidHolidayOnDB(AppUser userData, LocalDateTime requestDate, String requestReason) {
        PaidHoliday paidHoliday = new PaidHoliday();
        paidHoliday.setAppUser(userData);
        paidHoliday.setRequestDate(requestDate);
        paidHoliday.setRequestReason(requestReason);
        paidHoliday.setRequestStatus(HolidayRquestStatus.PENDING);
        //TODO: 作成日を自動挿入できるようにする。
        paidHoliday.setCreateAt(LocalDateTime.now());
        //TODO: 更新日を自動挿入できるようにする。
        paidHoliday.setUpdateAt(LocalDateTime.now());
        return Optional.of(paidHolidayService.createPaidHoliday(paidHoliday));
    }

    private Optional<LocalDateTime> convertLocalDateTime(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date formatDate = null;
        try {
            formatDate = dateFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        if (formatDate != null) {
            return Optional.ofNullable(LocalDateTime.ofInstant(formatDate.toInstant(), ZoneId.systemDefault()));

        } else {
            return Optional.empty();
        }
    }
}
