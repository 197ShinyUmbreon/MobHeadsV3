package io.github.shinyumbreon197.mobheadsv3.head;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.itemStack.HeadItemStack;
import io.github.shinyumbreon197.mobheadsv3.tool.Serializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.util.ArrayList;
import java.util.List;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class PlayerHead {

    public static void registerOnlinePlayers(){
        List<Player> toRegister = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player:toRegister){
            if (debug) System.out.println("Checking Register for " + player.getDisplayName()); //debug
            if (!MobHead.isUUIDRegistered(player.getUniqueId())){
                if (debug) System.out.println("New player. Registering " + player.getDisplayName()); //debug
                writeNewPlayerToFile(player);
            }
        }
    }

    public static void writeNewPlayerToFile(Player player){
        MobHead newPlayerHead = buildPlayerHead(player);
        MobHead.addMobHead(newPlayerHead);
        MobHeadsV3.playerRegistry.addToRegistry(Serializer.serializeItemStack(newPlayerHead.getHeadItemStack()));
    }

    public static void registerPlayerHistory(){
        MobHeadsV3.playerRegistry.reloadPlayerRegistry();
        MobHeadsV3.playerRegistry.updateRegistryFromFile();
        for (String string:MobHeadsV3.playerRegistry.getRegistry()){
            MobHead mobHead = rebuildPlayerHead(Serializer.deserializeItemStack(string));
            System.out.println("Registering "+mobHead.getDisplayName()+", "+mobHead.getUuid().toString()+"...");
            MobHead.addMobHead(mobHead);
        }
    }

    public static MobHead buildPlayerHead(Player player){
        String headName = player.getName()+"'s Head";
        ItemStack playerHead = buildPlayerHeadItem(player);
        return new MobHead(player.getUniqueId(),headName,EntityType.PLAYER,playerHead,null,List.of());
    }

    public static ItemStack buildPlayerHeadItem(Player player){
        String headName = player.getName()+"'s Head";
        return HeadItemStack.customHead(headName,player.getUniqueId(),player.getPlayerProfile(), List.of());
    }

    public static MobHead rebuildPlayerHead(ItemStack headItem){
        MobHead mobHead = null;
        SkullMeta skullMeta = (SkullMeta) headItem.getItemMeta();
        if (skullMeta != null){
            PlayerProfile pp = skullMeta.getOwnerProfile();
            if (pp != null){
                String headName = pp.getName()+"'s Head";
                mobHead = new MobHead(pp.getUniqueId(),headName,EntityType.PLAYER,headItem,null,List.of());
            }
        }
        return mobHead;
    }

}
