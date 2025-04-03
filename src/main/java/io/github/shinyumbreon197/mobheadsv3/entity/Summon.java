package io.github.shinyumbreon197.mobheadsv3.entity;

import io.github.shinyumbreon197.mobheadsv3.AVFX;
import io.github.shinyumbreon197.mobheadsv3.Hud;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.data.Data;
import io.github.shinyumbreon197.mobheadsv3.function.PotionEffectManager;
import io.github.shinyumbreon197.mobheadsv3.function.Util;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ColorableArmorMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;
import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.getPlugin;

public class Summon implements Listener {

    private static final Random random = new Random();

    private static final Set<Entity> summonersOnCooldown = new HashSet<>();
    private static final List<Summon> summons = new ArrayList<>();

    public static void resetSummoner(LivingEntity summoner){
        castingSummonersCountdownMap.remove(summoner);

    }

    //private static final Map<Player, Scoreboard> summonerScoreboardMap = new HashMap<>();
//    private static ScoreboardManager scoreboardManager;
//    private static ScoreboardManager getScoreboardManager(){
//        if (scoreboardManager != null)return scoreboardManager;
//        scoreboardManager = Bukkit.getScoreboardManager();
//        return scoreboardManager;
//    }

    private static final Set<EntityType> summonTypes = Set.of(
            EntityType.WOLF, EntityType.SILVERFISH, EntityType.BEE, EntityType.VEX, EntityType.SNOW_GOLEM, EntityType.ZOMBIFIED_PIGLIN, EntityType.FOX
    );
    public static boolean isSummonType(EntityType entityType){
        return summonTypes.contains(entityType);
    }
    private static final Map<EntityType,Integer> summonLifespanMap = Map.of( // multiples of half seconds!
            EntityType.WOLF, 60*2,
            EntityType.SILVERFISH, 20*2,
            EntityType.BEE, 30*2,
            EntityType.VEX, 60*2,
            EntityType.SNOW_GOLEM, 30*2,
            EntityType.ZOMBIFIED_PIGLIN, 60*2,
            EntityType.FOX,45*2
    );
    private static final Map<EntityType,Integer> summonCooldownMap = Map.of(
            EntityType.WOLF, 10*20,
            EntityType.SILVERFISH, 15*20,
            EntityType.BEE, 2*20,
            EntityType.VEX, 10*20,
            EntityType.SNOW_GOLEM, 5*20,
            EntityType.ZOMBIFIED_PIGLIN, 10*20,
            EntityType.FOX, 10*20
    );
    private static final Map<EntityType,EntityType> summonCommandingTypeMap = Map.of(
            EntityType.WOLF, EntityType.WOLF,
            EntityType.SILVERFISH, EntityType.SILVERFISH,
            EntityType.BEE, EntityType.BEE,
            EntityType.VEX, EntityType.VEX,
            EntityType.SNOW_GOLEM, EntityType.SNOW_GOLEM,
            EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOMBIFIED_PIGLIN,
            EntityType.FOX, EntityType.FOX
    );
    private static final Map<EntityType,Integer> summonAmountMap = Map.of(
            EntityType.WOLF, 2,
            EntityType.SILVERFISH, 8,
            EntityType.BEE, 1,
            EntityType.VEX, 2,
            EntityType.SNOW_GOLEM, 1,
            EntityType.ZOMBIFIED_PIGLIN, 1,
            EntityType.FOX, 3
    );
    private static final Map<EntityType,Integer> summonCastingTimeMap = Map.of(
            EntityType.WOLF,2*20,
            EntityType.SILVERFISH, 10,
            EntityType.BEE, 20,
            EntityType.VEX, 2*20,
            EntityType.SNOW_GOLEM, 10,
            EntityType.ZOMBIFIED_PIGLIN, 20,
            EntityType.FOX, 30
    );

//    private static void updateScoreboards(){
//        Map<Player,Set<Summon>> playerSummonSetMap = new HashMap<>();
//        for (Summon summon:summons){
//            if (summon.getOwner() instanceof Player){
//                Player player = (Player) summon.getOwner();
//                Set<Summon> summons = playerSummonSetMap.getOrDefault(player, new HashSet<>());
//                summons.add(summon);
//                playerSummonSetMap.put(player, summons);
//            }
//        }
//        if (playerSummonSetMap.size() == 0)return;
//        for (Player player:playerSummonSetMap.keySet()){
//            Scoreboard scoreboard = summonerScoreboardMap.getOrDefault(player, getScoreboardManager().getNewScoreboard());
//
//            Set<Summon> summons = playerSummonSetMap.getOrDefault(player, new HashSet<>());
//            for (Summon summon:summons){
//
//            }
//        }
//
//    }

    private static final Map<LivingEntity,Integer> castingSummonersCountdownMap = new HashMap<>();
    public static void castSummon(LivingEntity owner, LivingEntity target, EntityType summonType){
        if (summonersOnCooldown.contains(owner) || castingSummonersCountdownMap.containsKey(owner))return;
        int summonCastTime = summonCastingTimeMap.getOrDefault(summonType, 0);
        Location eyeLoc = owner.getEyeLocation();
        AVFX.playSummonBeginEffect(eyeLoc);
        double height = eyeLoc.getY() - owner.getLocation().getY();
        new BukkitRunnable(){
            @Override
            public void run() {
                int castTimeElapsed = castingSummonersCountdownMap.getOrDefault(owner, 0);
                if (owner.isDead() || target == null || target.isDead() || owner.isDead() || MobHead.getMobHeadWornByEntity(owner) == null){
                    castingSummonersCountdownMap.remove(owner);
                    if (owner instanceof Player) Hud.progressBarEnd((Player) owner);
                    cancel();
                    return;
                }
                if (castTimeElapsed >= summonCastTime){
                    castingSummonersCountdownMap.remove(owner);
                    if (owner instanceof Player) Hud.progressBarEnd((Player) owner);
                    createNewSummon(owner,target,summonType);
                    cancel();
                    return;
                }
                castTimeElapsed++;
                if ((castTimeElapsed & 1) == 0) AVFX.playSummonSummoningEffect(owner.getEyeLocation(), castTimeElapsed, height, summonCastTime);
                if (owner instanceof Player){
                    String typeName = Util.friendlyEntityTypeName(summonType);
                    Hud.progressBar((Player) owner,summonCastTime,castTimeElapsed,true,"Summon Type: " + typeName,false);
                }
                castingSummonersCountdownMap.put(owner, castTimeElapsed);
            }
        }.runTaskTimer(getPlugin(),0,1);
    }

    public static void watchSummons(){
        List<Summon> remove = new ArrayList<>();
        for (Summon summonObj:summons){
            Mob summonEnt = summonObj.getSummon();
            if (summonEnt.isDead()){
                remove.add(summonObj);
                continue;
            }
            int lifeSpan = summonObj.getLifespan();
            if (lifeSpan <= 0){
                remove.add(summonObj);
                continue;
            }
            lifeSpan--;
            summonObj.setLifespan(lifeSpan);
            Entity owner = summonObj.getOwner();
            MobHead mobHead = MobHead.getMobHeadWornByEntity(owner);
            if (owner == null || owner.isDead() || mobHead == null){
                remove.add(summonObj);
                continue;
            }
            EntityType headType = mobHead.getEntityType();
            EntityType commanderType = summonCommandingTypeMap.get(summonEnt.getType());
            if (!commanderType.equals(headType)){
                remove.add(summonObj);
                continue;
            }
            LivingEntity currentTarget = summonEnt.getTarget();
            if (currentTarget == null || currentTarget.isDead() || currentTarget.equals(owner)){
                LivingEntity newTarget = getNewSummonTarget(summonObj);
                if (newTarget == null){
                    remove.add(summonObj);
                    continue;
                }else{
                    summonEnt.setTarget(newTarget);
                    summonObj.setTarget(newTarget);
                }
            }
            AVFX.playSummonContinuousEffect(summonEnt);
        }
        for (Summon summonObj:remove) removeSummon(summonObj);
        //updateScoreboards();
    }
    private static ItemStack wolfArmor;
    private static ItemStack getWolfArmor(){
        if (wolfArmor != null)return wolfArmor;
        ItemStack armor = new ItemStack(Material.WOLF_ARMOR);
        ColorableArmorMeta armorMeta = (ColorableArmorMeta) armor.getItemMeta();
        assert armorMeta != null;
        armorMeta.setColor(Color.fromRGB(50,0,100));
        armor.setItemMeta(armorMeta);
        Damageable damageMeta = (Damageable) armor.getItemMeta();
        assert damageMeta != null;
        damageMeta.setDamage(63);
        armor.setItemMeta(damageMeta);
        wolfArmor = armor;
        return wolfArmor;
    }
    private static ItemStack foxMace;
    private static ItemStack getFoxMace(){
        if (foxMace != null)return foxMace;
        ItemStack mace = new ItemStack(Material.MACE);
        ItemMeta itemMeta = mace.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Fox Mace");
        itemMeta.addEnchant(Enchantment.WIND_BURST, 3, false);
        mace.setItemMeta(itemMeta);
        foxMace = mace;
        return foxMace;
    }
    private static ItemStack foxSword;
    private static ItemStack getFoxSword(){
        if (foxSword != null)return foxSword;
        ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta itemMeta = sword.getItemMeta();
        assert itemMeta != null;
        itemMeta.addEnchant(Enchantment.KNOCKBACK,4,true);
        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Foxy Bonk Stick");
        sword.setItemMeta(itemMeta);
        foxSword = sword;
        return foxSword;
    }
    public static void removeSummon(Summon summonObj){
        summons.remove(summonObj);
        Mob summon = summonObj.getSummon();
        Item nameTag = summonObj.getNameTag();
        AVFX.playSummonDispelEffect(summon.getLocation());
        nameTag.remove();
        summon.remove();
    }
    private static void createNewSummon(LivingEntity owner, LivingEntity target, EntityType summonType){
        if (owner == null || owner.isDead() || target == null || owner.isDead())return;
        MobHead mobHead = MobHead.getMobHeadWornByEntity(owner);
        if (mobHead == null)return;
        if (summonersOnCooldown.contains(owner))return;
        World world = owner.getWorld();
        int lifespan = summonLifespanMap.getOrDefault(summonType, 0);
        int lifespanTicks = lifespan * 10;
        int cooldown = summonCooldownMap.getOrDefault(summonType, 0);
        if (lifespan == 0 || cooldown == 0)return;
        int amount = summonAmountMap.getOrDefault(summonType, 0);
        for (int i = 0; i < amount; i++) {
            Mob summon;
            Vector velocity = new Vector();
            Location spawnLocation = summonSpawnLocation(owner, summonType, target);
            switch (summonType){
                case WOLF -> {
                    Wolf wolf = (Wolf) world.spawnEntity(spawnLocation, EntityType.WOLF);
                    String headVariant = mobHead.getVariant();
                    Wolf.Variant variant = Data.getWolfVariantMap().get(headVariant);
                    if (debug) System.out.println("Summon.java::createNewSummon():[Wolf]:headVariant:" + headVariant + " found variant: " + variant); //debug
                    if (variant == null) variant = Wolf.Variant.PALE;
                    wolf.setVariant(variant);
                    //wolf.setVariant(Wolf.Variant.ASHEN);
                    wolf.setCollarColor(DyeColor.BLACK);
                    wolf.setAngry(true);
                    wolf.setAdult();
                    wolf.setBreed(false);
                    EntityEquipment entityEquipment = wolf.getEquipment();
                    assert entityEquipment != null;
                    entityEquipment.setItem(EquipmentSlot.BODY,getWolfArmor(), true);
                    entityEquipment.setChestplateDropChance(0.0f);
                    PotionEffectManager.addEffectToEntity(wolf, new PotionEffect(PotionEffectType.HEALTH_BOOST, lifespanTicks, 1, false, false, false));
                    PotionEffectManager.addEffectToEntity(wolf, new PotionEffect(PotionEffectType.SPEED, lifespanTicks,0,false,false,false));
                    //PotionEffectManager.addEffectToEntity(wolf, new PotionEffect(PotionEffectType.STRENGTH, lifespanTicks, 0, false, false, false));
                    wolf.setHealth(16);
                    summon = wolf;
                }
                case SILVERFISH -> {
                    Silverfish silverfish = (Silverfish) world.spawnEntity(spawnLocation, EntityType.SILVERFISH);
                    silverfish.setHealth(6);
                    //PotionEffectManager.addEffectToEntity(silverfish, new PotionEffect(PotionEffectType.STRENGTH, lifespanTicks,0,false,false,false));
                    PotionEffectManager.addEffectToEntity(silverfish, new PotionEffect(PotionEffectType.SPEED, lifespanTicks,0,false,false,false));
                    summon = silverfish;
                }
                case BEE -> {
                    Bee bee = (Bee) world.spawnEntity(spawnLocation, EntityType.BEE);
                    bee.setAnger(1);
                    bee.setCannotEnterHiveTicks(999999);
                    bee.setAdult();
                    bee.setHealth(2);
                    velocity = owner.getLocation().getDirection().multiply(-0.6).add(new Vector(0.0,0.25,0.0));
                    summon = bee;
                }
                case VEX -> {
                    Vex vex = (Vex) world.spawnEntity(spawnLocation, EntityType.VEX);
                    vex.setLifeTicks(1200);
                    vex.setHealth(6);
                    PotionEffectManager.addEffectToEntity(vex, new PotionEffect(PotionEffectType.WEAKNESS,lifespanTicks,0, false, false, false));
                    velocity = owner.getLocation().getDirection().multiply(-0.6).add(new Vector(0.0,0.1,0.0));
                    summon = vex;
                }
                case SNOW_GOLEM -> {
                    Snowman snowman = (Snowman) world.spawnEntity(spawnLocation, EntityType.SNOW_GOLEM);
                    snowman.setDerp(true);
                    PotionEffectManager.addEffectToEntity(snowman, new PotionEffect(PotionEffectType.SLOWNESS, lifespanTicks, 9, false, false, false));
                    PotionEffectManager.addEffectToEntity(snowman, new PotionEffect(PotionEffectType.HEALTH_BOOST, lifespanTicks, 2, false, false, false));
                    summon = snowman;
                }
                case ZOMBIFIED_PIGLIN -> {
                    PigZombie pigZombie = (PigZombie) world.spawnEntity(spawnLocation, EntityType.ZOMBIFIED_PIGLIN);
                    pigZombie.setAngry(true);
                    summon = pigZombie;
                }
                case FOX -> {
                    Fox fox = (Fox) world.spawnEntity(spawnLocation, EntityType.FOX);
                    String headVariant = mobHead.getVariant();
                    Fox.Type type = Data.getFoxVariantMap().get(headVariant);
                    if (debug) System.out.println("Summon.java::createNewSummon():[Fox]:headVariant:" + headVariant + " found variant: " + type); //debug
                    if (type == null) type = Fox.Type.RED;
                    fox.setFoxType(type);
                    PotionEffectManager.addEffectToEntity(fox, new PotionEffect(PotionEffectType.WEAKNESS, lifespanTicks, 0, false, false, false));
                    EntityEquipment ee = fox.getEquipment();
                    if (ee != null){
                        ee.setItemInMainHand(getFoxSword());
                        ee.setItemInMainHandDropChance(0f);
                    }
                    if (owner instanceof AnimalTamer) fox.setFirstTrustedPlayer((AnimalTamer) owner);
                    summon = fox;
                }
                default -> {return;}
            }

            ItemStack nameTagItem = MobHead.getHeadItemOfEntity(owner);
            Item nameTag = (Item) world.dropItem(owner.getLocation(), nameTagItem);
            nameTag.setPickupDelay(999999);
            nameTag.setCustomName(ChatColor.RED + owner.getName());
            nameTag.setCustomNameVisible(true);
            nameTag.setTicksLived(4800);

            if (i == 0){
                summon.setTarget(target);
            }else{
                LivingEntity randomTarget = randomNearbyTarget(owner,summon,10);
                if (randomTarget == null) randomTarget = target;
                summon.setTarget(randomTarget);
            }
            summon.setPersistent(false);
            summon.setLootTable(null);
            summon.setRemoveWhenFarAway(true);
            summon.setVelocity(velocity);

            summon.addPassenger(nameTag);

            AVFX.playSummonEffect(summon.getLocation(), summonType);

            Summon summonObj = new Summon(summon, owner, target, nameTag, lifespan);
            summons.add(summonObj);
        }
        summonersOnCooldown.add(owner);
        new BukkitRunnable(){
            @Override
            public void run() {
                summonersOnCooldown.remove(owner);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(), cooldown);
    }
    private static Location summonSpawnLocation(LivingEntity owner, EntityType summonType, LivingEntity target){
        switch (summonType){
            case BEE, VEX -> {
                return owner.getEyeLocation().subtract(owner.getLocation().getDirection());
            }
            default -> {
                List<Block> surrounding = Util.getBlocksSurroundingEntity(owner);
                List<Block> valid = new ArrayList<>();
                for (Block block:surrounding){
                    if (!block.isPassable() || block.getType().equals(Material.LAVA))continue;
                    Block below = block.getRelative(BlockFace.DOWN);
                    if (!below.getType().isSolid() && !below.isLiquid())continue;
                    valid.add(block);
                }
                if (valid.size() == 0)return owner.getLocation();
                Random random = new Random();
                return valid.get(random.nextInt(0,valid.size())).getLocation().add(0.5,0.5,0.5);
            }
        }
        //return owner.getLocation();
    }
    private static Mob randomNearbyTarget(LivingEntity owner, Mob summonEntity, double maxRange){
        List<Mob> nearbyWithinRange = new ArrayList<>();
        Vector ownerLocPoint = owner.getLocation().toVector();
        for (Mob mob:nearbyValidTargets(owner,summonEntity)){
            double distance = ownerLocPoint.distance(mob.getLocation().toVector());
            if (distance <= maxRange) nearbyWithinRange.add(mob);
        }
        if (nearbyWithinRange.size() != 0){
            return nearbyWithinRange.get(random.nextInt(nearbyWithinRange.size()));
        }else return null;
    }
    private static Mob closestNearbyTarget(LivingEntity owner, Mob summonEntity){
        double closestDistance = -1;
        Mob closestTarget = null;
        for (Mob mob:nearbyValidTargets(owner,summonEntity)){
            double distance = owner.getLocation().toVector().distance(mob.getLocation().toVector());
            if (closestDistance == -1 || distance < closestDistance){
                closestTarget = mob;
                closestDistance = distance;
            }
        }
        return closestTarget;
    }
    private static List<Mob> nearbyValidTargets(LivingEntity owner, Mob summonEntity){
        List<Entity> nearby = owner.getNearbyEntities(20,6,20);
        List<Mob> hostile = new ArrayList<>();
        for (Entity entity:nearby){
            if (!(entity instanceof Mob))continue;
            Mob mob = (Mob) entity;
            LivingEntity target = mob.getTarget();
            if (target == null)continue;
            boolean summonCanSee = summonEntity.hasLineOfSight(mob);
            boolean ownerCanSee = owner.hasLineOfSight(mob);
            boolean seen = summonCanSee || ownerCanSee;
            if (seen && target.equals(owner)) hostile.add(mob);
        }
        return hostile;
    }
    private static LivingEntity getNewSummonTarget(Summon summon){
        LivingEntity owner = summon.getOwner();
        Mob summonEntity = summon.getSummon();
        if (owner.getLastDamageCause() != null && owner.getLastDamageCause() instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) owner.getLastDamageCause();
            if (!edbee.isCancelled() && !edbee.getDamager().isDead() && edbee.getDamager() instanceof LivingEntity){
                return (LivingEntity) edbee.getDamager();
            }
        }
        return closestNearbyTarget(owner,summonEntity);
    }
    public static boolean isEntitySummon(Entity entity){
        Summon summon = getSummonFromEntity(entity);
        return summon != null;
    }
    public static Summon getSummonFromEntity(Entity entity){
        if (!(entity instanceof Mob))return null;
        Mob mob = (Mob) entity;
        for (Summon summon:summons) if (summon.getSummon().equals(mob))return summon;
        return null;
    }
    public static LivingEntity getOwnerFromSummon(Entity entity){
        if (!(entity instanceof LivingEntity))return null;
        LivingEntity summon = (LivingEntity) entity;
        for (Summon summonObj:summons) if (summonObj.getSummon().equals(summon)) return summonObj.getOwner();
        return null;
    }
    public static boolean isOwnerDamagedBySummon(Entity attacker, LivingEntity damaged){
        if (!(attacker instanceof Mob))return false;
        Mob mob = (Mob) attacker;
        Summon summon = getSummonFromEntity(mob);
        if (summon == null)return false;
        LivingEntity owner = summon.getOwner();
        return owner.equals(damaged);
    }

    @EventHandler
    public static void summonAttemptTargetChange(EntityTargetLivingEntityEvent etlee){
        if (!(etlee.getEntity() instanceof Mob))return;
        Mob mob = (Mob) etlee.getEntity();
        Summon summon = getSummonFromEntity(mob);
        if (summon == null)return;
        Entity newTarget = etlee.getTarget();
        LivingEntity setTarget = summon.getTarget();
        if (newTarget == null || !newTarget.equals(setTarget)) etlee.setCancelled(true);
    }

    @EventHandler
    public static void summonAttacks(EntityDamageByEntityEvent edbee){
        Entity attacker = edbee.getDamager();
        if (!(attacker instanceof Mob))return;
        Summon summon = getSummonFromEntity(attacker);
        if (summon == null)return;
        Entity damaged = edbee.getEntity();
        if (attacker.getType().equals(EntityType.BEE)){
            if (damaged instanceof LivingEntity){
                PotionEffect pe = PotionEffectManager.buildSimpleEffect(PotionEffectType.POISON,1, 5*20);
                PotionEffectManager.addEffectToEntity(damaged, pe);
            }
            Bee bee = (Bee) attacker;
            bee.setHealth(0);
            removeSummon(summon);
        }
    }
    @EventHandler
    public static void summonDeath(EntityDeathEvent ede){
        Summon summon = getSummonFromEntity(ede.getEntity());
        if (summon == null)return;
        ede.setDroppedExp(0);
        ede.getDrops().clear();
    }

    @EventHandler
    public static void summonProjectileHit(ProjectileHitEvent phe){
        Entity hit = phe.getHitEntity();
        if (hit == null)return;
        Projectile projectile = phe.getEntity();
        if (!(projectile.getShooter() instanceof Mob))return;
        Mob source = (Mob) projectile.getShooter();
        Summon summon = getSummonFromEntity(source);
        if (summon == null)return;
        LivingEntity owner = summon.getOwner();
        if (owner.equals(hit)){
            phe.setCancelled(true);
            projectile.remove();
        }
    }

    @EventHandler
    public static void summonDropItem(EntityDropItemEvent edie){
        Entity entity = edie.getEntity();
        if (Summon.isEntitySummon(entity)){
            edie.setCancelled(true);
        }
    }

    private Mob summon;
    private LivingEntity owner;
    private LivingEntity target;
    private Item nameTag;
    private int lifespan;

    public void setSummon(Mob summon){
        this.summon = summon;
    }
    public Mob getSummon(){
        return this.summon;
    }
    public void setOwner(LivingEntity owner){
        this.owner = owner;
    }
    public LivingEntity getOwner(){
        return this.owner;
    }
    public void setTarget(LivingEntity target){
        this.target = target;
    }
    public LivingEntity getTarget(){
        return this.target;
    }
    public void setNameTag(Item nameTag){
        this.nameTag = nameTag;
    }
    public Item getNameTag(){
        return this.nameTag;
    }
    public void setLifespan(int lifespan){
        this.lifespan = lifespan;
    }
    public int getLifespan(){
        return this.lifespan;
    }

    public Summon(){

    }
    public Summon(Mob summon, LivingEntity owner, LivingEntity target, Item nameTag, int lifespan){
        this.summon = summon;
        this.owner = owner;
        this.target = target;
        this.nameTag = nameTag;
        this.lifespan = lifespan;
    }

}
