package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.AVFX;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.entity.Summon;
import io.github.shinyumbreon197.mobheadsv3.event.main.MainThread;
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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;
import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.getPlugin;

public class EntityDeath implements Listener {

    @EventHandler
    public static void onEntityDeath(EntityDeathEvent deathEvent){
        //if (debug) System.out.println("EntityDeathEvent"); //debug
        MobHead deadHead = MobHead.getMobHeadWornByEntity(deathEvent.getEntity());
        if (deadHead != null){
            AVFX.playHeadDeathSound(deathEvent.getEntity(), deadHead);
            new BukkitRunnable(){
                @Override
                public void run() {
                    MainThread.headRemovalEffects(deathEvent.getEntity(), deadHead);
                }
            }.runTaskLater(getPlugin(), 1);
        }
        if (deathEvent.getEntity().getType().equals(EntityType.ARMOR_STAND))return;
        if (deathEvent.getEntity() instanceof Mob){
            CreatureEvents.removeFromAttackAggroMap((Mob)deathEvent.getEntity());
        }
        if (Summon.isEntitySummon(deathEvent.getEntity()))return;
        EntityDamageEvent damageEvent = deathEvent.getEntity().getLastDamageCause();
        if (damageEvent instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) damageEvent;
            Entity damager = edbee.getDamager();
            MobHead mobHead = MobHead.getMobHeadWornByEntity(damager);
            if (mobHead != null) CreatureEvents.killedByHeadedCreature(damager, mobHead, deathEvent.getEntity());
        }
        HeadItemDrop.creatureDeath(deathEvent,damageEvent);
        EntityType abilityType = Util.getAbilityDamageData(deathEvent.getEntity());
        if (debug) System.out.println("EntityDeathEvent: abilityType = " + abilityType);
        if (abilityType != null){
            Entity killed = deathEvent.getEntity();
            Entity killer = null;
            if (killed.getLastDamageCause() instanceof EntityDamageByEntityEvent){
                if (debug) System.out.println("Before Killer Correction: " + killer);
                killer = Util.getTrueAttacker(((EntityDamageByEntityEvent)killed.getLastDamageCause()).getDamager());
                if (debug) System.out.println("After Killer Correction: " + killer);
                //killer = ((EntityDamageByEntityEvent)killed.getLastDamageCause()).getDamager();
            }
            if (killer != null){
                switch (abilityType){
                    case FROG -> {
                        if (killer instanceof Player && !(killed instanceof Player)){
                            List<ItemStack> drops = new ArrayList<>(deathEvent.getDrops());
                            if (debug) System.out.println("Frog Kill drops: " + drops);
                            MobHead mobHead = MobHead.getMobHeadWornByEntity(killer);
                            if (mobHead != null && mobHead.getEntityType().equals(EntityType.FROG) && killed.getType().equals(EntityType.MAGMA_CUBE)){
                                String variant = mobHead.getVariant();
                                if (variant.matches("temperate")){
                                    drops.add(new ItemStack(Material.OCHRE_FROGLIGHT));
                                }else if (variant.matches("cold")){
                                    drops.add(new ItemStack(Material.VERDANT_FROGLIGHT));
                                }else if (variant.matches("warm")){
                                    drops.add(new ItemStack(Material.PEARLESCENT_FROGLIGHT));
                                }
                            }
                            CreatureEvents.frogGetDrops((Player) killer, drops);
                            deathEvent.getDrops().clear();
                        }
                    }
                }
                if (deathEvent instanceof PlayerDeathEvent){
                    PlayerDeathEvent pde = (PlayerDeathEvent) deathEvent;
                    String deathMessage = pde.getDeathMessage();
                    if (deathMessage != null){
                        String killerName = killer.getName();
                        String killedName = killed.getName();
                        deathMessage = deathMessage.substring(killedName.length());
                        deathMessage = deathMessage.replace(killedName,killerName);
                        deathMessage = killedName + deathMessage;
                        pde.setDeathMessage(deathMessage);
                    }

                }
            }
        }
    }


}
