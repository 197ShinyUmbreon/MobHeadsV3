package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.Config;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class IncrementStatistic implements Listener {

    @EventHandler
    public static void onIncrementStatistic(PlayerStatisticIncrementEvent psie){
        Player player = psie.getPlayer();
        MobHead mobHead = MobHead.getMobHeadWornByEntity(player);
        if (debug) System.out.println("onIncrementStatistic() " + psie.getStatistic().name()); //debug
        if (mobHead == null)return;
        EntityType headType = mobHead.getEntityType();
        switch (psie.getStatistic()){
            case DAMAGE_DEALT -> {
            }
            case DAMAGE_TAKEN -> {
            }
            case DEATHS -> {
            }
            case MOB_KILLS -> {
            }
            case PLAYER_KILLS -> {
            }
            case FISH_CAUGHT -> {
            }
            case ANIMALS_BRED -> {
            }
            case LEAVE_GAME -> {
            }
            case JUMP -> {onJump(player, headType);}
            case DROP_COUNT -> {
            }
            case DROP -> {
            }
            case PICKUP -> {
            }
            case PLAY_ONE_MINUTE -> {
            }
            case TOTAL_WORLD_TIME -> {
            }
            case WALK_ONE_CM -> {
            }
            case WALK_ON_WATER_ONE_CM -> {
            }
            case FALL_ONE_CM -> {
            }
            case SNEAK_TIME -> {
            }
            case CLIMB_ONE_CM -> {
            }
            case FLY_ONE_CM -> {
            }
            case WALK_UNDER_WATER_ONE_CM -> {
            }
            case MINECART_ONE_CM -> {
            }
            case BOAT_ONE_CM -> {
            }
            case PIG_ONE_CM -> {
            }
            case HORSE_ONE_CM -> {
            }
            case SPRINT_ONE_CM -> {
            }
            case CROUCH_ONE_CM -> {
            }
            case AVIATE_ONE_CM -> {
            }
            case MINE_BLOCK -> {
            }
            case USE_ITEM -> {
            }
            case BREAK_ITEM -> {
            }
            case CRAFT_ITEM -> {
            }
            case KILL_ENTITY -> {
            }
            case ENTITY_KILLED_BY -> {
            }
            case TIME_SINCE_DEATH -> {
            }
            case TALKED_TO_VILLAGER -> {
            }
            case TRADED_WITH_VILLAGER -> {
            }
            case CAKE_SLICES_EATEN -> {
            }
            case CAULDRON_FILLED -> {
            }
            case CAULDRON_USED -> {
            }
            case ARMOR_CLEANED -> {
            }
            case BANNER_CLEANED -> {
            }
            case BREWINGSTAND_INTERACTION -> {
            }
            case BEACON_INTERACTION -> {
            }
            case DROPPER_INSPECTED -> {
            }
            case HOPPER_INSPECTED -> {
            }
            case DISPENSER_INSPECTED -> {
            }
            case NOTEBLOCK_PLAYED -> {
            }
            case NOTEBLOCK_TUNED -> {
            }
            case FLOWER_POTTED -> {
            }
            case TRAPPED_CHEST_TRIGGERED -> {
            }
            case ENDERCHEST_OPENED -> {
            }
            case ITEM_ENCHANTED -> {
            }
            case RECORD_PLAYED -> {
            }
            case FURNACE_INTERACTION -> {
            }
            case CRAFTING_TABLE_INTERACTION -> {
            }
            case CHEST_OPENED -> {
            }
            case SLEEP_IN_BED -> {
            }
            case SHULKER_BOX_OPENED -> {
            }
            case TIME_SINCE_REST -> {
            }
            case SWIM_ONE_CM -> {
            }
            case DAMAGE_DEALT_ABSORBED -> {
            }
            case DAMAGE_DEALT_RESISTED -> {
            }
            case DAMAGE_BLOCKED_BY_SHIELD -> {
            }
            case DAMAGE_ABSORBED -> {
            }
            case DAMAGE_RESISTED -> {
            }
            case CLEAN_SHULKER_BOX -> {
            }
            case OPEN_BARREL -> {
            }
            case INTERACT_WITH_BLAST_FURNACE -> {
            }
            case INTERACT_WITH_SMOKER -> {
            }
            case INTERACT_WITH_LECTERN -> {
            }
            case INTERACT_WITH_CAMPFIRE -> {
            }
            case INTERACT_WITH_CARTOGRAPHY_TABLE -> {
            }
            case INTERACT_WITH_LOOM -> {
            }
            case INTERACT_WITH_STONECUTTER -> {
            }
            case BELL_RING -> {
            }
            case RAID_TRIGGER -> {
            }
            case RAID_WIN -> {
            }
            case INTERACT_WITH_ANVIL -> {
            }
            case INTERACT_WITH_GRINDSTONE -> {
            }
            case TARGET_HIT -> {
            }
            case INTERACT_WITH_SMITHING_TABLE -> {
            }
            case STRIDER_ONE_CM -> {
            }
        }
    }

    private static void onJump(Player player, EntityType headType){
        boolean sneaking = player.isSneaking();
        switch (headType){
            case FROG -> {if (sneaking) CreatureEvents.frogLeap(player);}
            case RABBIT -> {if (sneaking) CreatureEvents.rabbitLeap(player);}
            case SHULKER -> {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        if (sneaking) CreatureEvents.startShulkerLevitation(player);
                    }
                }.runTaskLater(MobHeadsV3.getPlugin(),1);
            }
            case ENDER_DRAGON -> {if (sneaking) CreatureEvents.enderDragonElytraTakeoff(player,true);}
            case GOAT -> {if (sneaking) CreatureEvents.goatTakeOff(player, true);}
            case GHAST -> {if (sneaking) CreatureEvents.ghastFloat(player);}
            case SLIME, MAGMA_CUBE -> {if (sneaking) CreatureEvents.slimeJump(player);}
            case CAMEL -> {if (sneaking) CreatureEvents.camelDash(player);}
            //case FOX -> {if (sneaking) CreatureEvents.foxPounce(player);}
            case BREEZE -> {if (sneaking) CreatureEvents.breezeSneakJump(player);}
        }
    }

}
