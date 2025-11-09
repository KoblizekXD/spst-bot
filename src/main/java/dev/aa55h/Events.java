package dev.aa55h;

import dev.aa55h.util.Database;
import dev.aa55h.util.Environment;
import dev.aa55h.util.Memcached;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Objects;

public final class Events {
    private static final Logger log = LoggerFactory.getLogger(Events.class);

    private Events() {}
    
    public static void onSuccessfulVerification(JDA jda, Member member, String email, String code) {
        log.info("User {} has been successfully verified.", member.getUser().getAsTag());
        Database.addUser(member.getId(), email, code);
        Memcached.getInstance().invalidateCode(member.getId());
        jda.getTextChannelById(Environment.LOG_CHANNEL_ID).sendMessageEmbeds(new EmbedBuilder()
                        .setFooter(member.getEffectiveName(), member.getUser().getAvatarUrl())
                        .setTitle("Uživatel verifikován")
                        .addField("Uživatel", member.getUser().getEffectiveName(), false)
                        .addField("Email", email, false)
                        .addField("Kód", code, false)
                        .addField("ID uživatele", member.getId(), false)
                        .setColor(Color.GREEN)
                .build()).queue();
        Guild guild = member.getGuild();
        Objects.requireNonNull(guild);
        if (Environment.UNVERIFIED_ROLE_ID != null)
            guild.removeRoleFromMember(member.getUser(), guild.getRoleById(Environment.UNVERIFIED_ROLE_ID)).queue();
        if (Environment.VERIFIED_ROLE_ID != null)
            guild.addRoleToMember(member.getUser(), guild.getRoleById(Environment.VERIFIED_ROLE_ID)).queue();
    }
    
    public static void onVerificationEmailSent(JDA jda, Member member, String email, String code) {
        log.info("Verification email sent to user {}.", member.getUser().getAsTag());
        jda.getTextChannelById(Environment.LOG_CHANNEL_ID).sendMessageEmbeds(new EmbedBuilder()
                        .setFooter(member.getEffectiveName(), member.getUser().getAvatarUrl())
                        .setTitle("Odeslán verifikační email")
                        .addField("Uživatel", member.getUser().getEffectiveName(), false)
                        .addField("Email", email, false)
                        .addField("Kód", code, false)
                        .addField("ID uživatele", member.getId(), false)
                        .setColor(Color.BLUE)
                .build()).queue();
    }
    
    public static void onUnsuccessfulEmailSent(JDA jda, Member member, String email, String reason) {
        log.info("Failed to send verification email to user {}.", member.getUser().getAsTag());
        jda.getTextChannelById(Environment.LOG_CHANNEL_ID).sendMessageEmbeds(new EmbedBuilder()
                        .setFooter(member.getEffectiveName(), member.getUser().getAvatarUrl())
                        .setTitle("Chyba při odesílání verifikačního emailu")
                        .addField("Uživatel", member.getUser().getEffectiveName(), false)
                        .addField("ID uživatele", member.getId(), false)
                        .addField("Email", email, false)
                        .addField("Důvod", reason, false)
                        .setColor(Color.RED)
                .build()).queue();
        
    }
}
