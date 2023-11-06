package miau.hxppytwitch;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.PortalCreateEvent;

public class MCEvents implements Listener {
    @EventHandler
    void onJoin(PlayerJoinEvent e) {
        HTPlugin plugin = HTPlugin.getInstance();
        Player player = e.getPlayer();
        if (player.getName().equals(plugin.getConfig().getString("master-nickname"))) {
            plugin.master = player;
            plugin.isMasterOnline = true;
            plugin.startNickNameCycle();
        }
    }

    @EventHandler
    void onQuit(PlayerQuitEvent e) {
        HTPlugin plugin = HTPlugin.getInstance();
        Player player = e.getPlayer();
        if (player.getName().equals(plugin.getConfig().getString("master-nickname"))) {
            plugin.isMasterOnline = false;
        }
    }

    @EventHandler
    void onChat(PlayerChatEvent e) {
        HTPlugin plugin = HTPlugin.getInstance();
        Player player = e.getPlayer();
        System.out.println(plugin.isStreamOnline + " " +plugin.isMasterOnline);
    }
}
