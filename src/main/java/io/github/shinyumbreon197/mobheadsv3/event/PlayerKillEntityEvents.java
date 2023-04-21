package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.DecollationSmith;
import io.github.shinyumbreon197.mobheadsv3.Data;
import io.github.shinyumbreon197.mobheadsv3.effect.AVFX;
import io.github.shinyumbreon197.mobheadsv3.head.passive.multi.FrogHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class PlayerKillEntityEvents implements Listener {

    @EventHandler
    public void onPlayerKillEntity(EntityDeathEvent e){
        LivingEntity killed = e.getEntity();
        Player player = e.getEntity().getKiller();
        boolean frogKill = false;
        boolean headDropEligible = true;
        PersistentDataContainer data = killed.getPersistentDataContainer();
        String killerUUIDString = data.get(FrogHead.frogFoodKey, PersistentDataType.STRING);
        if (killerUUIDString != null){
            UUID killerUUID = UUID.fromString(killerUUIDString);
            player = Bukkit.getPlayer(killerUUID);
            frogKill = true;
        }
        if (player == null)return;
        if (killed instanceof Ageable && !((Ageable) killed).isAdult())headDropEligible = false;
        if (!Data.entityTypes.contains(killed.getType()))headDropEligible = false;
        ItemStack headItemDrop = null;
        if (headDropEligible) headItemDrop = playerKillAdditionalDrops(player, killed);
        if (headItemDrop != null){
            e.getDrops().add(headItemDrop);
            AVFX.playHeadDropEffect(killed.getEyeLocation());
        }
        if (frogKill && !(killed instanceof Player)){
            List<ItemStack> overflow = new ArrayList<>();
            for (ItemStack drop:e.getDrops()){
                if (Data.getFoodMats().contains(drop.getType()))continue;
                Map<Integer, ItemStack> overflowMap = player.getInventory().addItem(drop);
                if (overflowMap.size() != 0){
                    overflow.add(overflowMap.get(0));
                }
            }
            for (ItemStack drop:overflow){
                player.getWorld().dropItem(player.getLocation(), drop);
            }
            e.getDrops().clear();
        }
    }

    private ItemStack playerKillAdditionalDrops(Player player, LivingEntity killed){
        ItemStack drop = null;
        List<EntityDamageEvent.DamageCause> weaponCauses = Arrays.asList(
                EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK, EntityDamageEvent.DamageCause.ENTITY_ATTACK
        );
        EntityDamageEvent entityDamageEvent = killed.getLastDamageCause();
        boolean withWeapon = entityDamageEvent != null && weaponCauses.contains(entityDamageEvent.getCause());

        Random random = new Random();
        boolean success;
        boolean guaranteed = false;
        //boolean guaranteed = true;
        int chance = 4;
        int roll = random.nextInt(199);
        if (withWeapon){
            ItemStack weapon = player.getInventory().getItemInMainHand();
            if (DecollationSmith.hasDecollation(weapon)){
                guaranteed = true;
                ItemStack replacement = DecollationSmith.removeDecollation(weapon);
                player.getInventory().setItemInMainHand(replacement);
            }else{
                if (weapon.containsEnchantment(Enchantment.LOOT_BONUS_MOBS)){
                    int enchLv = weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
                    chance = chance + enchLv*2;
                }
            }
        }
        success = guaranteed || roll < chance;
        if (success){
            ItemStack headItem = HeadUtil.GetHeadItemFromEntity(killed);
            if (headItem != null) drop = headItem;
        }
        return drop;
    }

}
