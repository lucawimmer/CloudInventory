package de.lucawimmer.cloudinventory.commands;

import de.lucawimmer.cloudinventory.api.CloudInventoryAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FarmweltCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args[0].equals("lobby"))
            CloudInventoryAPI.sendToServer(commandSender.getName(), "lobby");
        else if(args[0].equals("farmwelt"))
            CloudInventoryAPI.sendToServer(commandSender.getName(), "server1");
        return true;
    }
}
