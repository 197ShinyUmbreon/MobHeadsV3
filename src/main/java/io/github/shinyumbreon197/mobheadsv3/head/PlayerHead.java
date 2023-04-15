package io.github.shinyumbreon197.mobheadsv3.head;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import io.github.shinyumbreon197.mobheadsv3.tool.Serializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PlayerHead {

    private static final Sound interactSound = Sound.ENTITY_PLAYER_HURT;

    public static void registerOnlinePlayers(){
        List<Player> toRegister = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player:toRegister){
            if (!HeadData.mobHeadByUUID.containsKey(player.getUniqueId())){
                writeNewPlayerToFile(player);
            }
        }
    }

    public static boolean isNewPlayer(Player player){
        return !HeadData.mobHeadByUUID.containsKey(player.getUniqueId());
    }

    public static void writeNewPlayerToFile(Player player){
        MobHead newPlayerHead = buildPlayerHead(player);
        HeadData.registerHead(newPlayerHead);
        MobHeadsV3.playerRegistry.addToRegistry(Serializer.serializeItemStack(newPlayerHead.getHeadItem()));
    }

    public static void registerPlayerHistory(){
        MobHeadsV3.playerRegistry.reloadPlayerRegistry();
        MobHeadsV3.playerRegistry.updateRegistryFromFile();
        for (String string:MobHeadsV3.playerRegistry.getRegistry()){
            MobHead mobHead = rebuildPlayerHead(Serializer.deserializeItemStack(string));
            System.out.println("Registering "+mobHead.getName()+", "+mobHead.getUuid().toString()+"...");
            HeadData.registerHead(mobHead);
        }
    }

    public static MobHead buildPlayerHead(Player player){
        String headName = player.getName()+"'s Head";
        ItemStack playerHead = buildPlayerHeadItem(player);
        return new MobHead(EntityType.PLAYER, null, headName, playerHead, null, player.getUniqueId(), interactSound);
    }

    public static ItemStack buildPlayerHeadItem(Player player){
        String headName = player.getName()+"'s Head";
        return HeadUtil.customHead(headName, player.getUniqueId(), player.getPlayerProfile());
    }

    public static MobHead rebuildPlayerHead(ItemStack headItem){
        MobHead mobHead = new MobHead();
        SkullMeta skullMeta = (SkullMeta) headItem.getItemMeta();
        if (skullMeta != null){
            PlayerProfile pp = skullMeta.getOwnerProfile();
            if (pp != null){
                String headName = pp.getName()+"'s Head";
                mobHead = new MobHead(EntityType.PLAYER, null, headName, headItem, null, pp.getUniqueId(), interactSound);
            }
        }
        return mobHead;
    }

}
