package io.github.shinyumbreon197.mobheadsv3.tool;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class StringBuilder {

    //Public Methods ----------------------------------------------------------------------

    public static String toSimplifiedString(String input){
        String simplified = input.toLowerCase();
        simplified = simplified.replace(" ", "_").replace("&", "_and_");
        return simplified;
    }

    public static String getPlayerListName(MobHead mobHead, Player player){
        String newName;
        if (mobHead == null){
            newName = player.getName();
        }else{
            EntityType entityType = mobHead.getEntityType();
            if (entityType.equals(EntityType.PLAYER)){
                String headName = mobHead.getHeadName().replace("'s Head", "");
                newName = player.getName() + " (" + headName + ")";
            }else{
                newName = player.getName() + " (" + friendlyEntityTypeName(entityType) + ")";
            }
        }
        return newName;
    }

    //Private Methods -------------------------------------------------------------------------

    private static String friendlyEntityTypeName(EntityType entityType){
        String original = entityType.name();
        original = original.replace('_', ' ');
        java.lang.StringBuilder builder = new java.lang.StringBuilder(original.length());
        int index = 0;
        for (Character c:original.toCharArray()){
            if ((c == ' ') | index == 0){
                builder.append(c);
                index++;
                continue;
            }
            if (builder.charAt(builder.length()-1) == (' ')){
                builder.append(c);
                index++;
                continue;
            }
            c = Character.toLowerCase(c);
            builder.append(c);
            index++;
        }
        return builder.toString();
    }

}
