package br.com.diogomurano.tempmute.commands;

import br.com.diogomurano.tempmute.MutePlugin;
import br.com.diogomurano.tempmute.model.MutePlayer;
import br.com.diogomurano.tempmute.utils.UtilFormat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TempmuteCommand implements CommandExecutor {

    private MutePlugin plugin;

    public TempmuteCommand(MutePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("command.tempmute")) {
            if (args.length != 2) {
                sender.sendMessage("§cUsage: /tempmute <player> <time>.");
                return false;
            }

            final String playerName = args[0];
            final String timeString = args[1];

            int seconds = UtilFormat.toSeconds(timeString);
            if (seconds == -1) {
                sender.sendMessage("§cInvalid time.");
                return false;
            }

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                final MutePlayer mutedPlayer = MutePlayer.builder()
                        .playerName(playerName)
                        .expires(System.currentTimeMillis() + (seconds * 1000))
                        .build();
                plugin.getDataManager().insertPlayer(mutedPlayer);

                if(Bukkit.getPlayer(playerName) != null) {
                    plugin.getPlayerService().add(mutedPlayer);
                }
            });
            sender.sendMessage("§aSuccessfully muted");
        }
        return false;
    }
}
