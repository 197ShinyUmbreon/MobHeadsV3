package io.github.shinyumbreon197.mobheadsv3.entity;

import io.github.shinyumbreon197.mobheadsv3.AVFX;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.data.Data;
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
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ColorableArmorMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
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
            EntityType.WOLF, EntityType.SILVERFISH, EntityType.BEE, EntityType.VEX, EntityType.SNOW_GOLEM
    );
    public static boolean isSummonType(EntityType entityType){
        return summonTypes.contains(entityType);
    }
    private static final Map<EntityType,Integer> summonLifespanMap = Map.of( // multiples of half seconds!
            EntityType.WOLF, 60*2,
            EntityType.SILVERFISH, 15*2,
            EntityType.BEE, 30*2,
            EntityType.VEX, 20*2,
            EntityType.SNOW_GOLEM, 30*2
    );
    private static final Map<EntityType,Integer> summonCooldownMap = Map.of(
            EntityType.WOLF, 5*20,
            EntityType.SILVERFISH, 1,
            EntityType.BEE, 2*20,
            EntityType.VEX, 3*20,
            EntityType.SNOW_GOLEM, 5*20
    );
    private static final Map<EntityType,EntityType> summonCommandingTypeMap = Map.of(
            EntityType.WOLF, EntityType.WOLF,
            EntityType.SILVERFISH, EntityType.SILVERFISH,
            EntityType.BEE, EntityType.BEE,
            EntityType.VEX, EntityType.VEX,
            EntityType.SNOW_GOLEM, EntityType.SNOW_GOLEM
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
    public static void removeSummon(Summon summonObj){
        summons.remove(summonObj);
        Mob summon = summonObj.getSummon();
        Item nameTag = summonObj.getNameTag();
        AVFX.playSummonDispelEffect(summon.getLocation());
        nameTag.remove();
        summon.remove();
    }
    public static void createNewSummon(LivingEntity owner, LivingEntity target, EntityType summonType){
        if (owner == null || owner.isDead() || target == null || owner.isDead())return;
        MobHead mobHead = MobHead.getMobHeadWornByEntity(owner);
        if (mobHead == null)return;
        if (summonersOnCooldown.contains(owner))return;
        World world = owner.getWorld();
        Location spawnLocation = summonSpawnLocation(owner, summonType);
        Mob summon;
        int lifespan = summonLifespanMap.getOrDefault(summonType, 0);
        int lifespanTicks = lifespan * 10;
        int cooldown = summonCooldownMap.getOrDefault(summonType, 0);
        if (lifespan == 0 || cooldown == 0)return;
        Vector velocity = new Vector();
        switch (summonType){
            case WOLF -> {
                Wolf wolf = (Wolf) world.spawnEntity(spawnLocation, EntityType.WOLF);
                String headVariant = mobHead.getVariant();
                Wolf.Variant variant = Data.getWolfVariantMap().get(headVariant);
                if (debug) System.out.println("Summon.java::createNewSummon():[Wolf]:headVariant:" + headVariant); //debug
                if (variant == null) variant = Wolf.Variant.PALE;
                wolf.setVariant(variant);
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
                PotionEffectManager.addEffectToEntity(wolf, new PotionEffect(PotionEffectType.STRENGTH, lifespanTicks, 0, false, false, false));
                wolf.setHealth(16);
                summon = wolf;
            }
            case SILVERFISH -> {
                Silverfish silverfish = (Silverfish) world.spawnEntity(spawnLocation, EntityType.SILVERFISH);
                silverfish.setHealth(4);
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
            default -> {return;}
        }

        ItemStack nameTagItem = MobHead.getHeadItemOfEntity(owner);
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
            case WOLF, SILVERFISH, SNOW_GOLEM -> {
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
        List<Entity> nearby = owner.getNearbyEntities(20,6,20);
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
