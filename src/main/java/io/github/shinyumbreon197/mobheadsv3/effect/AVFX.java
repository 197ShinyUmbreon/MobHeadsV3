package io.github.shinyumbreon197.mobheadsv3.effect;

import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.head.passive.multi.ParrotHead;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static io.github.shinyumbreon197.mobheadsv3.tool.EffectUtil.randomOffsetCenter;

public class AVFX {

    //COMBINED A/V EFFECTS ---------------------------------------------------------------------------------
    public static void playHeadDropEffect(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.spawnParticle(Particle.EXPLOSION_LARGE,location,1);
        world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.2F, 1.2F);
        world.playSound(location, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 0.4F, 1.3F);
    }

    public static void playHeadInteractEffect(Location location, MobHead mobHead){
        World world = location.getWorld();
        if (world == null)return;
        Sound interactSound = mobHead.getInteractSound();
        EntityType entityType = mobHead.getEntityType();
        float volume = 0.6F;
        switch (entityType){
            default -> {}
            case PLAYER -> {interactSound = playerInteractSound(); volume = 0.4f;}
            case FOX -> {interactSound = foxInteractSound(); volume = 0.8f;}
            case ENDER_DRAGON -> {volume = 0.5f;}
            case WOLF -> {interactSound = wolfInteractSound(); volume = 0.4f;}
            case TURTLE -> {volume = 1.3f;}
            case BAT -> {volume = 0.05f;}
            case OCELOT -> {volume = 1.5f;}
            case PANDA -> {interactSound = pandaInteractSound();}
            case PARROT -> {interactSound = parrotInteractSound();}
            case RABBIT -> {volume = 1.6f;}
            case FROG -> {volume = 1.0f;}
            case RAVAGER -> {volume = 0.2f;}
            case WITHER -> {volume = 0.3f;}
            case BLAZE -> blazeHeadInteractEffect(location);
        }
        if (interactSound != null) world.playSound(location, interactSound, volume, 1.0F);
    }

    public static void playSummonEffect(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.playSound(location, Sound.BLOCK_SOUL_SAND_BREAK,1.8f,1.1f);
        world.spawnParticle(Particle.SMOKE_LARGE,location,16,0.4,0.5, 0.4,0.0, null);
    }
    public static void playWolfSummonEffect(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.playSound(location,Sound.ENTITY_WOLF_HOWL,0.1f, 1.0f);
        playSummonEffect(location);
    }
    public static void playBeeSummonEffect(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.playSound(location,Sound.BLOCK_BEEHIVE_EXIT,1.2f, 1.0f);
        playSummonEffect(location);
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

    public static void playBlazeHeadFlameEffect(Location headLoc){
        World world = headLoc.getWorld();
        if (world == null)return;
        List<Vector> particleVectors = Arrays.asList(
                new Vector(0.25, 0.0, 0.0), new Vector(-0.25, 0.0, 0.0), new Vector(0.0, 0.0, 0.25),
                new Vector(0.0, 0.0, -0.25), new Vector(0.25, 0.0, 0.25), new Vector(-0.25, 0.0, 0.25),
                new Vector(-0.25, 0.0, -0.25), new Vector(0.25, 0.0, -0.25)
        );
        for (Vector vector:particleVectors){
            Location loc = headLoc.clone();
            loc.add(vector);
            double xOffset = vector.getX()/6;
            double zOffset = vector.getZ()/6;
            world.spawnParticle(Particle.FLAME, loc, 0, xOffset, 0.2, zOffset,0.25,null);
        }
        float addPitch = new Random().nextFloat(0.4F);
        world.playSound(headLoc, Sound.ITEM_FIRECHARGE_USE, 0.05F, 1.0F+addPitch);
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


    //SOUND EFFECTS -------------------------------------------------------------------------------------
    public static void playHeadHurtSound(LivingEntity livingEntity, MobHead mobHead){
        EntityType entityType = mobHead.getEntityType();
        Sound hurtSound = null;
        float volume = 0.6F;
        switch (entityType) {
            case PLAYER -> {hurtSound = Sound.ENTITY_PLAYER_HURT;}
            case AXOLOTL -> {hurtSound = Sound.ENTITY_AXOLOTL_HURT; volume = 1.2F;}
            case PIG -> hurtSound = Sound.ENTITY_PIG_HURT;
            case COW, MUSHROOM_COW -> {hurtSound = Sound.ENTITY_COW_HURT; volume = 0.6F;}
            case CHICKEN -> hurtSound = Sound.ENTITY_CHICKEN_HURT;
            case WOLF -> {hurtSound = Sound.ENTITY_WOLF_HURT; volume = 0.6F;}
            case DONKEY -> hurtSound = Sound.ENTITY_DONKEY_HURT;
            case MULE -> hurtSound = Sound.ENTITY_MULE_HURT;
            case DOLPHIN -> hurtSound = Sound.ENTITY_DOLPHIN_HURT;
            case COD -> hurtSound = Sound.ENTITY_COD_HURT;
            case SALMON -> hurtSound = Sound.ENTITY_SALMON_HURT;
            case PUFFERFISH -> hurtSound = Sound.ENTITY_PUFFER_FISH_HURT;
            case TROPICAL_FISH -> hurtSound = Sound.ENTITY_TROPICAL_FISH_HURT;
            case TURTLE -> hurtSound = Sound.ENTITY_TURTLE_HURT;
            case STRIDER -> hurtSound = Sound.ENTITY_STRIDER_HURT;
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
            case GUARDIAN -> {hurtSound = Sound.ENTITY_GUARDIAN_HURT; volume = 1.4F;}
            case ELDER_GUARDIAN -> hurtSound = Sound.ENTITY_ELDER_GUARDIAN_HURT_LAND;
            case RAVAGER -> hurtSound = Sound.ENTITY_RAVAGER_HURT;
            case VEX -> hurtSound = Sound.ENTITY_VEX_HURT;
            case EVOKER -> hurtSound = Sound.ENTITY_EVOKER_HURT;
            case SPIDER, CAVE_SPIDER -> hurtSound = Sound.ENTITY_SPIDER_HURT;
            case ENDERMAN -> hurtSound = Sound.ENTITY_ENDERMAN_HURT;
            case ENDERMITE -> hurtSound = Sound.ENTITY_ENDERMITE_HURT;
            case GHAST -> {hurtSound = Sound.ENTITY_GHAST_HURT; volume = 0.6F;}
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
        }
        if (hurtSound != null){
            livingEntity.getWorld().playSound(livingEntity.getLocation(), hurtSound, volume, 1.0F);
        }
    }

    public static void playHeadDeathSound(LivingEntity livingEntity, MobHead mobHead){
        EntityType entityType = mobHead.getEntityType();

        Sound deathSound = null;
        float volume = 0.6F;
        switch (entityType) {
            case PLAYER -> {deathSound = Sound.ENTITY_PLAYER_DEATH;}
            case AXOLOTL -> {deathSound = Sound.ENTITY_AXOLOTL_DEATH;volume = 1.0F;}
            case PIG -> deathSound = Sound.ENTITY_PIG_DEATH;
            case COW, MUSHROOM_COW -> {deathSound = Sound.ENTITY_COW_DEATH;volume = 0.6F;}
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
            case STRIDER -> deathSound = Sound.ENTITY_STRIDER_DEATH;
            case GOAT -> deathSound = Sound.ENTITY_GOAT_SCREAMING_DEATH;
            case SQUID -> deathSound = Sound.ENTITY_SQUID_DEATH;
            case BEE -> deathSound = Sound.ENTITY_BEE_DEATH;
            case BAT -> {deathSound = Sound.ENTITY_BAT_DEATH; volume = 0.4F;}
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
            case SILVERFISH -> {deathSound = Sound.ENTITY_SILVERFISH_DEATH; volume = 0.6F;}
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
            case GUARDIAN -> {deathSound = Sound.ENTITY_GUARDIAN_DEATH; volume = 1.0F;}
            case ELDER_GUARDIAN -> deathSound = Sound.ENTITY_ELDER_GUARDIAN_DEATH_LAND;
            case RAVAGER -> deathSound = Sound.ENTITY_RAVAGER_DEATH;
            case VEX -> deathSound = Sound.ENTITY_VEX_DEATH;
            case EVOKER -> deathSound = Sound.ENTITY_EVOKER_DEATH;
            case SPIDER, CAVE_SPIDER -> deathSound = Sound.ENTITY_SPIDER_DEATH;
            case ENDERMAN -> deathSound = Sound.ENTITY_ENDERMAN_DEATH;
            case ENDERMITE -> deathSound = Sound.ENTITY_ENDERMITE_DEATH;
            case GHAST -> {deathSound = Sound.ENTITY_GHAST_DEATH; volume = 0.6F;}
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
            case ENDER_DRAGON -> {deathSound = Sound.ENTITY_ENDER_DRAGON_DEATH; volume = 0.4F;}
            case WARDEN -> {deathSound = Sound.ENTITY_WARDEN_DEATH;}
            case ALLAY -> {deathSound = Sound.ENTITY_ALLAY_DEATH;}
            case FROG -> {deathSound = Sound.ENTITY_FROG_DEATH;}
            case TADPOLE -> {deathSound = Sound.ENTITY_TADPOLE_DEATH;}
        }
        if (deathSound != null){
            livingEntity.getWorld().playSound(livingEntity.getLocation(),deathSound,volume, 1.0F);
        }
    }

    public static void playFrogTongueSound(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.playSound(location, Sound.ENTITY_FROG_TONGUE, 2F, 1.1F);
    }

    public static void playFrogEatenSounds(Location location, Sound eatenDeathSound){
        World world = location.getWorld();
        if (world == null)return;
        world.playSound(location, Sound.ENTITY_FROG_AMBIENT, 1.4F, 1.0F);
        world.playSound(location, Sound.ENTITY_PLAYER_BURP, 0.6F,1.0F);
        if (eatenDeathSound != null) world.playSound(location,eatenDeathSound, 0.8F, 1.0F);
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

    //SOUND BUILDERS --------------------------------------------------------------------------------------
    private static Sound parrotInteractSound(){
        Random random = new Random();
        return ParrotHead.getParrotSounds().get(random.nextInt(ParrotHead.getParrotSounds().size()));
    }

    private static Sound foxInteractSound(){
        Random random = new Random();
        int i = random.nextInt(99);
        if (i < 49){
            return Sound.ENTITY_FOX_AMBIENT;
        }else if (i < 79){
            return Sound.ENTITY_FOX_SNIFF;
        }else return Sound.ENTITY_FOX_SCREECH;
    }

    private static Sound wolfInteractSound(){
        Random random = new Random();
        int i = random.nextInt(99);
        if (i < 69){
            return Sound.ENTITY_WOLF_AMBIENT;
        }else if (i < 89){
            return Sound.ENTITY_WOLF_GROWL;
        }else return Sound.ENTITY_WOLF_HOWL;
    }

    private static Sound pandaInteractSound(){
        Random random = new Random();
        int i = random.nextInt(99);
        if (i < 59){
            return Sound.ENTITY_PANDA_AMBIENT;
        }else if (i < 89){
            return Sound.ENTITY_PANDA_EAT;
        }else return Sound.ENTITY_PANDA_SNEEZE;
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
