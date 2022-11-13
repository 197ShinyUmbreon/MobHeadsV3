package io.github.shinyumbreon197.mobheadsv3;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

public class Effects {

    public static void playHeadEffect(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.spawnParticle(Particle.EXPLOSION_LARGE,location,1);
        world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.2F, 1.2F);
        world.playSound(location, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 0.4F, 1.3F);
    }

}
