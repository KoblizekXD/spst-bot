package dev.aa55h.commands;

import dev.aa55h.util.Database;
import dev.aa55h.util.Environment;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.List;

public class FixRolesCommand extends AbstractCommand {
    public FixRolesCommand(JDA jda) {
        super(jda);
    }

    @Override
    public String getName() {
        return "fix-roles";
    }

    @Override
    public SlashCommandData create() {
        return Commands.slash("fix-roles", "Opravit role ověřeným členům(dle databáze).")
                .setContexts(InteractionContextType.GUILD)
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        List<Member> serverVerified = event.getGuild().getMembersWithRoles(event.getGuild().getRoleById(Environment.VERIFIED_ROLE_ID));
        List<String> dbVerified = Database.getAll();
        int count = 0;
        for (Member member : serverVerified) {
            if (!dbVerified.contains(member.getId())) {
                event.getGuild().removeRoleFromMember(member, event.getGuild().getRoleById(Environment.VERIFIED_ROLE_ID)).queue();
                event.getGuild().addRoleToMember(member, event.getGuild().getRoleById(Environment.UNVERIFIED_ROLE_ID)).queue();
                count++;
            }
        }
        event.reply("Opraveny role pro " + count + " členů.").queue();
    }
}
