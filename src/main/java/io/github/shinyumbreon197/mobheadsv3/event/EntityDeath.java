package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.AVFX;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.entity.Summon;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import io.github.shinyumbreon197.mobheadsv3.function.HeadItemDrop;
import io.github.shinyumbreon197.mobheadsv3.function.Util;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EntityDeath implements Listener {

    @EventHandler
    public static void onEntityDeath(EntityDeathEvent deathEvent){
        //if (debug) System.out.println("EntityDeathEvent"); //debug
        if (deathEvent.getEntity().getType().equals(EntityType.ARMOR_STAND))return;
        if (Summon.isEntitySummon(deathEvent.getEntity()))return;
        EntityDamageEvent damageEvent = deathEvent.getEntity().getLastDamageCause();
        HeadItemDrop.creatureDeath(deathEvent,damageEvent);
        EntityType abilityType = Util.getAbilityDamageData(deathEvent.getEntity());
        if (abilityType != null){
            Entity killed = deathEvent.getEntity();
            Entity killer = null;
            if (killed.getLastDamageCause() instanceof EntityDamageByEntityEvent){
                killer = ((EntityDamageByEntityEvent)killed.getLastDamageCause()).getDamager();
            }
            if (killer != null){
                switch (abilityType){
                    case FROG -> {
                        if (killer instanceof Player && !(killed instanceof Player)){
                            List<ItemStack> drops = new ArrayList<>(deathEvent.getDrops());
                            MobHead mobHead = MobHead.getMobHeadWornByEntity(killer);
                            if (mobHead != null && mobHead.getEntityType().equals(EntityType.FROG) && killed.getType().equals(EntityType.MAGMA_CUBE)){
                                String variant = mobHead.getVariant();
                                if (variant.matches(Frog.Variant.TEMPERATE.toString())){
                                    drops.add(new ItemStack(Material.OCHRE_FROGLIGHT));
                                }else if (variant.matches(Frog.Variant.COLD.toString())){
                                    drops.add(new ItemStack(Material.VERDANT_FROGLIGHT));
                                }else if (variant.matches(Frog.Variant.WARM.toString())){
                                    drops.add(new ItemStack(Material.PEARLESCENT_FROGLIGHT));
                                }
                            }
                            CreatureEvents.frogGetDrops((Player) killer, drops);
                            deathEvent.getDrops().clear();
                        }
                    }
                }
            }
        }
        MobHead deadHead = MobHead.getMobHeadWornByEntity(deathEvent.getEntity());
        if (deadHead == null)return;
        AVFX.playHeadDeathSound(deathEvent.getEntity(), deadHead);
    }


}
