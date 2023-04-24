package io.github.shinyumbreon197.mobheadsv3.event;

import com.sun.jna.platform.unix.solaris.LibKstat;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.effect.PotionFX;
import io.github.shinyumbreon197.mobheadsv3.effect.WornMechanics;
import io.github.shinyumbreon197.mobheadsv3.entity.Summon;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.effect.AfflictedEffects;
import io.github.shinyumbreon197.mobheadsv3.effect.AVFX;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.UUID;

public class AttackDamageDeathEvents implements Listener {

    @EventHandler
    public void onEntityTakeDamage(EntityDamageEvent e){
        if (!(e.getEntity() instanceof LivingEntity))return;
        LivingEntity damaged = (LivingEntity) e.getEntity();
        damageHandler(e);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        deathHandler(e);
    }

    @EventHandler
    public static void onProjectileHit(ProjectileHitEvent e){
        Projectile projectile = e.getEntity();
        ProjectileSource source = projectile.getShooter();
        Entity entity = e.getHitEntity();
        MobHead hitMobHead = HeadUtil.getMobHeadFromEntity(projectile);
        if (hitMobHead != null) WornMechanics.projectileHitWearer(e, hitMobHead);
        PersistentDataContainer data = projectile.getPersistentDataContainer();
        if (!data.has(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING) || !(entity instanceof LivingEntity))return;
        LivingEntity livingEntity = (LivingEntity) entity;
        String UUIDString = data.get(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING);
        LivingEntity shooter = null;
        MobHead shooterMobHead = null;
        if (UUIDString != null && source instanceof LivingEntity && UUIDString.equals(((LivingEntity) source).getUniqueId().toString())){
            shooter = (LivingEntity) source;
            shooterMobHead = HeadUtil.getMobHeadFromEntity(shooter);
        }
        if (shooter == null || shooterMobHead == null)return;
        WornMechanics.wearerProjectileHitEntity(e, shooterMobHead, shooter, livingEntity);
    }

    private void damageHandler(EntityDamageEvent e){
        //if (e instanceof EntityDamageByBlockEvent)return;
        if (!(e.getEntity() instanceof LivingEntity))return;
        boolean damageByEntityEvent = e instanceof EntityDamageByEntityEvent;
        LivingEntity damaged = (LivingEntity) e.getEntity();
        LivingEntity attacker = null;
        if (damageByEntityEvent){
            Entity entity = ((EntityDamageByEntityEvent) e).getDamager();
            if (entity instanceof Projectile){
                Projectile projectile = (Projectile) entity;
                if (projectile.getShooter() instanceof LivingEntity) attacker = (LivingEntity) projectile.getShooter();
            }else if (entity instanceof LivingEntity){
                attacker = (LivingEntity) entity;
            }else return;
        }
        if (Summon.isSummon(attacker)){
            if (attacker.getType().equals(EntityType.BEE)){
                Bee beeSummon = (Bee) attacker;
                beeSummon.setHealth(0);
            }
        }
        if (Summon.isSummon(damaged)){
            if (damaged.getHealth() == 0 || damaged.isDead()) damaged.setCustomName(null);
        }
        EntityDamageEvent.DamageCause damageCause = e.getCause();
        MobHead damagedHead = null;
        MobHead attackerHead = null;
        if (damaged.getEquipment() != null) damagedHead = HeadUtil.getMobHeadFromHeadItem(damaged.getEquipment().getHelmet());
        if (damageByEntityEvent && attacker.getEquipment() != null) attackerHead = HeadUtil.getMobHeadFromHeadItem(attacker.getEquipment().getHelmet());

        boolean canceled = false;

        if (damageByEntityEvent && attackerHead != null){
            EntityType headType = attackerHead.getEntityType();
            if (!damaged.isDead()){damaged.addPotionEffects(AfflictedEffects.getPotionEffects(headType));}
            switch (headType){
                default -> {}
                case ENDERMAN -> {
                    if (damaged.getType().equals(EntityType.ENDERMAN) && attacker.getType().equals(EntityType.PLAYER)){
                        WornMechanics.addEndermanAggroMap(damaged.getUniqueId(),(Player) attacker);
                    }
                }
            }
        }
        if (damagedHead != null){
            EntityType headType = damagedHead.getEntityType();
            switch (headType){
                default -> {}
                case WITHER_SKELETON, WITHER -> {if (damageCause.equals(EntityDamageEvent.DamageCause.WITHER)) canceled = true;}
                case FROG -> {if (damageCause.equals(EntityDamageEvent.DamageCause.FALL)){canceled = !WornMechanics.frogFallDamage(e);}}
                case WOLF, SILVERFISH, BEE -> {WornMechanics.summonReinforcements(damaged, attacker, headType);}
                case ENDERMAN -> {WornMechanics.endermanDamageEffect(damaged, damageCause);}
                case RABBIT -> {if (e instanceof EntityDamageByEntityEvent) WornMechanics.gainEffectsOnDamagedByEntity(damaged, headType);}
                case LLAMA, TRADER_LLAMA -> {if (e instanceof EntityDamageByEntityEvent) WornMechanics.llamaSpit(damaged, attacker);}
            }
        }

        e.setCancelled(canceled);

        boolean playSound = damagedHead != null && !e.isCancelled() && e.getFinalDamage() > 0;
        if (playSound) AVFX.playHeadHurtSound(damaged);
    }

    private void deathHandler(EntityDeathEvent e){
        if (e.getEntity().getEquipment() == null)return;
        MobHead mobHead = HeadUtil.getMobHeadFromEntity(e.getEntity());
        if (mobHead == null)return;

        switch (mobHead.getEntityType()){
            default -> {}
        }
        AVFX.playHeadDeathSound(e.getEntity());
    }

}
