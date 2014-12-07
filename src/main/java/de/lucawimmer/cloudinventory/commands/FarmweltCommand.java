package de.lucawimmer.cloudinventory.commands;

import de.lucawimmer.cloudinventory.api.CloudInventoryAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FarmweltCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        CloudInventoryAPI.sendToServer(commandSender.getName(), "server1");
        return true;
    }
}
