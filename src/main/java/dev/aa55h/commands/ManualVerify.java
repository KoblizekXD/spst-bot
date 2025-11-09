package dev.aa55h.commands;

import dev.aa55h.util.Database;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class ManualVerify extends AbstractCommand {
    public ManualVerify(JDA jda) {
        super(jda);
    }

    @Override
    public String getName() {
        return "manual-verify";
    }

    @Override
    public SlashCommandData create() {
        return Commands.slash(getName(), "Manuálně ověřit uživatele.")
                .addOption(OptionType.USER, "user", "Uživatel k ověření.", true)
                .addOption(OptionType.STRING, "email", "Email uživatele.", true)
                .setContexts(InteractionContextType.GUILD)
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Database.addUser(event.getOption("user").getAsUser().getId(),
                event.getOption("email").getAsString(),
                "MANUAL");
    }
}
