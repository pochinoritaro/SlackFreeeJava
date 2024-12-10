package com.bot.slack.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.slack.api.bolt.App;

@Component
public class ScheduledTask {

    private final App app;

    public ScheduledTask(App app) {
        this.app = app;
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void performScheduledTask() {
        // Slack Appの処理を実行
        try {
            app.client().chatPostMessage(r -> r
                    .channel("bot") // 投稿先チャンネルID
                    .text("Hello from Scheduled Task!")); // 投稿するメッセージ
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
