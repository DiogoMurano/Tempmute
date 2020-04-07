package br.com.diogomurano.tempmute.commands;

import br.com.diogomurano.tempmute.MutePlugin;
import br.com.diogomurano.tempmute.services.MutePlayerService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnmuteCommand implements CommandExecutor {

    private MutePlugin plugin;
    private MutePlayerService playerService;

    public UnmuteCommand(MutePlugin plugin) {
        this.plugin = plugin;
        playerService = plugin.getPlayerService();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("command.unmute")) {
            if (args.length != 2) {
                sender.sendMessage("§cUsage: /unmute <player>.");
                return false;
            }

            final String playerName = args[0];

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (plugin.getDataManager().findPlayerByName(playerName).isPresent()) {
                    plugin.getDataManager().removePlayer(playerName);

                    playerService.findByName(playerName).ifPresent(playerService::remove);

                    sender.sendMessage("§aSuccessfully unmuted");
                } else {
                    sender.sendMessage("Player is not muted");
                }
            });
            sender.sendMessage("§a");
        }
        return false;
    }
}
