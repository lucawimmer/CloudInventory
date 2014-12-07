package de.lucawimmer.cloudinventory.storage;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.lucawimmer.cloudinventory.CloudInventory;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

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
                for (final String s : waitingConnections.keySet()) {
                    if (Bukkit.getPlayer(s) != null) {
                        final String dest = waitingConnections.get(s);
                        removeConnectingPlayer(s);
                        initiateTeleport(s, dest);
                    }
                }
            }
        }, 10L, 1L);
    }

    private static void initiateTeleport(final String p, final String dest) {
        if(Bukkit.getPlayer(p) != null) {
            Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(CloudInventory.getInstance(), new Runnable() {
                @Override
                public void run() {

                    new BukkitRunnable() {
                        int seconds = 5;

                        public void run() {
                            if (seconds > -1) {
                                switch (seconds) {
                                    case 5:
                                        Bukkit.getPlayer(p).sendMessage("§7[§6H§7] §fDu wirst in 5 Sekunden teleportiert.");
                                        seconds--;
                                        break;
                                    case 4:
                                        Bukkit.getPlayer(p).sendMessage("§7[§6H§7] §fDu wirst in 4 Sekunden teleportiert.");
                                        seconds--;
                                        break;
                                    case 3:
                                        Bukkit.getPlayer(p).sendMessage("§7[§6H§7] §fDu wirst in 3 Sekunden teleportiert.");
                                        seconds--;
                                        break;
                                    case 2:
                                        Bukkit.getPlayer(p).sendMessage("§7[§6H§7] §fDu wirst in 2 Sekunden teleportiert.");
                                        seconds--;
                                        break;
                                    case 1:
                                        Bukkit.getPlayer(p).sendMessage("§7[§6H§7] §fDu wirst in 1 Sekunden  teleportiert.");
                                        seconds--;
                                        break;
                                    case 0:
                                        try {
                                            final ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                            out.writeUTF("Connect");
                                            if (dest != null) {
                                                if (!dest.equals("default")) {
                                                    out.writeUTF(dest);
                                                } else {
                                                    out.writeUTF(CloudInventory.getDefaultConfig().getString("default-forward-server"));
                                                }
                                                Bukkit.getPlayer(p).sendPluginMessage((CloudInventory.getInstance()), "BungeeCord", out.toByteArray());
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        cancel();
                                        break;
                                }
                            }
                        }
                    }.runTaskTimer(CloudInventory.getInstance(), 0, 20);
                }
            }, 20L * 4);
        }
    }
}
