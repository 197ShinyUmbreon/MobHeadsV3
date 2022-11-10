package io.github.shinyumbreon197.mobheadsv3.tool;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.util.UUID;

public class HeadItem {

    public static ItemStack customHead(String headName, UUID uuid, URL texture) {
        PlayerProfile pp = Bukkit.createPlayerProfile(uuid,headName);
        PlayerTextures pt = pp.getTextures();
        pt.setSkin(texture);
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        assert headMeta != null;
        headMeta.setOwnerProfile(pp);
        headMeta.setDisplayName(ChatColor.YELLOW+headName);
        PersistentDataContainer data = headMeta.getPersistentDataContainer();
        data.set(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING, uuid.toString());

        head.setItemMeta(headMeta);
        return head;
    }

    public static UUID getHeadUUID(ItemStack input){
        if (input == null || !input.getType().equals(Material.PLAYER_HEAD))return null;
        SkullMeta skullMeta = (SkullMeta) input.getItemMeta();
        if (skullMeta == null)return null;
        PersistentDataContainer data = skullMeta.getPersistentDataContainer();
        String uuidString = data.get(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING);
        if (uuidString == null)return null;
        return UUID.fromString(uuidString);
    }

    public static boolean isMobHead(ItemStack input){
        if (input == null || !input.getType().equals(Material.PLAYER_HEAD))return false;
        SkullMeta skullMeta = (SkullMeta) input.getItemMeta();
        if (skullMeta == null)return false;
        PersistentDataContainer data = skullMeta.getPersistentDataContainer();
        return data.has(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING);
    }

    public static EntityType getHeadEntityType(ItemStack input){
        if (!isMobHead(input))return null;
        UUID uuid = getHeadUUID(input);
        return HeadData.entityTypeLookupMap.get(uuid);
    }

    public static String getHeadVariant(ItemStack input){
        if (!isMobHead(input))return null;
        UUID uuid = getHeadUUID(input);
        return HeadData.variantLookupMap.get(uuid);
    }


}
