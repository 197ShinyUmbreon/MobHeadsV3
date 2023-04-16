package io.github.shinyumbreon197.mobheadsv3.tool;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

public class EffectUtil {

    public static boolean isExposedToWaterO(LivingEntity target){
        Location tLoc = target.getEyeLocation();
        World tWorld = target.getWorld();
        Location highestBlock = tWorld.getHighestBlockAt(tLoc).getLocation();
        boolean blockAbove = highestBlock.getY() > tLoc.getY();
        boolean exposedToRain = !blockAbove && !tWorld.isClearWeather();

        return target.isInWater() || exposedToRain;
    }

    private static boolean isExposedToWater(LivingEntity target){
        Location tLoc = target.getEyeLocation();
        World world = target.getWorld();
        Location highestBlock = world.getHighestBlockAt(tLoc).getLocation();
        boolean blockAbove = highestBlock.getY() > tLoc.getY();

        boolean isRaining = !world.isClearWeather();
        double temp = tLoc.getBlock().getTemperature();
        double humid = tLoc.getBlock().getHumidity();
        boolean inWater = target.isInWater();
        //int skyLight = pLoc.getBlock().getLightFromSky();
        return  (inWater || ((isRaining && !blockAbove) && temp > 0 && humid > 0));
    }

    private static boolean isExposedToSnowfall(LivingEntity target){
        Location tLoc = target.getEyeLocation();
        World world = target.getWorld();
        Location highestBlock = world.getHighestBlockAt(tLoc).getLocation();
        boolean blockAbove = highestBlock.getY() > tLoc.getY();

        boolean isRaining = !world.isClearWeather();
        double temp = tLoc.getBlock().getTemperature();
        double humid = tLoc.getBlock().getHumidity();
        return  (isRaining && !blockAbove) && temp <= 0 && humid > 0;
    }

}
