package com.bot.slack.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bot.slack.entity.user.AppUser;
import com.bot.slack.repository.AppUserRepository;


@Service
public class AppUserService {
    @Autowired
    private AppUserRepository appUserRepository;

    public Optional<AppUser> getUserBySlackId(String slackId) {
        return Optional.ofNullable(appUserRepository.findBySlackId(slackId));
    }

    public AppUser createUser(AppUser user) {
        return appUserRepository.save(user);
    }
}
