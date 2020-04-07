package br.com.diogomurano.tempmute;

import br.com.diogomurano.tempmute.commands.TempmuteCommand;
import br.com.diogomurano.tempmute.commands.UnmuteCommand;
import br.com.diogomurano.tempmute.data.DataManager;
import br.com.diogomurano.tempmute.listener.MuteListener;
import br.com.diogomurano.tempmute.services.MutePlayerService;
import br.com.diogomurano.tempmute.services.MutePlayerServiceImpl;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MutePlugin extends JavaPlugin {

    private MutePlayerService playerService;
    private DataManager dataManager;

    @Override
    public void onLoad() {
        playerService = new MutePlayerServiceImpl();

        this.dataManager = new DataManager(new File("storage.db"));
        this.dataManager.setupConnection();
        this.dataManager.setupTable();
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new MuteListener(this), this);

        getCommand("tempmute").setExecutor(new TempmuteCommand(this));
        getCommand("unmute").setExecutor(new UnmuteCommand(this));
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public MutePlayerService getPlayerService() {
        return playerService;
    }
}
