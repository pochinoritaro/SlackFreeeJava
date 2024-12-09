package com.bot.slack.command.base;

import com.slack.api.bolt.context.builtin.ViewSubmissionContext;
import com.slack.api.bolt.request.builtin.ViewSubmissionRequest;
import com.slack.api.bolt.response.Response;

public abstract class CommandCallback {
        public abstract Response handle(ViewSubmissionRequest req, ViewSubmissionContext ctx);
}
