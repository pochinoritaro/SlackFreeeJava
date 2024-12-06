package com.bot.slack.command;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.methods.SlackApiException;

import java.io.IOException;

import org.springframework.stereotype.Component;


@Component
public class TestCommand {
    public static void handle(SlashCommandRequest req, SlashCommandContext ctx) {
        TestCommand.testCommandHandle(req, ctx);
    }

    public static void testCommandHandle(SlashCommandRequest req, SlashCommandContext ctx) {
        String userMessage = "Hello, this is a message from the bot!";
        System.out.println(userMessage);

        try {
            ctx.client().chatPostMessage(r -> r
                    .channel(req.getPayload().getChannelId()) // メッセージを送信するチャンネルID
                    .text(userMessage) // 返信するメッセージ内容
            );

        } catch (IOException e) {
            e.printStackTrace();

        } catch (SlackApiException e) {
            e.printStackTrace();

        }
    }
}
