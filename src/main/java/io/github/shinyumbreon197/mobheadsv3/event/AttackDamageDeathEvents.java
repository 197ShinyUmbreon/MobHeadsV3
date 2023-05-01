package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.effect.AVFX;
import io.github.shinyumbreon197.mobheadsv3.effect.AfflictedEffects;
import io.github.shinyumbreon197.mobheadsv3.effect.WornMechanics;
import io.github.shinyumbreon197.mobheadsv3.entity.Summon;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;

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

    private void damageHandler(EntityDamageEvent ede){
        boolean canceled = false;
        LivingEntity damaged = (LivingEntity) ede.getEntity();
        LivingEntity attacker = null;
        MobHead damagedMobHead = HeadUtil.getMobHeadFromEntity(damaged);
        MobHead attackerMobHead = null;
        EntityType damagedHeadType = null;
        EntityType attackerHeadType = null;
        if (damagedMobHead != null) damagedHeadType = damagedMobHead.getEntityType();
        EntityDamageEvent.DamageCause damageCause = ede.getCause();

        if (ede instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) ede;
            if (edbee.getDamager() instanceof Projectile){
                Projectile projectile = (Projectile) edbee.getDamager();
                if (projectile.getShooter() instanceof LivingEntity){
                    attacker = (LivingEntity) projectile.getShooter();
                }
            }else attacker = (LivingEntity) edbee.getDamager();
            if (attacker != null){
                attackerMobHead = HeadUtil.getMobHeadFromEntity(attacker);
                if (attackerMobHead != null) attackerHeadType = attackerMobHead.getEntityType();
            }
        }

        if (Summon.isSummon(attacker)){
            if (attacker.getType().equals(EntityType.BEE)){
                attacker.setHealth(0);
            }
        }
        if (Summon.isSummon(damaged)){
            if (damaged.getHealth() == 0 || damaged.isDead()) damaged.setCustomName(null);
        }

        if (damagedHeadType != null){
            WornMechanics.gainEffectsOnDamagedByEntity(damaged, damagedHeadType);
            switch (damagedHeadType){
                case ENDERMAN -> {WornMechanics.endermanDamageEffect(damaged, damageCause);}
            }
            if (attacker == null){
                switch (damagedHeadType){
                    case WITHER_SKELETON, WITHER -> {if (damageCause.equals(EntityDamageEvent.DamageCause.WITHER)) canceled = true;}
                    case FROG -> {if (damageCause.equals(EntityDamageEvent.DamageCause.FALL)){canceled = !WornMechanics.frogFallDamage(ede);}}
                    case GOAT -> {if (damaged instanceof Player && damageCause.equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)){
                        double damage = Math.floor(ede.getDamage()*0.5);
                        if (damage != 0){
                            ede.setDamage(damage);
                            WornMechanics.goatBreakBlock((Player) damaged);
                        }else canceled = true;
                    }}
                }
            }else{
                switch (damagedHeadType){
                    case AXOLOTL -> {WornMechanics.axolotlRemoveTarget(damaged, attacker);}
                    case WOLF, SILVERFISH, BEE -> {WornMechanics.summonReinforcements(damaged, attacker, damagedHeadType);}
                    case LLAMA, TRADER_LLAMA -> {WornMechanics.llamaSpit(damaged, attacker);}
                    case GOAT -> {canceled = WornMechanics.goatInvuln(damageCause, damaged);}
                }
            }
        }
        if (attackerHeadType != null){
            if (!damaged.isDead()){damaged.addPotionEffects(AfflictedEffects.getPotionEffects(attackerHeadType));}
            switch (attackerHeadType){
                case ENDERMAN -> {
                    if (damaged.getType().equals(EntityType.ENDERMAN) && attacker.getType().equals(EntityType.PLAYER)){
                        WornMechanics.addEndermanAggroMap(damaged.getUniqueId(),(Player) attacker);
                    }
                }

            }
        }

        ede.setCancelled(canceled);
        if (damagedMobHead != null && !ede.isCancelled() && ede.getFinalDamage() > 0) AVFX.playHeadHurtSound(damaged, damagedMobHead);
    }

    private void deathHandler(EntityDeathEvent e){
        LivingEntity deadEnt = e.getEntity();
        MobHead mobHead = HeadUtil.getMobHeadFromEntity(deadEnt);
        if (mobHead != null) {
            switch (mobHead.getEntityType()){
                default -> {}
            }
            AVFX.playHeadDeathSound(deadEnt, mobHead);
        }
    }
}
