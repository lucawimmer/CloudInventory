package de.lucawimmer.cloudinventory.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.lucawimmer.cloudinventory.CloudInventory;
import de.lucawimmer.cloudinventory.api.CloudInventoryAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FarmweltCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args[0].equals("lobby"))
            CloudInventoryAPI.sendToServer(commandSender.getName(), "lobby");
        else if (args[0].equals("farmwelt"))
            CloudInventoryAPI.sendToServer(commandSender.getName(), "server1");
        else if (args[0].equals("test"))
            try {
                commandSender.sendMessage("test3");
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                commandSender.sendMessage("test4");
                out.writeUTF("Connect");
                commandSender.sendMessage("test5");
                out.writeUTF("puffer");
                commandSender.sendMessage("test6");
                ((Player) commandSender).sendPluginMessage((CloudInventory.getInstance()), "BungeeCord", out.toByteArray());
                commandSender.sendMessage("test7");
            } catch (Exception e) {
                commandSender.sendMessage("test8");
                e.printStackTrace();
            }
        return true;
    }
}
