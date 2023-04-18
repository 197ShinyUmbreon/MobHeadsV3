package io.github.shinyumbreon197.mobheadsv3.tool;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EffectUtil {

    public static boolean isExposedToWaterO(LivingEntity target){
        Location tLoc = target.getEyeLocation();
        World tWorld = target.getWorld();
        Location highestBlock = tWorld.getHighestBlockAt(tLoc).getLocation();
        boolean blockAbove = highestBlock.getY() > tLoc.getY();
        boolean exposedToRain = !blockAbove && !tWorld.isClearWeather();

        return target.isInWater() || exposedToRain;
    }

    public static boolean isExposedToWater(LivingEntity target){
        Location tLoc = target.getEyeLocation();
        World world = target.getWorld();
        Location highestBlock = world.getHighestBlockAt(tLoc).getLocation();
        boolean blockAbove = highestBlock.getY() > tLoc.getY();

        boolean isRaining = !world.isClearWeather();
        double temp = tLoc.getBlock().getTemperature();
        double humid = tLoc.getBlock().getHumidity();
        boolean inWater = target.isInWater();
        //int skyLight = pLoc.getBlock().getLightFromSky();
        return  (inWater || temp > 0 && humid > 0 && ((isRaining && !blockAbove)));
    }

    public static boolean isExposedToSnowfall(LivingEntity target){
        Location tLoc = target.getEyeLocation();
        World world = target.getWorld();
        Location highestBlock = world.getHighestBlockAt(tLoc).getLocation();
        boolean blockAbove = highestBlock.getY() > tLoc.getY();

        boolean isRaining = !world.isClearWeather();
        double temp = tLoc.getBlock().getTemperature();
        double humid = tLoc.getBlock().getHumidity();
        return temp <= 0 && humid > 0 && (isRaining && !blockAbove);
    }

    private static final List<Material> safeDestMats = Arrays.asList(
            Material.AIR, Material.CAVE_AIR, Material.GRASS, Material.TALL_GRASS, Material.FERN, Material.LARGE_FERN
    );

    private static final List<Material> unsafeDestMats = Arrays.asList(
            Material.LAVA, Material.WATER, Material.POINTED_DRIPSTONE, Material.LAVA_CAULDRON, Material.COBWEB, Material.AIR,
            Material.CAVE_AIR, Material.VOID_AIR, Material.FIRE, Material.SOUL_FIRE, Material.CAMPFIRE, Material.SOUL_CAMPFIRE,
            Material.SEA_PICKLE, Material.SEAGRASS, Material.KELP, Material.KELP_PLANT, Material.TALL_SEAGRASS
    );

    public static Location randomTeleportLoc(Location origin, Entity entity, int max, int min, boolean safe){
        Vector looking = entity.getLocation().getDirection();
        Random random = new Random();
        Location destination;

        List<Vector> excluded = new ArrayList<>();
        for (int i = 0; i < (min)*2; i++) {
            int x; int y; int z;
            if (i >= (min)/2){
                x = -(i-min);
            }else{
                x = i;
            }
            for (int j = 0; j < (min)*2; j++) {
                if (j >= (min)/2){
                    y = -(j-min);
                }else{
                    y = j;
                }
                for (int k = 0; k < (min)*2; k++) {
                    if (k >= (min)/2){
                        z = -(k-min);
                    }else{
                        z = k;
                    }
                    excluded.add(new Vector(x, y, z));
                }
            }
        }
        List<Vector> xyzs = new ArrayList<>();
        for (int i = 0; i < (max)*2; i++) {
            int x; int y = 0; int z = 0;
            if (i >= (max)/2){
                x = -(i-max);
            }else{
                x = i;
            }
            for (int j = 0; j < (max)*2; j++) {
                z = 0;
                if (j >= (max)/2){
                    y = -(j-max);
                }else{
                    y = j;
                }
                for (int k = 0; k < (max)*2; k++) {
                    if (k >= (max)/2){
                        z = -(k-max);
                    }else{
                        z = k;
                    }
                    Vector vec = new Vector(x, y, z);
                    if (!excluded.contains(vec)){
                        xyzs.add(vec);
                    }
                }
            }
        }
        if (safe){
            List<Vector> safeDests = new ArrayList<>();
            for (Vector xyz:xyzs){
                Location loc = origin.clone().add(xyz);
                Block block = loc.getBlock();
                if (!unsafeDestMats.contains(block.getType())){
                    Block feet = loc.clone().add(0,1,0).getBlock();
                    if (safeDestMats.contains(feet.getType())){
                        Block torso = loc.clone().add(0,2,0).getBlock();
                        if (safeDestMats.contains(torso.getType())){
                            safeDests.add(xyz);
                        }
                    }
                }
            }
            if (safeDests.size() != 0){
                int randomIndex = random.nextInt(safeDests.size());
                destination = origin.clone().add(safeDests.get(randomIndex)).add(0.5,1.0,0.5);
                if (safeDestMats.contains(destination.clone().add(0,2,0).getBlock().getType())){
                    destination.add(0.0,0.5,0.0);
                }
            }else{
                return null;
            }
        }else{
            int randomIndex = random.nextInt(xyzs.size());
            destination = origin.clone().add(xyzs.get(randomIndex)).add(0.5,1.0,0.5);
        }
        destination.setDirection(looking);
        return destination;
    }

    public static double distance3d(Location point0, Location point1){
        double x0 = point0.getX(); double y0 = point0.getY(); double z0 = point0.getZ();
        boolean x0neg = x0 < 0; boolean y0neg = y0 < 0; boolean z0neg = z0 < 0;
        double x1 = point1.getX(); double y1 = point1.getY(); double z1 = point1.getZ();
        boolean x1neg = x1 < 0; boolean y1neg = y1 < 0; boolean z1neg = z1 < 0;
        double xDif = 0; double yDif = 0; double zDif = 0;
        if ((!x0neg & !x1neg) | (x0neg & x1neg)) xDif = x0 - x1; if ((!x0neg & x1neg) | (x0neg & !x1neg)) xDif = x0 + x1;
        if (xDif < 0) xDif = xDif*-1;
        if ((!y0neg & !y1neg) | (y0neg & y1neg)) yDif = y0 - y1; if ((!y0neg & y1neg) | (y0neg & !y1neg)) yDif = y0 + y1;
        if (yDif < 0) yDif = yDif*-1;
        if ((!z0neg & !z1neg) | (z0neg & z1neg)) zDif = z0 - z1; if ((!z0neg & z1neg) | (z0neg & !z1neg)) zDif = z0 + z1;
        if (zDif < 0) zDif = zDif*-1;
        double horizontalDist = Math.sqrt((xDif*xDif) + (zDif*zDif));
        return Math.sqrt((yDif*yDif) + (horizontalDist*horizontalDist));
    }

    public static Vector projectileVector(Location startLoc, Location endLoc, double velocity){
        if (startLoc == null | endLoc == null)return new Vector();
        // Declare coordinates
        double startX = startLoc.getX(); double startY = startLoc.getY(); double startZ = startLoc.getZ();
        double endX = endLoc.getX(); double endY = endLoc.getY()+1.0; double endZ = endLoc.getZ();
        // Calculate distance, check and remember if negative, adjust if negative
        double diffX = endX - startX; boolean diffXisNegative = diffX < 0; if (diffXisNegative) diffX = diffX*-1;
        double diffY = endY - startY; boolean diffYisNegative = diffY < 0; if (diffYisNegative) diffY = diffY*-1;
        double diffZ = endZ - startZ; boolean diffZisNegative = diffZ < 0; if (diffZisNegative) diffZ = diffZ*-1;
        // Prepare a sum of the distances
        double diffSum = diffX + diffY + diffZ;
        // Get each distance value as a percentage, and adjust against intended velocity
        double x = diffX/(diffSum/velocity); double y = diffY/(diffSum/velocity); double z = diffZ/(diffSum/velocity);
        // If the distance was originally negative, reimplement that
        if (diffXisNegative) x = x*-1; if (diffYisNegative) y = y*-1; if (diffZisNegative) z = z*-1;
        return new Vector(x, y, z);
    }

}
