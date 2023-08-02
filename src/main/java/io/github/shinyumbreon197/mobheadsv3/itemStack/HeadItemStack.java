package io.github.shinyumbreon197.mobheadsv3.itemStack;

import io.github.shinyumbreon197.mobheadsv3.data.Key;
import io.github.shinyumbreon197.mobheadsv3.function.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.util.*;

public class HeadItemStack {

    private static Map<EntityType, Material> vanillaHeadMap;
    private static Map<EntityType, Material> getVanillaHeadMap(){
        if (vanillaHeadMap != null) return vanillaHeadMap;
        vanillaHeadMap = buildVanillaHeadMap();
        return getVanillaHeadMap();
    }
    private static Map<EntityType,Material> buildVanillaHeadMap(){
        Map<EntityType,Material> map = new HashMap<>();
        map.put(EntityType.CREEPER, Material.CREEPER_HEAD);
        map.put(EntityType.ENDER_DRAGON, Material.DRAGON_HEAD);
        map.put(EntityType.SKELETON, Material.SKELETON_SKULL);
        map.put(EntityType.WITHER_SKELETON, Material.WITHER_SKELETON_SKULL);
        map.put(EntityType.ZOMBIE, Material.ZOMBIE_HEAD);
        return map;
    }
    public static ItemStack customVanillaHead(EntityType headType, List<String> lore, UUID uuid){
        return customVanillaHead(headType,lore,uuid,new HashMap<>());
    }
    public static ItemStack customVanillaHead(EntityType headType, List<String> lore, UUID uuid, Map<Enchantment,Integer> enchantments){
        ItemStack itemStack = new ItemStack(getVanillaHeadMap().get(headType));
        ItemMeta itemMeta = addEnchantments(itemStack.getItemMeta(), enchantments);
        assert itemMeta != null;
        itemMeta.setLore(colorLore(lore));
        String name = Util.friendlyMaterialName(itemStack.getType());
        itemMeta.setDisplayName(ChatColor.YELLOW + name);
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        data.set(Key.headUUID, PersistentDataType.STRING, uuid.toString());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static ItemMeta addLoreToHeadMeta(ItemMeta itemMeta, List<String> lore){
        itemMeta.setLore(colorLore(lore));
        return itemMeta;
    }

    private static List<String> colorLore(List<String> lore){
        List<String> newLore = new ArrayList<>();
        for (String string:lore){
            newLore.add(ChatColor.DARK_PURPLE + string);
        }
        return newLore;
    }
    public static ItemStack customHead(String headName, UUID uuid, URL texture, List<String> lore){
        return customHead(headName,uuid,texture,lore,new HashMap<>());
    }
    public static ItemStack customHead(String headName, UUID uuid, URL texture, List<String> lore, Map<Enchantment, Integer> enchantments) {
        PlayerProfile pp = customPlayerProfile(headName, uuid, texture);
        return customHead(headName, uuid, pp, lore, enchantments);
    }
    public static ItemStack customHead(String headName, UUID uuid, PlayerProfile pp, List<String> lore){
        return customHead(headName,uuid,pp,lore,new HashMap<>());
    }
    public static ItemStack customHead(String headName, UUID uuid, PlayerProfile pp, List<String> lore, Map<Enchantment,Integer> enchantments){
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta = (SkullMeta) addEnchantments(headMeta, enchantments);
        assert headMeta != null;
        headMeta.setOwnerProfile(pp);
        headMeta.setDisplayName(ChatColor.YELLOW+headName);
        headMeta = (SkullMeta) addLoreToHeadMeta(headMeta,lore);
        PersistentDataContainer data = headMeta.getPersistentDataContainer();
        data.set(Key.headUUID, PersistentDataType.STRING, uuid.toString());
        head.setItemMeta(headMeta);
        return head;
    }

    private static ItemMeta addEnchantments(ItemMeta headMeta, Map<Enchantment,Integer> enchantments){
        for (Enchantment enchantment:enchantments.keySet()){
            headMeta.addEnchant(enchantment,enchantments.get(enchantment), true);
        }
        //headMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return headMeta;
    }

    private static PlayerProfile customPlayerProfile(String headName, UUID uuid, URL texture){
        PlayerProfile pp = Bukkit.createPlayerProfile(uuid,headName);
        PlayerTextures pt = pp.getTextures();
        pt.setSkin(texture);
        return pp;
    }

}
