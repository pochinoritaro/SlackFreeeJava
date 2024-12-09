package com.bot.slack.command.base;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;

public abstract class BasicCommand {
    protected String commandName;

    public abstract Response handle(SlashCommandRequest req, SlashCommandContext ctx);

    public final String getCommandName() {
        return commandName;
    };
}
