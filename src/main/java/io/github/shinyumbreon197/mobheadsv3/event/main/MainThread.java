package io.github.shinyumbreon197.mobheadsv3.event.main;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.Packets;
import io.github.shinyumbreon197.mobheadsv3.event.relay.CreatureTickRelay;
import io.github.shinyumbreon197.mobheadsv3.function.PotionEffectManager;
import io.github.shinyumbreon197.mobheadsv3.function.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

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
            MobHeadsV3.cOut(
                    ChatColor.RED + "MAIN THREAD SKIPPED! " +
                    ChatColor.WHITE + "Total occurrences since last reload: " +
                    ChatColor.AQUA + threadSkips
            );
            return;
        }
        //if (debug) System.out.println("on5Ticks()\nheadWearersMap: " + headWearersMap + "\nNoLongerWearingMap: " + noLongerWearingMap); //debug
        new BukkitRunnable(){
            @Override
            public void run() {
                runHeadedEffects();
                threadActive = false;
                //if (debug) runTimer(false);
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
            }else wearing.put(livEnt,wornHead);
        }
        Map<LivingEntity,UUID> wasWearing = new HashMap<>(headWearersMap);
        List<LivingEntity> removeEffects = new ArrayList<>();
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
        for (LivingEntity removeEffect:removeEffects){
            PotionEffectManager.removeInfinitePotionEffects(removeEffect);
            MobHead mobHead = MobHead.getMobHeadFromUUID(wasWearing.get(removeEffect));
            PotionEffectManager.addHeadRemovalPotionEffects(removeEffect, mobHead);
            headRemovalEffects(removeEffect, mobHead);
            if (removeEffect instanceof Player) updatePlayerList(((Player)removeEffect),null);
        }
        headWearersMap.clear();
        for (LivingEntity wear:wearing.keySet()){
            MobHead mobHead = wearing.get(wear);
            assert mobHead != null;
            headWearersMap.put(wear, mobHead.getUuid());
            UUID uuid = mobHead.getUuid();
            PotionEffectManager.updateEffects(wear, uuid);
            CreatureTickRelay.tickRelay(wear, uuid);
            if (wear instanceof Player) updatePlayerList(((Player)wear),MobHead.getMobHeadFromUUID(uuid));
        }
    }

    private static void headRemovalEffects(LivingEntity target, MobHead mobHead){
        if (mobHead == null)return;
        EntityType headType = mobHead.getEntityType();
        switch (headType){
            case BAT, WARDEN -> {
                if (target instanceof Player) Packets.removeGlow((Player) target);
            }
        }
    }

    private static void updatePlayerList(Player player, MobHead mobHead){
        String listingString;
        String playerName = player.getDisplayName();
        if (mobHead != null){
            String headTypeName = Util.friendlyEntityTypeName(mobHead.getEntityType());
            listingString = playerName + " (" + headTypeName + ")";
        }else listingString = playerName;
        player.setPlayerListName(listingString);
    }

}
