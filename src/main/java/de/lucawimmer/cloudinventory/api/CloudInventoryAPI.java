package de.lucawimmer.cloudinventory.api;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.lucawimmer.cloudinventory.CloudInventory;
import de.lucawimmer.cloudinventory.listener.BukkitListener;
import org.bukkit.Bukkit;

public class CloudInventoryAPI {

    public static void sendToServer(String name, String dest) {
        if (!CloudInventory.getDefaultConfig().getBoolean("use-as-puffer") && CloudInventory.getDefaultConfig().getBoolean("enable-puffer")) {
            try {
                String bungeename = CloudInventory.getDefaultConfig().getString("puffer-server").split(":")[3];
                Bukkit.getPlayer(name).sendMessage(CloudInventory.getDefaultConfig().getString("puffer-server"));
                Bukkit.getPlayer(name).sendMessage("test");
                try {
                    Bukkit.getPlayer(name).sendMessage("test3");
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    Bukkit.getPlayer(name).sendMessage("test4");
                    out.writeUTF("Connect");
                    Bukkit.getPlayer(name).sendMessage("test5");
                    out.writeUTF(bungeename);
                    Bukkit.getPlayer(name).sendMessage("test6");
                    Bukkit.getPlayer(name).sendPluginMessage((CloudInventory.getInstance()), "BungeeCord", out.toByteArray());
                    Bukkit.getPlayer(name).sendMessage("test7");
                } catch (Exception e) {
                    Bukkit.getPlayer(name).sendMessage("test8");
                    e.printStackTrace();
                } finally {
                    Bukkit.getPlayer(name).sendMessage("test9");
                }
                if (!BukkitListener.playerDest.containsKey(name)) {
                    BukkitListener.playerDest.put(name, dest);
                } else {
                    BukkitListener.playerDest.remove(name);
                    BukkitListener.playerDest.put(name, dest);
                }
                Bukkit.getPlayer(name).sendMessage("test2");
            } catch(Exception ex) {
                ex.printStackTrace();
            } finally {
                Bukkit.getPlayer(name).sendMessage("test10");
            }
        } else {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            try {
                out.writeUTF("Connect");
                out.writeUTF(dest);
                Bukkit.getPlayer(name).sendPluginMessage((CloudInventory.getInstance()), "BungeeCord", out.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
