package miau.hxppytwitch.MCCommands;

import miau.hxppytwitch.HTPlugin;
import org.bukkit.command.CommandSender;

public class ReloadConfigCommand extends AbstractCommand {
    public ReloadConfigCommand() {
        super("htreload");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        HTPlugin plugin = HTPlugin.getInstance();
        plugin.reloadConfig();
    }
}
