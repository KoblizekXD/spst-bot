package dev.aa55h.commands;

import dev.aa55h.Events;
import dev.aa55h.util.Database;
import dev.aa55h.util.Environment;
import dev.aa55h.util.Memcached;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.regex.Pattern;

public class VerifyCommand extends AbstractCommand {

    public static final Pattern emailPattern = Pattern.compile("^[a-z]+\\.\\d{2}@spst\\.eu$");
    protected static final String[] blocked = {
            "rg2@spst.eu",
            "ama2@spst.eu",
            "amb2@spst.eu",
            "amb3@spst.eu",
            "ame1@spst.eu",
            "ame3@spst.eu",
            "amu2@spst.eu",
            "amz1@spst.eu",
            "amz2@spst.eu",
            "amz3@spst.eu",
            "auk1@spst.eu",
            "auk2@spst.eu",
            "auk4@spst.eu",
            "aut1@spst.eu",
            "aut1r@spst.eu",
            "aut4r@spst.eu",
            "ctvrtaci@spst.eu",
            "ctvrtacir@spst.eu",
            "ele1r@spst.eu",
            "ena1@spst.eu",
            "ena2@spst.eu",
            "ena3@spst.eu",
            "enb2@spst.eu",
            "enb3@spst.eu",
            "ene1@spst.eu",
            "ene1r@spst.eu",
            "ene2r@spst.eu",
            "ene3@spst.eu",
            "ene4@spst.eu",
            "ene4r@spst.eu",
            "ers1@spst.eu",
            "ers2@spst.eu",
            "ers3@spst.eu",
            "it@spst.eu",
            "ita1@spst.eu",
            "ita1r@spst.eu",
            "ita2@spst.eu",
            "ita3@spst.eu",
            "ita4@spst.eu",
            "ita4r@spst.eu",
            "itb1@spst.eu",
            "pdiddy@spst.eu",
            "itb2r@spst.eu",
            "itb3@spst.eu",
            "itb3sk1@spst.eu",
            "kel1@spst.eu",
            "mau4@spst.eu",
            "mel1@spst.eu",
            "mel4r@spst.eu",
            "meo1@spst.eu",
            "meo1r@spst.eu",
            "meo2r@spst.eu",
            "meo3@spst.eu",
            "mer3@spst.eu",
            "meu1@spst.eu",
            "meu1r@spst.eu",
            "meu3@spst.eu",
            "meu4@spst.eu",
            "mse1@spst.eu",
            "mse2@spst.eu",
            "mse4@spst.eu",
            "msk1@spst.eu",
            "msk2r@spst.eu",
            "msk3@spst.eu",
            "msk4r@spst.eu",
            "oko1@spst.eu",
            "oko2@spst.eu",
            "oko3@spst.eu",
            "oku3@spst.eu",
            "ozs1@spst.eu",
            "ozs3@spst.eu",
            "ozs3r@spst.eu",
            "ozu1r@spst.eu",
            "ozu2@spst.eu",
            "pod2@spst.eu",
            "psb2@spst.eu",
            "psb3@spst.eu",
            "rop_spst@spst.eu",
            "sel1@spst.eu",
            "sel3@spst.eu",
            "sel4@spst.eu",
            "smazat@spst.eu",
            "spst@spst.eu",
            "str2r@spst.eu",
            "str4@spst.eu",
            "studentispst@spst.eu",
            "tit1@spst.eu",
            "tit2@spst.eu",
            "tla1@spst.eu",
            "tla1r@spst.eu",
            "tla3@spst.eu",
            "tlb1r@spst.eu",
            "tlb3@spst.eu",
            "tlb3r@spst.eu",
            "tly3@spst.eu",
            "tly4@spst.eu",
            "tretaci@spst.eu",
            "tretacir@spst.eu",
            "uca1@spst.eu",
            "uce1@spst.eu",
            "itb4@spst.eu",
            "ama3@spst.eu",
            "ame1r@spst.eu",
            "ame2@spst.eu",
            "ame3r@spst.eu",
            "amu1@spst.eu",
            "amu2r@spst.eu",
            "amu3@spst.eu",
            "amz3r@spst.eu",
            "auk3@spst.eu",
            "aut2@spst.eu",
            "aut2r@spst.eu",
            "aut3@spst.eu",
            "aut3r@spst.eu",
            "aut4@spst.eu",
            "druhaci@spst.eu",
            "druhacir@spst.eu",
            "ele1@spst.eu",
            "ele2@spst.eu",
            "ele2r@spst.eu",
            "ele3@spst.eu",
            "ele3r@spst.eu",
            "enb1@spst.eu",
            "ene2@spst.eu",
            "ene3r@spst.eu",
            "ers4@spst.eu",
            "ita2r@spst.eu",
            "ita3r@spst.eu",
            "itb1r@spst.eu",
            "itb2@spst.eu",
            "itb3r@spst.eu",
            "itb4r@spst.eu",
            "krouzek@spst.eu",
            "ma1a@spst.eu",
            "ma1b@spst.eu",
            "mau2@spst.eu",
            "mau3@spst.eu",
            "mee1@spst.eu",
            "mee2@spst.eu",
            "mee3@spst.eu",
            "mel2@spst.eu",
            "mel2r@spst.eu",
            "mel3@spst.eu",
            "mel3r@spst.eu",
            "mel4@spst.eu",
            "memes@spst.eu",
            "meo2@spst.eu",
            "mer4@spst.eu",
            "meu2@spst.eu",
            "mez1@spst.eu",
            "mez2@spst.eu",
            "mez3@spst.eu",
            "mse3@spst.eu",
            "msk2@spst.eu",
            "msk3r@spst.eu",
            "msk4@spst.eu",
            "oku1@spst.eu",
            "oku2@spst.eu",
            "oku3r@spst.eu",
            "ozs2@spst.eu",
            "ozs2r@spst.eu",
            "ozu1@spst.eu",
            "ozu3@spst.eu",
            "pau1@spst.eu",
            "pau1r@spst.eu",
            "pau2@spst.eu",
            "pau2r@spst.eu",
            "pau3@spst.eu",
            "pau3r@spst.eu",
            "pau4@spst.eu",
            "pau4r@spst.eu",
            "prvaci@spst.eu",
            "prvacir@spst.eu",
            "psa1@spst.eu",
            "psa2@spst.eu",
            "psa3@spst.eu",
            "psa4@spst.eu",
            "psb1@spst.eu",
            "psb4@spst.eu",
            "rodicesel1@spst.eu",
            "sel1r@spst.eu",
            "sel2@spst.eu",
            "str1@spst.eu",
            "str1r@spst.eu",
            "str2@spst.eu",
            "str3@spst.eu",
            "str3r@spst.eu",
            "str4r@spst.eu",
            "test@spst.eu",
            "tla2@spst.eu",
            "tla3r@spst.eu",
            "tla4@spst.eu",
            "tlb1@spst.eu",
            "tlb2@spst.eu",
            "tlb4@spst.eu",
            "tly1@spst.eu",
            "tly2@spst.eu",
            "tly2r@spst.eu",
            "tly4r@spst.eu",
            "ucitele_ucebna@spst.eu",
            "office@spst.eu",
            "studenti@spst.eu",
            "senat@spst.eu",
            "PHrbackova@spst.cz",
            "ACahova@spst.cz",
            "LHavlat@spst.cz",
            "LNechvatalova@spst.cz",
            "MRihacek@spst.cz",
            "MCejpkova@spst.cz",
            "DCafourkova@spst.cz",
            "LZbrankova@spst.cz",
            "TCermak@spst.cz",
            "MDvorakova@spst.cz",
            "lveskrnova@spst.cz",
            "ADobiasova@spst.cz",
            "DBalabanova@spst.cz",
            "kbarkolova@spst.cz",
            "PBastova@spst.cz",
            "mbauerova@spst.cz",
            "MBloudicek@spst.cz",
            "JBobek@spst.cz",
            "ZBobkova@spst.cz",
            "kbohm2@spst.cz",
            "FBranc@spst.cz",
            "mbrozek@spst.cz",
            "MBuclova@spst.cz",
            "MDocekal@spst.cz",
            "IDocekalova@spst.cz",
            "pdocekalova@spst.cz",
            "JDostal@spst.cz",
            "EDvorakova@spst.cz",
            "eeis@spst.cz",
            "VElBehani@spst.cz",
            "EFejtova@spst.cz",
            "JHana@spst.cz",
            "EHavlatova@spst.cz",
            "IHedbavna@spst.cz",
            "ahink@spst.cz",
            "RHlavnicka@spst.cz",
            "ohonzikova@spst.cz",
            "ihorka@spst.cz",
            "mhroznicek@spst.cz",
            "ZHruska2@spst.cz",
            "lhruskovic@spst.cz",
            "khrebacka@spst.cz",
            "pchalupa@spst.cz",
            "ochudoba@spst.cz",
            "jkacetlova@spst.cz",
            "vkafonek@spst.cz",
            "ikafonkova@spst.cz",
            "MKlempar@spst.cz",
            "PKlimes@spst.cz",
            "LKliner@spst.cz",
            "JKolarova@spst.cz",
            "MKolman@spst.cz",
            "EKolmanova@spst.cz",
            "rkopecky@spst.cz",
            "MKostelnikova@spst.cz",
            "rkracmarova@spst.cz",
            "jkratky@spst.cz",
            "MKrejci@spst.cz",
            "ikucharova@spst.cz",
            "mlorencova@spst.cz",
            "mloup@spst.cz",
            "FLustig@spst.cz",
            "DMadra@spst.cz",
            "JMan@spst.cz",
            "RMatejkova@spst.cz",
            "MMatousek@spst.cz",
            "OMatousek@spst.cz",
            "LMertlova@spst.cz",
            "JMozorova@spst.cz",
            "smusilova@spst.cz",
            "kmuller@spst.cz",
            "ANohacek@spst.cz",
            "JNovacek@spst.cz",
            "KNovackova@spst.cz",
            "mnovackova@spst.cz",
            "pnovackova@spst.cz",
            "JNovotna@spst.cz",
            "monovotna@spst.cz",
            "AOdehnalova@spst.cz",
            "JOndrackova@spst.cz",
            "PPacalova@spst.cz",
            "PPalatova@spst.cz",
            "jpetr@spst.cz",
            "npistovcakova@spst.cz",
            "hprusova@spst.cz",
            "JRiedel@spst.cz",
            "MRozmahelova@spst.cz",
            "jruzicka@spst.cz",
            "mruzicka@spst.cz",
            "HRyglova@spst.cz",
            "zsamkova@spst.cz",
            "asedlackova@spst.cz",
            "dskala@spst.cz",
            "sspencer@spst.cz",
            "ostejskal@spst.cz",
            "msimkova@spst.cz",
            "ZSiroky@spst.cz",
            "DSkarka@spst.cz",
            "KStaffova@spst.cz",
            "pstol@spst.cz",
            "zstruncova@spst.cz",
            "KTomanek@spst.cz",
            "JVackova@spst.cz",
            "pvafkova@spst.cz",
            "FVala@spst.cz",
            "tvalek@spst.cz",
            "MValentinyova@spst.cz",
            "jvecera@spst.cz",
            "JVesela@spst.cz",
            "mvesely@spst.cz",
            "vesely@spst.cz",
            "mvesely02@spst.cz",
            "PVesely@spst.cz",
            "JVidlakova@spst.cz",
            "jvomela@spst.cz",
            "lzverinova@spst.cz",
            "jbartusek@spst.cz",
            "PBocek@spst.cz",
            "lbuchal@spst.cz",
            "ZCejpek@spst.cz",
            "mcendelin@spst.cz",
            "PCizek@spst.cz",
            "lengelmann@spst.cz",
            "JFrenc@spst.cz",
            "phrdlickova@spst.cz",
            "RHrdy@spst.cz",
            "SChalupa@spst.cz",
            "LJan@spst.cz",
            "PKaleta@spst.cz",
            "PKhek@spst.cz",
            "MKosielski@spst.cz",
            "KKovac@spst.cz",
            "VMendlik@spst.cz",
            "pnestrojil@spst.cz",
            "LNovotny@spst.cz",
            "ROkrina@spst.cz",
            "JPelan@spst.cz",
            "JPospisil@spst.cz",
            "MRous@spst.cz",
            "msmolik@spst.cz",
            "SSvoboda@spst.cz",
            "rsimunek@spst.cz",
            "dsplichal@spst.cz",
            "jstastny@spst.cz",
            "pvavra@spst.cz",
            "BVidensky@spst.cz",
            "JVlkova@spst.cz",
            "PVodinsky@spst.cz",
            "LVorlicek@spst.cz",
            "LVrba@spst.cz",
            "BZahradka@spst.cz",
            "dzbranek@spst.cz",
            "pzejda@spst.cz",
            "vzejda@spst.cz",
            "hdudrova@spst.cz",
            "AHanakova@spst.cz",
            "ZHruska@spst.cz",
            "HJirova@spst.cz",
            "ZKovac@spst.cz",
            "LNemcova@spst.cz",
            "mnovotny02@spst.cz",
            "KPachlova@spst.cz",
            "RFertigova@spst.cz",
            "mchladkova@spst.cz",
            "mjasova@spst.cz",
            "ZJelinkova@spst.cz",
            "mkaraskova@spst.cz",
            "AKostelnikova@spst.cz",
            "JKratka@spst.cz",
            "MKrejcova@spst.cz",
            "rpechova@spst.cz",
            "MKudrnova@spst.cz",
            "jmatejkova@spst.cz",
            "LNyklova@spst.cz",
            "MSedlakova@spst.cz",
            "SSyrova@spst.cz",
            "MBlahova@spst.cz",
            "MHruza@spst.cz",
            "AHybner@spst.cz",
            "pdostalkova@spst.cz",
            "jmoravcova@spst.cz",
            "LMusilova@spst.cz",
            "mnovotny@spst.cz",
            "SOgnarova@spst.cz",
            "mpacal@spst.cz",
            "epruzova@spst.cz",
            "dsedlakova@spst.cz",
            "DStavova@spst.cz",
            "BTesna@spst.cz",
            "DVodakova@spst.cz",
            "DVolfova@spst.cz",
            "IBochnickova@spst.cz",
            "mcahova@spst.cz",
            "mheliskova@spst.cz",
            "APokorna@spst.cz",
            "mruzickova@spst.cz",
            "PRuzickova@spst.cz",
            "JSvobodova@spst.cz"
    };
    private static final Logger log = LoggerFactory.getLogger(VerifyCommand.class);

    private final Session session;

    public VerifyCommand(JDA jda) {
        super(jda);
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.ssl.enable", "true");
        session = Session.getInstance(props);
    }

    @Override
    public String getName() {
        return "verify";
    }

    @Override
    public SlashCommandData create() {
        return Commands.slash(getName(), "Použij pro verifikaci svého školního účtu.")
                .addOption(OptionType.STRING, "e-mail", "Svůj školní e-mail(končící @spst.eu/@spst.cz.", true)
                .setContexts(InteractionContextType.GUILD);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        if (event.getMember().getUser().isBot()
            || !event.getChannelId().equals(Environment.CHANNEL_VERIFICATION_ID)) return;
        String email = event.getOption("e-mail").getAsString();
        Logger userLogger = LoggerFactory.getLogger(event.getMember().getUser().getName());
        if (Database.exists(event.getUser().getId())) {
            userLogger.info("[{}] Attempted to verify but is already verified.", event.getMember().getId());
            event.reply("Jsi již ověřený uživatel! Pokud chceš změnit svůj e-mail, kontaktuj prosím administrátory.").setEphemeral(true).queue();
            return;
        }
        userLogger.info("[{}] Requested verification for email: {}", event.getMember().getId(), email);
        event.deferReply(true).setEphemeral(true).queue();
        if (!emailPattern.matcher(email).matches()) {
            event.getHook().sendMessage("Neplatný e-mail! Ujisti se, že používáš školní e-mail končící na @spst.eu nebo @spst.cz").setEphemeral(true).queue();
            Events.onUnsuccessfulEmailSent(event.getJDA(), event.getMember(), email, "Invalid email format");
            return;
        }

        if (isBlocked(email)) {
            event.getHook().sendMessage("Tento e-mail je zablokován a nemůže být použit pro verifikaci.").setEphemeral(true).queue();
            Events.onUnsuccessfulEmailSent(event.getJDA(), event.getMember(), email, "Blocked email");
            return;
        }

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("studenti@spst.eu"));
            String code = Memcached.getInstance().createCode(email, event.getMember().getIdLong());
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Verifikace na Discord server SPŠT");
            message.setText("Ahoj,\n\npro dokončení verifikace na Discord server SPŠT, si zkopíruj tento kód: \n\n" + code + "\n\nA vlož ho do příkazu /code\n\nPokud jsi o tento e-mail nežádal, můžeš ho ignorovat. Kód vyprší za hodinu.\n\nS pozdravem,\nTým Studenti SPŠT");

            Transport.send(message, "studenti@spst.eu", Environment.GMAIL_OAUTH_TOKEN);
            Events.onVerificationEmailSent(event.getJDA(), event.getMember(), email, code);
            event.getHook().sendMessage("Verifikační e-mail byl odeslán na " + email + ". Zkontroluj svou schránku a zadej kód pomocí příkazu /code.").setEphemeral(true).queue();
        } catch (MessagingException e) {
            log.error("Failed to send verification email to {}", email, e);
            event.getHook().sendMessage("Došlo k neznámé chybě, zkus to prosím později.").setEphemeral(true).queue();
        }
    }

    private boolean isBlocked(String email) {
        for (String id : blocked) {
            if (id.equals(email)) {
                return true;
            }
        }
        return false;
    }
}
