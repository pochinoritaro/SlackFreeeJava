package com.bot.slack.command.paidholiday.request;

import com.bot.slack.command.base.ModalCommand;
import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.response.views.ViewsOpenResponse;
import org.springframework.stereotype.Component;
import com.slack.api.model.view.View;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import static com.slack.api.model.block.element.BlockElements.*;
import static com.slack.api.model.view.Views.*;
import java.time.LocalDate;


@Component
public class PaidHolidayRequestCommand extends ModalCommand<PaidHolidayRequestCallback> {
    public PaidHolidayRequestCommand() {
        this.commandName = "/modal";
        this.callBackId = "paidHolidayRequest";
        this.callBack = new PaidHolidayRequestCallback();
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
        return view(view -> view
            .callbackId(getCallbackId())
            .type("modal")
            .notifyOnClose(false)
            .title(viewTitle(title -> title.type("plain_text").text("有給申請").emoji(true)))
            .submit(viewSubmit(submit -> submit.type("plain_text").text("申請").emoji(true)))
            .close(viewClose(close -> close.type("plain_text").text("キャンセル").emoji(true)))
            .blocks(asBlocks(
                input(input -> input
                        .blockId("requestDate")
                        .element(datePicker(dp -> dp
                            .actionId("date")
                            .initialDate(LocalDate.now().toString())))
                        .label(plainText(pt -> pt.text("申請日").emoji(true)))
                ),
                input(input -> input
                        .blockId("requestReason")
                        .element(plainTextInput(pti -> pti
                            .actionId("reason")
                            .multiline(true)))
                        .label(plainText(pt -> pt.text("申請理由").emoji(true)))
                    )
                )
            )
        );
    }
}
