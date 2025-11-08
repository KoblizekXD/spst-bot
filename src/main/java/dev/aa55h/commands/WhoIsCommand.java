package dev.aa55h.commands;

import dev.aa55h.util.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;

public class WhoIsCommand extends AbstractCommand {
    public WhoIsCommand(JDA jda) {
        super(jda);
    }

    @Override
    public String getName() {
        return "whois";
    }

    @Override
    public SlashCommandData create() {
        return Commands.slash("whois", "Zobrazí informace o uživateli.")
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                .setContexts(InteractionContextType.GUILD)
                .addOption(OptionType.USER, "user", "Uživatel, o kterém chcete získat informace.", true);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        net.dv8tion.jda.api.entities.User discordUser = event.getOption("user").getAsUser();
        Database.UserEntry user = Database.getUser(discordUser.getId());
        if (user != null) {
            event.replyEmbeds(new EmbedBuilder()
                            .setColor(Color.CYAN)
                            .setTitle(discordUser.getEffectiveName())
                            .setThumbnail(discordUser.getAvatarUrl())
                            .addField("Discord ID", discordUser.getId(), false)
                            .addField("Kód užit pro ověření", user.code(), false)
                            .addField("E-mail", user.email(), false)
                            .addField("Registrován dne", user.verifiedAt(), false)
                    .build()).setEphemeral(true).queue();
        } else {
            event.reply("Uživatel nebyl nalezen v databázi.").setEphemeral(true).queue();
        }
    }
}
