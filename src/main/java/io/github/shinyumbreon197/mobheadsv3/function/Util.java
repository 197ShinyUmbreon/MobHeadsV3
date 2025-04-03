package io.github.shinyumbreon197.mobheadsv3.function;

import io.github.shinyumbreon197.mobheadsv3.AVFX;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.data.Key;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;
import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.getPlugin;

public class Util {

    // Container Management --------------------------------------------------------------------------------------------
    public static ItemStack[] getShulkerBoxItemContents(ItemStack itemStack){
        ShulkerBox shulkerBox = getShulkerBoxFromItemStack(itemStack);
        if (shulkerBox == null)return null;
        return shulkerBox.getInventory().getContents();
    }
    public static boolean setShulkerBoxItemContents(ItemStack itemStack, ItemStack[] contents){
        ShulkerBox shulkerBox = getShulkerBoxFromItemStack(itemStack);
        if (shulkerBox == null)return false;
        shulkerBox.getInventory().setContents(contents);
        return true;
    }
    public static List<ItemStack> getBundleContents(ItemStack bundle){
        if (bundle == null || !bundle.getType().equals(Material.BUNDLE))return null;
        BundleMeta bundleMeta = (BundleMeta) bundle.getItemMeta();
        if (bundleMeta == null)return null;
        return bundleMeta.getItems();
    }
    private static ShulkerBox getShulkerBoxFromItemStack(ItemStack itemStack){
        if (!itemStack.getType().equals(Material.SHULKER_BOX))return null;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof BlockStateMeta))return null;
        BlockStateMeta blockStateMeta = (BlockStateMeta) itemMeta;
        if (!(blockStateMeta.getBlockState() instanceof ShulkerBox))return null;
        return (ShulkerBox) blockStateMeta.getBlockState();
    }

    // Strings ---------------------------------------------------------------------------------------------------------
    public static String getVariantString(LivingEntity target){
        EntityType type = target.getType();
        switch (type){
            case AXOLOTL -> {return ((Axolotl)target).getVariant().toString();}
            case CAT -> {return ((Cat)target).getCatType().getKeyOrThrow().getKey();}
            case FOX -> {return ((Fox)target).getFoxType().toString();}
            case FROG -> {return ((Frog)target).getVariant().getKeyOrThrow().getKey();}
            case HORSE -> {return ((Horse)target).getColor().toString();}
            case LLAMA -> {return ((Llama)target).getColor().toString();}
            case TRADER_LLAMA -> {return ((TraderLlama)target).getColor().toString();}
            case MOOSHROOM -> {return ((MushroomCow)target).getVariant().toString();}
            case PANDA -> {
                Panda.Gene gene0 = ((Panda)target).getMainGene();
                Panda.Gene gene1 = ((Panda)target).getHiddenGene();
                if (gene0 == Panda.Gene.BROWN && gene1 == Panda.Gene.BROWN)return Panda.Gene.BROWN.toString();
                return Panda.Gene.NORMAL.toString();
            }
            case PARROT -> {return ((Parrot)target).getVariant().toString();}
            case RABBIT -> {return ((Rabbit)target).getRabbitType().toString();}
            case SHEEP -> {
                DyeColor color = ((Sheep)target).getColor();
                if (color == null) color = DyeColor.WHITE;
                return color.toString();
            }
            case VILLAGER -> {return ((Villager)target).getVillagerType().getKeyOrThrow().getKey();}
            case ZOMBIE_VILLAGER -> {return ((ZombieVillager)target).getVillagerType().getKeyOrThrow().getKey();}
            case WOLF -> {return ((Wolf)target).getVariant().getKeyOrThrow().getKey();}
            case PIG -> {return ((Pig)target).getVariant().getKeyOrThrow().toString();}
            case COW -> {return ((Cow)target).getVariant().getKeyOrThrow().toString();}
            case CHICKEN -> {return ((Chicken)target).getVariant().getKeyOrThrow().toString();}
        }
        return null;
    }

    public static boolean hasWorkingElytra(LivingEntity livingEntity){
        EntityEquipment ee = livingEntity.getEquipment();
        if (ee == null)return false;
        ItemStack elytra = ee.getChestplate();
        if (elytra == null || !elytra.getType().equals(Material.ELYTRA))return false;
        Damageable damageMeta = (Damageable) elytra.getItemMeta();
        if (damageMeta == null)return false;
        int damage = damageMeta.getDamage();
        return damage < 432;
    }

    public static boolean useHeldItem(Player player, Material targetMat, boolean consume){
        PlayerInventory inv = player.getInventory();
        ItemStack mainHand = inv.getItemInMainHand();
        ItemStack offHand = inv.getItemInOffHand();

        boolean main;
        ItemStack target;
        if (mainHand.getType().equals(targetMat)){
            main = true;
            target = mainHand;
        }else if (offHand.getType().equals(targetMat)){
            main = false;
            target = offHand;
        }else return false;

        if (consume){
            int count = target.getAmount();
            count--;
            target.setAmount(count);
            if (main){
                inv.setItemInMainHand(target);
            }else inv.setItemInOffHand(target);
        }
        return true;
    }
    public static Vector getDirection(Vector origin, Vector destination){
        Vector zero = new Vector();
        double distance = origin.distance(destination);
        Vector difference = destination.clone().subtract(origin);
        if (difference.equals(zero)) return zero;
        double x = 0;
        double y = 0;
        double z = 0;
        if (difference.getX() != 0) x = difference.getX() / distance;
        if (difference.getY() != 0) y = difference.getY() / distance;
        if (difference.getZ() != 0) z = difference.getZ() / distance;
        return new Vector(x,y,z);
    }

    public static List<Block> getBlockAndNeighbors(Block originBlock, BlockFace blockFace){
        List<Block> blocks = new ArrayList<>(List.of(originBlock));
        if (blockFace.equals(BlockFace.EAST) || blockFace.equals(BlockFace.WEST)){
            blocks.add(originBlock.getRelative(BlockFace.UP));
            blocks.add(originBlock.getRelative(BlockFace.DOWN));
            blocks.add(originBlock.getRelative(BlockFace.NORTH));
            blocks.add(originBlock.getRelative(BlockFace.SOUTH));
        }else if (blockFace.equals(BlockFace.NORTH) || blockFace.equals(BlockFace.SOUTH)){
            blocks.add(originBlock.getRelative(BlockFace.UP));
            blocks.add(originBlock.getRelative(BlockFace.DOWN));
            blocks.add(originBlock.getRelative(BlockFace.EAST));
            blocks.add(originBlock.getRelative(BlockFace.WEST));
        }else if (blockFace.equals(BlockFace.UP) || blockFace.equals(BlockFace.DOWN)){
            blocks.add(originBlock.getRelative(BlockFace.NORTH));
            blocks.add(originBlock.getRelative(BlockFace.SOUTH));
            blocks.add(originBlock.getRelative(BlockFace.EAST));
            blocks.add(originBlock.getRelative(BlockFace.WEST));
        }
        return blocks;
    }

    public static List<Block> getBlocksSurroundingEntity(Entity entity){
        List<Block> blocks = new ArrayList<>();
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                for (int y = 0; y < 2; y++) {
                    Location loc = entity.getLocation().clone().add(x,y,z);
                    blocks.add(loc.getBlock());
                }
            }
        }
        return blocks;
    }

    public static Block getNearestVerticalSurface(Location origin, int maxDistance, boolean upwards, boolean downwards){ //boolean fluidSurface){
        if (maxDistance < 0) maxDistance = 0;
        if (!upwards && !downwards){
            upwards = true;
            downwards = true;
        }
        Block originBlock = origin.getBlock();
        List<Block> aboveBlocks = new ArrayList<>();
        List<Block> belowBlocks = new ArrayList<>();
        for (int i = 1; i <= maxDistance; i++) {
            if (upwards) aboveBlocks.add(originBlock.getRelative(BlockFace.UP, i));
            if (downwards) belowBlocks.add(originBlock.getRelative(BlockFace.DOWN, i));
        }
        Block nearest = null;
        Integer nearestDistance = null;
        List<List<Block>> lists = List.of(List.of(originBlock), aboveBlocks, belowBlocks);
        for (List<Block> list:lists){
            for (Block block:list){
                if (block.getCollisionShape().getBoundingBoxes().size() == 0)continue;
                Block above = block.getRelative(BlockFace.UP);
                if (above.getCollisionShape().getBoundingBoxes().size() != 0)continue;
                int distance = Math.abs(block.getLocation().getBlockY() - origin.getBlockY());
                if (nearestDistance != null && distance > nearestDistance)continue;
                nearest = block;
                nearestDistance = distance;
            }
        }
        return nearest;
    }

    public static List<LivingEntity> nearbyLivingEnts(Location origin, double x, double y, double z){
        World world = origin.getWorld();
        if (world == null)return new ArrayList<>();
        return world.getNearbyEntities(origin,x,y,z).stream()
                .filter(entity -> entity instanceof LivingEntity).map(LivingEntity.class::cast)
                .collect(Collectors.toList()
                );
    }

    public static void addAbilityDamageData(Entity target, EntityType abilityType){
        if (debug) System.out.println("addAbilityDamageData() ->add() target: " + target); //debug
        PersistentDataContainer data = target.getPersistentDataContainer();
        data.set(Key.tookAbilityDamage, PersistentDataType.STRING, abilityType.toString());
        new BukkitRunnable(){
            @Override
            public void run() {
                if (debug) System.out.println("addAbilityDamageData() ->remove() target: " + target); //debug
                PersistentDataContainer data = target.getPersistentDataContainer();
                data.remove(Key.tookAbilityDamage);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(),5);
    }
    public static EntityType getAbilityDamageData(Entity target){
        PersistentDataContainer data = target.getPersistentDataContainer();
        if (!data.has(Key.tookAbilityDamage, PersistentDataType.STRING))return null;
        String abilityTypeString = data.get(Key.tookAbilityDamage, PersistentDataType.STRING);
        if (abilityTypeString == null)return null;
        EntityType abilityType = EntityType.valueOf(abilityTypeString);
        if (debug) System.out.println("getAbilityDamageData() target: " + target + " abilityType: " + abilityType.toString()); //debug
        return abilityType;
    }
    public static boolean hasTakenAbilityDamage(Entity target){
        PersistentDataContainer data = target.getPersistentDataContainer();
        boolean hasTakenDamage = data.has(Key.tookAbilityDamage, PersistentDataType.STRING);
        if (debug) System.out.println("hasTakenAbilityDamage() target: " + target + " hasTakenDamage: " + hasTakenDamage); //debug
        return hasTakenDamage;
    }

    public static double getLivingEntitySpeed(LivingEntity livingEntity){
        Vector velocity = livingEntity.getVelocity();
        return Math.abs(velocity.getX()) + Math.abs(velocity.getY()) + Math.abs(velocity.getZ());
    }
    public static BlockFace getLivingEntity3AxisMovementDirection(LivingEntity livingEntity){
        Vector velocity = livingEntity.getVelocity();
        double vx = velocity.getX();
        double vy = velocity.getY();
        double vz = velocity.getZ();
        double avx = Math.abs(vx);
        double avy = Math.abs(vy);
        double avz = Math.abs(vz);
        BlockFace direction = BlockFace.SELF;
        if (avx > avy && avx > avz){ // X has the highest absolute value
            if (vx > 0){ // X is positive bound
                direction = BlockFace.EAST;
            }else direction = BlockFace.WEST; // X is negative bound
        }else if (avz > avx && avz > avy){ // Z has the highest absolute value
            if (vz > 0){ // Z is positive bound
                direction = BlockFace.SOUTH;
            }else direction = BlockFace.NORTH; // Z is negative bound
        }else if (avy > avx && avy > avz){ // Y has the highest absolute value
            if (vy > 0){ // Y is positive bound
                direction = BlockFace.UP;
            }else direction = BlockFace.DOWN; // Y is negative bound
        }
        return direction;
    }

    public static Entity getTrueAttacker(Entity entity){
        if (entity instanceof Projectile){
            ProjectileSource projectileSource = ((Projectile)entity).getShooter();
            if (projectileSource instanceof Entity){
                PersistentDataContainer data = entity.getPersistentDataContainer();
                String stringUUID = data.get(Key.breezeReflectionKey, PersistentDataType.STRING);
                UUID uuid = null;
                if (stringUUID != null) uuid = UUID.fromString(stringUUID);
                if (uuid != null){
                    return Bukkit.getEntity(uuid);
                }else return (Entity) projectileSource;
            }
        }
        return entity;
    }

    private static String buildFriendlyString(String input){
        char[] charArray = new char[input.length()];
        boolean previousUnderscore = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (i == 0 || previousUnderscore){
                charArray[i] = c;
                previousUnderscore = false;
                continue;
            }
            if (c == '_'){
                charArray[i] = (' ');
                previousUnderscore = true;
                continue;
            }
            charArray[i] = (Character.toLowerCase(c));
        }
        return String.copyValueOf(charArray);
    }
    public static String friendlyEntityTypeName(EntityType entityType){
        return buildFriendlyString(entityType.toString());
    }
    public static String friendlyMaterialName(Material material){
        return buildFriendlyString(material.toString());
    }

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

        boolean isRaining = world.hasStorm();
        double temp = tLoc.getBlock().getTemperature();
        double humid = tLoc.getBlock().getHumidity();
        boolean inWater = target.isInWater();
        //int skyLight = pLoc.getBlock().getLightFromSky();
        return  (inWater || (temp > 0 && humid > 0 && isRaining && !blockAbove));
    }

    private enum Climate{
        TEMPERATE, COLD, HOT, HUMID, DRY
    }
    public static List<Climate> getClimate(Location location){
        World world = location.getWorld();
        if (world == null) return List.of(Climate.TEMPERATE);
        World.Environment environment = world.getEnvironment();
        switch (environment){
            default -> {return List.of(Climate.TEMPERATE);}
            case NETHER -> {return List.of(Climate.HOT, Climate.DRY);}
            case NORMAL -> {
                List<Climate> climateList = new ArrayList<>();
                double humidity = location.getBlock().getHumidity();
                double temperature = location.getBlock().getTemperature();
                if (humidity <= 0){
                    climateList.add(Climate.DRY);
                }else if (humidity >= 0.85){
                    climateList.add(Climate.HUMID);
                }
                if (temperature <= 0){
                    climateList.add(Climate.COLD);
                }else if (temperature >= 2){
                    climateList.add(Climate.HOT);
                }
                if (climateList.size() == 0)climateList.add(Climate.TEMPERATE);
                return climateList;
            }
        }
    }

    public static boolean isWalkingOnSandyBlock(LivingEntity target){
        Block block = target.getLocation().getBlock().getRelative(BlockFace.DOWN);
        return block.getType().toString().contains("SAND");
    }

    public static boolean isExposedToSnowfall(LivingEntity target){
        Location tLoc = target.getEyeLocation();
        World world = target.getWorld();
        Location highestBlock = world.getHighestBlockAt(tLoc).getLocation();
        boolean blockAbove = highestBlock.getY() > tLoc.getY();

        boolean isRaining = world.hasStorm();
        double temp = tLoc.getBlock().getTemperature();
        double humid = tLoc.getBlock().getHumidity();
        return temp <= 0 && humid > 0 && (isRaining && !blockAbove);
    }

//    public static List<Block> getPlayerLookingCylinder(Player player, int distance, int radius){
//        List<Block> los = player.getLineOfSight(new HashSet<>(Arrays.asList(Material.values())),distance);
//        List<Block> blocks = new ArrayList<>(los);
//        for (int i = 0; i < los.size(); i++) {
//            Block center = los.get(i);
//            for (int j = 0; j < ; j++) {
//
//            }
//        }
//
//        if (debug) System.out.println("getPlayerLookingCylinder() blocks.size(): " + blocks.size()); //debug
//        return blocks;
//    }

    public static List<Block> getFirstFromSkyBlocks(Location origin, int radius){
        List<Block> blocks = new ArrayList<>();
        World world = origin.getWorld();
        if (world == null)return blocks;
        int ox = origin.getBlockX();
        int oz = origin.getBlockZ();

        int px = ox + radius - 1;
        int pz = oz + radius - 1;
        int nx = ox - radius;
        int nz = oz - radius;

        for (int x = nx; x <= px; x++) {
            for (int z = nz; z <= pz; z++) {
                blocks.add(world.getHighestBlockAt(x,z));
            }
        }
        if (debug) System.out.println("getFirstFromSkyBlocks() blocks.size(): " + blocks.size()); //debug
        return blocks;
    }
    public static List<Block> getNearbyBlocks(Location origin, int xSize, int ySize, int zSize){
        int ox = origin.getBlockX();
        int oy = origin.getBlockY();
        int oz = origin.getBlockZ();

        Vector posCorner = new Vector(ox + (xSize - 1), oy + (ySize - 1), oz + (zSize - 1));
        Vector negCorner = new Vector(ox - xSize, oy - ySize, oz - zSize);

        List<Block> blocks = new ArrayList<>();
        for (int x = negCorner.getBlockX(); x <= posCorner.getBlockX(); x++) {
            for (int y = negCorner.getBlockY(); y <= posCorner.getBlockY(); y++) {
                for (int z = negCorner.getBlockZ(); z <= posCorner.getBlockZ(); z++) {
                    Vector point = new Vector(x,y,z);
                    blocks.add(new Location(origin.getWorld(), point.getBlockX(), point.getBlockY(), point.getZ()).getBlock());
                }
            }
        }
        if (debug) System.out.println("blocks.size(): " + blocks.size()); //debug
        return blocks;
    }
    private static final Set<Material> unsafeFloorMats = Set.of(
            Material.MAGMA_BLOCK, Material.COBWEB, Material.CAMPFIRE, Material.SOUL_CAMPFIRE
    );
    private static final Set<Material> unsafeAirMats = Set.of(
            Material.FIRE, Material.SOUL_FIRE, Material.COBWEB, Material.KELP_PLANT, Material.SEAGRASS, Material.TALL_SEAGRASS
    );
    public static Location getSafeTeleportLoc(Location origin, int x, int y, int z){
        List<Location> safeDests = new ArrayList<>();
        List<Block> nearbyBlocks = Util.getNearbyBlocks(origin,x,y,z);
        for (Block groundBlock:nearbyBlocks){
            if (groundBlock.isPassable() || groundBlock.isLiquid() || unsafeFloorMats.contains(groundBlock.getType()))continue;
            if (groundBlock.getBlockData() instanceof Waterlogged && ((Waterlogged)groundBlock.getBlockData()).isWaterlogged())continue;
            int gbbs = groundBlock.getCollisionShape().getBoundingBoxes().size();
            if (gbbs != 1)continue;
            Block air0 = groundBlock.getRelative(BlockFace.UP);
            if (!air0.isPassable() || air0.isLiquid() || unsafeAirMats.contains(air0.getType()))continue;
            if (air0.getBlockData() instanceof Waterlogged && ((Waterlogged)air0.getBlockData()).isWaterlogged())continue;
            int abbs0 = air0.getCollisionShape().getBoundingBoxes().size();
            if (abbs0 != 0)continue;
            Block air1 = air0.getRelative(BlockFace.UP);
            if (!air1.isPassable() || air1.isLiquid() || unsafeAirMats.contains(air1.getType()))continue;
            if (air1.getBlockData() instanceof Waterlogged && ((Waterlogged)air1.getBlockData()).isWaterlogged())continue;
            int abbs1 = air1.getCollisionShape().getBoundingBoxes().size();
            if (abbs1 != 0)continue;
            if (debug) System.out.println("groundBlock: " + groundBlock.getType() + " air0: " + air0.getType() + " air1: " + air1.getType()); //debug
            safeDests.add(groundBlock.getLocation().add(0.5,1.5,0.5).setDirection(origin.getDirection()));
        }
        if (safeDests.size() == 0) return origin;
        return safeDests.get(new Random().nextInt(0,safeDests.size()));
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
        if (startLoc == null || endLoc == null)return new Vector();
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

    public static double randomOffsetCenter(double variation){
        Random random = new Random();
        double floor = (variation/2)*-1;
        return floor + random.nextDouble(variation);
    }

    public static List<Block> getFacingBlocks(Location location, BlockFace facing, int height, int depth){
        List<Block> blocks = new ArrayList<>();
        World world = location.getWorld();
        if (world == null)return blocks;
        Block currentBlock = location.getBlock();
        Vector direction = location.getDirection();
        double x = direction.getX();
        double z = direction.getZ();
        //System.out.println("direction: "+direction); //debug
        if (x > 0.15 && x <= 0.85 && z > -0.85 && z <= -0.15){
            facing = BlockFace.NORTH_EAST;
        }else if (x > 0.15 && x <= 0.85 && z > 0.15 && z <= 0.85){
            facing = BlockFace.SOUTH_EAST;
        }else if (x > -0.85 && x <= -0.15 && z > 0.15 && z <= 0.85){
            facing = BlockFace.SOUTH_WEST;
        }else if (x > -0.85 && x <= -0.15 && z > -0.85 && z <= -0.15){
            facing = BlockFace.NORTH_WEST;
        }

        List<BlockFace> cardinalFaces = List.of(
                BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST
        );
        List<BlockFace> ordinalFaces = List.of(
                BlockFace.NORTH_EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST, BlockFace.NORTH_WEST
        );
        List<BlockFace> interOrdinalFaces = List.of(
                BlockFace.NORTH_NORTH_EAST, BlockFace.EAST_NORTH_EAST, BlockFace.EAST_SOUTH_EAST,
                BlockFace.SOUTH_SOUTH_EAST, BlockFace.SOUTH_SOUTH_WEST, BlockFace.WEST_SOUTH_WEST,
                BlockFace.WEST_NORTH_WEST, BlockFace.NORTH_NORTH_WEST
        );
        Map<BlockFace, BlockFace> interToOrdinalMap = new HashMap<>();
        interToOrdinalMap.put(BlockFace.NORTH_NORTH_EAST, BlockFace.NORTH_EAST);
        interToOrdinalMap.put(BlockFace.EAST_NORTH_EAST, BlockFace.NORTH_EAST);
        interToOrdinalMap.put(BlockFace.EAST_SOUTH_EAST, BlockFace.SOUTH_EAST);
        interToOrdinalMap.put(BlockFace.SOUTH_SOUTH_EAST, BlockFace.SOUTH_EAST);
        interToOrdinalMap.put(BlockFace.SOUTH_SOUTH_WEST, BlockFace.SOUTH_WEST);
        interToOrdinalMap.put(BlockFace.WEST_SOUTH_WEST, BlockFace.SOUTH_WEST);
        interToOrdinalMap.put(BlockFace.WEST_NORTH_WEST, BlockFace.NORTH_WEST);
        interToOrdinalMap.put(BlockFace.NORTH_NORTH_WEST, BlockFace.NORTH_WEST);

        if (interOrdinalFaces.contains(facing)) facing = interToOrdinalMap.get(facing);
        boolean cardinal = cardinalFaces.contains(facing);
        //System.out.println("facing: "+facing); //debug
        if (cardinal) {
            Block block = currentBlock.getRelative(facing);
            for (int i = -depth; i < depth+height; i++) {
                blocks.add(world.getBlockAt(block.getLocation().add(0, i,0)));
            }
        }else{
            List<BlockFace> chosen = new ArrayList<>();
            for (BlockFace blockFace:cardinalFaces){
                if (facing.toString().contains(blockFace.toString())) chosen.add(blockFace);
            }
            //System.out.println("chosen: "+chosen); //debug
            for (BlockFace blockFace:chosen){
                Block block = currentBlock.getRelative(blockFace);
                for (int i = -depth; i < depth+height; i++) {
                    blocks.add(world.getBlockAt(block.getLocation().add(0, i,0)));
                }
            }
        }
        return blocks;
    }

    /*
    source (Entity) - Optional; The source of the damage. Should typically align with origin.
    origin (Location) - Origin of the effect
    range (double) - Maximum distance that entities will be affected.
    damage (double) - Maximum damage inflicted. Decays if falloff is > 0. Triggers a DamageEvent if not set to 0.
    knockback (double) - Maximum knockback velocity. Decays if falloff is > 0.
    falloff (float) - Must be between 0 and 1. Determines reduction of Damage and Knockback when further from the Origin.
        0 means no Damage or Knockback falloff while in range, 1 means a 100% reduction at the furthest edge of range.
    exclusions (List<Entity>) - A list of entities that will be excluded from the effect if in range.
     */
    public static void explosionKnockbackEffect(@Nullable Entity source, Location origin, double range, double damage, double knockback, float falloff, @Nullable List<Entity> exclusions){
        if (debug) System.out.println(
                "explosionKnockbackEffect()\nsource: " + source + "\norigin: " + origin + "\nrange: " + range + "\ndamage: " + damage +
                "\nknockback: " + knockback + "\nfalloff: " + falloff + "\nexclusions: " + exclusions
        );
        World world = origin.getWorld();
        if (world == null)return;
        List<LivingEntity> livingEntities = world.getNearbyEntities(origin, range,range,range).stream()
                .filter(entity -> entity instanceof LivingEntity)
                .map(LivingEntity.class::cast)
                .collect(Collectors.toList())
        ;
        if (debug) System.out.println("nearbyEntities: " + livingEntities);
        List<LivingEntity> targets = new ArrayList<>();
        Vector originVector = origin.toVector();
        for (LivingEntity livingEntity:livingEntities){
            if (exclusions != null && exclusions.contains(livingEntity))continue;
            if (livingEntity.getLocation().equals(origin)){
                targets.add(livingEntity);
                continue;
            }
            RayTraceResult rayTrace = world.rayTraceEntities(origin,Util.getDirection(originVector,livingEntity.getLocation().toVector()),range,0.2, entity -> !(entity.equals(source)));
            if (rayTrace != null && rayTrace.getHitEntity() != null && rayTrace.getHitEntity().equals(livingEntity))targets.add(livingEntity);
        }
        if (debug) System.out.println("targets: " + targets);
        if (targets.size() == 0)return;
        for (LivingEntity livingEntity:targets){
            Vector targetLocationVector = livingEntity.getEyeLocation().toVector();
            Vector direction = Util.getDirection(originVector,targetLocationVector);
            double distance = originVector.distance(targetLocationVector);
            double multiplier = (distance / range);
            if (falloff != 0) multiplier = multiplier / falloff;
            double finalDamage = damage * multiplier;
            if (damage >= 1){
                livingEntity.damage(finalDamage, source);
            }else livingEntity.teleport(livingEntity.getLocation().add(0,0.1,0));
            Vector velocity = direction.multiply(knockback * multiplier);
            livingEntity.setVelocity(livingEntity.getVelocity().add(velocity));
        }
    }

    // Time & Date Functions -------------------------------------------------------------------------------------------
    public static String getCurrentDateAndTime(){
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();
        String year = String.valueOf(date.getYear());
        String month = String.valueOf(date.getMonthValue());
        if (month.length() == 1){
            month = "0" + month;
        }
        String day = String.valueOf(date.getDayOfMonth());
        if (day.length() == 1){
            day = "0" + day;
        }
        String hour = String.valueOf(time.getHour());
        if (hour.length() == 1){
            hour = "0" + hour;
        }
        String minute = String.valueOf(time.getMinute());
        if (minute.length() == 1){
            minute = "0" + minute;
        }
        String returnValue = year + month + day + hour + minute;
        if (debug) System.out.println("getCurrentDateAndTime(): " + returnValue);
        return returnValue;
    }
    public static String getFriendlyDateAndTimeString(String dateAndTime){
        List<String> stringList = getDateAndTimeStringList(dateAndTime);
        StringBuilder stringBuilder = new StringBuilder();
        int size = stringList.size();
        int i = 0;
        for (String string:stringList){
            stringBuilder.append(string);
            if (i == 1){
                stringBuilder.append(", ");
            }else if (i == 2){
                stringBuilder.append(". ");
            }else if (i == 3){
                stringBuilder.append(":");
            }
            else stringBuilder.append(" ");
            i++;
        }
        String returnValue = stringBuilder.toString();
        if (debug) System.out.println("getFriendlyDateAndTimeString(): " + returnValue);
        return returnValue;
    }
    public static List<String> getDateAndTimeStringList(String dateAndTime){
        if (dateAndTime.length() != 12){
            System.out.println("getFriendlyDateAndTimeString() Was given a dateAndTime String of more or less than 12 characters.");
            return List.of("INVALID DATE AND TIME STRING");
        }
        String year = dateAndTime.substring(0, 4);
        int yearNum = Integer.parseInt(year);
        String month = dateAndTime.substring(4, 6);
        int monthNum = Integer.parseInt(month);
        String day = dateAndTime.substring(6, 8);
        int dayNum = Integer.parseInt(day);
        String hour = dateAndTime.substring(8, 10);
        int hourNum = Integer.parseInt(hour);
        String minute = dateAndTime.substring(10, 12);
        int minuteNum = Integer.parseInt(minute);
        List<String> meridiemStrings = getMeridiemTime(hourNum);
        hour = meridiemStrings.get(0);
        String meridiem = meridiemStrings.get(1);

        month = getMonthNameFromNumeral(monthNum);
        day = getDaySpokenNameFromNumeral(dayNum);

        List<String> stringList = new ArrayList<>();
        stringList.add(month);
        stringList.add(day);
        stringList.add(year);
        stringList.add(hour);
        stringList.add(minute);
        stringList.add(meridiem);
        if (debug) System.out.println("getDateAndTimeStringList(): " + stringList);
        return stringList;
    }
    public static String getMonthNameFromNumeral(int numeral){
        if (numeral < 1 || numeral > 12){
            System.out.println("getMonthNameFromNumeral() Was given a numeral less than one, or greater than twelve.");
            return "INVALID NUMERAL PROVIDED";
        }
        return getMonthNumeralNameMap().getOrDefault(numeral,"");
    }
    private static Map<Integer, String> monthNumeralNameMap;
    public static Map<Integer, String> getMonthNumeralNameMap(){
        if (monthNumeralNameMap != null)return monthNumeralNameMap;
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "January");
        map.put(2, "February");
        map.put(3, "March");
        map.put(4, "April");
        map.put(5, "May");
        map.put(6, "June");
        map.put(7, "July");
        map.put(8, "August");
        map.put(9, "September");
        map.put(10, "October");
        map.put(11, "November");
        map.put(12, "December");
        monthNumeralNameMap = map;
        return getMonthNumeralNameMap();
    }
    public static String getDaySpokenNameFromNumeral(int i){
        int lastDigit = i % 10;
        String returnValue = i + getDaySpokenNameMap().getOrDefault(lastDigit, "");
        if (debug) System.out.println("getDaySpokenNameFromNumeral(" + i + "): " + returnValue);
        return returnValue;
    }
    private static Map<Integer, String> daySpokenNameMap;
    public static Map<Integer, String> getDaySpokenNameMap(){
        if (daySpokenNameMap != null)return daySpokenNameMap;
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "th");
        map.put(1, "st");
        map.put(2, "nd");
        map.put(3, "rd");
        map.put(4, "th");
        map.put(5, "th");
        map.put(6, "th");
        map.put(8, "th");
        map.put(9, "th");
        daySpokenNameMap = map;
        return getDaySpokenNameMap();
    }
    public static List<String> getMeridiemTime(int hour){
        String meridiem = "A.M.";
        if (hour >= 12){
            hour = hour - 12;
            meridiem = "P.M.";
        }
        if (hour == 0) hour = 12;
        return List.of(String.valueOf(hour), meridiem);
    }

}
