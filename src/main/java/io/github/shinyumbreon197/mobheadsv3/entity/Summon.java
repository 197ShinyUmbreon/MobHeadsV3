package io.github.shinyumbreon197.mobheadsv3.entity;

import io.github.shinyumbreon197.mobheadsv3.AVFX;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.data.Key;
import io.github.shinyumbreon197.mobheadsv3.function.PotionEffectManager;
import io.github.shinyumbreon197.mobheadsv3.function.Util;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class Summon {

    public static void startSummonThread(){
        new BukkitRunnable(){
            @Override
            public void run() {
                watchSummoners();
                cleanupItems();
            }
        }.runTaskTimer(MobHeadsV3.getPlugin(),0,20);
    }
    private static final NamespacedKey summonerKey = Key.summoner;
    private static final NamespacedKey lifeKey = Key.summonLife;
    private static final NamespacedKey targetKey = Key.summonTarget;
    private static int getSummonLifeRemaining(Mob summon){
        Integer i = 0;
        if (summon == null) return i;
        PersistentDataContainer data = summon.getPersistentDataContainer();
        if (data.has(lifeKey,PersistentDataType.INTEGER)) i = data.get(lifeKey,PersistentDataType.INTEGER);
        if (i == null) i = 0;
        return i;
    }
    private static void updateSummonLifeRemaining(Mob summon, int life){
        PersistentDataContainer data = summon.getPersistentDataContainer();
        data.set(lifeKey,PersistentDataType.INTEGER,life);
    }
    public static LivingEntity getSummonerFromSummon(Mob summon){
        String uuidString = null;
        PersistentDataContainer data = summon.getPersistentDataContainer();
        if (data.has(summonerKey,PersistentDataType.STRING)) uuidString = data.get(summonerKey,PersistentDataType.STRING);
        if (uuidString == null)return null;
        Entity matchedEnt = Bukkit.getEntity(UUID.fromString(uuidString));
        if (!(matchedEnt instanceof LivingEntity))return null;
        return (LivingEntity) matchedEnt;
    }
    private static void updateSummonTarget(Mob summon, LivingEntity target){
        summon.setTarget(target);
        UUID targetUUID = target.getUniqueId();
        PersistentDataContainer data = summon.getPersistentDataContainer();
        data.set(targetKey,PersistentDataType.STRING,targetUUID.toString());
    }
    private static boolean reaffirmSummonTarget(Mob summon){
        LivingEntity currentTarget = summon.getTarget();
        PersistentDataContainer data = summon.getPersistentDataContainer();
        String stringUUID = null;
        if (data.has(targetKey, PersistentDataType.STRING)) stringUUID = data.get(targetKey,PersistentDataType.STRING);
        if (currentTarget == null || stringUUID == null || !currentTarget.getUniqueId().toString().matches(stringUUID)){
            return findNewTarget(summon);
        }
        return true;
    }

    private static boolean findNewTarget(Mob summon){
        LivingEntity summoner = getSummonerFromSummon(summon);
        if (summoner == null)return false;
        List<Mob> nearbyMobs = summon.getNearbyEntities(30,15,30).stream()
                .filter(entity -> entity instanceof Mob)
                .map(Mob.class::cast)
                .collect(Collectors.toList()
        );
        List<Mob> validTargets = new ArrayList<>();
        for (Mob nearbyMob:nearbyMobs){
            if (nearbyMob.isDead() || nearbyMob.equals(summoner))continue;
            LivingEntity target = nearbyMob.getTarget();
            if (target != null && target.equals(summoner)) validTargets.add(nearbyMob);
        }
        if (validTargets.size() == 0)return false;
        Random random = new Random();
        updateSummonTarget(summon, validTargets.get(random.nextInt(0,validTargets.size())));
        return true;
    }

    private static final List<EntityType> summonTypes = List.of(EntityType.WOLF,EntityType.BEE,EntityType.SILVERFISH, EntityType.VEX);
    public static List<EntityType> getSummonTypes(){
        return summonTypes;
    }

    private static final Map<LivingEntity,List<Mob>> summoners = new HashMap<>();
    private static void addSummonToSummoner(LivingEntity summoner, Mob summon){
        List<Mob> summons;
        if (summoners.containsKey(summoner)){
            summons = summoners.get(summoner);
        }else summons = new ArrayList<>();
        summons.add(summon);
        summoners.put(summoner,summons);
    }
    public static void dispelSummon(Mob remove){
        for (LivingEntity summoner:summoners.keySet()){
            List<Mob> toKeep = new ArrayList<>();
            boolean hit = false;
            for (Mob summon:summoners.get(summoner)){
                if (summon.equals(remove)){
                    hit = true;
                }else{
                    toKeep.add(summon);
                }
            }
            if (hit){
                summoners.put(summoner,toKeep);
                break;
            }
        }
        removeSummon(remove);
    }
    private static void watchSummoners(){
        Map<LivingEntity,List<Mob>> newMap = new HashMap<>();
        List<Mob> toRemove = new ArrayList<>();
        for (LivingEntity summoner:summoners.keySet()){
            List<Mob> summons = summoners.get(summoner);
            if (summons.size() == 0)continue;
            List<Mob> toKeep = new ArrayList<>();
            for (Mob summon:summons){
                int life = getSummonLifeRemaining(summon);
                boolean validTarget = reaffirmSummonTarget(summon);
                if (!validTarget || summon.isDead() || life == 0){
                    toRemove.add(summon);
                    continue;
                }
                life--;
                updateSummonLifeRemaining(summon,life);
                toKeep.add(summon);
            }
            if (toKeep.size() == 0)continue;
            newMap.put(summoner,toKeep);
        }
        for (Mob remove:toRemove){
            AVFX.playSummonDispelEffect(remove.getLocation());
            removeSummon(remove);
        }
        summoners.clear();
        summoners.putAll(newMap);
    }
    private static void cleanupItems(){
        List<Entity> toRemove = new ArrayList<>();
        for (World world:Bukkit.getWorlds()){
            for (Entity entity: world.getEntities()){
                if (!(entity instanceof Item) || entity.getVehicle() != null)continue;
                PersistentDataContainer data = entity.getPersistentDataContainer();
                if (data.has(summonerKey,PersistentDataType.STRING)){
                    toRemove.add(entity);
                }
            }
        }
        for (Entity entity:toRemove) entity.remove();
    }
    private static void removeSummon(Mob summon){
        for (Entity passenger:summon.getPassengers()){
            if (passenger instanceof Item) passenger.remove();
        }
        summon.remove();
    }

    private static final List<LivingEntity> summonerCooldown = new ArrayList<>();
    private static boolean onCooldown(LivingEntity owner){
        return summonerCooldown.contains(owner);
    }
    private static void putOnCooldown(LivingEntity summoner, int seconds){
        if (onCooldown(summoner))return;
        summonerCooldown.add(summoner);
        new BukkitRunnable(){
            @Override
            public void run() {
                summonerCooldown.remove(summoner);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(), seconds * 20L);
    }

    public static void spawnSummon(Location location, EntityType entityType, LivingEntity summoner, Entity target){
        if (onCooldown(summoner) || !(target instanceof LivingEntity))return;
        LivingEntity livingTarget = (LivingEntity) target;
        World world = location.getWorld();
        if (world == null)return;

        Location summonLoc = getSummoningLoc(summoner,entityType);

        Mob summon = specializeSummon((Mob) world.spawnEntity(summonLoc,entityType));
        summon.setTarget(livingTarget);
        summon.setRemoveWhenFarAway(true);
        summon.setCanPickupItems(false);
        summon.setLootTable(null);
        summon.setPersistent(false);

        Vector velocity = getSummoningVelocity(summoner, entityType);
        summon.setVelocity(velocity);

        Item ownerHeadItem = getOwnerHeadItem(summoner, summon.getLocation());
        summon.addPassenger(ownerHeadItem);

        PersistentDataContainer data = summon.getPersistentDataContainer();
        data.set(summonerKey, PersistentDataType.STRING, summoner.getUniqueId().toString());
        data.set(targetKey,PersistentDataType.STRING,livingTarget.getUniqueId().toString());
        data.set(lifeKey,PersistentDataType.INTEGER,getSummonTypeLifespan(entityType));
        AVFX.playSummonEffect(summonLoc,entityType);
        //addSummon(new Summon(summon,summoner,livingTarget,getSummonTypeLifespan(entityType),getSummonTypeCooldown(entityType)));
        addSummonToSummoner(summoner,summon);
        putOnCooldown(summoner,getSummonTypeCooldown(entityType));
    }

    private static Location getSummoningLoc(LivingEntity owner, EntityType summonType){
        Location summonLoc = owner.getLocation();
        switch (summonType){
            case WOLF, SILVERFISH -> {summonLoc = Util.getSafeTeleportLoc(owner.getLocation(),2,2,2);}
            case BEE -> {summonLoc = owner.getEyeLocation().add(0,0.3,0);}
            case VEX -> {summonLoc = owner.getEyeLocation();}
        }
        return summonLoc;
    }

    private static Vector getSummoningVelocity(LivingEntity owner, EntityType summonType){
        Vector velocity = new Vector();
        Vector facing = owner.getLocation().getDirection();
        facing.setY(0);
        switch (summonType){
            case WOLF -> {}
            case BEE -> {velocity = velocity.add(facing).multiply(-0.6).add(new Vector(0,0.25,0));}
            case SILVERFISH -> {}
            case VEX -> {velocity = facing.clone().multiply(-0.3);}
        }
        return velocity;
    }

    public static boolean entityIsSummon(Entity entity){
        PersistentDataContainer data = entity.getPersistentDataContainer();
        return data.has(summonerKey,PersistentDataType.STRING);
    }

    private static Item getOwnerHeadItem(LivingEntity owner, Location spawnLoc){
        ItemStack headItem = new ItemStack(Material.PLAYER_HEAD);
        MobHead mobHead = MobHead.getMobHeadOfEntity(owner);
        if (mobHead != null) headItem = mobHead.getHeadItemStack();
        World world = owner.getWorld();
        Item item = world.dropItem(spawnLoc,headItem);
        PersistentDataContainer data = item.getPersistentDataContainer();
        data.set(summonerKey, PersistentDataType.STRING, owner.getUniqueId().toString());
        item.setPickupDelay(99999);
        item.setCustomName(ChatColor.RED + owner.getName());
        item.setCustomNameVisible(true);
        return item;
    }
    public static PotionEffect getSummonAfflictionEffect(EntityType summonType){
        switch (summonType){
            default -> {
                return null;
            }
            case BEE -> {
                return PotionEffectManager.buildSimpleEffect(PotionEffectType.POISON,1,5*20);
            }
        }
    }

    private static Mob specializeSummon(Mob summon){
        switch (summon.getType()){
            case WOLF -> {
                Wolf wolfSummon = (Wolf) summon;
                wolfSummon.setAngry(true);
                wolfSummon.setCollarColor(DyeColor.BLACK);
                wolfSummon.addPotionEffects(List.of(
                        new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,9999,0,false),
                        new PotionEffect(PotionEffectType.SPEED,9999,0,false)
                ));
            }
            case BEE -> {
                Bee beeSummon = (Bee) summon;
                beeSummon.setAnger(1);
                beeSummon.setCannotEnterHiveTicks(9999);
                beeSummon.setHealth(6);
                beeSummon.addPotionEffects(List.of(
                        new PotionEffect(PotionEffectType.INCREASE_DAMAGE,9999,0,false),
                        new PotionEffect(PotionEffectType.SPEED,9999,0,false)
                ));
            }
            case SILVERFISH -> {
                Silverfish silverfishSummon = (Silverfish) summon;
                silverfishSummon.addPotionEffects(List.of(
                        new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,9999,0,false),
                        new PotionEffect(PotionEffectType.INCREASE_DAMAGE,9999,0,false),
                        new PotionEffect(PotionEffectType.SPEED,9999,0,false)
                ));
            }
            case VEX -> {
                Vex vexSummon = (Vex) summon;
                vexSummon.setBound(vexSummon.getLocation());
                vexSummon.setHealth(4);
                vexSummon.addPotionEffects(List.of(
                        new PotionEffect(PotionEffectType.WEAKNESS,9999,0,false)
                ));
            }
        }
        return summon;
    }

    private static int getSummonTypeCooldown(EntityType summonType){
        switch (summonType){
            case WOLF -> {return 5;}
            case BEE, VEX -> {return 1;}
            case SILVERFISH -> {return 0;}
        }
        return -1;
    }

    private static int getSummonTypeLifespan(EntityType summonType){
        switch (summonType){
            case WOLF -> {return 60*20;}
            case BEE -> {return 30*20;}
            case SILVERFISH, VEX -> {return 15*20;}
        }
        return -1;
    }



}
