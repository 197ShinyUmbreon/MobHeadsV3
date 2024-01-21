package io.github.shinyumbreon197.mobheadsv3.head;

import io.github.shinyumbreon197.mobheadsv3.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.data.HeadData;
import io.github.shinyumbreon197.mobheadsv3.file.PlayerRegistry;
import io.github.shinyumbreon197.mobheadsv3.itemStack.HeadItemStack;
import io.github.shinyumbreon197.mobheadsv3.tool.Serializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class PlayerHead {

    private static final PlayerRegistry registry = MobHeadsV3.playerRegistry;

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
        registry.addToRegistry(Serializer.serializeItemStack(newPlayerHead.getHeadItemStack()));
    }

    public static void updatePlayerFile(Player player, MobHead oldHead){
        PlayerProfile pp = player.getPlayerProfile();
        String url = pp.getTextures().getSkin().toString();
        if (debug) System.out.println(url + " <--- New URL"); //debug
        SkullMeta skullMeta = (SkullMeta) oldHead.getHeadItemStack().getItemMeta();
        assert skullMeta != null;
        PlayerProfile savedPP = skullMeta.getOwnerProfile();
        if (savedPP != null){
            String savedURL = savedPP.getTextures().getSkin().toString();
            if (debug) System.out.println(savedURL + " <--- Old URL"); //debug
            if (!url.matches(savedURL)){
                MobHeadsV3.cOut(player.getDisplayName() + " has a new skin. Updating head...");
                registry.removeFromRegistry(oldHead);
                MobHead.removeMobHead(player.getUniqueId());
                writeNewPlayerToFile(player);
            }
        }
    }

    public static void registerPlayerHistory(){
        registry.reloadPlayerRegistry();
        registry.updateRegistryFromFile();
        for (String string:registry.getRegistry()){
            MobHead mobHead = rebuildPlayerHead(Serializer.deserializeItemStack(string));
            MobHeadsV3.cOut("Registering "+mobHead.getHeadName()+", "+mobHead.getUuid().toString()+"...");
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
