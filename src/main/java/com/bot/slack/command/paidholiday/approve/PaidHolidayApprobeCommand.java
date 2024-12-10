package com.bot.slack.command.paidholiday.approve;

import com.bot.slack.command.base.ModalCommand;
import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.response.views.ViewsOpenResponse;
import org.springframework.stereotype.Component;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.composition.BlockCompositions;
import com.slack.api.model.block.composition.OptionObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.element.CheckboxesElement;
import com.slack.api.model.view.View;
import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.view.Views.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
public class PaidHolidayApprobeCommand extends ModalCommand {
    public PaidHolidayApprobeCommand() {
        this.commandName = "/approbe";
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
                            .options(users.buildOptions()).build()
                            )
                        )
                    )
                )
            )
        );
    }
}

class RequestUserBuilder {

    private OptionObject buildOption(String requestDate, String userName, int index) {
        StringBuilder dateTemp = new StringBuilder().append("取得申請日: ");
        StringBuilder userTemp = new StringBuilder().append("申請者: ");
        dateTemp.append(requestDate);
        userTemp.append(userName);

        return BlockCompositions.option(o -> o
                .text(PlainTextObject.builder()
                        .text(dateTemp.toString())
                        .emoji(true)
                        .build())
                .description(PlainTextObject.builder()
                        .text(userTemp.toString())
                        .emoji(true)
                        .build())
                .value(String.valueOf(index)));
    }

    public List<OptionObject> buildOptions() {
        String[] users = new String[4];
        String date = "2024/12/03";
        List<OptionObject> options = new ArrayList<>();

        users[0] = "田中";
        users[1] = "サトウ";
        users[2] = "高橋";
        users[3] = "角森";

        int index = 0;
        for (String user : users) {
            options.add(buildOption(date, user, index));
            index++;
        }
        return options;
    }
}