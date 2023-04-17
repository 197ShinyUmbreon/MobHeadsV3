package io.github.shinyumbreon197.mobheadsv3.effect;

import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class WornMechanics {

    public static void runHeadTickMechanics(List<LivingEntity> headed){
        for (LivingEntity livingEntity:headed){
            MobHead mobHead = HeadUtil.getMobHeadFromEntity(livingEntity);
            if (mobHead != null) executeTickMechanics(livingEntity, mobHead);
        }
    }

    private static void executeTickMechanics(LivingEntity livingEntity, MobHead mobHead){
        EntityType entityType = mobHead.getEntityType();
        switch (entityType){
            default -> {}
            case DOLPHIN -> {regenerateAir(livingEntity);}
            case COD, SALMON, TROPICAL_FISH -> {regenerateAir(livingEntity);}
            case PUFFERFISH -> {regenerateAir(livingEntity);}
            case TURTLE -> {regenerateAir(livingEntity);}
            case SQUID -> {regenerateAir(livingEntity);}
            case SKELETON_HORSE -> {regenerateAir(livingEntity);}
            case GLOW_SQUID -> {regenerateAir(livingEntity);}
            case TADPOLE -> {regenerateAir(livingEntity);}
            case DROWNED -> {regenerateAir(livingEntity);}
            case AXOLOTL -> {regenerateAir(livingEntity);}
            case FROG -> {regenerateAir(livingEntity);}
        }
    }

    //Tick Triggered ---------------------------------------------------------------------------------------
    private static void regenerateAir(LivingEntity livingEntity){
        int maxAir = livingEntity.getMaximumAir();
        int airRemaining = livingEntity.getRemainingAir();

        int newAir = airRemaining + 8;
        if (newAir > maxAir) newAir = maxAir;

        livingEntity.setRemainingAir(newAir);
    }

    //Event Triggered --------------------------------------------------------------------------------------
    //PlayerStatisticIncrementEvent
    public static void frogJump(Player player){
        if (!player.isSneaking())return;
        Vector velocity = player.getVelocity(); //System.out.println("Pre-velocity: "+velocity);
        Vector direction = player.getLocation().getDirection(); //System.out.println("Direction: "+direction);
        double x = velocity.getX() + (direction.getX() * 0.8);
        double y = (velocity.getY() * 2) + (direction.getY() * 0.1);
        double z = velocity.getZ() + (direction.getZ() * 0.8);
        velocity.setX(x);
        velocity.setY(y);
        velocity.setZ(z);
        //System.out.println("Post-velocity: "+velocity);
        player.setVelocity(velocity);
        AVFX.playFrogJumpEffect(player.getLocation());
    }

    //EntityDamageEvent
    public static boolean frogFallDamage(EntityDamageEvent e){
        boolean frogFallDamage = e.getFinalDamage() > 3;
        if (frogFallDamage){
            double newDamage;
            newDamage = e.getFinalDamage() - 3;
            if (newDamage <= 0){return false;}
            e.setDamage(newDamage);
        }
        return frogFallDamage;
    }






}
