package io.github.shinyumbreon197.mobheadsv3.tool;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

public class EffectUtil {

    public static boolean isExposedToWater(LivingEntity target){
        Location tLoc = target.getEyeLocation();
        World tWorld = target.getWorld();
        Location highestBlock = tWorld.getHighestBlockAt(tLoc).getLocation();
        boolean blockAbove = highestBlock.getY() > tLoc.getY();
        boolean exposedToRain = !blockAbove && !tWorld.isClearWeather();

        return target.isInWater() || exposedToRain;
    }

}
