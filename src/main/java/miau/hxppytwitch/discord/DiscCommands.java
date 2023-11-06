package miau.hxppytwitch.discord;

import miau.hxppytwitch.HTPlugin;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.bukkit.Server;

public class DiscCommands {
    @DiscCommand(name = "players", description = "shows a count of online players")
    void showPlayers(SlashCommandInteractionEvent e) {
        SlashCommandInteraction inter = e.getInteraction();
        Server server = HTPlugin.getInstance().getServer();
        int playerCount = server.getOnlinePlayers().size();
        ReplyCallbackAction callbackAction = inter
                .reply("На сервере сейчас **"+playerCount+"** игроков")
                .setEphemeral(true);
        if (playerCount > 0)
            callbackAction.addActionRow(
                    Button.primary("playerList", "\uD83D\uDC65")
            );
        callbackAction.queue();

    }
}
