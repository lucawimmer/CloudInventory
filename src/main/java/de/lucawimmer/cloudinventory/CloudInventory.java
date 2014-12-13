package de.lucawimmer.cloudinventory;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.lucawimmer.cloudinventory.commands.FarmweltCommand;
import de.lucawimmer.cloudinventory.listener.BukkitListener;
import de.lucawimmer.cloudinventory.server.Server;
import de.lucawimmer.cloudinventory.storage.PufferStorage;
import de.lucawimmer.cloudinventory.utils.SimpleConfig;
import de.lucawimmer.cloudinventory.utils.SimpleConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class CloudInventory extends JavaPlugin {

    /* Statische Variablen */

    // Config
    private static SimpleConfig CONFIG;
    private static JavaPlugin PLUGIN;
    private static Server SERVER = null;

    /* Globale Funktionen */

    // Config
    public static SimpleConfig getDefaultConfig() {
        return CONFIG;
    }

    public static JavaPlugin getInstance() {
        return PLUGIN;
    }

    /*-----------------------------------*/

    @Override
    public void onEnable() {
        PLUGIN = this;
        SimpleConfigManager manager = new SimpleConfigManager(this);
        String[] header = {"CloudInventory", "Developed and written by", "Luca Wimmer (magl1te)", "business@luca.bz"};
        CONFIG = manager.getNewConfig("config.yml", header);
        CONFIG.addDefault("enable-plugin", true, new String[]{"Should the plugin be enabled?"});
        try {
            CONFIG.addDefault("ip", InetAddress.getLocalHost().getHostAddress(), new String[]{"IP of this CloudInventory Server"});
        } catch (UnknownHostException ignored) {
        }
        CONFIG.addDefault("port", 2151, new String[]{"Port of this CloudInventory Server"});
        CONFIG.addDefault("communication-password", "pl3as3_ch@ng3_m3", new String[]{"Password for communication within the servers"});
        CONFIG.addDefault("bungee-name", "default", new String[]{"Name of the bungeecord server"});
        CONFIG.addDefault("servers", Arrays.asList("178.251.229.190:2152:pl3as3_ch@ng3_m3"));
        CONFIG.addDefault("enable-puffer", false, new String[]{"Enable a puffer server?"});
        CONFIG.addDefault("use-as-puffer", false, new String[]{"Do you want this server as puffer server?"});
        CONFIG.addDefault("puffer-server", "178.251.229.190:2153:pl3as3_ch@ng3_m3:bungeename", new String[]{"If you use this as puffer server its unimportant"});
        CONFIG.addDefault("default-forward-server", "default");
        CONFIG.saveConfig();
        try {
            SERVER = new Server(InetAddress.getByName(CONFIG.getString("ip")), CONFIG.getInt("port"));
            SERVER.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        if (CONFIG.getBoolean("use-as-puffer")) {
            PufferStorage.pufferClientProccessing();
        }

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getServer().getPluginManager().registerEvents(new BukkitListener(), this);
        //getCommand("farmwelt").setExecutor(new FarmweltCommand());
    }

    public void onDisable() {
        try {
            SERVER.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
