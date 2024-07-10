package io.github.shinyumbreon197.mobheadsv3.command;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.data.Key;
import io.github.shinyumbreon197.mobheadsv3.gui.MobHeadGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

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
                    boolean repairedItem = repairedHeadItem != null;
                    boolean repairedBlock = false;
                    if (debug) System.out.println("repairedItem = " + repairedItem);
                    if (!repairedItem){
                        Block headBlock = player.getLocation().add(0,-0.1, 0).getBlock();
                        if (debug) System.out.println("repair block target is: " + headBlock);
                        if (debug) System.out.println("Block was tilestate? " + (headBlock.getState() instanceof TileState));
                        if (headBlock.getState() instanceof TileState){
                            TileState tileState = (TileState) headBlock.getState();
                            PersistentDataContainer data = tileState.getPersistentDataContainer();
                            NamespacedKey key = Key.getHeadIDStorageType(data);
                            if (debug) System.out.println("namespacekey = " + key);
                            if (key != null){
                                String uuid = data.get(key, PersistentDataType.STRING);
                                if (debug) System.out.println("uuid = " + uuid);
                                if (uuid != null){
                                    MobHead mobHead = MobHead.getMobHeadFromUUID(UUID.fromString(uuid));
                                    if (debug) System.out.println("mobHead = " + mobHead);
                                    if (mobHead != null){
                                        repairedHeadItem = mobHead.getHeadItemStack();
                                        if (debug) System.out.println("repairedHeadItem = " + repairedHeadItem);
                                        repairedBlock = true;
                                        headBlock.setType(Material.AIR);
                                    }
                                }
                            }
                        }
                    }
                    if (repairedItem || repairedBlock){
                        MobHeadsV3.messagePlayer(player, ChatColor.YELLOW + "Head(s) were repaired/updated.");
                    }else{
                        MobHeadsV3.messagePlayer(player, ChatColor.RED + "Held item and/or block below was invalid or could not be repaired.");
                    }
                    if (repairedItem){
                        player.getInventory().setItemInMainHand(repairedHeadItem);
                    }else if (repairedBlock){
                        Map<Integer,ItemStack> map = player.getInventory().addItem(repairedHeadItem);
                        for (ItemStack item: map.values()){
                            player.getWorld().dropItem(player.getLocation(), item);
                        }
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
