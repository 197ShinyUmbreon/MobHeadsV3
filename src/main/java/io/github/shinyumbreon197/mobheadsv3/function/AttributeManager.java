package io.github.shinyumbreon197.mobheadsv3.function;

import io.github.shinyumbreon197.mobheadsv3.data.Groups;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class AttributeManager {

    public static void setAttributes(LivingEntity target, EntityType headType, boolean equip){
        if (Groups.isEquine(headType)){
            AttributeInstance ai = target.getAttribute(Attribute.GENERIC_STEP_HEIGHT);
            if (ai == null)return;
            if (equip){
                ai.setBaseValue(1.2);
            }else ai.setBaseValue(ai.getDefaultValue());
        }
    }

}
