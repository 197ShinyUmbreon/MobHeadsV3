package io.github.shinyumbreon197.mobheadsv3.function;

import io.github.shinyumbreon197.mobheadsv3.AVFX;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class HeadEquip {

    public static void clickEquipHead(PlayerInteractEvent pie){
        if (debug) System.out.println("clickEquipHead()"); //debug
        Player player = pie.getPlayer();
        PlayerInventory inv = player.getInventory();
        ItemStack equippedHelm = inv.getHelmet();
        ItemStack skullItem = pie.getItem();
        assert skullItem != null;
        if (debug) System.out.println("skullItem: " + skullItem.getItemMeta().getDisplayName()); //debug
        if (skullItem.isSimilar(equippedHelm))return;
        ItemStack overflow = null;
        int stackSize = skullItem.getAmount();
        if (stackSize > 1){
            overflow = skullItem.clone();
            overflow.setAmount(stackSize - 1);
            skullItem.setAmount(1);
        }
        if (debug) System.out.println("overflow: " + overflow); //debug

        EquipmentSlot hand = pie.getHand();
        if (debug) System.out.println("hand: " + hand); //debug
        if (hand == null)return;

        ItemStack oldHelm = null;
        if (equippedHelm != null) oldHelm = equippedHelm.clone();
        if (debug) System.out.println("oldHelm: " + oldHelm); //debug
        inv.setHelmet(skullItem);
        if (hand.equals(EquipmentSlot.HAND)){
            inv.setItemInMainHand(oldHelm);
        }else if (hand.equals(EquipmentSlot.OFF_HAND)){
            inv.setItemInOffHand(oldHelm);
        }else return;

        if (overflow != null) {
            Map<Integer,ItemStack> overflowMap =  inv.addItem(overflow);
            if (overflowMap.size() > 0){
                for (ItemStack itemStack: overflowMap.values()){
                    player.getWorld().dropItem(player.getLocation(),itemStack);
                }
            }

        }

        AVFX.playHeadEquipSound(player.getEyeLocation()); // make client-side only

    }

}
