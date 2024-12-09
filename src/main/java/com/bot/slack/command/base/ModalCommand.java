package com.bot.slack.command.base;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.model.view.View;

public abstract class ModalCommand<C extends CommandCallback> extends BasicCommand {
    protected String callBackId;
    protected C callBack;

    protected abstract View createView(SlashCommandRequest req, SlashCommandContext ctx);

    public final String getCallbackId() {
        return callBackId;
    };

    public final C getCallback() {
        return callBack;
    };
}
