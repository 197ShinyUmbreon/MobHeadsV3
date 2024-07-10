package io.github.shinyumbreon197.mobheadsv3;

import io.github.shinyumbreon197.mobheadsv3.data.Key;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
        String uuidString = data.get(Key.master,PersistentDataType.STRING);
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
        data.set(Key.master, PersistentDataType.STRING, guardianChestUUID.toString());
        chestMeta.setColor(Color.fromRGB(0x68efbe));
        chestMeta.setDisplayName(ChatColor.AQUA+"Guardian's Repellent");
        chestMeta.addEnchant(Enchantment.UNBREAKING, 2, false);
        chestMeta.addEnchant(Enchantment.PROTECTION, 2, false);
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
        data.set(Key.master, PersistentDataType.STRING, bruteAxeUUID.toString());
        axeMeta.setDisplayName(ChatColor.AQUA+"Brute's Axe");
        axeMeta.addEnchant(Enchantment.UNBREAKING,2, false);
        axeMeta.addEnchant(Enchantment.SHARPNESS, 3, false);
        axeMeta.addEnchant(Enchantment.LOOTING, 2, true);
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
        data.set(Key.master, PersistentDataType.STRING, vexSwordUUID.toString());
        swordMeta.setDisplayName(ChatColor.AQUA+"Vex's Sword");
        swordMeta.addEnchant(Enchantment.UNBREAKING, 1, false);
        swordMeta.addEnchant(Enchantment.SHARPNESS, 2, false);
        swordMeta.addEnchant(Enchantment.KNOCKBACK, 3, true);
        sword.setItemMeta(swordMeta);
        addTrophyItem(sword, vexSwordUUID);
        return sword;
    }

    private static final UUID zombiePiglinSwordUUID = UUID.fromString("766cbcba-5fe3-11ed-9b6a-0242ac120002");
    private static ItemStack zombiePiglinSword;
    public static ItemStack getZombiePiglinSword(){
        if (zombiePiglinSword == null) zombiePiglinSword = buildZombiePiglinSword();
        return zombiePiglinSword;
    }
    private static ItemStack buildZombiePiglinSword(){
        ItemStack sword = new ItemStack(Material.GOLDEN_SWORD, 1);
        ItemMeta swordMeta = sword.getItemMeta();
        assert swordMeta != null;
        PersistentDataContainer data = swordMeta.getPersistentDataContainer();
        data.set(Key.master, PersistentDataType.STRING, zombiePiglinSwordUUID.toString());
        swordMeta.setDisplayName(ChatColor.AQUA+"Zombie Piglin's Sword");
        swordMeta.addEnchant(Enchantment.UNBREAKING, 2, false);
        swordMeta.addEnchant(Enchantment.SMITE, 4, false);
        swordMeta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        sword.setItemMeta(swordMeta);
        addTrophyItem(sword, zombiePiglinSwordUUID);
        return sword;
    }

    private static final UUID vindicatorAxeUUID = UUID.fromString("766cbee0-5fe3-11ed-9b6a-0242ac120002");
    private static ItemStack vindicatorAxe;
    public static ItemStack getVindicatorAxe(){
        if (vindicatorAxe == null) vindicatorAxe = buildVindicatorAxe();
        return vindicatorAxe;
    }
    private static ItemStack buildVindicatorAxe(){
        ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
        ItemMeta axeMeta = axe.getItemMeta();
        assert axeMeta != null;
        PersistentDataContainer data = axeMeta.getPersistentDataContainer();
        data.set(Key.master, PersistentDataType.STRING, vindicatorAxeUUID.toString());
        axeMeta.setDisplayName(ChatColor.AQUA+"Vindicator's Axe");
        axeMeta.addEnchant(Enchantment.UNBREAKING, 1, false);
        axeMeta.addEnchant(Enchantment.SHARPNESS, 3, false);
        axeMeta.addEnchant(Enchantment.LOOTING, 2, true);
        axe.setItemMeta(axeMeta);
        addTrophyItem(axe, vindicatorAxeUUID);
        return axe;
    }

    private static final UUID blindnessArrowUUID = UUID.fromString("766cc0ac-5fe3-11ed-9b6a-0242ac120002");
    private static ItemStack blindnessArrow;
    public static ItemStack getBlindnessArrow(){
        if (blindnessArrow == null) blindnessArrow = buildBlindnessArrow();
        return blindnessArrow;
    }
    private static ItemStack buildBlindnessArrow(){
        ItemStack arrows = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta potionMeta = (PotionMeta) arrows.getItemMeta();
        assert potionMeta != null;
        PersistentDataContainer data = potionMeta.getPersistentDataContainer();
        data.set(Key.master, PersistentDataType.STRING, blindnessArrowUUID.toString());
        potionMeta.setDisplayName(ChatColor.AQUA+"Arrow of Blindness");
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*20,0), true);
        arrows.setItemMeta(potionMeta);
        return arrows;
    }

}
