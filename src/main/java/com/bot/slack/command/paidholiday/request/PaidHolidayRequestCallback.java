package com.bot.slack.command.paidholiday.request;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bot.slack.command.base.CommandCallback;
import com.bot.slack.entity.user.AppUser;
import com.bot.slack.service.AppUserService;
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
    AppUserService appUserService;

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
            userData = setUserOnDB(userId, employeeId, userEmail);
            ctx.logger.info("User data has been successfully registered.");
        }

        System.out.println(userData.get());

        Map<String, Map<String, Value>> requestValues = req.getPayload().getView().getState().getValues();
        Value requestDate = requestValues.get("requestDate").get("date");
        Value requestReason = requestValues.get("requestReason").get("reason");
        System.out.println(requestDate.getSelectedDate());
        System.out.println(requestReason.getValue());
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


    private Optional<AppUser> setUserOnDB(String slackId, String employeeId, String userEmail) {
        AppUser userData = new AppUser();
        userData.setSlackId(slackId);
        userData.setEmployeeId(employeeId);
        userData.setEmail(userEmail);
        return Optional.of(appUserService.createUser(userData));
    }
}
