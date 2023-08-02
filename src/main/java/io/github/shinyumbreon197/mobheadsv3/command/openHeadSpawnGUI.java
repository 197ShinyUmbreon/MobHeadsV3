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

public class openHeadSpawnGUI implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender){
            System.out.println(
                    MobHeadsV3.getPluginName()+"This command can only be used in-game by a Player."
            );
            return true;
        }
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.isOp()){
                new BukkitRunnable(){
                    public void run(){
                        MobHeadGUI.openSpawnGUI(player);
                    }
                }.run();
            }else{
                player.sendMessage(
                        MobHeadsV3.nameColored(ChatColor.RED+"You are not an operator!")
                );
            }
            return true;
        }
        return false;
    }

}
