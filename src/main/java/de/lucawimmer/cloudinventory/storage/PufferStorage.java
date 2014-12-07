package de.lucawimmer.cloudinventory.storage;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.lucawimmer.cloudinventory.CloudInventory;
import org.bukkit.Bukkit;

import java.util.HashMap;

public class PufferStorage {
    private static HashMap<String, String> waitingConnections = new HashMap<String, String>();

    public static void addConnectingPlayer(String p, String s) {
        waitingConnections.put(p, s);
    }

    public static void removeConnectingPlayer(String p) {
        waitingConnections.remove(p);
    }

    public static String getConnectingPlayerDestination(String p) {
        return waitingConnections.get(p);
    }

    public static boolean hasConnectingPlayer(String p) {
        if (waitingConnections.containsKey(p))
            return true;

        return false;
    }

    public static void pufferClientProccessing() {
        Bukkit.getLogger().info("Starting pufferClientProcessing Task");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CloudInventory.getInstance(), new Runnable() {

            @Override
            public void run() {
                for (String s : waitingConnections.keySet()) {
                    if (Bukkit.getPlayer(s) != null) {
                        Bukkit.getLogger().info(waitingConnections.get(s));
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();

                        try {
                            out.writeUTF("Connect");
                            if (!waitingConnections.get(s).equals("default")) {
                                out.writeUTF(waitingConnections.get(s));
                            } else {
                                out.writeUTF(CloudInventory.getDefaultConfig().getString("default-forward-server"));
                            }
                            Bukkit.getPlayer(s).sendPluginMessage((CloudInventory.getInstance()), "BungeeCord", out.toByteArray());
                            removeConnectingPlayer(s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, 10L, 1L);

    }
}
