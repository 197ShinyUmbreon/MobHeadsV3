package io.github.shinyumbreon197.mobheadsv3.tool;

import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;

public class StringBuilder {

    private static final MobHeadsV3 plugin = MobHeadsV3.getPlugin();

    public static String toSimplifiedString(String input){
        String simplified = input.toLowerCase();
        simplified = simplified.replace(" ", "_").replace("&", "_and_");
        return simplified;
    }

}