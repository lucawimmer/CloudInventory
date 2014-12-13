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
                try {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Connect");
                    out.writeUTF(bungeename);
                    Bukkit.getPlayer(name).sendPluginMessage((CloudInventory.getInstance()), "BungeeCord", out.toByteArray());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!BukkitListener.playerDest.containsKey(name)) {
                    BukkitListener.playerDest.put(name, dest);
                } else {
                    BukkitListener.playerDest.remove(name);
                    BukkitListener.playerDest.put(name, dest);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
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
