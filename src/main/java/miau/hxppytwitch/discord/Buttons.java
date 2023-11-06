package miau.hxppytwitch.discord;

import miau.hxppytwitch.HTPlugin;
import miau.hxppytwitch.utils.HxppyUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.bukkit.entity.Player;

public class Buttons {
    @Button(id = "playerList")
    void showPlayersOnline(ButtonInteractionEvent e) {
        HTPlugin plugin = HTPlugin.getInstance();
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Игроки на сервере:")
                .setColor(HxppyUtils.embedColor);
        StringBuilder sb = new StringBuilder();
        for (Player player: plugin.getServer().getOnlinePlayers())
            sb.append(player.getName() + "\n");
        embed.setDescription(sb.toString());
        e.replyEmbeds(embed.build())
                .setEphemeral(true)
                .queue();
    }
}
