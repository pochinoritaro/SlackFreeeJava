package com.bot.slack.command.paidholiday.approve;

import com.bot.slack.command.base.ModalCommand;
import com.bot.slack.entity.paidholiday.HolidayRquestStatus;
import com.bot.slack.service.PaidHolidayService;
import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.response.views.ViewsOpenResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.composition.BlockCompositions;
import com.slack.api.model.block.composition.OptionObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.element.CheckboxesElement;
import com.slack.api.model.view.View;
import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.view.Views.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class PaidHolidayApprobeCommand extends ModalCommand {

    @Autowired
    private PaidHolidayService paidHolidayService;

    public PaidHolidayApprobeCommand() {
        this.commandName = "/approve";
        this.callBackId = "paidHolidayApprobe";
    }

    @Override
    public Response handle(SlashCommandRequest req, SlashCommandContext ctx) {
        try {
            ViewsOpenResponse response = ctx.client().viewsOpen(r -> r
                .triggerId(req.getPayload().getTriggerId())
                .view(createView(req, ctx)));

            if (!response.isOk()) {
                return ctx.ack(":x: モーダルの表示に失敗しました: " + response.getError());
    
            }
            return ctx.ack();

        } catch (Exception e) {
            e.printStackTrace();
            return ctx.ack();

        }
    }

    @Override
    protected View createView(SlashCommandRequest req, SlashCommandContext ctx) {
        RequestUserBuilder users = new RequestUserBuilder();
        List<OptionObject> holidayOptions = paidHolidayService.getAllHolidayRequestByStatus(HolidayRquestStatus.PENDING).stream()
            .map(d -> users.buildOption(d.getRequestDate().toString(), d.getAppUser().getRealName(), d.getRequestReason(), d.getId()))
                .collect(Collectors.toList());

        return view(view -> view
            .callbackId(getCallbackId())
            .type("modal")
            .notifyOnClose(false)
            .title(viewTitle(title -> title.type("plain_text").text("有給承認").emoji(true)))
            .submit(viewSubmit(submit -> submit.type("plain_text").text("承認").emoji(true)))
            .close(viewClose(close -> close.type("plain_text").text("閉じる").emoji(true)))
            .blocks(asBlocks(
                Blocks.actions(actions -> actions
                    .elements(Arrays.asList(
                        CheckboxesElement.builder()
                            .actionId("requestUsers")
                            .options(holidayOptions).build()
                            )
                        )
                    )
                )
            )
        );
    }
}

class RequestUserBuilder {

    public OptionObject buildOption(String requestDate, String realName, String requestReason, long index) {
        return BlockCompositions.option(o -> o
                .text(PlainTextObject.builder()
                        .text(
                            String.format(
                                        "取得申請日: %s", 
                                        requestDate)
                        )
                        .emoji(true)
                        .build())

                .description(PlainTextObject.builder()
                        .text(String.format("申請者 :%s\n申請理由: %s", 
                                realName, requestReason))
                        .emoji(true)
                        .build())
                .value(String.valueOf(index)));
    }
}