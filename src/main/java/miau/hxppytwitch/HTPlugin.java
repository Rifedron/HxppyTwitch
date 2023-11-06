package miau.hxppytwitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.events.ChannelGoOfflineEvent;
import com.github.twitch4j.helix.domain.Stream;
import miau.hxppytwitch.MCCommands.AbstractCommand;
import miau.hxppytwitch.MCCommands.ReloadConfigCommand;
import miau.hxppytwitch.discord.DiscCommandManager;
import miau.hxppytwitch.discord.DiscEvents;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.defaults.ReloadCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;

public final class HTPlugin extends JavaPlugin {

    private static HTPlugin instance;
    TwitchClient twitchClient;
    private JDA jda;
    public Player master;
    public boolean isMasterOnline =false;
    public boolean isStreamOnline = false;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        loadMcCommands();
        try {
            jda = launchJDA();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        launchTwitchClient();
        Bukkit.getPluginManager().registerEvents(new MCEvents(), instance);
    }

    @Override
    public void onDisable() {
        twitchClient.close();
    }

    public static HTPlugin getInstance() {
        return instance;
    }

    private void launchTwitchClient() {
        twitchClient = TwitchClientBuilder.builder()
                .withDefaultAuthToken(new OAuth2Credential("twitch","3e9ty0pp7otc0jan0i4ttej9f4wesr"))
                .withEnableHelix(true)
                .build();
        launchTwitchEvents();
    }
    private JDA launchJDA() throws InterruptedException {
        return JDABuilder.createDefault(getConfig().getString("disc-bot-token"), EnumSet.allOf(GatewayIntent.class))
                .enableCache(EnumSet.allOf(CacheFlag.class))
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new DiscEvents(), new DiscCommandManager())
                .build().awaitReady();
    }
    private void launchTwitchEvents() {
        twitchClient.getClientHelper().enableStreamEventListener(getConfig().getString("master-twitch"));

        twitchClient.getEventManager().onEvent(ChannelGoLiveEvent.class, event -> {
            Stream stream = event.getStream();
            String notification = getConfig().getString("stream-notification");
            notification = notification
                    .replace("{streamerName}", stream.getUserName())
                    .replace("{streamerLogin}", stream.getUserLogin());
            jda.getTextChannelById(getConfig().getString("discord-channel"))
                    .sendMessage(notification)
                    .queue();
            isStreamOnline = true;
            startNickNameCycle();
        });
        twitchClient.getEventManager().onEvent(ChannelGoOfflineEvent.class, event -> {
            isStreamOnline = false;
        });
    }
    private void loadMcCommands() {
        new ReloadConfigCommand();
    }
    public void startNickNameCycle() {
        if (isMasterOnline && isStreamOnline) {
            master.setPlayerListName(master.getPlayerListName()+ChatColor.GRAY+ "●");
            recursiveNickUpdater();
        }
    }
    private boolean updatePhase = true;
    public void recursiveNickUpdater() {
        Bukkit.getScheduler().runTaskLater(instance, () -> {
            if (isMasterOnline && isStreamOnline) {
                String newName;
                if (updatePhase) {
                    newName = master.getPlayerListName().replace(ChatColor.GRAY + "●", ChatColor.DARK_RED + "●");
                } else {
                    newName = master.getPlayerListName().replace(ChatColor.DARK_RED + "●", ChatColor.GRAY + "●");
                }
                master.setPlayerListName(newName);
                updatePhase=!updatePhase;
                recursiveNickUpdater();
            } else
                master.setPlayerListName(null);
        }, 15);
    }
}
