package dev.aa55h.commands;

import dev.aa55h.Events;
import dev.aa55h.util.Environment;
import dev.aa55h.util.Memcached;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CodeCommand extends AbstractCommand {
    public CodeCommand(JDA jda) {
        super(jda);
    }

    @Override
    public String getName() {
        return "code";
    }

    @Override
    public SlashCommandData create() {
        return Commands.slash("code", "Použij pro zadání ověřovacího kódu zaslaného na tvůj e-mail.")
                .addOption(OptionType.STRING, "code", "Ověřovací kód zaslaný na tvůj e-mail.", true);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        if (event.getMember().getUser().isBot()
                || !event.getChannelId().equals(Environment.CHANNEL_VERIFICATION_ID)) return;
        String code = event.getOption("code").getAsString();
        String codeData = Memcached.getInstance().getCodeData(event.getMember().getId());
        if (codeData == null) {
            event.reply("Neplatný nebo expirovaný kód! Zkontroluj svůj e-mail a zkus to znovu.").setEphemeral(true).queue();
            return;
        }
        String[] split = codeData.split("\\|");
        if (!split[0].equals(code)) {
            event.reply("Neplatný kód! Zkontroluj svůj e-mail a zkus to znovu.").setEphemeral(true).queue();
            return;
        }
        String email = split[1];
        long id = Long.parseLong(split[2]);
        if (event.getMember().getIdLong() != id) {
            event.reply("Tento kód nepatří to tebe! Ujisti se, že používáš správný kód zaslaný na tvůj e-mail.").setEphemeral(true).queue();
            return;
        }
        event.reply("Ověření úspěšné! Tvůj e-mail " + email + " byl spojen s tvým Discord účtem.").setEphemeral(true).queue();
        Events.onSuccessfulVerification(jda, event.getMember(), email, code);
    }
}
