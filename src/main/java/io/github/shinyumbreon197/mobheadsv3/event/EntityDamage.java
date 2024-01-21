package io.github.shinyumbreon197.mobheadsv3.event;

import com.comphenix.protocol.PacketType;
import io.github.shinyumbreon197.mobheadsv3.AVFX;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.data.Key;
import io.github.shinyumbreon197.mobheadsv3.entity.Summon;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import io.github.shinyumbreon197.mobheadsv3.function.PotionEffectManager;
import io.github.shinyumbreon197.mobheadsv3.function.Util;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;
import static io.github.shinyumbreon197.mobheadsv3.entity.Summon.getSummonTypes;

public class EntityDamage implements Listener {

    @EventHandler
    public static void onEntityDamage(EntityDamageEvent ede){
        Entity damaged = ede.getEntity();
        Entity damager;
        MobHead damagedHead = MobHead.getMobHeadWornByEntity(damaged);
        MobHead damagerHead;
        if (ede instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) ede;
            boolean projectile = false;
            damager = edbee.getDamager();
            if (Summon.entityIsSummon(damager)){
                Mob summon = (Mob) damager;
                switch (summon.getType()){
                    case BEE -> {
                        PotionEffectManager.addEffectToEntity(damaged, Summon.getSummonAfflictionEffect(EntityType.BEE));
                        Summon.dispelSummon(summon);
                    }
                }
            }
            if (damager instanceof Projectile){
                //if (debug) System.out.println(((Projectile)damager).getPersistentDataContainer()); //debug
                if (isAbilityProjectile((Projectile) damager)){
                    //if (debug) System.out.println("isAbilityProjectile"); //debug
                    EntityType abilityType = getAbilityDamageType(damager);
                    if (abilityType != null){
                        switch (abilityType){
                            case LLAMA -> {ede.setDamage(3);}
                        }
                        Util.addAbilityDamageData(damaged, abilityType);
                    }
                }
                damager = Util.getTrueAttacker(damager);
                projectile = true;
            }
            if (debug) System.out.println("projectile: " + projectile); //debug
            if (debug && damager instanceof Player){
                Player player = (Player) damager;
                player.sendMessage(
                      player.getDisplayName() + " damaged " + damaged.getName() + " for " + edbee.getDamage() +
                      " of type " + edbee.getCause().name()
                );
            }
            //if (debug) System.out.println("onEntityDamage() projectile: " + projectile + " damager: " + damager); //debug
            if (damagedHead != null) headedEntityTakeDamageFromEntity(damagedHead, (EntityDamageByEntityEvent) ede);
            damagerHead = MobHead.getMobHeadWornByEntity(damager);
            if (damagerHead != null){
                headedEntityDamageEntity(damagerHead,projectile,edbee);
            }
        }
        if (damagedHead != null && !ede.isCancelled()) headedEntityTakeDamage(damagedHead, ede);
        //if (debug) System.out.println("damage: " + ede.getDamage()); //debug
    }
    private static boolean isAbilityProjectile(Projectile projectile){
        PersistentDataContainer data = projectile.getPersistentDataContainer();
        return data.has(Key.abilityProjectile, PersistentDataType.STRING);
    }
    private static EntityType getAbilityDamageType(Entity source){
        EntityType abilityType = null;
        PersistentDataContainer data = source.getPersistentDataContainer();
        if (!data.has(Key.abilityProjectile, PersistentDataType.STRING))return null;
        String stringType = data.get(Key.abilityProjectile,PersistentDataType.STRING);
        if (stringType == null)return null;
        if (stringType.matches("LLAMA") || stringType.matches("TRADER_LLAMA")) abilityType = EntityType.LLAMA;
        return abilityType;
    }

    private static void headedEntityTakeDamage(MobHead damagedHead, EntityDamageEvent ede){
        EntityType damagedHeadType = damagedHead.getEntityType();
        CreatureEvents.damageTypeResistance(damagedHeadType,ede);
        if (!ede.isCancelled()) CreatureEvents.damageTypeReactions(damagedHeadType,ede);

        if (ede.isCancelled())return;
        if (ede.getFinalDamage() != 0) AVFX.playHeadHurtSound((LivingEntity) ede.getEntity(),damagedHead);
    }

    private static final List<EntityDamageEvent.DamageCause> attackCauses =  List.of(
            EntityDamageEvent.DamageCause.ENTITY_ATTACK, EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK,
            EntityDamageEvent.DamageCause.PROJECTILE
    );

    private static void headedEntityTakeDamageFromEntity(MobHead damagedHead, EntityDamageByEntityEvent edbee){
        LivingEntity damaged = (LivingEntity) edbee.getEntity();
        EntityDamageEvent.DamageCause cause = edbee.getCause();
        //if (debug) System.out.println("headedEntityTakeDamageFromEntity() cause: " + cause); //debug
        if (Util.hasTakenAbilityDamage(damaged)){
            //if (debug) System.out.println("hasTakenAbilityDamage damaged: " + damaged); //debug
            cause = EntityDamageEvent.DamageCause.CUSTOM;
        }
        if (!attackCauses.contains(cause))return;
//        if (debug) System.out.println(
//                "headedEntityTakeDamageFromEntity()\ndamagedHead: " +damagedHead.getDisplayName() + "\ndamager: " + edbee.getDamager().getName()
//        );
        EntityType damagedHeadType = damagedHead.getEntityType();
        Entity damager = edbee.getDamager();
        if (damagedHeadType.equals(EntityType.GOAT) && cause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) && (CreatureEvents.isGoatPhysicalAttackImmune(damaged))){
            //if (debug) System.out.println("goat + entity_attack + isphysicalattackimmune"); //debug
            AVFX.playGoatInvulnerableSound(damager.getLocation());
            edbee.setDamage(0);
            edbee.setCancelled(true);
            return;
        }
        if (getSummonTypes().contains(damagedHeadType)) CreatureEvents.spawnSummon(damagedHeadType,edbee);

        boolean projectile = false;
        if (damager instanceof Projectile){
            damager = Util.getTrueAttacker(damager);
            projectile = true;
        }
        if (damager instanceof LivingEntity){
            LivingEntity livDamager = (LivingEntity) damager;
            CreatureEvents.applyRetaliationEffects(damagedHeadType, damaged,  livDamager, projectile);
        }

    }

    private static void headedEntityDamageEntity(MobHead damagerHead, boolean projectile, EntityDamageByEntityEvent edbee){
        EntityType damagerHeadType = damagerHead.getEntityType();
        Entity damaged = edbee.getEntity();
        EntityDamageEvent.DamageCause cause = edbee.getCause();
        if (Util.hasTakenAbilityDamage(damaged)) cause = EntityDamageEvent.DamageCause.CUSTOM;
        if (!attackCauses.contains(cause))return;
        if (projectile) damaged = Util.getTrueAttacker(damaged);

//        if (debug) System.out.println("headedEntityDamageEntity() damaged: " + damaged.getName() +
//                " damager: " + edbee.getDamager().getName() + " cause: " + cause + " damage: " + edbee.getDamage()
//        );

        if (damaged instanceof LivingEntity){
            LivingEntity livDamaged = (LivingEntity) damaged;
            CreatureEvents.applyAfflictionsToTarget(damagerHeadType,livDamaged,projectile);
        }
    }

}
