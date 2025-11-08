package dev.aa55h;

import dev.aa55h.commands.*;
import dev.aa55h.util.Database;
import dev.aa55h.util.Environment;
import dev.aa55h.util.Memcached;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Bot extends ListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(Bot.class);
    private final AbstractCommand[] commands;
    private final JDA jda;

    public Bot(AbstractCommand[] commands, JDA jda) {
        this.commands = commands;
        this.jda = jda;
    }
    
    public static void main(String[] args) throws InterruptedException {
        Database.connect();
        JDA api = JDABuilder.createDefault(Environment.TOKEN)
                .build()
                .awaitReady();

        AbstractCommand[] commands = {
                new VerifyCommand(api),
                new CodeCommand(api),
                new UnverifyCommand(api),
                new FixRolesCommand(api),
                new WhoIsCommand(api)
        };
        
        api.addEventListener(new Bot(commands, api));
        api.updateCommands().addCommands(Arrays.stream(commands).map(AbstractCommand::create).toList()).queue();
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        Memcached.getInstance().shutdown();
        Database.disconnect();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        for (AbstractCommand command : commands) {
            if (event.getName().equals(command.getName())) {
                command.handle(event);
                return;
            }
        }
    }
    
    public static void checkEnv() {
        if (Environment.TOKEN == null) log.warn("TOKEN is not set");
        if (Environment.POSTGRES_DB == null) log.warn("POSTGRES_DB is not set");
        if (Environment.MEMCACHED_HOST == null) log.warn("MEMCACHED_URL is not set");
        if (Environment.LOG_CHANNEL_ID == null) log.warn("LOG_CHANNEL_ID is not set");
        if (Environment.VERIFIED_ROLE_ID == null) log.warn("VERIFIED_ROLE_ID is not set");
        if (Environment.UNVERIFIED_ROLE_ID == null) log.warn("UNVERIFIED_ROLE_ID is not set");
        if (Environment.GMAIL_OAUTH_TOKEN == null) log.warn("GMAIL_OAUTH_TOKEN is not set");
        if (Environment.CHANNEL_VERIFICATION_ID == null) log.warn("CHANNEL_VERIFICATION_ID is not set");
    }
}
