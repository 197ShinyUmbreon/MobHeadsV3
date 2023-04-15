package io.github.shinyumbreon197.mobheadsv3.command;

import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.gui.MobHeadGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SpawnHeadedEntity implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender){
            System.out.println(MobHeadsV3.namePlain("This command is usable by Players only!"));
        }else if (commandSender instanceof Player){
            new BukkitRunnable(){
                public void run(){
                    MobHeadGUI.openSummonGUI((Player) commandSender, List.of(strings));
                }
            }.run();
            return true;
        }
        return false;
    }

}
