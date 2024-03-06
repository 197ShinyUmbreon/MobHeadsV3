package io.github.shinyumbreon197.mobheadsv3.entity;

import io.github.shinyumbreon197.mobheadsv3.AVFX;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.function.PotionEffectManager;
import io.github.shinyumbreon197.mobheadsv3.function.Util;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class Summon implements Listener {

    private static final Set<Entity> summonersOnCooldown = new HashSet<>();
    private static final List<Summon> summons = new ArrayList<>();

    private static final Set<EntityType> summonTypes = Set.of(
            EntityType.WOLF, EntityType.SILVERFISH, EntityType.BEE, EntityType.VEX, EntityType.SNOWMAN
    );
    public static boolean isSummonType(EntityType entityType){
        return summonTypes.contains(entityType);
    }
    private static final Map<EntityType,Integer> summonLifespanMap = Map.of( // multiples of half seconds!
            EntityType.WOLF, 60*2,
            EntityType.SILVERFISH, 15*2,
            EntityType.BEE, 30*2,
            EntityType.VEX, 20*2,
            EntityType.SNOWMAN, 30*2
    );
    private static final Map<EntityType,Integer> summonCooldownMap = Map.of(
            EntityType.WOLF, 5*20,
            EntityType.SILVERFISH, 1,
            EntityType.BEE, 2*20,
            EntityType.VEX, 3*20,
            EntityType.SNOWMAN, 5*20
    );
    private static final Map<EntityType,EntityType> summonCommandingTypeMap = Map.of(
            EntityType.WOLF, EntityType.WOLF,
            EntityType.SILVERFISH, EntityType.SILVERFISH,
            EntityType.BEE, EntityType.BEE,
            EntityType.VEX, EntityType.VEX,
            EntityType.SNOWMAN, EntityType.SNOWMAN
    );

    public static void watchSummons(){
        List<Summon> copy = new ArrayList<>(summons);
        for (Summon summonObj:copy){
            Mob summonEnt = summonObj.getSummon();
            if (summonEnt.isDead()){
                removeSummon(summonObj);
                continue;
            }
            int lifeSpan = summonObj.getLifespan();
            if (lifeSpan <= 0){
                removeSummon(summonObj);
                continue;
            }
            lifeSpan--;
            summonObj.setLifespan(lifeSpan);
            Entity owner = summonObj.getOwner();
            MobHead mobHead = MobHead.getMobHeadWornByEntity(owner);
            if (owner == null || owner.isDead() || mobHead == null){
                removeSummon(summonObj);
                continue;
            }
            EntityType headType = mobHead.getEntityType();
            EntityType commanderType = summonCommandingTypeMap.get(summonEnt.getType());
            if (!commanderType.equals(headType)){
                removeSummon(summonObj);
                continue;
            }
            LivingEntity currentTarget = summonEnt.getTarget();
            if (currentTarget == null || currentTarget.isDead() || currentTarget.equals(owner)){
                LivingEntity newTarget = getNewSummonTarget(summonObj);
                if (newTarget == null){
                    removeSummon(summonObj);
                    continue;
                }else{
                    summonEnt.setTarget(newTarget);
                    summonObj.setTarget(newTarget);
                }
            }
            AVFX.playSummonContinuousEffect(summonEnt);
        }
    }
    public static void removeSummon(Summon summonObj){
        summons.remove(summonObj);
        Mob summon = summonObj.getSummon();
        Item nameTag = summonObj.getNameTag();
        AVFX.playSummonDispelEffect(summon.getLocation());
        nameTag.remove();
        summon.setHealth(0);
        summon.remove();
    }
    public static void createNewSummon(LivingEntity owner, LivingEntity target, EntityType summonType){
        if (summonersOnCooldown.contains(owner))return;
        World world = owner.getWorld();
        Location spawnLocation = summonSpawnLocation(owner, summonType);
        Mob summon;
        int lifespan = summonLifespanMap.getOrDefault(summonType, 0);
        int cooldown = summonCooldownMap.getOrDefault(summonType, 0);
        if (lifespan == 0 || cooldown == 0)return;
        Vector velocity = new Vector();
        switch (summonType){
            case WOLF -> {
                Wolf wolf = (Wolf) world.spawnEntity(spawnLocation, EntityType.WOLF);
                wolf.setCollarColor(DyeColor.BLACK);
                wolf.setAngry(true);
                wolf.setAdult();
                wolf.setBreed(false);
                wolf.setHealth(8);
                summon = wolf;
            }
            case SILVERFISH -> {
                Silverfish silverfish = (Silverfish) world.spawnEntity(spawnLocation, EntityType.SILVERFISH);
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
                PotionEffect pe = new PotionEffect(PotionEffectType.WEAKNESS,lifespan,0, false, false, false);
                vex.addPotionEffect(pe);
                velocity = owner.getLocation().getDirection().multiply(-0.6).add(new Vector(0.0,0.1,0.0));
                summon = vex;
            }
            case SNOWMAN -> {
                Snowman snowman = (Snowman) world.spawnEntity(spawnLocation, EntityType.SNOWMAN);
                snowman.setDerp(true);
                PotionEffect pe = new PotionEffect(PotionEffectType.SPEED, lifespan,1,false, false, false);
                snowman.addPotionEffect(pe);
                summon = snowman;
            }
            default -> {return;}
        }

        ItemStack nameTagItem = MobHead.getHeadItemOfEntity(owner);
        if (nameTagItem == null) nameTagItem = new ItemStack(Material.ARMOR_STAND);
        Item nameTag = (Item) world.dropItem(owner.getLocation(), nameTagItem);
        nameTag.setPickupDelay(999999);
        nameTag.setCustomName(ChatColor.RED + owner.getName());
        nameTag.setCustomNameVisible(true);
        nameTag.setTicksLived(4800);

        summon.setTarget(target);
        summon.setPersistent(false);
        summon.setLootTable(null);
        summon.setRemoveWhenFarAway(true);
        summon.setVelocity(velocity);

        summon.addPassenger(nameTag);
        summon.setTarget(target);

        AVFX.playSummonEffect(summon.getLocation(), summonType);

        Summon summonObj = new Summon(summon, owner, target, nameTag, lifespan);
        summons.add(summonObj);
        summonersOnCooldown.add(owner);
        new BukkitRunnable(){
            @Override
            public void run() {
                summonersOnCooldown.remove(owner);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(), cooldown);
    }
    private static Location summonSpawnLocation(LivingEntity owner, EntityType summonType){
        switch (summonType){
            case WOLF, SILVERFISH, SNOWMAN -> {
                List<Block> surrounding = Util.getBlocksSurroundingEntity(owner);
                List<Block> valid = new ArrayList<>();
                for (Block block:surrounding){
                    if (!block.getType().isAir() && !block.isLiquid())continue;
                    Block below = block.getRelative(BlockFace.DOWN);
                    if (!below.getType().isSolid() && !below.isLiquid())continue;
                    valid.add(block);
                }
                if (valid.size() == 0)return owner.getLocation();
                Random random = new Random();
                return valid.get(random.nextInt(0,valid.size())).getLocation().add(0.5,0.5,0.5);

            }
            case BEE, VEX -> {
                return owner.getEyeLocation().subtract(owner.getLocation().getDirection());
            }
        }
        return owner.getLocation();
    }
    private static LivingEntity getNewSummonTarget(Summon summon){
        Entity owner = summon.getOwner();
        if (owner.getLastDamageCause() != null && owner.getLastDamageCause() instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) owner.getLastDamageCause();
            if (!edbee.isCancelled() && !edbee.getDamager().isDead() && edbee.getDamager() instanceof LivingEntity){
                return (LivingEntity) edbee.getDamager();
            }
        }
        List<Entity> nearby = owner.getNearbyEntities(10,6,10);
        List<Mob> hostile = new ArrayList<>();
        for (Entity entity:nearby){
            if (!(entity instanceof Mob))continue;
            Mob mob = (Mob) entity;
            LivingEntity target = mob.getTarget();
            if (target != null && target.equals(owner)) hostile.add(mob);
        }
        double closestDistance = -1;
        Mob closestTarget = null;
        for (Mob mob:hostile){
            double distance = owner.getLocation().toVector().distance(mob.getLocation().toVector());
            if (closestDistance == -1 || distance < closestDistance){
                closestTarget = mob;
                closestDistance = distance;
            }
        }
        return closestTarget;
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
