package dev.aa55h.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class AbstractCommand extends ListenerAdapter {
    protected final JDA jda;

    protected AbstractCommand(JDA jda) {
        this.jda = jda;
    }
    
    public abstract String getName();
    public abstract SlashCommandData create();
    public abstract void handle(SlashCommandInteractionEvent event);
}
