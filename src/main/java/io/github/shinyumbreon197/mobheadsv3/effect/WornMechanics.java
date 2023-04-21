package io.github.shinyumbreon197.mobheadsv3.effect;

import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.entity.Summon;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.head.passive.multi.FrogHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

import static io.github.shinyumbreon197.mobheadsv3.effect.PotionFX.applyPotionEffect;
import static io.github.shinyumbreon197.mobheadsv3.tool.EffectUtil.*;

public class WornMechanics {

    private static final Map<LivingEntity, MobHead> cooldownMap = new HashMap<>();

    private static boolean onCooldown(LivingEntity livingEntity, MobHead mobHead){
        return cooldownMap.containsKey(livingEntity) && cooldownMap.get(livingEntity).equals(mobHead);
    }
    private static void setCooldown(LivingEntity livingEntity, MobHead mobHead, int ticks){
        if (mobHead == null)return;
        cooldownMap.put(livingEntity, mobHead);
        new BukkitRunnable(){
            @Override
            public void run() {
                if (cooldownMap.containsKey(livingEntity)){
                    cooldownMap.remove(livingEntity, mobHead);
                }
            }
        }.runTaskLater(MobHeadsV3.getPlugin(), ticks);
    }

    public static void runHeadTickMechanics(List<LivingEntity> headed){
        for (LivingEntity livingEntity:headed){
            MobHead mobHead = HeadUtil.getMobHeadFromEntity(livingEntity);
            if (mobHead == null)return;
            EntityType entityType = mobHead.getEntityType();
            switch (entityType){
                default -> {}
                case DOLPHIN -> {regenerateAir(livingEntity);}
                case COD, SALMON, TROPICAL_FISH -> {regenerateAir(livingEntity);}
                case PUFFERFISH -> {regenerateAir(livingEntity);}
                case TURTLE -> {regenerateAir(livingEntity);}
                case SQUID -> {regenerateAir(livingEntity);}
                case SKELETON_HORSE -> {regenerateAir(livingEntity);}
                case GLOW_SQUID -> {regenerateAir(livingEntity);}
                case TADPOLE -> {regenerateAir(livingEntity);}
                case DROWNED -> {regenerateAir(livingEntity);}
                case AXOLOTL -> {regenerateAir(livingEntity);}
                case FROG -> {regenerateAir(livingEntity);}
                case ENDERMAN -> {damageFromWater(livingEntity);}
            }
        }
    }

    //Tick Triggered ---------------------------------------------------------------------------------------
    private static void regenerateAir(LivingEntity livingEntity){
        int maxAir = livingEntity.getMaximumAir();
        int airRemaining = livingEntity.getRemainingAir();
        int newAir = airRemaining + 8;
        if (newAir > maxAir) newAir = maxAir;
        livingEntity.setRemainingAir(newAir);
    }
    public static void damageFromWater(LivingEntity livingEntity){
        if (isExposedToWater(livingEntity)) livingEntity.damage(1);
    }

    //Event Triggered --------------------------------------------------------------------------------------
    //Generic
    public static void endermanTeleport(Entity entity){
        if (entity instanceof LivingEntity && entity.isDead())return;
        Location eLoc = entity.getLocation();
        Location dest = randomTeleportLoc(eLoc, entity, 8, 1, true);
        if (dest != null){
            entity.teleport(dest, PlayerTeleportEvent.TeleportCause.COMMAND);
            entity.setVelocity(new Vector(0,0,0));
            World world = eLoc.getWorld();
            if (world == null)return;
            new BukkitRunnable(){
                @Override
                public void run() {
                    AVFX.playEndermanTeleportSound(eLoc);
                }
            }.runTaskLater(MobHeadsV3.getPlugin(), 1);
        }
    }
    //EntityDamagedByEntityEvent
    public static void summonReinforcements(LivingEntity defender, LivingEntity attacker, EntityType summonType){
        if (defender.isDead())return;
        Random random = new Random();
        Location location = defender.getLocation().add(
                random.nextInt(-1, 1), 0, random.nextInt(-1, 1)
        );
        Entity summon = null;
        switch (summonType){
            case WOLF -> summon = Summon.wolfSummon(defender, location, attacker);
            case SILVERFISH -> summon = Summon.silverfishSummon(defender, location, attacker);
        }
        if (summon == null)return;
    }

    //EntityDamageEvent
    public static void endermanDamage(LivingEntity livingEntity, EntityDamageEvent.DamageCause cause){
        if (livingEntity.isDead())return;
        List<EntityDamageEvent.DamageCause> damageCauses = List.of(
                EntityDamageEvent.DamageCause.SUICIDE, EntityDamageEvent.DamageCause.FALL,
                EntityDamageEvent.DamageCause.FLY_INTO_WALL, EntityDamageEvent.DamageCause.POISON,
                EntityDamageEvent.DamageCause.STARVATION, EntityDamageEvent.DamageCause.FIRE_TICK,
                EntityDamageEvent.DamageCause.SUFFOCATION, EntityDamageEvent.DamageCause.VOID
        );
        if (damageCauses.contains(cause))return;
        endermanTeleport(livingEntity);
    }

    //PlayerStatisticIncrementEvent
    public static void frogEatEntity(PlayerInteractAtEntityEvent e, MobHead mobHead){
        if (!e.getHand().equals(EquipmentSlot.HAND))return;
        Player player = e.getPlayer();
        if (player.isSneaking())return;
        Entity entity = e.getRightClicked();

        boolean cooldown = onCooldown(player, mobHead);
        if (cooldown)return;
        if (frogIsBlacklisted(entity)) return;
        setCooldown(player, mobHead, 20);

        boolean edible = frogIsEdible(entity);
        boolean onFire = false;
        int gainedHealth = 0;
        int gainedFoodLv = 0;
        float gainedSaturation = 0F;
        int gainedAir = 0;

        if (entity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) entity;
            if (!edible){
                livingEntity.setVelocity(projectileVector(livingEntity.getLocation(),player.getLocation().add(0,0.5,0),0.4));
            }else{
                if (livingEntity instanceof WaterMob) gainedAir = 100;
                onFire = livingEntity.getFireTicks() > 0 || livingEntity.isVisualFire();
                AttributeInstance maxHealthAttribute = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                if (maxHealthAttribute == null)return;
                double maxHealth = maxHealthAttribute.getValue();
                gainedHealth = (int) Math.ceil(maxHealth * 0.2);
                gainedFoodLv = (int) Math.ceil(maxHealth * 0.3);
                gainedSaturation = (float) Math.ceil(maxHealth * 0.5);
                PersistentDataContainer data = entity.getPersistentDataContainer();
                data.set(FrogHead.frogFoodKey, PersistentDataType.STRING, player.getUniqueId().toString());
                livingEntity.setVelocity(projectileVector(livingEntity.getLocation().add(0,1,0),player.getEyeLocation(),1.8));
                livingEntity.setHealth(0);
            }
        }
        if ((entity instanceof Projectile) && edible){
            Projectile projectile = (Projectile) entity;
            switch (projectile.getType()){
                default -> {}
                case SHULKER_BULLET -> {
                    gainedFoodLv = 2;
                    gainedSaturation = 4;
                }
            }
        }
        switch (entity.getType()){
            case CREEPER -> {
                assert entity instanceof Creeper;
                frogEatCreeper(player, (Creeper) entity);
            }
            case ENDERMITE -> {
                //fxMultipleTeleportSickness(player);
            }
        }
        player.swingOffHand();
        AVFX.playFrogTongueSound(entity.getLocation());
        if (!edible)return;
        List<PotionEffect> gainedEffects = frogEatenEffects(entity);
        if (gainedHealth > 0){
            double health = player.getHealth();
            AttributeInstance pMaxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (pMaxHealthAttribute == null)return;
            double pMaxHealth = pMaxHealthAttribute.getValue();
            health = health + gainedHealth;
            if (health > pMaxHealth) health = pMaxHealth;
            player.setHealth(health);
        }
        if (gainedFoodLv > 0){
            int foodLv = player.getFoodLevel();
            foodLv = foodLv + gainedFoodLv;
            if (foodLv > 20) foodLv = 20;
            player.setFoodLevel(foodLv);
        }
        if (gainedSaturation > 0F){
            float saturationLv = player.getSaturation();
            saturationLv = saturationLv + gainedSaturation;
            if (saturationLv > 20F) saturationLv = 20F;
            player.setSaturation(saturationLv);
        }
        if (gainedAir > 0){
            int air = player.getRemainingAir();
            air = air + gainedAir;
            if (air > player.getMaximumAir()) air = player.getMaximumAir(); //if > 300
            player.setRemainingAir(air);
        }
        for (PotionEffect pe:gainedEffects){
            applyPotionEffect(player,pe.getType(),pe.getDuration(), pe.getAmplifier(), true, true);
        }
        boolean finalOnFire = onFire;
        new BukkitRunnable(){
            @Override
            public void run() {
                if (finalOnFire) player.setFireTicks(60);
                Sound deathSound = null;
                if (entity instanceof LivingEntity){
                    LivingEntity le = (LivingEntity) entity;
                    deathSound = le.getDeathSound();
                }
                AVFX.playFrogEatenSounds(player.getEyeLocation(), deathSound);
                entity.remove();
            }
        }.runTaskLater(MobHeadsV3.getPlugin(), 5);
    }
    public static void frogJump(Player player){
        if (!player.isSneaking())return;
        Vector velocity = player.getVelocity();
        Vector direction = player.getLocation().getDirection();
        double x = velocity.getX() + (direction.getX() * 0.8);
        double y = (velocity.getY() * 2) + (direction.getY() * 0.1);
        double z = velocity.getZ() + (direction.getZ() * 0.8);
        velocity.setX(x);
        velocity.setY(y);
        velocity.setZ(z);
        player.setVelocity(velocity);
        AVFX.playFrogJumpEffect(player.getLocation());
    }

    //EntityDamageEvent
    public static boolean frogFallDamage(EntityDamageEvent e){
        boolean frogFallDamage = e.getFinalDamage() > 3;
        if (frogFallDamage){
            double newDamage;
            newDamage = e.getFinalDamage() - 3;
            if (newDamage <= 0){return false;}
            e.setDamage(newDamage);
        }
        return frogFallDamage;
    }

    //ProjectileHitEvent
    public static void projectileHitWearer(ProjectileHitEvent e, MobHead mobHead){
        Entity hitEntity = e.getHitEntity();
        assert hitEntity != null;
        Projectile projectile = e.getEntity();
        EntityType headType = mobHead.getEntityType();
        switch (headType){
            default -> {}
            case ENDERMAN -> {
                e.setCancelled(true);
                endermanTeleport(hitEntity);
            }
        }
    }

    //PlayerInteractAtEntityEvent
    //Send the player glowing packets for every edible entity within a block radius
    private static boolean frogIsEdible(Entity entity){
        if (entity instanceof Projectile){
            Projectile projectile = (Projectile) entity;
            if (projectile.getType().equals(EntityType.SHULKER_BULLET)) return true;
        }
        if (entity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) entity;
            AttributeInstance maxHealthAttribute = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (maxHealthAttribute == null)return false;
            double maxHealth = maxHealthAttribute.getValue();
            if (livingEntity.getHealth() <= 4 || livingEntity.getHealth() <= maxHealth*0.2) return true;
        }
        switch (entity.getType()){
            case SLIME -> {
                assert entity instanceof Slime;
                Slime slime = (Slime) entity;
                if (slime.getSize() == 1) return true;
            }
            case MAGMA_CUBE -> {
                assert entity instanceof MagmaCube;
                MagmaCube magmaCube = (MagmaCube) entity;
                if (magmaCube.getSize() == 1) return true;
            }
            case COD, SALMON, TROPICAL_FISH, RABBIT, PUFFERFISH -> {return true;}
        }
        return false;
    }
    private static List<PotionEffect> frogEatenEffects(Entity entity){
        List<PotionEffect> effects = new ArrayList<>();
        switch (entity.getType()){
            case PLAYER -> {
                Player eatenPlayer = (Player) entity;
                for (PotionEffect pe: eatenPlayer.getActivePotionEffects()){
                    int duration = pe.getDuration();
                    if (duration > 6000) duration = 6000;
                    effects.add(new PotionEffect(pe.getType(), duration, pe.getAmplifier(), false));
                }
            }
            case CHICKEN, PARROT -> {
                effects.add(new PotionEffect(PotionEffectType.SLOW_FALLING, 3000, 0, false));
            }
            case MAGMA_CUBE, GHAST, STRIDER -> {
                effects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3000, 0, false));
            }
            case GLOW_SQUID, PHANTOM -> {
                effects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 3000, 0, false));
            }
            case BLAZE -> {
                effects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3000, 0, false));
                effects.add(new PotionEffect(PotionEffectType.SLOW_FALLING, 3000, 0, false));
            }
            case FROG, RABBIT -> {
                effects.add(new PotionEffect(PotionEffectType.JUMP, 3000, 1, false));
            }
            case DOLPHIN -> {
                effects.add(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 6000, 0, false));
            }
            case VEX -> {
                effects.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 1, false));
                effects.add(new PotionEffect(PotionEffectType.SPEED, 2400, 0, false));
            }
            case IRON_GOLEM -> {
                effects.add(new PotionEffect(PotionEffectType.SLOW, 600, 1, false));
                effects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 1, false));
            }
            case WARDEN -> {
                effects.add(new PotionEffect(PotionEffectType.HEALTH_BOOST, 3000, 0, false));
            }
            case RAVAGER -> {
                effects.add(new PotionEffect(PotionEffectType.SLOW, 600, 0, false));
                effects.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 0, false));
                effects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 0, false));
            }
            case PUFFERFISH, BEE -> {
                effects.add(new PotionEffect(PotionEffectType.POISON, 120, 0, false));
                effects.add(new PotionEffect(PotionEffectType.CONFUSION, 180, 0, false));
            }
            case GIANT -> {
                effects.add(new PotionEffect(PotionEffectType.SLOW, 2400, 4, false));
            }
            case WITHER_SKELETON -> {
                effects.add(new PotionEffect(PotionEffectType.WITHER, 60, 0, false));
            }
            case SHULKER -> {
                effects.add(new PotionEffect(PotionEffectType.LEVITATION, 180, 0, false));
                effects.add(new PotionEffect(PotionEffectType.SLOW_FALLING, 3000, 0, false));
            }
            case SHULKER_BULLET -> {
                effects.add(new PotionEffect(PotionEffectType.LEVITATION, 40, 0, false));
                effects.add(new PotionEffect(PotionEffectType.SLOW_FALLING, 1500, 0, false));
            }
        }
        return effects;
    }
    private static boolean frogIsBlacklisted(Entity entity){
        List<EntityType> blacklistedTypes = Arrays.asList(EntityType.ENDER_DRAGON, EntityType.WITHER);
        if (blacklistedTypes.contains(entity.getType()))return true;
        if (entity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) entity;
            if (livingEntity.isDead())return true;
            if (livingEntity instanceof Tameable){
                Tameable tameable = (Tameable) livingEntity;
                return tameable.isTamed();
            }
        }
        return false;
    }
    private static void frogEatCreeper(Player player, Creeper creeper){
        new BukkitRunnable(){
            @Override
            public void run() {
                AVFX.playFrogEatCreeperEffect(player.getLocation().add(0,1,0));
                List<Entity> nearby = player.getNearbyEntities(8, 5, 8);
                player.damage(2, creeper);
                player.setVelocity(player.getVelocity().add(new Vector(0,0.8,0)));
                for (Entity entity:nearby){
                    if (entity == null)continue;
                    if (entity instanceof LivingEntity){
                        LivingEntity le = (LivingEntity) entity;
                        if (le.equals(player))continue;
                        if (le.isDead())continue;
                        le.setVelocity(projectileVector(player.getLocation(), le.getEyeLocation(),4));
                        le.damage(3, player);
                    }
                }
            }
        }.runTaskLater(MobHeadsV3.getPlugin(), 5);
    }

    //EntityTargetLivingEntityEvent
    private static final Map<UUID, Player> endermanAggroMap = new HashMap<>();
    public static void addEndermanAggroMap(UUID entID, Player player){
        endermanAggroMap.put(entID, player);
        new BukkitRunnable(){
            @Override
            public void run() {
                if (endermanAggroMap.containsKey(entID) && endermanAggroMap.get(entID).equals(player)){
                    endermanAggroMap.remove(entID);
                }
            }
        }.runTaskLater(MobHeadsV3.getPlugin(), 60*20);
    }
    public static void onTargetWearer(EntityTargetLivingEntityEvent e, LivingEntity wearer, Mob targeting, MobHead mobHead){
        EntityType headType = mobHead.getEntityType();
        boolean sameType = targeting.getType().equals(headType);
        EntityTargetEvent.TargetReason reason = e.getReason();
        System.out.println("Targeting: "+targeting.getType()+" "+targeting.getEntityId()+" Targeted: "+wearer.getType()+" "+wearer.getEntityId()+" Reason: "+reason.toString()); //debug
        if (reason.equals(EntityTargetEvent.TargetReason.FORGOT_TARGET))return;
        List<EntityTargetEvent.TargetReason> closestTargetReasons = List.of(
                EntityTargetEvent.TargetReason.CLOSEST_ENTITY, EntityTargetEvent.TargetReason.CLOSEST_PLAYER, EntityTargetEvent.TargetReason.RANDOM_TARGET
        );
        if (sameType && headType.equals(EntityType.ENDERMAN) && wearer instanceof Player){
            Enderman enderman = (Enderman) targeting;
            boolean onMap = endermanAggroMap.containsKey(enderman.getUniqueId()) && endermanAggroMap.get(enderman.getUniqueId()).equals(wearer);
            if (!onMap){
                e.setCancelled(true);
            }
        }else if (closestTargetReasons.contains(reason) && sameType){
            e.setCancelled(true);
        }

        System.out.println("Canceled: "+e.isCancelled()); //debug
    }

}
