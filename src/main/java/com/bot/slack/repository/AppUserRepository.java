package com.bot.slack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bot.slack.entity.user.AppUser;
import com.bot.slack.entity.user.AppUserId;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, AppUserId> {
    AppUser findBySlackId(String slackId);
}
