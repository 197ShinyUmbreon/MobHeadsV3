package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.effect.WornEffects;
import io.github.shinyumbreon197.mobheadsv3.effect.WornMechanics;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class ScheduledEvents {

    private static final List<LivingEntity> wearingList = new ArrayList<>();
    //private static final List<LivingEntity> notWearingList = new ArrayList<>();
    private static final List<LivingEntity> wasWearingList = new ArrayList<>();
    private static final List<LivingEntity> noLongerWearing = new ArrayList<>();

    private static void debug(){
        if (wearingList.size() != 0){
            System.out.println(MobHeadsV3.namePlain("Entities with Heads equipped: "+wearingList.size()));
            for (LivingEntity wearing:wearingList){
                MobHead mobHead = HeadUtil.getMobHeadFromEntity(wearing);
                System.out.println(MobHeadsV3.namePlain(
                        wearing.getType()+" "+
                        wearing.getEntityId()+": "+
                        mobHead.getEntityType()+", "+
                        mobHead.getHeadItem().getType()+", "+
                        mobHead.getName()+", "+
                        mobHead.getVariant()
                    ));
            }
        }

    }
    public static void run10TickEvents(){
        updateWearingLists();
        runHeadedEffects();
        runCleanupEffects();


        //debug();
    }

    public static void updateWearingLists(){
        wasWearingList.clear();
        wasWearingList.addAll(wearingList);
        wearingList.clear();
        //notWearingList.clear();
        noLongerWearing.clear();
        List<LivingEntity> entities = new ArrayList<>();
        for (World world: Bukkit.getWorlds()){
            entities.addAll(world.getLivingEntities());
        }
        for (LivingEntity livingEntity:entities){
            if (livingEntity.getEquipment() == null || !HeadUtil.isMobHead(livingEntity.getEquipment().getHelmet())){
                //notWearingList.add(livingEntity);
                continue;
            }
            //System.out.println("MOBHEAD FOUND! "+livingEntity.getEntityId()); //debug
            wearingList.add(livingEntity);
        }
        noLongerWearing.addAll(wasWearingList);
        noLongerWearing.removeAll(wearingList);
    }

    private static void runHeadedEffects(){
        WornEffects.applyNewPotionEffects(wearingList);
        WornMechanics.runHeadTickMechanics(wearingList);
    }

    private static void runCleanupEffects(){
        WornEffects.removeInfinitePotionEffects(noLongerWearing);
    }


}
