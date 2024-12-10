package com.bot.slack.command.test;

import com.bot.slack.command.base.BasicCommand;
import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import org.springframework.stereotype.Component;
import java.io.IOException;


@Component
public class TestCommand extends BasicCommand {
    public TestCommand() {
        this.commandName = "/test";
    }

    @Override
    public Response handle(SlashCommandRequest req, SlashCommandContext ctx) {
        try {
            ctx.client().chatPostMessage(r -> r
                .channel(req.getPayload().getChannelId()) // メッセージを送信するチャンネルID
                .text("test") // 返信するメッセージ内容
            );
        } catch (IOException e) {
            e.printStackTrace();
            return ctx.ack("failed by IOException");

        } catch (SlackApiException e) {
            e.printStackTrace();
            return ctx.ack("failed by SlackApiException");
        }
        return ctx.ack();
    }
}
