package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.AVFX;
import io.github.shinyumbreon197.mobheadsv3.Decollation;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.data.Key;
import io.github.shinyumbreon197.mobheadsv3.entity.Summon;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import io.github.shinyumbreon197.mobheadsv3.function.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class ProjectileEvents implements Listener {

    @EventHandler
    public static void onProjectileLaunch(ProjectileLaunchEvent ple){
        Projectile projectile = ple.getEntity();
        ProjectileSource source = projectile.getShooter();

        if (source instanceof LivingEntity){
            if (debug){
                System.out.println(((LivingEntity)projectile.getShooter()));
                System.out.println(projectile.getType());
                System.out.println(projectile.getVelocity().length());
            }
            LivingEntity livingSource = (LivingEntity) source;
            MobHead mobHead = MobHead.getMobHeadWornByEntity(livingSource);
            boolean isSummon = Summon.isEntitySummon(livingSource);
            if (projectile.getType().equals(EntityType.SNOWBALL) && (isSummon || mobHead != null && mobHead.getEntityType().equals(EntityType.SNOW_GOLEM))){
                CreatureEvents.snowmanThrowSnowball(livingSource, (Snowball) projectile, isSummon);
            }
            if (mobHead != null){
                EntityType headType = mobHead.getEntityType();
                switch (headType){
                    case BLAZE -> {
                        projectile.setVisualFire(true);
                    }
                    case BREEZE -> {
                        if (!(projectile instanceof WindCharge) || !(source instanceof Player))return;
                        Random random = new Random();
                        int roll = random.nextInt(0,10);
                        if (roll >= 8)return; //80%
                        for (ItemStack charge:((Player)source).getInventory().addItem(new ItemStack(Material.WIND_CHARGE)).values()){
                            ((Player) source).getWorld().dropItem(((Player) source).getLocation(),charge);
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public static void onProjectileLand(ProjectileHitEvent phe){
        Projectile projectile = phe.getEntity();
        Entity hitEnt = phe.getHitEntity();
        Block hitBlock = phe.getHitBlock();
        ProjectileSource projectileSource = projectile.getShooter();
        boolean wasEntity = hitEnt != null;
        boolean wasBlock = hitBlock != null;

        if (Decollation.isDecollationPearlEntity(projectile)){
            phe.setCancelled(true);
            Decollation.runDecollationPearlHit((EnderPearl) projectile, hitEnt, hitBlock);
        }
        if (CreatureEvents.isSnowmanSnowball(projectile)){
            phe.setCancelled(true);
            CreatureEvents.snowmanSnowballEffect(hitEnt, (Snowball) projectile);
        }
        if (CreatureEvents.isBlazeFireball(projectile)){
            if (wasBlock){
                projectile.remove();
                phe.setCancelled(true);
            }
        }
        if (wasEntity && CreatureEvents.isBreezeReflection(projectile)){
            Util.addAbilityDamageData(hitEnt, EntityType.BREEZE);
        }
        if (CreatureEvents.isDragonBreath(projectile)){
            CreatureEvents.dragonBreathLandHit(hitEnt);
        }
        if (wasEntity && hitEnt instanceof LivingEntity && MobHead.isWearingHead(hitEnt)){
            MobHead mobHead = MobHead.getMobHeadWornByEntity(hitEnt);
            if (mobHead == null)return;
            EntityType headType = mobHead.getEntityType();
            switch (headType){
                case ENDERMAN -> {
                    phe.setCancelled(true);
                    CreatureEvents.endermanTeleportNearby((LivingEntity) hitEnt, true);
                }
                case BREEZE -> {
                    phe.setCancelled(true);
                    CreatureEvents.breezeReflectProjectile((LivingEntity) hitEnt, projectile);
                }
            }
        }
        if (projectileSource instanceof Entity && MobHead.isWearingHead((Entity) projectileSource)){
            Entity entitySource = (Entity) projectileSource;
            MobHead mobHead = MobHead.getMobHeadWornByEntity(entitySource);
            if (mobHead == null)return;
            EntityType headType = mobHead.getEntityType();
            switch (headType){
                case ENDERMAN, ENDERMITE -> {
                    if (projectile instanceof EnderPearl && ((EnderPearl)projectile).getItem().isSimilar(new ItemStack(Material.ENDER_PEARL))){
                        phe.setCancelled(true);
                        int refundPercent;
                        if (headType.equals(EntityType.ENDERMAN)){
                            refundPercent = 75;
                        }else refundPercent = 100;
                        Vector facing = entitySource.getLocation().getDirection();
                        customEnderPearlTeleport(entitySource,projectile.getLocation().setDirection(facing),hitEnt,refundPercent);
                        projectile.remove();
                    }
                }
            }
        }
    }

    public static void customEnderPearlTeleport(Entity thrower, Location destination, @Nullable Entity hitEnt, int refundPercent){
        Random random = new Random();
        boolean endermite = random.nextInt(0,100) < 5;
        if (endermite) thrower.getWorld().spawnEntity(thrower.getLocation(), EntityType.ENDERMITE);
        AVFX.playEndermanTeleportSound(thrower.getLocation());
        thrower.teleport(destination);
        AVFX.playEndermanTeleportSound(destination);
        if (hitEnt instanceof Damageable) ((Damageable) hitEnt).damage(0,thrower);
        boolean refund = random.nextInt(0,100) < refundPercent;
        if (refund){
            Item enderPearlItem = thrower.getWorld().dropItem(thrower.getLocation(),new ItemStack(Material.ENDER_PEARL));
            enderPearlItem.setPickupDelay(0);
            new BukkitRunnable(){
                @Override
                public void run() {
                    AVFX.playEndermanRegeneratePearlSound(thrower.getLocation());
                }
            }.runTaskLater(MobHeadsV3.getPlugin(),2);
        }
    }

}
