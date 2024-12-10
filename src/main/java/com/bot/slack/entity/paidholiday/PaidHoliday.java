package com.bot.slack.entity.paidholiday;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import com.bot.slack.entity.user.AppUser;
import jakarta.persistence.Column;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaidHoliday {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    private HolidayRquestStatus requestStatus;

    private String requestReason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "app_user_id", referencedColumnName = "id"),
        @JoinColumn(name = "app_slack_id", referencedColumnName = "slackId")
    })
    private AppUser appUser;
}
