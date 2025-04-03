package io.github.shinyumbreon197.mobheadsv3.function;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.*;

public class EnvironmentEffects {

    private static final boolean localDebug = true;

    public enum EnvironmentEffect {
        WET,
        SAND_TOED,
        SINGED,
        SUNLIT,
        MOONLIT
    }
    static class Effect{
        EnvironmentEffect effect;
        int duration;
        int amplifier;
        Effect(EnvironmentEffect effect, int duration, int amplifier){
            this.effect = effect;
            this.duration = duration;
            this.amplifier = amplifier;
        }

        public EnvironmentEffect getEffect() {return effect;}
        public void setEffect(EnvironmentEffect effect) {this.effect = effect;}
        public int getDuration() {return duration;}
        public void setDuration(int duration) {this.duration = duration;}
        public int getAmplifier() {return amplifier;}
        public void setAmplifier(int amplifier) {this.amplifier = amplifier;}
    }

    public static void scanEntities(){
        for (World world:Bukkit.getWorlds()){
            for (Entity entity: world.getEntities()){
                if (!(entity instanceof LivingEntity))continue;
                Block underneath = entity.getLocation().getBlock().getRelative(BlockFace.DOWN);
                if (underneath.getType().toString().contains("SAND")){
                    addEnvironmentalEffect(entity, EnvironmentEffect.SAND_TOED);
                }
            }
        }
    }

    private static final Map<UUID, Set<Effect>> entityIDCurrentEffectsMap = new HashMap<>();

    public static void runEffects(){
        Set<UUID> removeIDs = new HashSet<>();
        Set<UUID> entityIDs = new HashSet<>(entityIDCurrentEffectsMap.keySet());
        for (UUID entityID:entityIDs){
            Entity entity = Bukkit.getEntity(entityID);
            if (entity == null){
                //entityIDs.remove(entityID);
                removeIDs.add(entityID);
                continue;
            }
            Set<Effect> effects = entityIDCurrentEffectsMap.get(entityID);
            if (effects == null || effects.size() == 0){
                //entityIDs.remove(entityID);
                removeIDs.add(entityID);
                continue;
            }
            Set<Effect> removeEffects = new HashSet<>();
            for (Effect effect:effects){
                if (!runEffect(effect, entity)){
                    //effects.remove(effect);
                    removeEffects.add(effect);
                    continue;
                }
            }
            effects.removeAll(removeEffects);
            entityIDCurrentEffectsMap.put(entityID,effects);
        }
        for (UUID removeID:removeIDs) entityIDCurrentEffectsMap.remove(removeID);
    }

    private static boolean runEffect(Effect effect, Entity entity){
        int duration = effect.getDuration();
        if (duration <= 0) return false;
        effect.setDuration(duration - 1);
        switch (effect.getEffect()){
            case SAND_TOED -> runSandToedEffect(entity);
        }
        return true;
    }

    public static boolean addEnvironmentalEffect(Entity entity, EnvironmentEffect environmentEffect){ //add duration & amplifier
        if (hasEnvironmentEffect(entity, environmentEffect))return false; // redo this
        UUID entityID = entity.getUniqueId();
        Effect effect = new Effect(environmentEffect, 20, 0);
        Set<Effect> effects = entityIDCurrentEffectsMap.getOrDefault(entityID, new HashSet<>());
        effects.add(effect);
        entityIDCurrentEffectsMap.put(entityID, effects);
        return true;
    }

    public static boolean hasEnvironmentEffect(Entity entity, EnvironmentEffect environmentEffect){
        Set<Effect> effects = entityIDCurrentEffectsMap.get(entity.getUniqueId());
        if (effects == null || effects.size() == 0)return false;
        for (Effect effect:effects){
            if (effect.getEffect().equals(environmentEffect))return true;
        }
        return false;
    }

    private static void runSandToedEffect(Entity entity){
        if (localDebug) System.out.println(entity.getUniqueId() + " " + entity.getType() +  " My toes are sandy.");
    }


}
