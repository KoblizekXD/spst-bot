package dev.aa55h.commands;

import dev.aa55h.util.Database;
import dev.aa55h.util.Environment;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FixRolesCommand extends AbstractCommand {
    private static final Logger log = LoggerFactory.getLogger(FixRolesCommand.class);

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
        event.deferReply(true).queue();

        Guild guild = event.getGuild();
        if (guild == null) {
            event.getHook().sendMessage("This command can only be used in a server.").queue();
            return;
        }

        Role verifiedRole = guild.getRoleById(Environment.VERIFIED_ROLE_ID);
        Role unverifiedRole = guild.getRoleById(Environment.UNVERIFIED_ROLE_ID);

        if (verifiedRole == null || unverifiedRole == null) {
            event.getHook().sendMessage("Required roles were not found on this server.").queue();
            return;
        }

        List<Member> serverVerified = guild.getMembersWithRoles(verifiedRole);
        Set<String> dbVerified = new HashSet<>(Database.getAll());

        log.info("Fixing roles for {} verified members on server, {} verified in database.",
                serverVerified.size(), dbVerified.size());

        int count = 0;
        for (Member member : serverVerified) {
            if (!dbVerified.contains(member.getUser().getId())) {
                guild.removeRoleFromMember(member, verifiedRole)
                        .and(guild.addRoleToMember(member, unverifiedRole))
                        .queue();
                count++;
            }
        }

        event.getHook().sendMessage("Opraveny role pro " + count + " členů.").queue();
    }

}
