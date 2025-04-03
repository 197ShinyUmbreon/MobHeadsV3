package io.github.shinyumbreon197.mobheadsv3.function;

import io.github.shinyumbreon197.mobheadsv3.data.Groups;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class AttributeManager {

    public static void setAttributes(LivingEntity target, EntityType headType, boolean enable){
        if (Groups.isEquine(headType)){
            AttributeInstance aiStepHeight = target.getAttribute(Attribute.STEP_HEIGHT);
            if (aiStepHeight == null)return;
            if (enable){
                aiStepHeight.setBaseValue(1.2);
            }else aiStepHeight.setBaseValue(aiStepHeight.getDefaultValue());
        }
    }

}
