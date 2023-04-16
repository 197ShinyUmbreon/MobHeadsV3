package io.github.shinyumbreon197.mobheadsv3.effect;

import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class AVFX {

    //COMBINED A/V EFFECTS ---------------------------------------------------------------------------------

    public static void playHeadEffect(Location location){
        World world = location.getWorld();
        if (world == null)return;
        world.spawnParticle(Particle.EXPLOSION_LARGE,location,1);
        world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.2F, 1.2F);
        world.playSound(location, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 0.4F, 1.3F);
    }

    //SOUND EFFECTS -------------------------------------------------------------------------------------

    public static void playHurtSound(LivingEntity livingEntity){
        assert livingEntity.getEquipment() != null;
        ItemStack headItem = livingEntity.getEquipment().getHelmet();
        MobHead mobHead = HeadUtil.getMobHeadFromHeadItem(headItem);
        if (mobHead == null)return;
        EntityType entityType = mobHead.getEntityType();

        Sound hurtSound = null;
        float volume = 0.8F;
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
            case BAT -> {hurtSound = Sound.ENTITY_BAT_HURT; volume = 0.6F;}
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

    public static void playDeathSound(LivingEntity livingEntity){
        assert livingEntity.getEquipment() != null;
        ItemStack headItem = livingEntity.getEquipment().getHelmet();
        MobHead mobHead = HeadUtil.getMobHeadFromHeadItem(headItem);
        if (mobHead == null)return;
        EntityType entityType = mobHead.getEntityType();

        Sound deathSound = null;
        float volume = 0.8F;
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
            case BAT -> {deathSound = Sound.ENTITY_BAT_DEATH; volume = 0.6F;}
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
            case SILVERFISH -> {deathSound = Sound.ENTITY_SILVERFISH_DEATH; volume = 1.0F;}
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

    //PARTICLE EFFECTS -----------------------------------------------------------------------------------

}
