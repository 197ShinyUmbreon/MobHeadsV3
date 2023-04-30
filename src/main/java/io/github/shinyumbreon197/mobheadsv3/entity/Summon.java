package io.github.shinyumbreon197.mobheadsv3.entity;

import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.effect.AVFX;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class Summon implements Listener {

    private static final NamespacedKey ownerNSK = new NamespacedKey(MobHeadsV3.getPlugin(),"owner");
    private static final NamespacedKey targetNSK = new NamespacedKey(MobHeadsV3.getPlugin(),"target");
    private static final NamespacedKey headItemNSK = new NamespacedKey(MobHeadsV3.getPlugin(), "display");
    private static final List<Mob> summons = new ArrayList<>();
    private static final List<Item> headItems = new ArrayList<>();
    private static final Map<Entity, Integer> summonLifeTimerMap = new HashMap<>();
    private static final Map<LivingEntity, Integer> summonCooldownTimerMap = new HashMap<>();

    @EventHandler
    public static void onUnloadSummon(ChunkUnloadEvent e){
        for (Entity entity:e.getChunk().getEntities()){
            if (isSummon(entity)){
                for (Entity passenger: entity.getPassengers()){
                    if (passenger instanceof Item){
                        headItems.remove(passenger);
                        passenger.remove();
                    }
                }
                summons.remove(entity);
                entity.remove();
            }
        }
    }

    @EventHandler
    public static void onSummonTarget(EntityTargetLivingEntityEvent e){
        if (!isSummon(e.getEntity()))return;
        Mob summon = (Mob) e.getEntity();
        LivingEntity target = e.getTarget();
        if (target == null)return;
        LivingEntity owner = getSummonOwner(summon);
        if (owner == null)return;
        boolean canceled = false;
        if (target.equals(owner) || target.equals(summon) || owner.isDead() || target.isDead() || (owner instanceof Player) && ((Player) owner).isSneaking()){
            canceled = true;
        }else if (target instanceof Mob){
            LivingEntity targetOwner = getSummonOwner((Mob) target);
            if (targetOwner == null)return;
            if (!targetOwner.equals(owner))return;
            canceled = true;
        }
        e.setCancelled(canceled);
    }

    public static Entity getSummonTarget(Mob summon){
        if (!isSummon(summon))return null;
        PersistentDataContainer data = summon.getPersistentDataContainer();
        String targetUUIDString = data.get(targetNSK, PersistentDataType.STRING);
        if (targetUUIDString == null)return null;
        UUID targetUUID = UUID.fromString(targetUUIDString);
        return Bukkit.getEntity(targetUUID);
    }

    public static LivingEntity getSummonOwner(Mob summon){
        if (!isSummon(summon))return null;
        PersistentDataContainer data = summon.getPersistentDataContainer();
        String ownerUUIDString = data.get(ownerNSK, PersistentDataType.STRING);
        if (ownerUUIDString == null)return null;
        UUID ownerUUID = UUID.fromString(ownerUUIDString);
        return (LivingEntity) Bukkit.getEntity(ownerUUID);
    }

    public static boolean isSummon(Entity summon){
        if (summon == null)return false;
        PersistentDataContainer data = summon.getPersistentDataContainer();
        return data.has(ownerNSK, PersistentDataType.STRING);
    }

    public static Location getSafeSummonLocation(Location summonerLoc, BlockFace facing){
        List<Material> airMats = List.of(Material.AIR, Material.CAVE_AIR, Material.WATER);
        List<Material> unsafeMats = List.of(Material.LAVA, Material.FIRE, Material.SOUL_FIRE, Material.CAMPFIRE, Material.SOUL_CAMPFIRE, Material.SWEET_BERRY_BUSH);
        World world = summonerLoc.getWorld();
        //summonerLoc = summonerLoc.add(0,1,0);
        if (world == null)return summonerLoc;
        Block source = world.getBlockAt(summonerLoc);
        Block above = source.getRelative(BlockFace.UP);
        Block below = source.getRelative(BlockFace.DOWN);
        List<Block> surround = new ArrayList<>();
        List<BlockFace> blockFaces = List.of(
                BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST,
                BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST
                );
        List<BlockFace> blockFacesToCheck = new ArrayList<>();
        for (BlockFace blockFace:blockFaces){
            if (blockFace.equals(facing))continue;
            blockFacesToCheck.add(blockFace);
        }
        blockFacesToCheck.add(facing);
        for (BlockFace blockFace:blockFacesToCheck){
            if (!blockFaces.contains(blockFace))continue;
            surround.add(source.getRelative(blockFace));
        }
        for (BlockFace blockFace:blockFacesToCheck){
            if (!blockFaces.contains(blockFace))continue;
            surround.add(above.getRelative(blockFace));
        }
        for (BlockFace blockFace:blockFacesToCheck){
            if (!blockFaces.contains(blockFace))continue;
            surround.add(below.getRelative(blockFace));
        }
        Location summonLoc = summonerLoc;
        for (Block block:surround){
            Material beneathMat = block.getRelative(BlockFace.DOWN).getType();
            Material sourceMat = block.getType();
            if (airMats.contains(sourceMat) && !unsafeMats.contains(beneathMat)){
                summonLoc = block.getLocation().add(0.5, 0, 0.5);
                //System.out.println("Found summon spot at "+summonLoc); //debug
                break;
            }
        }
        return summonLoc;
    }

    private static void manageDisplays(){
        List<Item> toRemove = new ArrayList<>();
        for (Item item: headItems){
            Entity vehicle = item.getVehicle();
            PersistentDataContainer data = item.getPersistentDataContainer();
            boolean isSummonDisplay = data.has(Summon.headItemNSK, PersistentDataType.STRING);
            if (!isSummonDisplay)continue;
            if (vehicle == null){
                toRemove.add(item);
                item.remove();
            }else{
                float yaw = vehicle.getLocation().getYaw();
                float pitch = vehicle.getLocation().getPitch();
                item.setRotation(yaw, pitch);
            }
        }
        headItems.removeAll(toRemove);
    }

    public static void manageSummons(){
        manageDisplays();
        List<LivingEntity> toRemoveFromCooldown = new ArrayList<>();
        for (LivingEntity owner:summonCooldownTimerMap.keySet()){
            int timer = summonCooldownTimerMap.get(owner);
            timer = timer - 10;
            if (timer <= 0){
                toRemoveFromCooldown.add(owner);
            }else summonCooldownTimerMap.put(owner, timer);
        }
        for (LivingEntity toRemove:toRemoveFromCooldown){
            summonCooldownTimerMap.remove(toRemove);
        }
        List<Entity> summonsToBeRemoved = new ArrayList<>();
        for (Mob summon:summons){
            if (summonLifeTimerMap.containsKey(summon)){
                int timer = summonLifeTimerMap.get(summon);
                timer = timer - 10;
                if (timer <= 0 || (summon instanceof LivingEntity && summon.isDead())){
                    summonLifeTimerMap.remove(summon);
                    summonsToBeRemoved.add(summon);
                }else summonLifeTimerMap.put(summon, timer);
            }
            PersistentDataContainer data = summon.getPersistentDataContainer();
            String ownerUUIDString = data.get(ownerNSK,PersistentDataType.STRING);
            String targetUUIDString = data.get(targetNSK,PersistentDataType.STRING);
            if (ownerUUIDString == null || targetUUIDString == null){
                summonsToBeRemoved.add(summon);
                continue;
            }
            UUID ownerUUID = UUID.fromString(ownerUUIDString);
            UUID targetUUID = UUID.fromString(targetUUIDString);
            LivingEntity owner = (LivingEntity) Bukkit.getEntity(ownerUUID);
            LivingEntity target = (LivingEntity) Bukkit.getEntity(targetUUID);
            if (owner == null){
                summonsToBeRemoved.add(summon);
                continue;
            }
            if (target == null || target.isDead() || target.equals(summon) || target.equals(owner)){
                target = findNewTarget(owner, summon);
                if (target == null || target.isDead() || target.equals(summon)){
                    summonsToBeRemoved.add(summon);
                    continue;
                }
            }
            EntityType summonType = summon.getType();
            summon.setTarget(target);
        }
        for (Entity toRemove:summonsToBeRemoved){
            summons.remove(toRemove);
            AVFX.playSummonEffect(toRemove.getLocation());
            if (toRemove.getPassengers().size() != 0){
                for (Entity passenger:toRemove.getPassengers()){
                    PersistentDataContainer data = passenger.getPersistentDataContainer();
                    if (data.has(headItemNSK,PersistentDataType.STRING)) passenger.remove();
                }
            }
            toRemove.remove();
        }
    }

    private static LivingEntity findNewTarget(LivingEntity owner, Mob summon){
        PersistentDataContainer data = summon.getPersistentDataContainer();
        List<Entity> nearbyOwner = owner.getNearbyEntities(15, 5, 15);
        for (Entity nearby:nearbyOwner){
            if (!(nearby instanceof Mob))continue;
            Mob mobNearby = (Mob) nearby;
            LivingEntity mobTarget = mobNearby.getTarget();
            if (mobTarget != null && mobTarget.equals(owner)){
                data.set(targetNSK, PersistentDataType.STRING, mobNearby.getUniqueId().toString());
                return mobNearby;
            }
        }
        return null;
    }

    public static void summon(LivingEntity owner, LivingEntity target, EntityType summonType){
        List<EntityType> whitelistedSummons = List.of(EntityType.WOLF, EntityType.SILVERFISH, EntityType.BEE);
        if (!whitelistedSummons.contains(summonType))return;
        if (summonCooldownTimerMap.get(owner) != null)return;
        Random random = new Random();
        Location location = owner.getLocation();
        World world = location.getWorld();
        if (world == null || target == null)return;
        int lifeTimer = 60*20;
        int cooldownTimer = 10*20;
        switch (summonType){
            case BEE -> {location = owner.getEyeLocation(); lifeTimer = 30*20; cooldownTimer = 2*20;}
            case SILVERFISH -> {location = getSafeSummonLocation(owner.getLocation(), owner.getFacing()); cooldownTimer = 20;}
            case WOLF -> {location = getSafeSummonLocation(owner.getLocation(), owner.getFacing());}
        }
        Mob summon = (Mob) world.spawnEntity(location, summonType);

        summons.add(summon);
        summonLifeTimerMap.put(summon, lifeTimer);
        summonCooldownTimerMap.put(owner, cooldownTimer);

        PersistentDataContainer data = summon.getPersistentDataContainer();
        data.set(ownerNSK, PersistentDataType.STRING, owner.getUniqueId().toString());
        data.set(targetNSK, PersistentDataType.STRING, target.getUniqueId().toString());

        switch (summonType){
            case BEE -> {
                Bee bee = (Bee) summon;
                bee.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, lifeTimer, 0, false));
                bee.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, lifeTimer,1,false));
                bee.setAnger(99);
                bee.setHealth(3.0);
                Vector direction = owner.getLocation().getDirection();
                direction.setX(direction.getX()*-0.3);
                direction.setZ(direction.getZ()*-0.3);
                direction.setY(0);
                bee.setVelocity(bee.getVelocity().setY(0.15).add(direction));
                AVFX.playBeeSummonEffect(location);
            }
            case WOLF -> {
                Wolf wolf = (Wolf) summon;
                wolf.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, lifeTimer,1,false));
                wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, lifeTimer,1,false));
                wolf.setAngry(true);
                wolf.setCollarColor(DyeColor.BLACK);
                AVFX.playWolfSummonEffect(location);
            }
            case SILVERFISH -> {
                summon.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, lifeTimer,0,false));
                summon.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, lifeTimer,1,false));
                AVFX.playSummonEffect(location);
            }
        }

        ItemStack ownerHeadItem = HeadUtil.getHeadItemFromEntity(owner);
        if (owner instanceof Player){
            MobHead ownerHead = HeadUtil.getHeadFromUUID(owner.getUniqueId());
            if (ownerHead != null) ownerHeadItem = ownerHead.getHeadItem();
        }

        Item item = world.dropItem(location, new ItemStack(Material.PLAYER_HEAD));
        if (ownerHeadItem != null) item.setItemStack(ownerHeadItem);
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setCustomName(ChatColor.RED+owner.getName());
        item.setCustomNameVisible(true);

        PersistentDataContainer itemData = item.getPersistentDataContainer();
        itemData.set(headItemNSK, PersistentDataType.STRING, summon.getUniqueId().toString());

        summon.addPassenger(item);
        summon.setTarget(target);
        summon.setRemoveWhenFarAway(true);
    }

}
