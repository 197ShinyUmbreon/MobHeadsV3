package io.github.shinyumbreon197.mobheadsv3.event.main;

import io.github.shinyumbreon197.mobheadsv3.Config;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.Packets;
import io.github.shinyumbreon197.mobheadsv3.entity.Summon;
import io.github.shinyumbreon197.mobheadsv3.event.relay.CreatureTickRelay;
import io.github.shinyumbreon197.mobheadsv3.function.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class MainThread {

    private static long lastTime = 0; //debug function timer
    private static void runTimer(boolean start){ //debug function
        World world = Bukkit.getWorld("world");
        if (world == null)return;
        long time = world.getFullTime();
        if (start){
            if (lastTime != 0){
                System.out.println("on5Ticks() Thread skipped!");
                lastTime = 0;
            }
            System.out.println("on5Ticks() Started: " + time);
            lastTime = time;
        }else{
            System.out.println("on5Ticks() Ended: " + time);
            long difference = time - lastTime;
            System.out.println("on5Ticks() Rough run time: " + difference + " ticks");
            lastTime = 0;
        }
    }

    private static int threadSkips = 0;
    private static boolean threadActive = false;

    public static void on5Ticks(){
        //if (debug) runTimer(true);
        //if (debug) System.out.println("threadActive: (Skipping tick?) " + threadActive);
        if (threadActive){
            threadSkips++;
            MobHeadsV3.cOut("[WARNING]" + " MAIN THREAD SKIPPED! " + "Total occurrences since last reload: " + threadSkips);
            return;
        }
        //if (debug) System.out.println("on5Ticks()\nheadWearersMap: " + headWearersMap + "\nNoLongerWearingMap: " + noLongerWearingMap); //debug
        threadActive = true;
        new BukkitRunnable(){
            @Override
            public void run() {
                updatePlayerList();
                runHeadedEffects();
                Summon.watchSummons();
                CreatureEvents.chestedWatchHolders();
                //if (debug) runTimer(false);
                threadActive = false;
            }
        }.run();
    }

    private static final Map<LivingEntity, UUID> headWearersMap = new HashMap<>();

    private static void runHeadedEffects(){
        List<LivingEntity> scanned = new ArrayList<>();
        for (World world:Bukkit.getWorlds()){
            scanned.addAll(world.getLivingEntities());
        }
        Map<LivingEntity,MobHead> wearing = new HashMap<>();
        List<LivingEntity> notWearing = new ArrayList<>();
        for (LivingEntity livEnt:scanned){
            MobHead wornHead = MobHead.getMobHeadWornByEntity(livEnt);
            if (wornHead == null){
                notWearing.add(livEnt);
            }else{
                //if (debug) System.out.println("runHeadedEffects() wearing.put -> " + livEnt.getName() + " " + wornHead.getHeadName()); //debug
                wearing.put(livEnt,wornHead);
            }
        }
        Map<LivingEntity,UUID> wasWearing = new HashMap<>(headWearersMap);
        List<LivingEntity> removeEffects = new ArrayList<>();
        if (!Config.headEffects){
            removeEffects.addAll(wasWearing.keySet());
        }else{
            for (LivingEntity wasLivEnt:wasWearing.keySet()){
                if (wearing.containsKey(wasLivEnt)){
                    MobHead wasHead = MobHead.getMobHeadFromUUID(wasWearing.get(wasLivEnt));
                    MobHead wornHead = MobHead.getMobHeadWornByEntity(wasLivEnt);
                    if (wornHead == null || !wornHead.equals(wasHead)){
                        removeEffects.add(wasLivEnt);
                    }
                }else if(notWearing.contains(wasLivEnt)) {
                    removeEffects.add(wasLivEnt);
                }
            }
        }
        for (LivingEntity removeEffect:removeEffects){
            PotionEffectManager.removeInfinitePotionEffects(removeEffect);
            MobHead mobHead = MobHead.getMobHeadFromUUID(wasWearing.get(removeEffect));
            PotionEffectManager.addHeadRemovalPotionEffects(removeEffect, mobHead);
            headRemovalEffects(removeEffect, mobHead);
            //Decoy.removeDecoyFromCreature(removeEffect);
        }
        headWearersMap.clear();
        for (LivingEntity wear:wearing.keySet()){
            MobHead mobHead = wearing.get(wear);
            assert mobHead != null;
            headWearersMap.put(wear, mobHead.getUuid());
            if (Config.headEffects)headWearEffects(wear, mobHead);
        }
    }
    public static void headRemovalEffects(LivingEntity target, MobHead mobHead){
        if (debug) System.out.println("headRemovalEffects() target: " + target.getName() + " MobHead: " + mobHead.getHeadName()); //debug
        if (mobHead == null)return;
        EntityType headType = mobHead.getEntityType();
        AttributeManager.setAttributes(target,headType,false);
        Summon.resetSummoner(target);
        switch (headType){
            case BAT, WARDEN, FROG -> {
                if (target instanceof Player) Packets.removeGlow((Player) target);
            }
            case SLIME, MAGMA_CUBE -> {
                CreatureEvents.slimeReset(target);
            }
            case STRIDER -> CreatureEvents.striderReplaceReset(target);
            case ARMADILLO -> CreatureEvents.armadilloResetKnockbackResist(target);
            case CREAKING ->{if (target instanceof Player) CreatureEvents.creakingRemoveHighlightedCreakingHearts((Player) target);}
        }
    }
    private static void headWearEffects(LivingEntity target, MobHead mobHead){
        AttributeManager.setAttributes(target, mobHead.getEntityType(), true);
        PotionEffectManager.updateEffects(target, mobHead);
        CreatureTickRelay.tickRelay(target, mobHead);
    }
    private static void updatePlayerList(){
        for (Player player:Bukkit.getOnlinePlayers()){
            MobHead mobHead = MobHead.getMobHeadWornByEntity(player);
            String listingString;
            String playerName = player.getDisplayName();
            if (mobHead != null){
                String headTypeName;
                if (mobHead.getEntityType().equals(EntityType.PLAYER)){
                    headTypeName = mobHead.getHeadName().replace("'s Head", "");
                }else headTypeName = Util.friendlyEntityTypeName(mobHead.getEntityType());
                listingString = playerName + " (" + headTypeName + ")";
            }else listingString = playerName;
            player.setPlayerListName(listingString);
        }

    }

}
