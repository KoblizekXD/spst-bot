package dev.aa55h.commands;

import dev.aa55h.util.Database;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class UnverifyCommand extends AbstractCommand {
    public UnverifyCommand(JDA jda) {
        super(jda);
    }

    @Override
    public String getName() {
        return "unverify";
    }

    @Override
    public SlashCommandData create() {
        return Commands.slash("unverify", "Zrušit ověření člena.")
                .setContexts(InteractionContextType.GUILD)
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                .addOption(OptionType.USER, "member", "Člen, jehož ověření chcete zrušit.", true);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        User member = event.getOption("member").getAsUser();
        Database.removeUser(member.getId());
        event.reply("Ověření člena " + member.getAsTag() + " bylo zrušeno.").queue();
    }
}
