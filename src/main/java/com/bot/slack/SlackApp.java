package com.bot.slack;

import java.util.List;
import com.bot.slack.command.base.BasicCommand;
import com.bot.slack.command.base.CommandCallback;
import com.bot.slack.command.base.ModalCommand;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;

@Configuration
@ComponentScan("com.bot.slack")
public class SlackApp {

    private static final Logger logger = LoggerFactory.getLogger(SlackApp.class);

    @Autowired
    private List<BasicCommand> basicCommands;

    @Autowired
    private List<ModalCommand<? extends CommandCallback>> modalCommands;

    // If you would like to run this app for a single workspace,
    // enabling this Bean factory should work for you.
    @Bean
    public AppConfig loadSingleWorkspaceAppConfig() {
        return AppConfig.builder()
            .singleTeamBotToken(System.getenv("SLACK_BOT_TOKEN"))
            .signingSecret(System.getenv("SLACK_SIGNING_SECRET"))
            .build();
    }

    // If you would like to run this app for multiple workspaces,
    // enabling this Bean factory should work for you.
    // @Bean
    public AppConfig loadOAuthConfig() {
        return AppConfig.builder()
            .singleTeamBotToken(null)
            .clientId(System.getenv("SLACK_CLIENT_ID"))
            .clientSecret(System.getenv("SLACK_CLIENT_SECRET"))
            .signingSecret(System.getenv("SLACK_SIGNING_SECRET"))
            .scope("app_mentions:read,channels:history,channels:read,chat:write")
            .oauthInstallPath("/slack/install")
            .oauthRedirectUriPath("/slack/oauth_redirect")
            .build();
    }

    @Bean
    public App initSlackApp(AppConfig config) {
        App app = new App(config);
        if (config.getClientId() != null) {
            app.asOAuthApp(true);
        }

        // BasicCommandのみを処理
        basicCommands.stream()
            .filter(command -> !(command instanceof ModalCommand))
                .forEach(command -> {
                    app.command(command.getCommandName(), command::handle);
                    logger.info("Basic Command Registered: " + command.getCommandName());
            });

        // ModalCommandのみを処理
        modalCommands.forEach(command -> {
            app.command(command.getCommandName(), command::handle);
            logger.info("Modal Command Registered: " + command.getCommandName());
            if (command.getCallback() != null) {
                app.viewSubmission(command.getCallbackId(), command.getCallback()::handle);
                logger.info(String.format(
                    String.format("The callback for command %s has been registered. Callback ID: %s", command.getCommandName(), command.getCallbackId())));
            }
        });
        
        return app;
    }
}