package de.lucawimmer.cloudinventory.listener;

import de.lucawimmer.cloudinventory.CloudInventory;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.json.simple.JSONObject;

import javax.xml.bind.DatatypeConverter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.file.Files;
import java.util.HashMap;

public class BukkitListener implements Listener {

    public static HashMap<String, String> playerDest = new HashMap<String, String>();

    @EventHandler
    public void onServerJoin(PlayerPreLoginEvent e) {

        if (playerDest.containsKey(e.getName()))
            playerDest.remove(e.getName());
    }

    @EventHandler
    public void onServerJoined(PlayerJoinEvent e) throws IOException {
        e.getPlayer().teleport(Bukkit.getWorld("name").getSpawnLocation());
        if (CloudInventory.getDefaultConfig().getBoolean("use-as-puffer")) {
            e.getPlayer().getInventory().clear();
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999, 999));
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999, 999));
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999, 999));
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 999, 999));
            e.getPlayer().setWalkSpeed(0.0F);
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10000, 128));
            e.setJoinMessage(null);
        }
    }

    @EventHandler
    public void onServerLeft(PlayerQuitEvent e) throws IOException {
        if (CloudInventory.getDefaultConfig().getBoolean("use-as-puffer")) {
            e.setQuitMessage(null);
        }
        e.getPlayer().teleport(Bukkit.getWorld("name").getSpawnLocation());
    }

    @EventHandler
    public void onServerChat(PlayerCommandPreprocessEvent e) throws IOException {
        if (CloudInventory.getDefaultConfig().getBoolean("use-as-puffer")) {
            if(!e.getPlayer().hasPermission("cloudinventory.chat.use"))  e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLostHealth(EntityDamageEvent e) {
        if (CloudInventory.getDefaultConfig().getBoolean("use-as-puffer")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onServerChange(PlayerQuitEvent e) throws IOException {
        if (!CloudInventory.getDefaultConfig().getBoolean("use-as-puffer") && !CloudInventory.getDefaultConfig().getBoolean("enable-puffer")) {
            for (String s : CloudInventory.getDefaultConfig().getStringList("servers")) {

                String ip = s.split(":")[0];
                Integer port = Integer.parseInt(s.split(":")[1]);
                String password = s.split(":")[2];

                e.getPlayer().saveData();

                File file = new File(e.getPlayer().getWorld().getWorldFolder(), "players/" + e.getPlayer().getName() + ".dat");
                byte[] bytes = Files.readAllBytes(file.toPath());

                JSONObject json = new JSONObject();
                json.put("header", "syncserver");
                json.put("password", password);
                json.put("server", Bukkit.getServer().getIp() + ":" + CloudInventory.getDefaultConfig().getInt("port"));
                json.put("player", e.getPlayer().getName());
                json.put("playerip", e.getPlayer().getAddress().toString());
                json.put("playerdata", DatatypeConverter.printBase64Binary(bytes));
                SocketAddress address = new InetSocketAddress(ip, port);
                Socket clientSocket = new Socket();
                clientSocket.connect(address, 20000);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                outToServer.writeBytes(json.toString());
                clientSocket.close();
                Bukkit.getLogger().info("Packet generated by " + e.getPlayer().getName() + " has been sent to all servers.");
            }
        }

        if (!CloudInventory.getDefaultConfig().getBoolean("use-as-puffer") && CloudInventory.getDefaultConfig().getBoolean("enable-puffer")) {
            try {
                String ip = CloudInventory.getDefaultConfig().getString("puffer-server").split(":")[0].replace("[", "");
                Integer port = Integer.parseInt(CloudInventory.getDefaultConfig().getString("puffer-server").split(":")[1]);
                String password = CloudInventory.getDefaultConfig().getString("puffer-server").split(":")[2];

                JSONObject json = new JSONObject();
                json.put("header", "puffer; left;");
                json.put("password", password);
                json.put("server", Bukkit.getServer().getIp() + ":" + CloudInventory.getDefaultConfig().getInt("port"));
                json.put("playerip", e.getPlayer().getAddress().toString());
                json.put("ownpassword", CloudInventory.getDefaultConfig().getString("communication-password"));
                json.put("player", e.getPlayer().getName());
                if (BukkitListener.playerDest.containsKey(e.getPlayer().getName())) {
                    json.put("destination", playerDest.get(e.getPlayer().getName()));
                    BukkitListener.playerDest.remove(e.getPlayer().getName());
                } else {
                    json.put("destination", "default");
                }
                SocketAddress address = new InetSocketAddress(ip, port);
                Socket clientSocket = new Socket();
                clientSocket.connect(address, 20000);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                outToServer.writeBytes(json.toString());
                clientSocket.close();
                Bukkit.getLogger().info("Packet generated by " + e.getPlayer().getName() + " has been sent to puffer server.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
}
