package io.github.shinyumbreon197.mobheadsv3.command;

import io.github.shinyumbreon197.mobheadsv3.data.Key;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class DebugEventsAndCommands implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (!player.isOp()){
                player.sendMessage(ChatColor.RED + "This command is for operators only.");
                return true;
            }
            if (command.getName().matches("mhdebug")){
                int length = args.length;
                if (length == 0)return false;
                String a0 = args[0];
                if (a0.matches("stick")){
                    player.getInventory().addItem(debugStick());
                }else if (a0.matches("chest")){
                    Block block = player.getLocation().add(0,-1,0).getBlock();
                    if (!block.getType().equals(Material.CHEST)){
                        player.sendMessage("Not a chest");
                        return true;
                    }
                    Chest chest = (Chest) block.getState();
                    player.sendMessage(String.valueOf(chest.getSeed()));
                }
            }
        }
        return false;
    }

    @EventHandler
    public static void onUseDebugStick(PlayerInteractEvent pie){
        Player player = pie.getPlayer();
        ItemStack stick = null;
        if (pie.getHand() != null && pie.getHand().equals(EquipmentSlot.HAND)){
            stick = player.getInventory().getItemInMainHand();
        }
        if (stick == null)return;
        if (!itemIsDebugStick(stick))return;
        Block block = pie.getClickedBlock();
        if (block != null){
            BlockState blockState = block.getState();
            if (blockState instanceof Skull){
                Skull skull = (Skull) blockState;
                PersistentDataContainer data = skull.getPersistentDataContainer();
                data.set(Key.headUUID, PersistentDataType.STRING, "194117a2-5fac-11ed-9b6a-0242ac120002");
                skull.update(true);
            }
        }

    }

    private static boolean itemIsDebugStick(ItemStack stick){
        if (!stick.getType().equals(Material.STICK))return false;
        ItemMeta stickMeta = stick.getItemMeta();
        if (stickMeta == null)return false;
        PersistentDataContainer data = stickMeta.getPersistentDataContainer();
        String idString = data.get(Key.master, PersistentDataType.STRING);
        return idString != null && idString.matches(debugStickID);
    }
    private static ItemStack debugStick(){
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta stickMeta = stick.getItemMeta();
        assert stickMeta != null;
        PersistentDataContainer data = stickMeta.getPersistentDataContainer();
        data.set(Key.master, PersistentDataType.STRING, debugStickID);
        stickMeta.setItemName(debugStickID);
        stickMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        stickMeta.setRarity(ItemRarity.EPIC);
        stickMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stick.setItemMeta(stickMeta);
        return stick;
    }
    private static final String debugStickID = "MobHeads Debug Stick";

}
