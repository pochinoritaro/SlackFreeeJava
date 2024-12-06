package com.bot.slack.controller;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jakarta_servlet.SlackAppServlet;
import jakarta.servlet.annotation.WebServlet;


@WebServlet("/slack/events")
public class SlackEventController extends SlackAppServlet {
    public SlackEventController(App app) {
        super(app);
    }
}