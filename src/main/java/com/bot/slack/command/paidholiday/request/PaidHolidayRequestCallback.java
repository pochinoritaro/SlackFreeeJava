package com.bot.slack.command.paidholiday.request;

import java.util.Map;

import org.springframework.stereotype.Component;
import com.bot.slack.command.base.CommandCallback;
import com.slack.api.bolt.context.builtin.ViewSubmissionContext;
import com.slack.api.bolt.request.builtin.ViewSubmissionRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.model.view.ViewState.Value;


@Component
public class PaidHolidayRequestCallback extends CommandCallback {
    public Response handle(ViewSubmissionRequest req, ViewSubmissionContext ctx) {
        Map<String, Map<String, Value>> requestValues = req.getPayload().getView().getState().getValues();
        Value requestDate = requestValues.get("requestDate").get("date");
        Value requestReason = requestValues.get("requestReason").get("reason");
        System.out.println(requestDate.getSelectedDate());
        System.out.println(requestReason.getValue());
        return ctx.ack();
    }
}
