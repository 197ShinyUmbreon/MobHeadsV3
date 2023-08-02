package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.AVFX;
import io.github.shinyumbreon197.mobheadsv3.Decollation;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.Random;

public class ProjectileLand implements Listener {

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
        if (wasEntity && hitEnt instanceof LivingEntity && MobHead.isWearingHead(hitEnt)){
            MobHead mobHead = MobHead.getMobHeadWornByEntity(hitEnt);
            if (mobHead == null)return;
            EntityType headType = mobHead.getEntityType();
            switch (headType){
                case ENDERMAN -> {
                    phe.setCancelled(true);
                    CreatureEvents.endermanTeleportNearby((LivingEntity) hitEnt, true);
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
