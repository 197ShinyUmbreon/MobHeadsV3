package io.github.shinyumbreon197.mobheadsv3.effect;

import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.head.passive.multi.FrogHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

import static io.github.shinyumbreon197.mobheadsv3.effect.PotionFX.applyPotionEffect;
import static io.github.shinyumbreon197.mobheadsv3.tool.EffectUtil.projectileVector;

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
            if (mobHead != null) executeTickMechanics(livingEntity, mobHead);
        }
    }

    private static void executeTickMechanics(LivingEntity livingEntity, MobHead mobHead){
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

    //Event Triggered --------------------------------------------------------------------------------------
    //PlayerStatisticIncrementEvent
    public static void frogJump(Player player){
        if (!player.isSneaking())return;
        Vector velocity = player.getVelocity(); //System.out.println("Pre-velocity: "+velocity);
        Vector direction = player.getLocation().getDirection(); //System.out.println("Direction: "+direction);
        double x = velocity.getX() + (direction.getX() * 0.8);
        double y = (velocity.getY() * 2) + (direction.getY() * 0.1);
        double z = velocity.getZ() + (direction.getZ() * 0.8);
        velocity.setX(x);
        velocity.setY(y);
        velocity.setZ(z);
        //System.out.println("Post-velocity: "+velocity);
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

    //PlayerInteractAtEntityEvent
    public static void interactAtEntityMechanicFrog(PlayerInteractAtEntityEvent e,MobHead mobHead){
        if (!e.getHand().equals(EquipmentSlot.HAND))return;

        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();
        boolean cooldown = onCooldown(player, mobHead);
        if (cooldown)return;
        List<EntityType> blacklistedTypes = Arrays.asList(EntityType.ENDER_DRAGON, EntityType.WITHER);
        if (blacklistedTypes.contains(entity.getType()))return;

        boolean edible = false;
        boolean onFire = false;
        int gainedHealth = 0;
        int gainedFoodLv = 0;
        float gainedSaturation = 0F;
        int gainedAir = 0;
        List<PotionEffect> gainedEffects = new ArrayList<>();

        if (entity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) entity;
            if (livingEntity.isDead())return;
            if (livingEntity instanceof Tameable){
                Tameable tameable = (Tameable) livingEntity;
                if (tameable.isTamed())return;
            }
            AttributeInstance maxHealthAttribute = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (maxHealthAttribute == null)return;
            double maxHealth = maxHealthAttribute.getValue();
            if (livingEntity.getHealth() <= 4 || livingEntity.getHealth() <= maxHealth*0.2) edible = true;
            if (livingEntity instanceof WaterMob) gainedAir = 100;
            onFire = livingEntity.getFireTicks() > 0 || livingEntity.isVisualFire();
            switch (livingEntity.getType()){
                default -> {
                    gainedFoodLv = (int) Math.ceil(maxHealth * 0.3);
                    gainedSaturation = (float) Math.ceil(maxHealth * 0.5);
                }
                case PLAYER -> {
                    Player eatenPlayer = (Player) livingEntity;
                    gainedFoodLv = 8;
                    gainedSaturation = 10F;
                    for (PotionEffect pe: eatenPlayer.getActivePotionEffects()){
                        int duration = pe.getDuration();
                        if (duration > 6000) duration = 6000;
                        gainedEffects.add(new PotionEffect(pe.getType(), duration, pe.getAmplifier(), false));
                    }
                }
                case SLIME -> {
                    Slime slime = (Slime) livingEntity;
                    if (slime.getSize() == 1) edible = true;
                    gainedHealth = 2;
                    gainedFoodLv = 2;
                    gainedSaturation = 2F;
                }
                case MAGMA_CUBE -> {
                    MagmaCube magmaCube = (MagmaCube) livingEntity;
                    if (magmaCube.getSize() == 1) edible = true;
                    gainedHealth = 3;
                    gainedFoodLv = 3;
                    gainedSaturation = 3F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3000, 0, false));
                }
                case COD, SALMON, TROPICAL_FISH -> {
                    assert livingEntity instanceof Fish;
                    Fish fish = (Fish) livingEntity;
                    edible = true;
                    gainedFoodLv = 2;
                    gainedSaturation = 2;
                }
                case GLOW_SQUID -> {
                    assert livingEntity instanceof GlowSquid;
                    GlowSquid glowSquid = (GlowSquid) livingEntity;
                    gainedFoodLv = 3;
                    gainedSaturation = 5F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 3000, 0, false));
                }
                case BLAZE -> {
                    Blaze blaze = (Blaze) livingEntity;
                    gainedFoodLv = 5;
                    gainedSaturation = 7F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3000, 0, false));
                    gainedEffects.add(new PotionEffect(PotionEffectType.SLOW_FALLING, 3000, 0, false));
                }
                case PHANTOM -> {
                    Phantom phantom = (Phantom) livingEntity;
                    gainedFoodLv = 4;
                    gainedSaturation = 4F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 3000, 0, false));
                }
                case FROG -> {
                    Frog frog = (Frog) livingEntity;
                    gainedFoodLv = 2;
                    gainedSaturation = 3F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.JUMP, 3000, 1, false));
                }
                case DOLPHIN -> {
                    assert livingEntity instanceof Dolphin;
                    Dolphin dolphin = (Dolphin) livingEntity;
                    gainedFoodLv = 4;
                    gainedSaturation = 4F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 6000, 0, false));
                }
                case VEX -> {
                    Vex vex = (Vex) livingEntity;
                    gainedFoodLv = 2;
                    gainedSaturation = 6F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 1, false));
                    gainedEffects.add(new PotionEffect(PotionEffectType.SPEED, 2400, 0, false));
                }
                case ENDERMITE -> {
                    Endermite endermite = (Endermite) livingEntity;
                    gainedFoodLv = 2;
                    gainedSaturation = 4;
                    //fxMultipleTeleportSickness(player);
                }
                case IRON_GOLEM -> {
                    IronGolem ironGolem = (IronGolem) livingEntity;
                    gainedFoodLv = 18;
                    gainedSaturation = 18F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.SLOW, 600, 1, false));
                    gainedEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 1, false));
                }
                case WARDEN -> {
                    Warden warden = (Warden) livingEntity;
                    gainedFoodLv = 20;
                    gainedSaturation = 20F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.HEALTH_BOOST, 3000, 0, false));
                }
                case RAVAGER -> {
                    Ravager ravager = (Ravager) livingEntity;
                    gainedFoodLv = 15;
                    gainedSaturation = 15F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.SLOW, 600, 0, false));
                    gainedEffects.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 0, false));
                    gainedEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 0, false));
                }
                case GHAST -> {
                    Ghast ghast = (Ghast) livingEntity;
                    gainedFoodLv = 6;
                    gainedSaturation = 12F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3000, 0, false));
                }
                case RABBIT -> {
                    edible = true;
                    Rabbit rabbit = (Rabbit) livingEntity;
                    gainedFoodLv = 3;
                    gainedSaturation = 4F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.JUMP, 3000, 1, false));
                }
                case PUFFERFISH -> {
                    edible = true;
                    assert livingEntity instanceof PufferFish;
                    PufferFish pufferFish = (PufferFish) livingEntity;
                    gainedFoodLv = 2;
                    gainedSaturation = 2F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.POISON, 120, 0, false));
                    gainedEffects.add(new PotionEffect(PotionEffectType.CONFUSION, 180, 0, false));
                }
                case STRIDER -> {
                    Strider strider = (Strider) livingEntity;
                    gainedFoodLv = 5;
                    gainedSaturation = 6F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3000, 0, false));
                }
                case GIANT -> {
                    Giant giant = (Giant) livingEntity;
                    gainedFoodLv = 20;
                    gainedSaturation = 20F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.SLOW, 2400, 4, false));
                }
                case CREEPER -> {
                    Creeper creeper = (Creeper) livingEntity;
                    gainedFoodLv = 5;
                    gainedSaturation = 6F;
                    frogEatCreeper(player, creeper);
                }
                case WITHER_SKELETON -> {
                    WitherSkeleton witherSkeleton = (WitherSkeleton) livingEntity;
                    gainedFoodLv = 2;
                    gainedSaturation = 6F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.WITHER, 60, 0, false));
                }
                case BEE -> {
                    Bee bee = (Bee) livingEntity;
                    gainedFoodLv = 1;
                    gainedSaturation = 3F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.POISON, 120, 1, false));
                    gainedEffects.add(new PotionEffect(PotionEffectType.CONFUSION, 140, 0, false));
                }
                case SHULKER -> {
                    Shulker shulker = (Shulker) livingEntity;
                    gainedFoodLv = 3;
                    gainedSaturation = 6F;
                    gainedEffects.add(new PotionEffect(PotionEffectType.LEVITATION, 180, 0, false));
                    gainedEffects.add(new PotionEffect(PotionEffectType.SLOW_FALLING, 3000, 0, false));
                }
            }
            if (!edible){
                livingEntity.setVelocity(projectileVector(livingEntity.getLocation(),player.getLocation().add(0,0.5,0),0.4));
                return;
            }

            PersistentDataContainer data = entity.getPersistentDataContainer();
            data.set(FrogHead.frogFoodKey, PersistentDataType.STRING, player.getUniqueId().toString());
            //livingEntity.setLastDamageCause(new EntityDamageByEntityEvent(player,livingEntity,EntityDamageEvent.DamageCause.CRAMMING,0));
            livingEntity.setVelocity(projectileVector(livingEntity.getLocation().add(0,1,0),player.getEyeLocation(),1.8));
            livingEntity.setHealth(0);
        }
        if (entity instanceof Projectile){
            Projectile projectile = (Projectile) entity;
            switch (projectile.getType()){
                default -> {}
                case SHULKER_BULLET -> {
                    edible = true;
                    gainedFoodLv = 2;
                    gainedSaturation = 4;
                    gainedEffects.add(new PotionEffect(PotionEffectType.LEVITATION, 40, 0, false));
                    gainedEffects.add(new PotionEffect(PotionEffectType.SLOW_FALLING, 1500, 0, false));
                }
            }
            if (!edible){
                return;
            }
        }

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
        if (gainedEffects.size() > 0){
            for (PotionEffect pe:gainedEffects){
                applyPotionEffect(player,pe.getType(),pe.getDuration(), pe.getAmplifier(), true, true);
            }
        }
        AVFX.playFrogTongueSound(entity.getLocation());

        setCooldown(player, mobHead, 20);
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






}
