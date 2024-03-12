package io.github.shinyumbreon197.mobheadsv3.command;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.gui.MobHeadGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class HeadCommands implements CommandExecutor, TabCompleter {

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
            int argsAmount = args.length;
            if (argsAmount == 0 || args[0].matches("list")){
                new BukkitRunnable(){
                    public void run(){
                        MobHeadGUI.openSpawnGUI(player);
                    }
                }.run();
                return true;
            }else if (argsAmount == 1){
                String function = args[0];
                if (function.matches("repair")){
                    ItemStack headItem = player.getInventory().getItemInMainHand();
                    ItemStack repairedHeadItem = MobHead.repairMobheadItemstack(headItem);
                    boolean success = repairedHeadItem != null;
                    if (success){
                        player.getInventory().setItemInMainHand(repairedHeadItem);
                        MobHeadsV3.messagePlayer(player, ChatColor.YELLOW + "Head(s) were repaired/updated.");
                    }else{
                        MobHeadsV3.messagePlayer(player, ChatColor.RED + "Held item was invalid or could not be repaired.");
                    }
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        List<String> completions = new ArrayList<>();
        if (!(sender instanceof Player))return commands;
        Player player = (Player) sender;
        int size = args.length;

        if (command.getName().matches("mobheads")){
            if (size == 1){
                String zero = args[0];
                commands.add("list");
                commands.add("repair");
                StringUtil.copyPartialMatches(zero, commands, completions);
            }
        }

        return commands;
    }
}
