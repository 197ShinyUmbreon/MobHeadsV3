package io.github.shinyumbreon197.mobheadsv3;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;
import static io.github.shinyumbreon197.mobheadsv3.function.Util.randomOffsetCenter;

public class AVFX {

    private static final Random random = new Random();

    //COMBINED A/V EFFECTS ---------------------------------------------------------------------------------
    public static void playHeadDropEffect(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.spawnParticle(Particle.EXPLOSION_LARGE,location,1);
        world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.2F, 1.2F);
        world.playSound(location, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 0.4F, 1.3F);
    }

    public static void playHeadInteractEffect(Location location, EntityType headType){
        World world = location.getWorld();
        if (world == null)return;
        Sound interactSound = null;
        float volume = 0.6F;
        switch (headType){
            default -> {}
            case PLAYER -> {interactSound = playerInteractSound(); volume = 0.4f;}
            case FOX -> {interactSound = foxInteractSound(); volume = 0.8f;}
            case ENDER_DRAGON -> {interactSound = Sound.ENTITY_ENDER_DRAGON_AMBIENT; volume = 0.5f;}
            case WOLF -> {interactSound = wolfInteractSound(); volume = 0.4f;}
            case TURTLE -> {interactSound = Sound.ENTITY_TURTLE_AMBIENT_LAND; volume = 1.3f;}
            case BAT -> {interactSound = Sound.ENTITY_BAT_AMBIENT; volume = 0.05f;}
            case OCELOT -> {interactSound = Sound.ENTITY_OCELOT_AMBIENT; volume = 1.5f;}
            case PANDA -> {interactSound = pandaInteractSound();}
            case PARROT -> {interactSound = parrotInteractSound();}
            case RABBIT -> {interactSound = Sound.ENTITY_RABBIT_AMBIENT; volume = 1.6f;}
            case FROG -> {interactSound = Sound.ENTITY_FROG_AMBIENT; volume = 1.0f;}
            case RAVAGER -> {interactSound = Sound.ENTITY_RAVAGER_ROAR; volume = 0.2f;}
            case WITHER -> {interactSound = Sound.ENTITY_WITHER_AMBIENT; volume = 0.3f;}
            case BLAZE -> {interactSound = Sound.ENTITY_BLAZE_AMBIENT; blazeHeadInteractEffect(location);}
            case ELDER_GUARDIAN -> {interactSound = Sound.ENTITY_ELDER_GUARDIAN_AMBIENT;}
            case WITHER_SKELETON -> {interactSound = Sound.ENTITY_WITHER_SKELETON_AMBIENT;}
            case STRAY -> {interactSound = Sound.ENTITY_STRAY_AMBIENT;}
            case HUSK -> {interactSound = Sound.ENTITY_HUSK_AMBIENT;}
            case ZOMBIE_VILLAGER -> {interactSound = Sound.ENTITY_ZOMBIE_VILLAGER_AMBIENT;}
            case SKELETON_HORSE -> {interactSound = Sound.ENTITY_SKELETON_HORSE_AMBIENT;}
            case ZOMBIE_HORSE -> {interactSound = Sound.ENTITY_ZOMBIE_HORSE_AMBIENT;}
            case DONKEY -> {interactSound = Sound.ENTITY_DONKEY_AMBIENT;}
            case MULE -> {interactSound = Sound.ENTITY_MULE_AMBIENT;}
            case EVOKER -> {interactSound = Sound.ENTITY_EVOKER_CAST_SPELL;}
            case VEX -> {interactSound = Sound.ENTITY_VEX_CHARGE;}
            case VINDICATOR -> {interactSound = Sound.ENTITY_VINDICATOR_AMBIENT;}
            case ILLUSIONER -> {interactSound = Sound.ENTITY_ILLUSIONER_CAST_SPELL;}
            case CREEPER -> {interactSound = Sound.ENTITY_CREEPER_PRIMED;}
            case SKELETON -> {interactSound = Sound.ENTITY_SKELETON_AMBIENT;}
            case SPIDER -> {interactSound = Sound.ENTITY_SPIDER_AMBIENT;}
            case GIANT -> {interactSound = Sound.ENTITY_ZOMBIE_AMBIENT;}
            case ZOMBIE -> {interactSound = Sound.ENTITY_ZOMBIE_AMBIENT;}
            case SLIME -> {interactSound = Sound.ENTITY_SLIME_SQUISH_SMALL;}
            case GHAST -> {interactSound = Sound.ENTITY_GHAST_AMBIENT;}
            case ZOMBIFIED_PIGLIN -> {interactSound = Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY;}
            case ENDERMAN -> {interactSound = Sound.ENTITY_ENDERMAN_AMBIENT;}
            case CAVE_SPIDER -> {interactSound = Sound.ENTITY_SPIDER_AMBIENT;}
            case SILVERFISH -> {interactSound = Sound.ENTITY_SILVERFISH_AMBIENT;}
            case MAGMA_CUBE -> {interactSound = Sound.ENTITY_MAGMA_CUBE_SQUISH_SMALL;}
            case WITCH -> {interactSound = Sound.ENTITY_WITCH_DRINK;}
            case ENDERMITE -> {interactSound = Sound.ENTITY_ENDERMITE_AMBIENT;}
            case GUARDIAN -> {interactSound = Sound.ENTITY_GUARDIAN_AMBIENT_LAND;}
            case SHULKER -> {interactSound = Sound.ENTITY_SHULKER_AMBIENT;}
            case PIG -> {interactSound = Sound.ENTITY_PIG_AMBIENT;}
            case SHEEP -> {interactSound = Sound.ENTITY_SHEEP_AMBIENT;}
            case COW -> {interactSound = Sound.ENTITY_COW_AMBIENT;}
            case CHICKEN -> {interactSound = Sound.ENTITY_CHICKEN_AMBIENT;}
            case SQUID -> {interactSound = Sound.ENTITY_SQUID_SQUIRT;}
            case MUSHROOM_COW -> {interactSound = Sound.ENTITY_COW_AMBIENT;}
            case SNOWMAN -> {interactSound = Sound.BLOCK_SNOW_STEP;}
            case IRON_GOLEM -> {interactSound = Sound.ENTITY_IRON_GOLEM_DAMAGE;}
            case HORSE -> {interactSound = Sound.ENTITY_HORSE_AMBIENT;}
            case POLAR_BEAR -> {interactSound = Sound.ENTITY_POLAR_BEAR_WARNING; volume = 0.4f;}
            case LLAMA -> {interactSound = Sound.ENTITY_LLAMA_AMBIENT;}
            case VILLAGER -> {interactSound = Sound.ENTITY_VILLAGER_NO;}
            case PHANTOM -> {interactSound = Sound.ENTITY_PHANTOM_AMBIENT;}
            case COD -> {interactSound = Sound.ENTITY_COD_FLOP;}
            case SALMON -> {interactSound = Sound.ENTITY_SALMON_FLOP;}
            case PUFFERFISH -> {interactSound = Sound.ENTITY_PUFFER_FISH_BLOW_UP;}
            case TROPICAL_FISH -> {interactSound = Sound.ENTITY_TROPICAL_FISH_FLOP;}
            case DROWNED -> {interactSound = Sound.ENTITY_DROWNED_AMBIENT;}
            case DOLPHIN -> {interactSound = Sound.ENTITY_DOLPHIN_PLAY;}
            case CAT -> {interactSound = catInteractSound();}
            case PILLAGER -> {interactSound = Sound.ENTITY_PILLAGER_AMBIENT;}
            case TRADER_LLAMA -> {interactSound = Sound.ENTITY_LLAMA_AMBIENT;}
            case WANDERING_TRADER -> {interactSound = Sound.ENTITY_WANDERING_TRADER_NO;}
            case BEE -> {interactSound = Sound.ENTITY_BEE_POLLINATE;}
            case HOGLIN -> {interactSound = Sound.ENTITY_HOGLIN_AMBIENT;}
            case PIGLIN -> {interactSound = Sound.ENTITY_PIGLIN_AMBIENT;}
            case STRIDER -> {interactSound = Sound.ENTITY_STRIDER_AMBIENT;}
            case ZOGLIN -> {interactSound = Sound.ENTITY_ZOGLIN_AMBIENT;}
            case PIGLIN_BRUTE -> {interactSound = Sound.ENTITY_PIGLIN_BRUTE_AMBIENT;}
            case AXOLOTL -> {interactSound = Sound.ENTITY_AXOLOTL_IDLE_AIR;}
            case GLOW_SQUID -> {interactSound = Sound.ENTITY_GLOW_SQUID_AMBIENT;}
            case GOAT -> {interactSound = Sound.ENTITY_GOAT_SCREAMING_AMBIENT;}
            case ALLAY -> {interactSound = Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM;}
            case TADPOLE -> {interactSound = Sound.ENTITY_TADPOLE_FLOP;}
            case WARDEN -> {interactSound = Sound.ENTITY_WARDEN_HEARTBEAT;}
            case CAMEL -> {interactSound = Sound.ENTITY_CAMEL_AMBIENT;}
            case SNIFFER -> {interactSound = Sound.ENTITY_SNIFFER_SEARCHING;}
        }
        if (debug) System.out.println("playHeadInteractEffect() interactSound " + interactSound); //debug
        if (interactSound != null) world.playSound(location, interactSound, volume, 1.0F);
    }

    public static void playSummonEffect(Location origin, EntityType summonType){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin, Sound.BLOCK_SOUL_SAND_BREAK,1.8f,1.1f);
        world.spawnParticle(Particle.SMOKE_LARGE,origin,16,0.4,0.5, 0.4,0.0, null);
        switch (summonType){
            case WOLF -> playWolfSummonEffect(origin);
            case BEE -> playBeeSummonEffect(origin);
            case SILVERFISH -> playSilverfishSummonEffect(origin);
            case VEX -> {}
        }
    }
    public static void playChestedPickup(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin,Sound.BLOCK_WOOD_BREAK, 0.6f, 1.1f);
        BlockData blockData = new ItemStack(Material.OAK_PLANKS).getType().createBlockData();
        world.spawnParticle(Particle.BLOCK_DUST,origin,5,0.2,0.05,0.2,0,blockData);
    }
    public static void playChestedItemSizzle(Location origin, boolean sound){
        World world = origin.getWorld();
        if (world == null)return;
        world.spawnParticle(Particle.SMOKE_NORMAL,origin,2,0,0.5,0,0.025,null);
        if (sound) world.playSound(origin, Sound.BLOCK_FIRE_EXTINGUISH,0.5f,1.1f);
    }
    public static void playChestedItemExplode(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin, Sound.ITEM_SHIELD_BREAK,0.5f, 1.4f);
        world.spawnParticle(Particle.EXPLOSION_NORMAL,origin,5,0.1,0.1,0.1,0,null);
    }
    public static void playSummonDispelEffect(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin, Sound.BLOCK_SOUL_SAND_BREAK,1.8f,0.8f);
        world.spawnParticle(Particle.SMOKE_LARGE,origin,16,0.4,0.5, 0.4,0.0, null);
    }
    public static void playWolfSummonEffect(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.playSound(location,Sound.ENTITY_WOLF_HOWL,0.1f, 1.0f);
    }
    public static void playBeeSummonEffect(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.playSound(location,Sound.BLOCK_BEEHIVE_EXIT,1.2f, 1.0f);
    }
    public static void playSilverfishSummonEffect(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin,Sound.BLOCK_STONE_BREAK,1.0f, 1.0f);
        world.spawnParticle(Particle.ITEM_CRACK,origin.add(0,0.1,0),
                10,0.2,0.1,0.2,0.05,new ItemStack(Material.STONE)
        );

    }

    public static void playFoxPounceLandExplosionEffect(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin,Sound.ENTITY_GENERIC_EXPLODE,1.0f, 1.1f);
        world.spawnParticle(Particle.EXPLOSION_HUGE, origin, 8,1.5,1.5,1.5);
    }
    public static void playFoxPounceEffect(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin, Sound.ENTITY_FOX_SCREECH, 0.6f, 1.1f);
    }
    public static void playFoxPounceLandHitEffect(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin,Sound.ENTITY_FOX_BITE,0.8f,1.2f);
    }
    public static void playFoxPounceLandEffect(Location origin, Block block){
        World world = origin.getWorld();
        if (world == null)return;
        BlockData blockData = block.getType().createBlockData();
        Sound sound = blockData.getSoundGroup().getBreakSound();
        world.playSound(origin, sound, 1.6f, 1.2f);
        world.spawnParticle(Particle.BLOCK_DUST, origin,10, blockData);
    }
    public static void playSheepEatEffect(Location blockLoc, Block block, Location playerMouthLoc){
        World world = blockLoc.getWorld();
        if (world == null)return;
        world.playSound(blockLoc,Sound.BLOCK_GRASS_BREAK,0.5f,1.0f);
        world.playSound(playerMouthLoc,Sound.ENTITY_GENERIC_EAT,0.5f,1.0f);
        world.spawnParticle(Particle.BLOCK_DUST,blockLoc,10,block.getType().createBlockData());
        world.spawnParticle(Particle.BLOCK_DUST,playerMouthLoc,5,Material.GRASS.createBlockData());
    }
    public static void playGoatRamBeginEffect(Location location){
        World world = location.getWorld();
        if (world == null)return;
        Particle.DustTransition dustTransition = new Particle.DustTransition(Color.GRAY, Color.WHITE, 1.0f);
        world.spawnParticle(Particle.DUST_COLOR_TRANSITION, location.add(0, 0.3, 0), 8, 0.3, 0.1, 0.3, dustTransition);
        world.playSound(location,Sound.ENTITY_GOAT_LONG_JUMP, 1.2f, 0.8f);
    }

    public static void playFrogJumpEffect(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.spawnParticle(Particle.WATER_SPLASH, location.add(0, 0.3, 0), 10, 0.3, 0.1, 0.3);
        world.playSound(location,Sound.ENTITY_FROG_LONG_JUMP, 1.6f, 0.8f);
    }

    public static void playFrogEatCreeperEffect(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.playSound(location,Sound.ENTITY_GENERIC_EXPLODE,1.0F,1.0F);
        world.spawnParticle(Particle.EXPLOSION_LARGE,location,5, 0.5, 0.5, 0.5, 0, null);
    }
    public static void playFrogEatCowEffect(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin,Sound.ENTITY_GENERIC_DRINK,0.6F,1.0F);
    }
    public static void playFrogFireballSpit(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin,Sound.ITEM_FIRECHARGE_USE,1.0f,1.1f);
    }

    private static final List<Vector> blazeHeadParticleVectors = Arrays.asList(
            new Vector(0.25, 0.0, 0.0), new Vector(-0.25, 0.0, 0.0), new Vector(0.0, 0.0, 0.25),
            new Vector(0.0, 0.0, -0.25), new Vector(0.25, 0.0, 0.25), new Vector(-0.25, 0.0, 0.25),
            new Vector(-0.25, 0.0, -0.25), new Vector(0.25, 0.0, -0.25)
    );
    public static void playBlazeHeadBurnEffect(Location origin, boolean fuel, boolean furnaceBlock, boolean xSpread, boolean invalid){
        World world = origin.getWorld();
        if (world == null)return;
        Random random = new Random();
        float pitch;
        float volume;
        Particle particle;
        double speed = 0.25;
        if (fuel){
            pitch = 1.0F;
            volume = 0.05F;
            particle = Particle.FLAME;
        }else{
            pitch = 1.4F;
            volume = 0.02F;
            particle = Particle.SMOKE_NORMAL;
            speed = 0.005;
        }
        if (invalid) particle = Particle.SMOKE_NORMAL;
        if (!furnaceBlock && !invalid){
            for (Vector vector: blazeHeadParticleVectors){
                Location loc = origin.clone();
                loc.add(vector);
                double xOffset = vector.getX()/6;
                double zOffset = vector.getZ()/6;
                world.spawnParticle(particle, loc, 0, xOffset, 0.2, zOffset, speed,null);
            }
        }else{
            speed = 0.01;
            double yBound = 0.2;
            double xBound = 0.1;
            double zBound = 0.1;
            if (xSpread){
                zBound = 0.4;
            }else xBound = 0.4;
            int count = 10;
            if (invalid) count = 1;
            for (int i = 0; i < count; i++) {
                double x = random.nextDouble(-xBound, xBound);
                double y = random.nextDouble(-yBound, yBound);
                double z = random.nextDouble(-zBound, zBound);
                Location loc = origin.clone().add(x,y,z);
                world.spawnParticle(particle, loc,0, 0,0.2,0,speed,null);
            }
        }
        if (!invalid){
            float addPitch = random.nextFloat(0.4F);
            world.playSound(origin, Sound.ITEM_FIRECHARGE_USE, volume, pitch+addPitch);
        }
    }

    private static void blazeHeadInteractEffect(Location headPos){
        World world = headPos.getWorld();
        if (world == null)return;
        List<Vector> points = Arrays.asList(
                new Vector(0.3, 0.0, 0.3), new Vector(-0.3, 0.0, 0.3),
                new Vector(0.3, 0.0, -0.3), new Vector(-0.3, 0.0, -0.3)
        );
        for (Vector point:points){
            point.setX(point.getX() + randomOffsetCenter(0.2));
            point.setY(point.getY() + randomOffsetCenter(0.1));
            point.setZ(point.getZ() + randomOffsetCenter(0.2));

            double xVar = randomOffsetCenter(0.2);
            double zVar = randomOffsetCenter(0.2);

            Location spawnLoc = headPos.clone();
            spawnLoc.add(point).add(0,-0.2,0);

            world.spawnParticle(Particle.SMOKE_LARGE,spawnLoc,0,xVar,0.1,zVar, 0.2, null);
        }
    }

    public static void playDecollationPearlEffect(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.strikeLightningEffect(location);
    }

    public static void playShulkerAfflictionEffect(Location entityBottom, double entityEyeHeight, boolean quiet){
        World world = entityBottom.getWorld();
        if (world == null)return;
        float volume = 1.0f;
        if (quiet) volume = 0.3f;
        entityBottom.add(0,entityEyeHeight  * 0.8,0);
        world.playSound(entityBottom,Sound.ENTITY_SHULKER_BULLET_HIT, volume,1.0f);
        world.spawnParticle(Particle.CLOUD,entityBottom,10,0.5,0.5,0.5,0.01);
    }

    public static void playChickenHatchEffect(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin,Sound.ENTITY_TURTLE_EGG_CRACK,0.3f,1.2f);
        world.playSound(origin,Sound.ENTITY_CHICKEN_AMBIENT, 0.5f,1.5f);
        world.spawnParticle(Particle.ITEM_CRACK,origin,10,0.1,0.1,0.1, 0.1, new ItemStack(Material.EGG));
    }

    public static void playAllayAttractEffect(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        Random random = new Random();
        world.spawnParticle(Particle.ELECTRIC_SPARK, origin, 8, 0.3, 0.3, 0.3, 0.1);
        world.playSound(origin, Sound.ENTITY_ALLAY_ITEM_THROWN, 0.4F, random.nextFloat(0.85F, 1.15F));
    }

    public static void playPandaMunchBambooEffect(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin,Sound.ENTITY_PANDA_EAT,0.6F, 1.0F);
        world.spawnParticle(Particle.ITEM_CRACK,origin,8,0.2,0.1,0.2,0.05,new ItemStack(Material.BAMBOO));
    }

    public static void playSlimeBounceEffect(Location origin, EntityType type){
        World world = origin.getWorld();
        if (world == null)return;
        ItemStack itemStack = new ItemStack(Material.SLIME_BALL);
        if (type.equals(EntityType.MAGMA_CUBE)) itemStack = new ItemStack(Material.MAGMA_CREAM);
        world.playSound(origin,Sound.ENTITY_SLIME_SQUISH,0.5f,1.2f);
        world.spawnParticle(Particle.ITEM_CRACK,origin.add(0,0.1,0),10,0.2,0.1,0.2,0.05,itemStack);
    }
    public static void playSlimeJumpEffect(Location origin, EntityType type){
        World world = origin.getWorld();
        if (world == null)return;
        ItemStack itemStack = new ItemStack(Material.SLIME_BALL);
        if (type.equals(EntityType.MAGMA_CUBE)) itemStack = new ItemStack(Material.MAGMA_CREAM);
        world.playSound(origin,Sound.ENTITY_SLIME_JUMP,0.5f,1.2f);
        world.spawnParticle(Particle.ITEM_CRACK,origin.add(0,0.1,0),10,0.2,0.1,0.2,0.05,itemStack);
    }

    public static void playCreeperExplosionEffect(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin,Sound.ENTITY_GENERIC_EXPLODE,0.8f,1.0f);
        world.spawnParticle(Particle.EXPLOSION_LARGE,origin,5,1.2,1.2,1.2,0.5);
    }


    //SOUND EFFECTS -------------------------------------------------------------------------------------

    public static void playHeadHurtSound(LivingEntity livingEntity, MobHead mobHead){
        EntityType entityType = mobHead.getEntityType();
        Sound hurtSound = null;
        float volume = 0.5F;
        switch (entityType) {
            case PLAYER -> {hurtSound = Sound.ENTITY_PLAYER_HURT;}
            case AXOLOTL -> {hurtSound = Sound.ENTITY_AXOLOTL_HURT; volume = 1.2F;}
            case PIG -> hurtSound = Sound.ENTITY_PIG_HURT;
            case COW, MUSHROOM_COW -> {hurtSound = Sound.ENTITY_COW_HURT;}
            case CHICKEN -> hurtSound = Sound.ENTITY_CHICKEN_HURT;
            case WOLF -> {hurtSound = Sound.ENTITY_WOLF_HURT;}
            case DONKEY -> hurtSound = Sound.ENTITY_DONKEY_HURT;
            case MULE -> hurtSound = Sound.ENTITY_MULE_HURT;
            case DOLPHIN -> hurtSound = Sound.ENTITY_DOLPHIN_HURT;
            case COD -> hurtSound = Sound.ENTITY_COD_HURT;
            case SALMON -> hurtSound = Sound.ENTITY_SALMON_HURT;
            case PUFFERFISH -> hurtSound = Sound.ENTITY_PUFFER_FISH_HURT;
            case TROPICAL_FISH -> hurtSound = Sound.ENTITY_TROPICAL_FISH_HURT;
            case TURTLE -> hurtSound = Sound.ENTITY_TURTLE_HURT;
            case STRIDER -> {hurtSound = Sound.ENTITY_STRIDER_HURT; volume = 0.8f;}
            case GOAT -> hurtSound = Sound.ENTITY_GOAT_SCREAMING_HURT;
            case SQUID -> hurtSound = Sound.ENTITY_SQUID_HURT;
            case BEE -> hurtSound = Sound.ENTITY_BEE_HURT;
            case BAT -> {hurtSound = Sound.ENTITY_BAT_HURT; volume = 0.4F;}
            case OCELOT -> hurtSound = Sound.ENTITY_OCELOT_HURT;
            case SNOWMAN -> hurtSound = Sound.ENTITY_SNOW_GOLEM_HURT;
            case PANDA -> hurtSound = Sound.ENTITY_PANDA_HURT;
            case POLAR_BEAR -> hurtSound = Sound.ENTITY_POLAR_BEAR_HURT;
            case SKELETON_HORSE -> hurtSound = Sound.ENTITY_SKELETON_HORSE_HURT;
            case ZOMBIE_HORSE -> hurtSound = Sound.ENTITY_ZOMBIE_HORSE_HURT;
            case VILLAGER -> hurtSound = Sound.ENTITY_VILLAGER_HURT;
            case WANDERING_TRADER -> hurtSound = Sound.ENTITY_WANDERING_TRADER_HURT;
            case IRON_GOLEM -> hurtSound = Sound.ENTITY_IRON_GOLEM_HURT;
            case GLOW_SQUID -> hurtSound = Sound.ENTITY_GLOW_SQUID_HURT;
            case SILVERFISH -> {hurtSound = Sound.ENTITY_SILVERFISH_HURT; volume = 1.0F;}
            case STRAY -> hurtSound = Sound.ENTITY_STRAY_HURT;
            case SHULKER -> hurtSound = Sound.ENTITY_SHULKER_HURT;
            case PHANTOM -> hurtSound = Sound.ENTITY_PHANTOM_HURT;
            case HUSK -> hurtSound = Sound.ENTITY_HUSK_HURT;
            case DROWNED -> hurtSound = Sound.ENTITY_DROWNED_HURT;
            case HOGLIN -> hurtSound = Sound.ENTITY_HOGLIN_HURT;
            case ZOGLIN -> hurtSound = Sound.ENTITY_ZOGLIN_HURT;
            case PIGLIN -> hurtSound = Sound.ENTITY_PIGLIN_HURT;
            case PIGLIN_BRUTE -> hurtSound = Sound.ENTITY_PIGLIN_BRUTE_HURT;
            case ZOMBIFIED_PIGLIN -> hurtSound = Sound.ENTITY_ZOMBIFIED_PIGLIN_HURT;
            case ZOMBIE_VILLAGER -> hurtSound = Sound.ENTITY_ZOMBIE_VILLAGER_HURT;
            case WITCH -> hurtSound = Sound.ENTITY_WITCH_HURT;
            case GUARDIAN -> {
                if (livingEntity.getRemainingAir() < livingEntity.getMaximumAir()){
                    hurtSound = Sound.ENTITY_GUARDIAN_HURT;
                }else hurtSound = Sound.ENTITY_GUARDIAN_HURT_LAND;
                volume = 1.4F;
            }
            case ELDER_GUARDIAN -> {
                if (livingEntity.getRemainingAir() < livingEntity.getMaximumAir()){
                    hurtSound = Sound.ENTITY_ELDER_GUARDIAN_HURT;
                }else hurtSound = Sound.ENTITY_ELDER_GUARDIAN_HURT_LAND;
                volume = 1.4F;
            }
            case RAVAGER -> hurtSound = Sound.ENTITY_RAVAGER_HURT;
            case VEX -> {hurtSound = Sound.ENTITY_VEX_HURT; volume = 1.0f;}
            case EVOKER -> hurtSound = Sound.ENTITY_EVOKER_HURT;
            case SPIDER, CAVE_SPIDER -> hurtSound = Sound.ENTITY_SPIDER_HURT;
            case ENDERMAN -> hurtSound = Sound.ENTITY_ENDERMAN_HURT;
            case ENDERMITE -> hurtSound = Sound.ENTITY_ENDERMITE_HURT;
            case GHAST -> {hurtSound = Sound.ENTITY_GHAST_HURT; volume = 0.4F;}
            case BLAZE -> hurtSound = Sound.ENTITY_BLAZE_HURT;
            case MAGMA_CUBE -> hurtSound = Sound.ENTITY_MAGMA_CUBE_HURT;
            case SLIME -> hurtSound = Sound.ENTITY_SLIME_HURT;
            case PILLAGER -> hurtSound = Sound.ENTITY_PILLAGER_HURT;
            case VINDICATOR -> hurtSound = Sound.ENTITY_VINDICATOR_HURT;
            case ILLUSIONER -> hurtSound = Sound.ENTITY_ILLUSIONER_HURT;
            case WITHER_SKELETON -> hurtSound = Sound.ENTITY_WITHER_SKELETON_HURT;
            case WITHER -> hurtSound = Sound.ENTITY_WITHER_HURT;
            case RABBIT -> {hurtSound = Sound.ENTITY_RABBIT_HURT; volume = 1.2F;}
            case CAT -> hurtSound = Sound.ENTITY_CAT_HURT;
            case HORSE -> hurtSound = Sound.ENTITY_HORSE_HURT;
            case LLAMA, TRADER_LLAMA -> hurtSound = Sound.ENTITY_LLAMA_HURT;
            case PARROT -> hurtSound = Sound.ENTITY_PARROT_HURT;
            case FOX -> hurtSound = Sound.ENTITY_FOX_HURT;
            case SHEEP -> hurtSound = Sound.ENTITY_SHEEP_HURT;
            case ZOMBIE -> hurtSound = Sound.ENTITY_ZOMBIE_HURT;
            case SKELETON -> hurtSound = Sound.ENTITY_SKELETON_HURT;
            case CREEPER -> hurtSound = Sound.ENTITY_CREEPER_HURT;
            case ENDER_DRAGON -> {hurtSound = Sound.ENTITY_ENDER_DRAGON_HURT; volume = 0.4F;}
            case WARDEN -> {hurtSound = Sound.ENTITY_WARDEN_HURT;}
            case ALLAY -> {hurtSound = Sound.ENTITY_ALLAY_HURT;}
            case FROG -> {hurtSound = Sound.ENTITY_FROG_HURT;}
            case TADPOLE -> {hurtSound = Sound.ENTITY_TADPOLE_HURT;}
            case CAMEL -> {hurtSound = Sound.ENTITY_CAMEL_HURT;}
            case SNIFFER -> {hurtSound = Sound.ENTITY_SNIFFER_HURT;}
        }
        if (hurtSound != null){
            livingEntity.getWorld().playSound(livingEntity.getLocation(), hurtSound, volume, 1.0F);
        }
    }

    public static void playHeadDeathSound(LivingEntity livingEntity, MobHead mobHead){
        EntityType headType = mobHead.getEntityType();
        playDeathSound(livingEntity.getLocation(), livingEntity, headType);
    }
    public static void playDeathSound(Location origin, @Nullable LivingEntity livingEntity, EntityType entityType){
        World world = origin.getWorld();
        if (world == null)return;
        Sound deathSound = null;
        float volume = 0.5F;
        switch (entityType) {
            case PLAYER -> {deathSound = Sound.ENTITY_PLAYER_DEATH;}
            case AXOLOTL -> {deathSound = Sound.ENTITY_AXOLOTL_DEATH;volume = 1.0F;}
            case PIG -> deathSound = Sound.ENTITY_PIG_DEATH;
            case COW, MUSHROOM_COW -> {deathSound = Sound.ENTITY_COW_DEATH;}
            case CHICKEN -> deathSound = Sound.ENTITY_CHICKEN_DEATH;
            case WOLF -> deathSound = Sound.ENTITY_WOLF_DEATH;
            case DONKEY -> deathSound = Sound.ENTITY_DONKEY_DEATH;
            case MULE -> deathSound = Sound.ENTITY_MULE_DEATH;
            case DOLPHIN -> deathSound = Sound.ENTITY_DOLPHIN_DEATH;
            case COD -> deathSound = Sound.ENTITY_COD_DEATH;
            case SALMON -> deathSound = Sound.ENTITY_SALMON_DEATH;
            case PUFFERFISH -> deathSound = Sound.ENTITY_PUFFER_FISH_DEATH;
            case TROPICAL_FISH -> deathSound = Sound.ENTITY_TROPICAL_FISH_DEATH;
            case TURTLE -> deathSound = Sound.ENTITY_TURTLE_DEATH;
            case STRIDER -> {deathSound = Sound.ENTITY_STRIDER_DEATH; volume = 0.8f;}
            case GOAT -> deathSound = Sound.ENTITY_GOAT_SCREAMING_DEATH;
            case SQUID -> deathSound = Sound.ENTITY_SQUID_DEATH;
            case BEE -> deathSound = Sound.ENTITY_BEE_DEATH;
            case BAT -> {deathSound = Sound.ENTITY_BAT_DEATH; volume = 0.25F;}
            case OCELOT -> deathSound = Sound.ENTITY_OCELOT_DEATH;
            case SNOWMAN -> deathSound = Sound.ENTITY_SNOW_GOLEM_DEATH;
            case PANDA -> deathSound = Sound.ENTITY_PANDA_DEATH;
            case POLAR_BEAR -> deathSound = Sound.ENTITY_POLAR_BEAR_DEATH;
            case SKELETON_HORSE -> deathSound = Sound.ENTITY_SKELETON_HORSE_DEATH;
            case ZOMBIE_HORSE -> deathSound = Sound.ENTITY_ZOMBIE_HORSE_DEATH;
            case VILLAGER -> deathSound = Sound.ENTITY_VILLAGER_DEATH;
            case WANDERING_TRADER -> deathSound = Sound.ENTITY_WANDERING_TRADER_DEATH;
            case IRON_GOLEM -> deathSound = Sound.ENTITY_IRON_GOLEM_DEATH;
            case GLOW_SQUID -> deathSound = Sound.ENTITY_GLOW_SQUID_DEATH;
            case SILVERFISH -> {deathSound = Sound.ENTITY_SILVERFISH_DEATH;}
            case STRAY -> deathSound = Sound.ENTITY_STRAY_DEATH;
            case SHULKER -> deathSound = Sound.ENTITY_SHULKER_DEATH;
            case PHANTOM -> deathSound = Sound.ENTITY_PHANTOM_DEATH;
            case HUSK -> deathSound = Sound.ENTITY_HUSK_DEATH;
            case DROWNED -> deathSound = Sound.ENTITY_DROWNED_DEATH;
            case HOGLIN -> deathSound = Sound.ENTITY_HOGLIN_DEATH;
            case ZOGLIN -> deathSound = Sound.ENTITY_ZOGLIN_DEATH;
            case PIGLIN -> deathSound = Sound.ENTITY_PIGLIN_DEATH;
            case PIGLIN_BRUTE -> deathSound = Sound.ENTITY_PIGLIN_BRUTE_DEATH;
            case ZOMBIFIED_PIGLIN -> deathSound = Sound.ENTITY_ZOMBIFIED_PIGLIN_DEATH;
            case ZOMBIE_VILLAGER -> deathSound = Sound.ENTITY_ZOMBIE_VILLAGER_DEATH;
            case WITCH -> deathSound = Sound.ENTITY_WITCH_DEATH;
            case GUARDIAN -> {
                if (livingEntity != null && livingEntity.getRemainingAir() < livingEntity.getMaximumAir()){
                    deathSound = Sound.ENTITY_GUARDIAN_DEATH;
                }else deathSound = Sound.ENTITY_GUARDIAN_DEATH_LAND;
                volume = 1.0F;
            }
            case ELDER_GUARDIAN -> {
                if (livingEntity != null && livingEntity.getRemainingAir() < livingEntity.getMaximumAir()){
                    deathSound = Sound.ENTITY_ELDER_GUARDIAN_DEATH;
                }else deathSound = Sound.ENTITY_ELDER_GUARDIAN_DEATH_LAND;
                volume = 1.0F;
            }
            case RAVAGER -> deathSound = Sound.ENTITY_RAVAGER_DEATH;
            case VEX -> deathSound = Sound.ENTITY_VEX_DEATH;
            case EVOKER -> deathSound = Sound.ENTITY_EVOKER_DEATH;
            case SPIDER, CAVE_SPIDER -> deathSound = Sound.ENTITY_SPIDER_DEATH;
            case ENDERMAN -> deathSound = Sound.ENTITY_ENDERMAN_DEATH;
            case ENDERMITE -> deathSound = Sound.ENTITY_ENDERMITE_DEATH;
            case GHAST -> {deathSound = Sound.ENTITY_GHAST_DEATH; volume = 0.4F;}
            case BLAZE -> deathSound = Sound.ENTITY_BLAZE_DEATH;
            case MAGMA_CUBE -> deathSound = Sound.ENTITY_MAGMA_CUBE_DEATH;
            case SLIME -> deathSound = Sound.ENTITY_SLIME_DEATH;
            case PILLAGER -> deathSound = Sound.ENTITY_PILLAGER_DEATH;
            case VINDICATOR -> deathSound = Sound.ENTITY_VINDICATOR_DEATH;
            case ILLUSIONER -> deathSound = Sound.ENTITY_ILLUSIONER_DEATH;
            case WITHER_SKELETON -> deathSound = Sound.ENTITY_WITHER_SKELETON_DEATH;
            case WITHER -> deathSound = Sound.ENTITY_WITHER_DEATH;
            case RABBIT -> {deathSound = Sound.ENTITY_RABBIT_DEATH; volume = 1.2F;}
            case CAT -> deathSound = Sound.ENTITY_CAT_DEATH;
            case HORSE -> deathSound = Sound.ENTITY_HORSE_DEATH;
            case LLAMA, TRADER_LLAMA -> deathSound = Sound.ENTITY_LLAMA_DEATH;
            case PARROT -> deathSound = Sound.ENTITY_PARROT_DEATH;
            case FOX -> deathSound = Sound.ENTITY_FOX_DEATH;
            case SHEEP -> deathSound = Sound.ENTITY_SHEEP_DEATH;
            case ZOMBIE -> deathSound = Sound.ENTITY_ZOMBIE_DEATH;
            case SKELETON -> deathSound = Sound.ENTITY_SKELETON_DEATH;
            case CREEPER -> deathSound = Sound.ENTITY_CREEPER_DEATH;
            case ENDER_DRAGON -> {deathSound = Sound.ENTITY_ENDER_DRAGON_DEATH; volume = 0.3F;}
            case WARDEN -> {deathSound = Sound.ENTITY_WARDEN_DEATH;}
            case ALLAY -> {deathSound = Sound.ENTITY_ALLAY_DEATH;}
            case FROG -> {deathSound = Sound.ENTITY_FROG_DEATH;}
            case TADPOLE -> {deathSound = Sound.ENTITY_TADPOLE_DEATH;}
            case CAMEL -> {deathSound = Sound.ENTITY_CAMEL_DEATH;}
            case SNIFFER -> {deathSound = Sound.ENTITY_SNIFFER_DEATH;}
        }
        if (deathSound != null){
            world.playSound(origin,deathSound,volume, 1.0F);
        }
    }

    public static void playFrogTongueSound(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.playSound(location, Sound.ENTITY_FROG_TONGUE, 2F, 1.1F);
    }
    public static void playFrogEatenSounds(Location location, EntityType eatenType){
        World world = location.getWorld();
        if (world == null)return;
        world.playSound(location, Sound.ENTITY_FROG_AMBIENT, 1.4F, 1.0F);
        world.playSound(location, Sound.ENTITY_PLAYER_BURP, 0.6F,1.0F);
        playDeathSound(location,null,eatenType);
    }
    public static void playFrogGetDropsSound(Location origin, boolean overflow){
        World world = origin.getWorld();
        if (world == null)return;
        Sound sound;
        if (overflow){
            sound = Sound.ITEM_BUNDLE_DROP_CONTENTS;
        }else sound = Sound.ENTITY_ITEM_PICKUP;
        world.playSound(origin,sound,1.2f,1.2f);
    }

    public static void playGoatRamHitSound(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.playSound(location, Sound.ENTITY_GOAT_RAM_IMPACT, 1.2f, 0.8f);
    }
    public static void playGoatInvulnerableSound(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.playSound(location, Sound.ITEM_SHIELD_BLOCK, 1.2f, 1.6f);
    }
    public static void playGoatBreakBlockSound(Location location, List<Material> blockMats, List<Location> blockLocs){
        World world = location.getWorld();
        if (world == null)return;
        playGoatRamHitSound(location);
        int i = 0;
        for (Material material:blockMats){
            Sound breakSound = material.createBlockData().getSoundGroup().getBreakSound();
            //System.out.println("breakSound: "+breakSound); //debug
            world.playSound(blockLocs.get(i), breakSound, 1.0f, 1.0f);
            i++;
        }
    }

    public static void playEndermanTeleportSound(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.playSound(location,Sound.ITEM_CHORUS_FRUIT_TELEPORT, 0.4F, 1.0F);
    }

    public static void playEndermanRegeneratePearlSound(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.playSound(location, Sound.ITEM_CHORUS_FRUIT_TELEPORT,0.5f, 1.5f);
    }

    public static void playLlamaSpitSound(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.playSound(location, Sound.ENTITY_LLAMA_SPIT,0.8F, 1.0F);
    }

    public static void playSpiderWebThrowSound(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.playSound(location, Sound.BLOCK_WOOL_BREAK, 0.6f, 1.5f);
    }

    public static void playSpiderWebHitSound(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.playSound(location, Sound.BLOCK_WOOL_BREAK, 0.6f, 1.8f);
    }

    public static void playCowMilkingSounds(Location location, boolean bucket){
        World world = location.getWorld();
        if (world == null)return;
        Sound sound;
        if (bucket){
            sound = Sound.ENTITY_COW_MILK;
        }else sound = Sound.ENTITY_GENERIC_DRINK;
        world.playSound(location, sound, 0.5f, 1.0f);
    }
    public static void playMooshroomSoupingSounds(Location location, boolean bowl){
        World world = location.getWorld();
        if (world == null)return;
        Sound sound;
        if (bowl){
            sound = Sound.ENTITY_MOOSHROOM_MILK;
        }else sound = Sound.ITEM_HONEY_BOTTLE_DRINK;
        world.playSound(location, sound, 0.5f, 1.0f);
    }
    public static void playParrotWarnSound(Location playerLoc, Location hostileLoc, Sound warnSound){
        World world = playerLoc.getWorld();
        if (world == null)return;
        Vector pVec = playerLoc.toVector();
        Vector hVec = hostileLoc.toVector();
        Vector direction = hVec.subtract(pVec).normalize();
        world.playSound(playerLoc.add(direction),warnSound,0.6f,1.0f);
    }
    public static void playElderGuardianFatigueSound(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin,Sound.ENTITY_ELDER_GUARDIAN_CURSE, 0.5f, 1.0f);
    }
    public static void playHeadEquipSound(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin,Sound.ITEM_ARMOR_EQUIP_LEATHER, 0.6f, 1.0f);
    } // make client-side only

    public static void playEnderDragonBoostSound(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin,Sound.ENTITY_ENDER_DRAGON_FLAP, 0.6f, 1.0f);
    }

    public static void playCamelDashSound(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin,Sound.ENTITY_CAMEL_DASH,0.8f,1.0f);
    }
    public static void playCamelRefreshSound(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.playSound(origin,Sound.ENTITY_CAMEL_DASH_READY,0.8f,1.0f);
    }

    //PARTICLE EFFECTS -----------------------------------------------------------------------------------
    public static void playImpostorParticles(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.spawnParticle(Particle.VILLAGER_ANGRY,location,3, 0.5, 0, 0.5,null);
    }

    public static void playGoatRamTrail(Location location){
        World world = location.getWorld();
        if (world == null)return;
        Particle.DustTransition dustTransition = new Particle.DustTransition(Color.GRAY, Color.WHITE, 1.0f);
        world.spawnParticle(Particle.REDSTONE, location.add(0, 0.9, 0), 2, 0.4, 0.8, 0.4, dustTransition);
    }

    public static void playGhastFlightParticles(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.spawnParticle(Particle.FLAME,location, 1,0.25,0.15,0.25,0.01,null);
    }

    public static void shulkerLevitateParticles(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        origin.add(0,0.25,0);
        world.spawnParticle(Particle.CLOUD,origin,1,0.2,0.2,0.2,0.01);
    }

    public static void playChickenIncubateParticle(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        origin.add(0,0.125,0);
        world.spawnParticle(Particle.SMALL_FLAME,origin,1,0.1,0.1,0.1,0.0);
    }
    public static void playChickenIncubateHayParticle(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        origin.add(0,0.125,0);
        world.spawnParticle(Particle.EGG_CRACK,origin,1,0.1,0.1,0.1,0.0);
    }
    public static void playEnderDragonBoostParticles(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.spawnParticle(Particle.DRAGON_BREATH,origin,3,0.2,0.2,0.2, 0.01);
    }
    public static void playFeatheredGlideParticles(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.spawnParticle(Particle.ITEM_CRACK,origin,1,0.2,0.2,0.2, 0.01, new ItemStack(Material.FEATHER));
    }
    public static void playBlazeGlideParticles(Location origin){
        World world = origin.getWorld();
        if (world == null)return;
        world.spawnParticle(Particle.SMALL_FLAME,origin,2,0.2,0.2,0.2, 0.01);
    }

    //SOUND BUILDERS --------------------------------------------------------------------------------------
    private static Sound parrotInteractSound(){
        return getParrotSounds().get(random.nextInt(getParrotSounds().size()));
    }
    private static List<Sound> parrotSounds;
    public static List<Sound> getParrotSounds(){
        if (parrotSounds == null) parrotSounds = buildParrotSounds();
        return parrotSounds;
    }
    private static List<Sound> buildParrotSounds(){
        List<Sound> sounds = new ArrayList<>();
        for (Sound sound:Sound.values()){
            if (sound.toString().contains("PARROT_IMITATE")) sounds.add(sound);
        }
        return sounds;
    }

    private static Sound foxInteractSound(){
        int i = random.nextInt(99);
        if (i < 49){
            return Sound.ENTITY_FOX_AMBIENT;
        }else if (i < 79){
            return Sound.ENTITY_FOX_SNIFF;
        }else return Sound.ENTITY_FOX_SCREECH;
    }

    private static Sound wolfInteractSound(){
        int i = random.nextInt(99);
        if (i < 69){
            return Sound.ENTITY_WOLF_AMBIENT;
        }else if (i < 89){
            return Sound.ENTITY_WOLF_GROWL;
        }else return Sound.ENTITY_WOLF_HOWL;
    }

    private static Sound pandaInteractSound(){
        int i = random.nextInt(99);
        if (i < 59){
            return Sound.ENTITY_PANDA_AMBIENT;
        }else if (i < 89){
            return Sound.ENTITY_PANDA_EAT;
        }else return Sound.ENTITY_PANDA_SNEEZE;
    }

    private static Sound catInteractSound(){
        int i = random.nextInt(99);
        if (i < 19){
            return Sound.ENTITY_CAT_PURREOW;
        }else if (i < 39){
            return Sound.ENTITY_CAT_PURR;
        }else return Sound.ENTITY_CAT_AMBIENT;
    }

    private static Sound playerInteractSound(){
        List<Sound> sounds = Arrays.asList(
                Sound.ENTITY_PLAYER_BURP, Sound.ENTITY_PLAYER_BIG_FALL, Sound.ENTITY_PLAYER_HURT,
                Sound.ENTITY_PLAYER_HURT_DROWN, Sound.ENTITY_PLAYER_HURT_FREEZE, Sound.ENTITY_PLAYER_HURT_ON_FIRE,
                Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, Sound.ENTITY_PLAYER_LEVELUP, Sound.ENTITY_PLAYER_SMALL_FALL,
                Sound.ITEM_ARMOR_EQUIP_GENERIC, Sound.ITEM_BUCKET_FILL, Sound.ITEM_BUCKET_FILL_LAVA,
                Sound.BLOCK_ANVIL_USE, Sound.BLOCK_BREWING_STAND_BREW, Sound.BLOCK_CHEST_OPEN, Sound.BLOCK_CHEST_CLOSE,
                Sound.BLOCK_ENCHANTMENT_TABLE_USE, Sound.BLOCK_ENDER_CHEST_OPEN, Sound.BLOCK_ENDER_CHEST_CLOSE,
                Sound.BLOCK_GRINDSTONE_USE, Sound.BLOCK_SMITHING_TABLE_USE, Sound.ENTITY_ITEM_BREAK,
                Sound.BLOCK_LADDER_STEP, Sound.ENTITY_EXPERIENCE_ORB_PICKUP
        );
        Random random = new Random();
        return sounds.get(random.nextInt(sounds.size()));
    }

    //PARTICLE BUILDERS -----------------------------------------------------------------------------------
}
