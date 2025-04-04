package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.AVFX;
import io.github.shinyumbreon197.mobheadsv3.Config;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.data.Groups;
import io.github.shinyumbreon197.mobheadsv3.data.Key;
import io.github.shinyumbreon197.mobheadsv3.entity.Summon;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import io.github.shinyumbreon197.mobheadsv3.function.Util;
import org.apache.maven.model.License;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.List;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class EntityDamage implements Listener {

    @EventHandler
    public static void onEntityDamage(EntityDamageEvent ede){
        Entity damaged = ede.getEntity();
        if (damaged.getType().equals(EntityType.ITEM)){
            Item item = (Item) damaged;
            ItemStack itemStack = item.getItemStack();
            if (CreatureEvents.chestedItemIsContainer(itemStack)){
                CreatureEvents.chestedContainerItemExplode(item);
            }
        }
        Entity damaging;
        MobHead damagedHead = MobHead.getMobHeadWornByEntity(damaged);
        MobHead damagingHead;
        if (ede instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) ede;
            boolean projectile = false;
            damaging = edbee.getDamager();
            if (damaging instanceof Projectile){
                //if (debug) System.out.println(((Projectile)damaging).getPersistentDataContainer()); //debug
                Entity source = Util.getTrueAttacker(damaging);
                if (isAbilityProjectile((Projectile) damaging)){
                    if (debug) System.out.println("isAbilityProjectile"); //debug
                    EntityType abilityType = getAbilityDamageType(damaging);
                    if (abilityType != null){
                        switch (abilityType){
                            case LLAMA -> {ede.setDamage(3);}
                            case ENDER_DRAGON -> ede.setDamage(6);
                            case BREEZE -> ede.setDamage(2);
                        }
                        Util.addAbilityDamageData(damaged, abilityType);
                    }
                }
                damaging = source;
                if (debug) System.out.println("Damager: " + damaging.getName() + " " + damaging.getUniqueId());
                projectile = true;
            }
            if (debug) System.out.println("projectile: " + projectile); //debug
            if (Summon.isEntitySummon(damaging) && damaged instanceof Damageable){
                LivingEntity summoner = Summon.getOwnerFromSummon(damaging);
                if (summoner != null){
                    Damageable damageable = (Damageable) damaged;
                    MobHead summonerHead = MobHead.getMobHeadWornByEntity(summoner);
                    if (summonerHead != null) Util.addAbilityDamageData(damageable, summonerHead.getEntityType());
                }
            }
            if (Summon.isEntitySummon(damaged)){
                if (Summon.getOwnerFromSummon(damaged).equals(damaging)){
                    ede.setCancelled(true);
                    return;
                }
            }
            if (debug && damaging instanceof Player){
                Player player = (Player) damaging;
                player.sendMessage(
                      player.getDisplayName() + " damaged " + damaged.getName() + " for " + edbee.getDamage() +
                      " of type " + edbee.getCause().name()
                );
            }
            //if (debug) System.out.println("onEntityDamage() projectile: " + projectile + " damaging: " + damaging); //debug
            if (Config.headEffects && damagedHead != null) headedEntityTakeDamageFromEntity(damagedHead, (EntityDamageByEntityEvent) ede);
            damagingHead = MobHead.getMobHeadWornByEntity(damaging);
            if (Config.headEffects && damagingHead != null){
                headedEntityDamageEntity(damagingHead,projectile,edbee);
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
        if (stringType.matches("BLAZE")) abilityType = EntityType.BLAZE;
        if (stringType.matches(EntityType.ENDER_DRAGON.toString())) abilityType = EntityType.ENDER_DRAGON;
        if (stringType.matches(EntityType.BREEZE.toString())) abilityType = EntityType.BREEZE;
        return abilityType;
    }

    private static void headedEntityTakeDamage(MobHead damagedHead, EntityDamageEvent ede){
        EntityType damagedHeadType = damagedHead.getEntityType();
        if (Config.headEffects) CreatureEvents.damageTypeResistance(damagedHeadType,ede);
        if (Config.headEffects && !ede.isCancelled()) CreatureEvents.damageTypeReactions(damagedHeadType,ede);

        if (ede.isCancelled())return;
        if (debug) System.out.println("FinalDamage: " + ede.getFinalDamage());
        boolean immune = false;
        if (ede.getEntity() instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) ede.getEntity();
            if (livingEntity.isDead())return;
            immune = livingEntity.getNoDamageTicks() > 10;
        }
        if (debug) System.out.println("immune: " + immune);
        if (ede.getFinalDamage() != 0 && !immune){
            AVFX.playHeadHurtSound((LivingEntity) ede.getEntity(),damagedHead);
        }
    }

    private static final List<EntityDamageEvent.DamageCause> attackCauses =  List.of(
            EntityDamageEvent.DamageCause.ENTITY_ATTACK, EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK,
            EntityDamageEvent.DamageCause.PROJECTILE, EntityDamageEvent.DamageCause.CUSTOM
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
        EntityType abilityDamaged = Util.getAbilityDamageData(damager);
        if (debug) System.out.println("Damager taken ability damage? " + abilityDamaged);
        if (abilityDamaged == EntityType.FROG){
            edbee.setCancelled(true);
            return;
        }
        if (Config.headEffects && Summon.isSummonType(damagedHeadType)) CreatureEvents.spawnSummon(damagedHeadType,edbee);

        boolean projectile = false;
        if (damager instanceof Projectile){
            damager = Util.getTrueAttacker(damager);
            projectile = true;
        }
        if (damager instanceof LivingEntity){
            LivingEntity livDamager = (LivingEntity) damager;
            if (Config.headEffects) CreatureEvents.applyRetaliationEffects(damagedHeadType, damaged,  livDamager, projectile);
        }

    }

    private static void headedEntityDamageEntity(MobHead damagingHead, boolean projectile, EntityDamageByEntityEvent edbee){
        if (!Config.headEffects)return;
        if (debug) System.out.println("damagingHead: " + damagingHead);
        EntityType damagingHeadType = damagingHead.getEntityType();
        Entity source = Util.getTrueAttacker(edbee.getDamager());
        if (!(source instanceof LivingEntity))return;
        LivingEntity damaging = (LivingEntity) source;
        Entity damaged = edbee.getEntity();
        EntityType damagedType = damaged.getType();
        EntityDamageEvent.DamageCause cause = edbee.getCause();
        if (Util.hasTakenAbilityDamage(damaged)) cause = EntityDamageEvent.DamageCause.CUSTOM;
        if (!attackCauses.contains(cause))return;
        if (projectile) damaged = Util.getTrueAttacker(damaged);

//        if (debug) System.out.println("headedEntityDamageEntity() damaged: " + damaged.getName() +
//                " damaging: " + edbee.getDamager().getName() + " cause: " + cause + " damage: " + edbee.getDamage()
//        );

        if (damaged instanceof LivingEntity){
            LivingEntity livDamaged = (LivingEntity) damaged;
            CreatureEvents.applyAfflictionsToTarget(damagingHeadType,livDamaged,projectile);
            if (damaged instanceof Mob){
                Mob mob = (Mob) damaged;
                CreatureEvents.addToAttackAggroMap(damaging,mob);
            }
            if ((damaged instanceof Monster || damaging.getType().equals(EntityType.WOLF)) && Groups.neutralTarget(damagingHeadType, damagedType)){
                CreatureEvents.nearbyTargetImposter((Mob) damaged,damaging,damagingHeadType);
            }
        }
    }

}
