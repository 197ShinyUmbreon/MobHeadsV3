package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.DecollationSmith;
import io.github.shinyumbreon197.mobheadsv3.Effects;
import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MobHeadDropEvents implements Listener {

    @EventHandler
    public void onPlayerKillEntity(EntityDeathEvent e){
        Player player = e.getEntity().getKiller();
        if (player == null)return;
        LivingEntity killed = e.getEntity();
        if (!HeadData.entityTypes.contains(killed.getType()))return;

        if (killed.getLastDamageCause() == null){
            System.out.println("getLastDamageCause == null"); //debug
            return;
        }
        List<EntityDamageEvent.DamageCause> weaponCauses = Arrays.asList(
                EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK, EntityDamageEvent.DamageCause.ENTITY_ATTACK
        );
        EntityDamageEvent entityDamageEvent = killed.getLastDamageCause();
        EntityDamageEvent.DamageCause damageCause = entityDamageEvent.getCause();
        boolean withWeapon = weaponCauses.contains(damageCause);

        Random random = new Random();
        boolean success;
        boolean guaranteed = false;
        int chance = 4;
        int roll = random.nextInt(199);
        if (withWeapon){
            ItemStack weapon = player.getInventory().getItemInMainHand();
            if (DecollationSmith.hasDecollation(weapon)){
                guaranteed = true;
                ItemStack replacement =  DecollationSmith.removeDecollation(weapon);
                player.getInventory().setItemInMainHand(replacement);
            }else{
                if (weapon.containsEnchantment(Enchantment.LOOT_BONUS_MOBS)){
                    int enchLv = weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
                    if (enchLv == 1){chance = chance + 2;}
                    if (enchLv == 2){chance = chance + 4;}
                    if (enchLv == 3){chance = chance + 6;}
                }
            }

        }
        success = guaranteed || roll < chance;
        if (!success)return;

        ItemStack headItem = HeadItem.getHeadFromEntity(killed);
        if (headItem == null)return;

        e.getDrops().add(headItem);
        Effects.playHeadEffect(killed.getEyeLocation());
    }

}
