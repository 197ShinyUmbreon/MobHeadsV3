package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.effect.WornMechanics;
import io.github.shinyumbreon197.mobheadsv3.entity.Summon;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class EntityTargetEntityEvents implements Listener {

    @EventHandler
    public static void onTargetEntity(EntityTargetLivingEntityEvent e){
        if (!(e.getEntity() instanceof Mob))return;
        Mob targeting = (Mob) e.getEntity();
        LivingEntity target = e.getTarget();
        if (target == null || target.isDead())return;
        if (Summon.isSummon(targeting)){
            Entity summonOwner = Summon.getSummonOwner(targeting);
            if (summonOwner == null)return;
            e.setCancelled(summonOwner.equals(targeting));
            return;
        }
        MobHead mobHead = HeadUtil.getMobHeadFromEntity(target);
        if (mobHead == null)return;
        WornMechanics.onTargetWearer(e, target, targeting, mobHead);
    }





}
