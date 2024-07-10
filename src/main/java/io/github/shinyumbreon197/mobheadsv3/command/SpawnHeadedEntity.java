package io.github.shinyumbreon197.mobheadsv3.command;

import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.gui.MobHeadGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class SpawnHeadedEntity implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender){
            System.out.println(MobHeadsV3.getPluginName() + "This command is usable by Players only!");
            return true;
        }else if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            if (player.isOp()){
                new BukkitRunnable(){
                    public void run(){
                        MobHeadGUI.openSummonGUI(player, List.of(strings));
                    }
                }.run();
            }else MobHeadsV3.messagePlayer(player, ChatColor.RED + "This command is for operators only.");
            return true;
        }
        return false;
    }

}
