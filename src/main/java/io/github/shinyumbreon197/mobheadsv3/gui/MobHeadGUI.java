package io.github.shinyumbreon197.mobheadsv3.gui;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MobHeadGUI implements Listener {

    private static final Map<Player, List<String>> argsMap = new HashMap<>();

    private static final List<Material> whitelistedMats = Arrays.asList(
            Material.PLAYER_HEAD, Material.STONE_BUTTON,
            Material.POLISHED_BLACKSTONE_BUTTON, Material.BARRIER,
            Material.CREEPER_HEAD, Material.ZOMBIE_HEAD, Material.SKELETON_SKULL,
            Material.WITHER_SKELETON_SKULL, Material.DRAGON_HEAD, Material.PIGLIN_HEAD
    );

    @EventHandler
    public static void onHeadGUIClick(InventoryClickEvent e){
        if (!e.getView().getType().equals(InventoryType.CHEST))return;
        String title = e.getView().getTitle();
        if (!title.contains(headGUIName))return;
        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        if (e.getClickedInventory() == null){
            e.setCancelled(true); close(player); return;
        }else if (clickedItem != null && e.getClickedInventory().equals(e.getView().getTopInventory())){
            clickedItem = clickedItem.clone();
            Material clickedItemMaterial = clickedItem.getType();
            int pageIndex;
            try{
                pageIndex = Integer.parseInt(""+title.charAt(title.length()-1)) -1;
            }catch (NumberFormatException ex){
                ex.printStackTrace();
                pageIndex = 0;
            }
            switch (clickedItemMaterial){
                case BARRIER: player.closeInventory(); break;
                case STONE_BUTTON:
                    player.openInventory(headInvs().get(pageIndex-1));
                    break;
                case POLISHED_BLACKSTONE_BUTTON:
                    player.openInventory(headInvs().get(pageIndex+1));
                    break;
                case PLAYER_HEAD, ZOMBIE_HEAD, SKELETON_SKULL, WITHER_SKELETON_SKULL, CREEPER_HEAD, DRAGON_HEAD, PIGLIN_HEAD:
                    if (player.isOp() || player.getGameMode().equals(GameMode.CREATIVE)){
                        if (argsMap.containsKey(player)){
                            spawnHeadedEntity(player, clickedItem);
                        }else{
                            addHeadItem(player, clickedItem, e.getClick());
                        }
                    }
                    break;
            }
        }
        e.setCancelled(true);
    }

    // Public Functions ----------------------------------------------------------------
    public static void openSpawnGUI(Player player){
        player.openInventory(headInvs().get(0));
    }

    public static void openSummonGUI(Player player, List<String> args){
        argsMap.put(player, args);
        player.openInventory(headInvs().get(0));
    }

    // Private Functions ----------------------------------------------------------------
    private static void close(Player player){
        player.closeInventory();
        //player.updateInventory();
        argsMap.remove(player);
    }

    private static void addHeadItem(Player player, ItemStack headItem, ClickType clickType){
        headItem.setAmount(1);
        int added = 1;
        boolean stack = clickType.equals(ClickType.SHIFT_LEFT);
        int emptySlot = player.getInventory().firstEmpty();
        if (emptySlot == -1){
            MobHeadsV3.messagePlayer(player, ChatColor.RED+"Your inventory is full!");
            return;
        }
        boolean merge = false;
        int invIndex = -1;
        for (ItemStack item:player.getInventory().getContents()){
            invIndex++;
            if (item == null)continue;
            if (headItem.getItemMeta() == null || item.getItemMeta() == null)continue;
            if (!headItem.getType().equals(item.getType())) continue;
            if (item.getAmount() >= item.getType().getMaxStackSize())continue;
            if (!headItem.getItemMeta().equals(item.getItemMeta()))continue;

            merge = true;
            break;
        }
        //System.out.println("ClickType:"+clickType+" stack:"+stack+" merge:"+merge); //debug
        if (stack){
            headItem.setAmount(64);
            added = 64;
            player.getInventory().setItem(emptySlot, headItem);
        }else if (merge){
            ItemStack mergedStack = player.getInventory().getItem(invIndex);
            if (mergedStack == null)return;
            int newCount = mergedStack.getAmount()+1;
            mergedStack.setAmount(newCount);
            player.getInventory().setItem(invIndex, mergedStack);
        }else{
            player.getInventory().setItem(emptySlot, headItem);
        }
        if (headItem.getItemMeta() == null)return;

        player.sendMessage(MobHeadsV3.getPluginNameColored() + ChatColor.AQUA +
                "Spawned in " + headItem.getItemMeta().getDisplayName() +
                ChatColor.AQUA+" x"+added
        );
    }

    private static void spawnHeadedEntity(Player player, ItemStack headItem){
        List<String> args = argsMap.get(player);
        EntityType entityType;
        try{
            entityType = EntityType.valueOf(args.get(0));
        }catch (IllegalArgumentException ignored){
            close(player);
            return;
        }
        World world = player.getWorld();
        Entity entity = world.spawnEntity(player.getLocation(), entityType);
        if (!(entity instanceof LivingEntity) || ((LivingEntity) entity).getEquipment() == null){
            entity.remove();
            return;
        }
        LivingEntity livingEntity = (LivingEntity) entity;
        livingEntity.getEquipment().setHelmet(headItem);
        close(player);
    }

    // Data -----------------------------------------------------------------------------

    // Inventories -----------------------------------------------------------------------
    public static List<Inventory> headInvs(){
        List<Inventory> invs = new ArrayList<>();
        List<ItemStack> items = new ArrayList<>();
        int index = 0;
        int headIndex = 0;
        for (MobHead mobHead: MobHead.getMobHeads()){
            ItemStack headItem = mobHead.getHeadItemStack();
            items.add(index, headItem);
            index++;
            if (index > 50 | headIndex == MobHead.getMobHeads().size()-1){
                Inventory inv = Bukkit.createInventory(
                        null, 54, headGUIName+" Page: "+(invs.size()+1)
                );
                int invIndex = 0;
                for (ItemStack item:items){
                    inv.setItem(invIndex, item);
                    invIndex++;
                }
                items.clear();
                if (headIndex > 54){
                    inv.setItem(51, invPrevButton());
                }
                if (headIndex != MobHead.getMobHeads().size()-1){
                    inv.setItem(52, invNextButton());
                }
                inv.setItem(53, invCloseButton());
                invs.add(inv);
                index = 0;
            }
            headIndex++;
        }
        return invs;
    }

    // Strings
    private static final String headGUIName = MobHeadsV3.getPluginName()+"Head GUI";

    // ItemStacks ------------------------------------------------------------------------
    private static ItemStack invNextButton(){
        ItemStack button = new ItemStack(Material.POLISHED_BLACKSTONE_BUTTON);
        ItemMeta buttonMeta = button.getItemMeta();
        assert buttonMeta != null;
        buttonMeta.setDisplayName(ChatColor.YELLOW+"Next Page -->");
        button.setItemMeta(buttonMeta);
        return button;
    }

    private static ItemStack invPrevButton(){
        ItemStack button = new ItemStack(Material.STONE_BUTTON);
        ItemMeta buttonMeta = button.getItemMeta();
        assert buttonMeta != null;
        buttonMeta.setDisplayName(ChatColor.YELLOW+"<-- Previous Page");
        button.setItemMeta(buttonMeta);
        return button;
    }

    private static ItemStack invCloseButton(){
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        assert barrierMeta != null;
        barrierMeta.setDisplayName(ChatColor.RED+"Exit Menu");
        barrier.setItemMeta(barrierMeta);
        return barrier;
    }

}
