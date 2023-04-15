package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.effect.AfflictedHeadEffects;
import io.github.shinyumbreon197.mobheadsv3.effect.AVFX;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class AttackDamageDeathEvents implements Listener {

    @EventHandler
    public void onEntityTakeDamage(EntityDamageEvent e){
        if (!(e.getEntity() instanceof LivingEntity))return;
        LivingEntity damaged = (LivingEntity) e.getEntity();
        damageHandler(e);
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof LivingEntity))return;
        damageHandler(e);
    }


    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        deathHandler(e);
    }

    private void damageHandler(EntityDamageEvent e){
        if (!(e.getEntity() instanceof LivingEntity))return;
        boolean damageByEntityEvent = e instanceof EntityDamageByEntityEvent;
        LivingEntity damaged = (LivingEntity) e.getEntity();
        LivingEntity attacker = null;
        if (damageByEntityEvent){
            Entity entity = ((EntityDamageByEntityEvent) e).getDamager();
            if (entity instanceof LivingEntity){
                attacker = (LivingEntity) entity;
            }else return;
        }
        EntityDamageEvent.DamageCause damageCause = e.getCause();
        MobHead damagedHead = null;
        MobHead attackerHead = null;
        if (damaged.getEquipment() != null) damagedHead = HeadUtil.getMobHeadFromHeadItem(damaged.getEquipment().getHelmet());
        if (damageByEntityEvent && attacker.getEquipment() != null) attackerHead = HeadUtil.getMobHeadFromHeadItem(attacker.getEquipment().getHelmet());

        boolean canceled = false;

        if (damageByEntityEvent && attackerHead != null){
            EntityType headType = attackerHead.getEntityType();
            if (!damaged.isDead()){damaged.addPotionEffects(AfflictedHeadEffects.getPotionEffects(headType));}
            switch (headType){
                default -> {}
                case PLAYER -> {}

                case ZOMBIE -> {}
                case SKELETON -> {}
                case CREEPER -> {}
                case WITHER_SKELETON -> {}
                case ENDER_DRAGON -> {}

                case COW -> {}
                case PIG -> {}
                case CHICKEN -> {}
                case WOLF -> {}
                case DONKEY -> {}
                case MULE -> {}
                case DOLPHIN -> {}
                case COD -> {}
                case SALMON -> {}
                case PUFFERFISH -> {}
                case TROPICAL_FISH -> {}
                case TURTLE -> {}
                case STRIDER -> {}
                case GOAT -> {}
                case SQUID -> {}
                case BEE -> {}
                case BAT -> {}
                case OCELOT -> {}
                case SNOWMAN -> {}
                case PANDA -> {}
                case POLAR_BEAR -> {}
                case SKELETON_HORSE -> {}
                case ZOMBIE_HORSE -> {}
                case WANDERING_TRADER -> {}
                case IRON_GOLEM -> {}
                case GLOW_SQUID -> {}
                case ALLAY -> {}
                case TADPOLE -> {}

                case SILVERFISH -> {}
                case STRAY -> {}
                case SHULKER -> {}
                case PHANTOM -> {}
                case HUSK -> {}
                case DROWNED -> {}
                case HOGLIN -> {}
                case ZOGLIN -> {}
                case PIGLIN -> {}
                case PIGLIN_BRUTE -> {}
                case WITCH -> {}
                case GUARDIAN -> {}
                case RAVAGER -> {}
                case VEX -> {}
                case EVOKER -> {}
                case SPIDER -> {}
                case ENDERMAN -> {}
                case GHAST -> {}
                case BLAZE -> {}
                case CAVE_SPIDER -> {}
                case MAGMA_CUBE -> {}
                case ZOMBIFIED_PIGLIN -> {}
                case SLIME -> {}
                case ENDERMITE -> {}
                case PILLAGER -> {}
                case VINDICATOR -> {}
                case ILLUSIONER -> {}

                case ELDER_GUARDIAN -> {}
                case WITHER -> {}
                case WARDEN -> {}

                case RABBIT -> {}
                case AXOLOTL -> {}
                case CAT -> {}
                case HORSE -> {}
                case LLAMA -> {}
                case TRADER_LLAMA -> {}
                case PARROT -> {}
                case FOX -> {}
                case SHEEP -> {}
                case MUSHROOM_COW -> {}
                case FROG -> {}
                case VILLAGER -> {}
                case ZOMBIE_VILLAGER -> {}
            }
        }
        if (damagedHead != null){
            EntityType headType = damagedHead.getEntityType();
            switch (headType){
                default -> {}
                case PLAYER -> {}

                case ZOMBIE -> {}
                case SKELETON -> {}
                case CREEPER -> {}
                case WITHER_SKELETON -> {if (damageCause.equals(EntityDamageEvent.DamageCause.WITHER)) canceled = true;}
                case ENDER_DRAGON -> {}

                case COW -> {}
                case PIG -> {}
                case CHICKEN -> {}
                case WOLF -> {}
                case DONKEY -> {}
                case MULE -> {}
                case DOLPHIN -> {}
                case COD -> {}
                case SALMON -> {}
                case PUFFERFISH -> {}
                case TROPICAL_FISH -> {}
                case TURTLE -> {}
                case STRIDER -> {}
                case GOAT -> {}
                case SQUID -> {}
                case BEE -> {}
                case BAT -> {}
                case OCELOT -> {}
                case SNOWMAN -> {}
                case PANDA -> {}
                case POLAR_BEAR -> {}
                case SKELETON_HORSE -> {}
                case ZOMBIE_HORSE -> {}
                case WANDERING_TRADER -> {}
                case IRON_GOLEM -> {}
                case GLOW_SQUID -> {}
                case ALLAY -> {}
                case TADPOLE -> {}

                case SILVERFISH -> {}
                case STRAY -> {}
                case SHULKER -> {}
                case PHANTOM -> {}
                case HUSK -> {}
                case DROWNED -> {}
                case HOGLIN -> {}
                case ZOGLIN -> {}
                case PIGLIN -> {}
                case PIGLIN_BRUTE -> {}
                case WITCH -> {}
                case GUARDIAN -> {}
                case RAVAGER -> {}
                case VEX -> {}
                case EVOKER -> {}
                case SPIDER -> {}
                case ENDERMAN -> {}
                case GHAST -> {}
                case BLAZE -> {}
                case CAVE_SPIDER -> {}
                case MAGMA_CUBE -> {}
                case ZOMBIFIED_PIGLIN -> {}
                case SLIME -> {}
                case ENDERMITE -> {}
                case PILLAGER -> {}
                case VINDICATOR -> {}
                case ILLUSIONER -> {}

                case ELDER_GUARDIAN -> {}
                case WITHER -> {if (damageCause.equals(EntityDamageEvent.DamageCause.WITHER)) canceled = true;}
                case WARDEN -> {}

                case RABBIT -> {}
                case AXOLOTL -> {}
                case CAT -> {}
                case HORSE -> {}
                case LLAMA -> {}
                case TRADER_LLAMA -> {}
                case PARROT -> {}
                case FOX -> {}
                case SHEEP -> {}
                case MUSHROOM_COW -> {}
                case FROG -> {}
                case VILLAGER -> {}
                case ZOMBIE_VILLAGER -> {}
            }
        }

        e.setCancelled(canceled);

        boolean playSound = damagedHead != null && !e.isCancelled() && e.getFinalDamage() > 0;
        if (playSound) AVFX.playHurtSound(damaged);
    }

    private void deathHandler(EntityDeathEvent e){
        if (e.getEntity().getEquipment() == null)return;
        MobHead mobHead = HeadUtil.getMobHeadFromEntity(e.getEntity());
        if (mobHead == null)return;

        switch (mobHead.getEntityType()){
            default -> {}
            case PLAYER -> {}

            case ZOMBIE -> {}
            case SKELETON -> {}
            case CREEPER -> {}
            case WITHER_SKELETON -> {}
            case ENDER_DRAGON -> {}

            case COW -> {}
            case PIG -> {}
            case CHICKEN -> {}
            case WOLF -> {}
            case DONKEY -> {}
            case MULE -> {}
            case DOLPHIN -> {}
            case COD -> {}
            case SALMON -> {}
            case PUFFERFISH -> {}
            case TROPICAL_FISH -> {}
            case TURTLE -> {}
            case STRIDER -> {}
            case GOAT -> {}
            case SQUID -> {}
            case BEE -> {}
            case BAT -> {}
            case OCELOT -> {}
            case SNOWMAN -> {}
            case PANDA -> {}
            case POLAR_BEAR -> {}
            case SKELETON_HORSE -> {}
            case ZOMBIE_HORSE -> {}
            case WANDERING_TRADER -> {}
            case IRON_GOLEM -> {}
            case GLOW_SQUID -> {}
            case ALLAY -> {}
            case TADPOLE -> {}

            case SILVERFISH -> {}
            case STRAY -> {}
            case SHULKER -> {}
            case PHANTOM -> {}
            case HUSK -> {}
            case DROWNED -> {}
            case HOGLIN -> {}
            case ZOGLIN -> {}
            case PIGLIN -> {}
            case PIGLIN_BRUTE -> {}
            case WITCH -> {}
            case GUARDIAN -> {}
            case RAVAGER -> {}
            case VEX -> {}
            case EVOKER -> {}
            case SPIDER -> {}
            case ENDERMAN -> {}
            case GHAST -> {}
            case BLAZE -> {}
            case CAVE_SPIDER -> {}
            case MAGMA_CUBE -> {}
            case ZOMBIFIED_PIGLIN -> {}
            case SLIME -> {}
            case ENDERMITE -> {}
            case PILLAGER -> {}
            case VINDICATOR -> {}
            case ILLUSIONER -> {}

            case ELDER_GUARDIAN -> {}
            case WITHER -> {}
            case WARDEN -> {}

            case RABBIT -> {}
            case AXOLOTL -> {}
            case CAT -> {}
            case HORSE -> {}
            case LLAMA -> {}
            case TRADER_LLAMA -> {}
            case PARROT -> {}
            case FOX -> {}
            case SHEEP -> {}
            case MUSHROOM_COW -> {}
            case FROG -> {}
            case VILLAGER -> {}
            case ZOMBIE_VILLAGER -> {}
        }
        AVFX.playDeathSound(e.getEntity());
    }


}
