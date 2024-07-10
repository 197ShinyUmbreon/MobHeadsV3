package io.github.shinyumbreon197.mobheadsv3.event.relay;

import io.github.shinyumbreon197.mobheadsv3.Config;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.Packets;
import io.github.shinyumbreon197.mobheadsv3.entity.Decoy;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import io.github.shinyumbreon197.mobheadsv3.function.Util;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CreatureTickRelay {
    public static void tickRelay(LivingEntity target, UUID headID){
        if (!Config.headEffects)return;
        MobHead mobHead = MobHead.getMobHeadFromUUID(headID);
        if (mobHead == null)return;
        EntityType headType = mobHead.getEntityType();
        switch (headType) {
            case PLAYER -> {
            }
            case ELDER_GUARDIAN -> {
            }
            case WITHER_SKELETON -> {
            }
            case STRAY -> {
            }
            case HUSK -> {
            }
            case ZOMBIE_VILLAGER -> {
            }
            case SKELETON_HORSE -> {
            }
            case ZOMBIE_HORSE -> {
            }
            case DONKEY -> {
            }
            case MULE -> {
            }
            case EVOKER -> {
            }
            case VEX -> {
            }
            case VINDICATOR -> {
            }
            case ILLUSIONER -> {
            }
            case CREEPER -> {
            }
            case SKELETON -> {
            }
            case SPIDER -> {
            }
            case GIANT -> {
            }
            case ZOMBIE -> {
            }
            case SLIME -> {
            }
            case GHAST -> {
            }
            case ZOMBIFIED_PIGLIN -> {
            }
            case ENDERMAN -> {if (Util.isExposedToWater(target)) target.damage(1);}
            case CAVE_SPIDER -> {
            }
            case SILVERFISH -> {
            }
            case BLAZE -> {}
            case MAGMA_CUBE -> {
            }
            case ENDER_DRAGON -> {
            }
            case WITHER -> {
            }
            case BAT -> { // (Packet) give glow to nearby ents
                if (target instanceof Player){
                    Packets.nearbyGlow((Player) target, 20);
                }
            }
            case WITCH -> {
            }
            case ENDERMITE -> {CreatureEvents.endermiteEndermanAggro(target);}
            case GUARDIAN -> {
            }
            case SHULKER -> { // Creatures gain Levitation while you have them leaded.
            }
            case PIG -> {
            }
            case SHEEP -> {
            }
            case COW -> {
            }
            case CHICKEN -> {}
            case SQUID -> {
            }
            //case WOLF -> {if (!Decoy.hasDecoy(target)) Decoy.addDecoyToCreature(target, EntityType.WOLF);}
            case MOOSHROOM -> {
            }
            case SNOW_GOLEM -> {
            }
            //case OCELOT -> {if (!Decoy.hasDecoy(target)) Decoy.addDecoyToCreature(target, EntityType.OCELOT);}
            case IRON_GOLEM -> {
            }
            case HORSE -> {
            }
            case RABBIT -> {
            }
            case POLAR_BEAR -> {
            }
            case LLAMA -> {
            }
            case PARROT -> {}
            case VILLAGER -> {
            }
            case TURTLE -> {
            }
            case PHANTOM -> {
            }
            case COD -> {
            }
            case SALMON -> {
            }
            case PUFFERFISH -> {
            }
            case TROPICAL_FISH -> {
            }
            case DROWNED -> {
            }
            case DOLPHIN -> {
            }
            //case CAT -> {if (!Decoy.hasDecoy(target)) Decoy.addDecoyToCreature(target, EntityType.CAT);}
            case PANDA -> {if (target instanceof Player) CreatureEvents.pandaSnackBamboo((Player) target);}
            case PILLAGER -> {
            }
            case RAVAGER -> {
            }
            case TRADER_LLAMA -> {
            }
            case WANDERING_TRADER -> {
            }
            case FOX -> {
            }
            case BEE -> {
            }
            case HOGLIN -> {
            }
            case PIGLIN -> {
            }
            case STRIDER -> {
                CreatureEvents.striderWalkOnLava(target);
            }
            case ZOGLIN -> {
            }
            case PIGLIN_BRUTE -> {
            }
            case AXOLOTL -> {
            }
            case GLOW_SQUID -> {
            }
            case GOAT -> {
            }
            case ALLAY -> {CreatureEvents.allayAttractNearbyItems(target);}
            case FROG -> {
                if (target instanceof Player) CreatureEvents.frogStartNearbyEdibleClock((Player) target);
            }
            case TADPOLE -> {
            }
            case WARDEN -> { // (Packet) Give glow to nearby ents
                if (target instanceof Player) Packets.nearbyGlow((Player) target, 40);
            }
            case CAMEL -> {
            }
            case SNIFFER -> { // (Packet) Highlight nearby sus blocks
                if (target instanceof Player) CreatureEvents.snifferHighlightSusNew((Player) target);
            }
        }
    }

}
