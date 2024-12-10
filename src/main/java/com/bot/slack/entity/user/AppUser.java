package com.bot.slack.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;

import java.util.List;
import java.util.UUID;

import com.bot.slack.entity.paidholiday.PaidHoliday;


@Entity
@IdClass(value = AppUserId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @Column(columnDefinition = "UUID", nullable = false, updatable = false)
    private UUID id = UUID.randomUUID();

    @Id
    @Column(nullable = false, updatable = false)
    private String slackId;

    private String employeeId;

    private String email;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaidHoliday> paidHoliday;
}
