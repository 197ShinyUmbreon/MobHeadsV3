package io.github.shinyumbreon197.mobheadsv3;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class Trophies {

    private static final List<Material> trophyMaterials = new ArrayList<>();

    private static final Map<UUID, ItemStack> trophyUUIDs = new HashMap<>();

    public static void addTrophyItem(ItemStack trophyItem, UUID uuid){
        if (!trophyMaterials.contains(trophyItem.getType())) trophyMaterials.add(trophyItem.getType());
        trophyUUIDs.put(uuid, trophyItem);
    }

    public static List<Material> getTrophyMaterials(){
        return trophyMaterials;
    }

    public static ItemStack getTrophyFromUUID(UUID uuid){
        if (trophyUUIDs.containsKey(uuid))return trophyUUIDs.get(uuid);
        return null;
    }

    public static UUID getUuidFromTrophy(ItemStack trophyItem){
        ItemMeta trophyMeta = trophyItem.getItemMeta();
        if (trophyMeta == null)return null;
        PersistentDataContainer data = trophyMeta.getPersistentDataContainer();
        String uuidString = data.get(MobHeadsV3.getPluginNSK(),PersistentDataType.STRING);
        if (uuidString == null)return null;
        return UUID.fromString(uuidString);
    }

    private static final UUID guardianChestUUID = UUID.fromString("766cb562-5fe3-11ed-9b6a-0242ac120002");
    private static ItemStack guardianChest;
    public static ItemStack getGuardianChest(){
        if (guardianChest == null) guardianChest = buildGuardianChest();
        return guardianChest;
    }
    private static ItemStack buildGuardianChest(){
        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta chestMeta = (LeatherArmorMeta) chest.getItemMeta();
        assert chestMeta != null;
        PersistentDataContainer data = chestMeta.getPersistentDataContainer();
        data.set(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING, guardianChestUUID.toString());
        chestMeta.setColor(Color.fromRGB(0x68efbe));
        chestMeta.setDisplayName(ChatColor.AQUA+"Guardian's Repellent");
        chestMeta.addEnchant(Enchantment.DURABILITY, 2, false);
        chestMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, false);
        chestMeta.addEnchant(Enchantment.THORNS, 3, false);
        chest.setItemMeta(chestMeta);
        addTrophyItem(chest, guardianChestUUID);
        return chest;
    }

    private static final UUID bruteAxeUUID = UUID.fromString("766cb77e-5fe3-11ed-9b6a-0242ac120002");
    private static ItemStack bruteAxe;
    public static ItemStack getBruteAxe(){
        if (bruteAxe == null) bruteAxe = buildBruteAxe();
        return bruteAxe;
    }
    private static ItemStack buildBruteAxe(){
        ItemStack axe = new ItemStack(Material.GOLDEN_AXE, 1);
        ItemMeta axeMeta = axe.getItemMeta();
        assert axeMeta != null;
        PersistentDataContainer data = axeMeta.getPersistentDataContainer();
        data.set(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING, bruteAxeUUID.toString());
        axeMeta.setDisplayName(ChatColor.AQUA+"Brute's Axe");
        axeMeta.addEnchant(Enchantment.DURABILITY,2, false);
        axeMeta.addEnchant(Enchantment.DAMAGE_ALL, 3, false);
        axeMeta.addEnchant(Enchantment.LOOT_BONUS_MOBS, 2, true);
        axe.setItemMeta(axeMeta);
        addTrophyItem(axe, bruteAxeUUID);
        return axe;
    }

    private static final UUID vexSwordUUID = UUID.fromString("766cb940-5fe3-11ed-9b6a-0242ac120002");
    private static ItemStack vexSword;
    public static ItemStack getVexSword(){
        if (vexSword == null) vexSword = buildVexSword();
        return vexSword;
    }
    private static ItemStack buildVexSword(){
        ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta swordMeta = sword.getItemMeta();
        assert swordMeta != null;
        PersistentDataContainer data = swordMeta.getPersistentDataContainer();
        data.set(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING, vexSwordUUID.toString());
        swordMeta.setDisplayName(ChatColor.AQUA+"Vex's Sword");
        swordMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 2, false);
        swordMeta.addEnchant(Enchantment.KNOCKBACK, 3, true);
        sword.setItemMeta(swordMeta);
        addTrophyItem(sword, vexSwordUUID);
        return sword;
    }

}
