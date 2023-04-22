package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.effect.WornMechanics;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.effect.AfflictedEffects;
import io.github.shinyumbreon197.mobheadsv3.effect.AVFX;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

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
        if (e.getHitEntity() == null)return;
        MobHead mobHead = HeadUtil.getMobHeadFromEntity(e.getHitEntity());
        if (mobHead == null)return;
        WornMechanics.projectileHitWearer(e, mobHead);
    }

    private void damageHandler(EntityDamageEvent e){
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
                case WOLF, SILVERFISH -> {WornMechanics.summonReinforcements(damaged, attacker, headType);}
                case ENDERMAN -> {WornMechanics.endermanDamageEffect(damaged, damageCause);}
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
