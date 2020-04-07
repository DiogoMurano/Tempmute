package br.com.diogomurano.tempmute.listener;

import br.com.diogomurano.tempmute.MutePlugin;
import br.com.diogomurano.tempmute.data.DataManager;
import br.com.diogomurano.tempmute.services.MutePlayerService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MuteListener implements Listener {

    private final MutePlayerService playerService;
    private final DataManager dataManager;

    public MuteListener(MutePlugin plugin) {
        this.playerService = plugin.getPlayerService();
        this.dataManager = plugin.getDataManager();
    }

    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        final String playerName = event.getName();
        dataManager.findPlayerByName(playerName).ifPresent(playerService::add);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        playerService.findByName(player.getName()).ifPresent(playerService::remove);
    }

    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent event) {
        final Player player = event.getPlayer();
        playerService.findByName(player.getName()).ifPresent(playerService::remove);
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        playerService.findByName(player.getName()).ifPresent(p -> {
            if (p.isMuted()) {
                player.sendMessage("§cYou are muted, wait for " + getCooldownTime(System.currentTimeMillis() - p.getExpires()));
                event.setCancelled(true);
            }
        });
    }

    private String getCooldownTime(long time) {
        time = time / 1000;
        int hours = time > 3600 ? (int) (time / 3600) : 0;
        int minutes = time > 60 ? (int) (time / 60) : 0;
        int seconds = (int) time;
        return (hours > 0 ? hours + " hours " : "") + (minutes > 0 ? minutes + " minutes " : "") + (seconds > 0 ? seconds
                + " seconds" : "");
    }

}
