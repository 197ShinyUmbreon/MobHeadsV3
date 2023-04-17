package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.DecollationSmith;
import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.effect.AVFX;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MobHeadDropEvents implements Listener {

    @EventHandler
    public void onPlayerKillEntity(EntityDeathEvent e){
        Player player = e.getEntity().getKiller();
        if (player == null)return;
        LivingEntity killed = e.getEntity();
        if (killed instanceof Ageable && !((Ageable) killed).isAdult())return;
        if (!HeadData.entityTypes.contains(killed.getType()))return;

        ItemStack headItemDrop = playerKillAdditionalDrops(player, killed);
        if (headItemDrop != null){
            e.getDrops().add(headItemDrop);
            AVFX.playHeadDropEffect(killed.getEyeLocation());
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
