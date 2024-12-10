package com.bot.slack.entity.user;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.UUID;


public class AppUserId implements Serializable {
    @Getter
    @Setter
    private UUID id;

    @Getter
    @Setter
    private String slackId;
}
