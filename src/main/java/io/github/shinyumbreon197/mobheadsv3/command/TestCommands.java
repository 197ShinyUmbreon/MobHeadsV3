package io.github.shinyumbreon197.mobheadsv3.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class TestCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))return false;
        Player player = (Player) sender;
        if (!player.isOp())return true;
        Vector direction = new Vector(1,0,0);
        Location location = player.getLocation();
        location.setDirection(direction);
        player.teleport(location);

        return true;
    }
}
