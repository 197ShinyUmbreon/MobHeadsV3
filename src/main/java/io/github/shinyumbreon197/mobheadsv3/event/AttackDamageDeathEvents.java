package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.effect.WornMechanics;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.effect.AfflictedEffects;
import io.github.shinyumbreon197.mobheadsv3.effect.AVFX;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class AttackDamageDeathEvents implements Listener {

    @EventHandler
    public void onEntityTakeDamage(EntityDamageEvent e){
        if (!(e.getEntity() instanceof LivingEntity))return;
        LivingEntity damaged = (LivingEntity) e.getEntity();
        damageHandler(e);
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof LivingEntity))return;
        damageHandler(e);
    }


    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        deathHandler(e);
    }

    private void damageHandler(EntityDamageEvent e){
        if (!(e.getEntity() instanceof LivingEntity))return;
        boolean damageByEntityEvent = e instanceof EntityDamageByEntityEvent;
        LivingEntity damaged = (LivingEntity) e.getEntity();
        LivingEntity attacker = null;
        if (damageByEntityEvent){
            Entity entity = ((EntityDamageByEntityEvent) e).getDamager();
            if (entity instanceof LivingEntity){
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
            }
        }
        if (damagedHead != null){
            EntityType headType = damagedHead.getEntityType();
            switch (headType){
                default -> {}
                case WITHER_SKELETON, WITHER -> {if (damageCause.equals(EntityDamageEvent.DamageCause.WITHER)) canceled = true;}
                case FROG -> {if (damageCause.equals(EntityDamageEvent.DamageCause.FALL)){canceled = !WornMechanics.frogFallDamage(e);}}
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
