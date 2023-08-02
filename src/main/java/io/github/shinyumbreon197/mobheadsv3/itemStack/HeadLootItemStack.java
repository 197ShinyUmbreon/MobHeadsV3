package io.github.shinyumbreon197.mobheadsv3.itemStack;

import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.List;

public class HeadLootItemStack {

    public static ItemStack pufferfishLoot(){
        ItemStack potion = new ItemStack(Material.POTION);
        PotionData potionData = new PotionData(PotionType.POISON, false, false);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        assert potionMeta != null;
        potionMeta.setBasePotionData(potionData);
        potion.setItemMeta(potionMeta);
        return potion;
    }
    public static ItemStack shulkerLoot(){
        ItemStack shulkerBoxItem = new ItemStack(Material.SHULKER_BOX);
        BlockStateMeta shulkerBoxMeta = (BlockStateMeta) shulkerBoxItem.getItemMeta();
        assert shulkerBoxMeta != null;
        ShulkerBox shulkerBox = (ShulkerBox) shulkerBoxMeta.getBlockState();
        Inventory shulkInv = shulkerBox.getInventory();
        ItemStack shell = new ItemStack(Material.SHULKER_SHELL);
        ItemStack chest = new ItemStack(Material.CHEST);
        shulkInv.setItem(4, shell);
        shulkInv.setItem(13, chest);
        shulkInv.setItem(22, shell);
        shulkerBoxMeta.setBlockState(shulkerBox);
        shulkerBoxItem.setItemMeta(shulkerBoxMeta);
        return shulkerBoxItem;
    }
    public static ItemStack strayLoot(){
        ItemStack arrows = new ItemStack(Material.TIPPED_ARROW, 16);
        PotionMeta potionMeta = (PotionMeta) arrows.getItemMeta();
        assert potionMeta != null;
        PotionData potionData = new PotionData(PotionType.SLOWNESS,false,false);
        potionMeta.setBasePotionData(potionData);
        arrows.setItemMeta(potionMeta);
        return arrows;
    }
    public static ItemStack axolotlLoot(){
        ItemStack potion = new ItemStack(Material.POTION);
        PotionData potionData = new PotionData(PotionType.REGEN, true, false);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        assert potionMeta != null;
        potionMeta.setBasePotionData(potionData);
        potion.setItemMeta(potionMeta);
        return potion;
    }
    public static ItemStack rabbitLoot(){
        ItemStack bundle = new ItemStack(Material.BUNDLE);
        BundleMeta bundleMeta = (BundleMeta) bundle.getItemMeta();
        ItemStack hide = new ItemStack(Material.RABBIT_HIDE);
        ItemStack foot = new ItemStack(Material.RABBIT_FOOT, 2);
        ItemStack body = new ItemStack(Material.RABBIT);
        List<ItemStack> contents = Arrays.asList(hide, foot, body);
        assert bundleMeta != null;
        bundleMeta.setItems(contents);
        bundle.setItemMeta(bundleMeta);
        return bundle;
    }
    public static ItemStack villagerLoot(){
        ItemStack potion = new ItemStack(Material.SPLASH_POTION);
        PotionData potionData = new PotionData(PotionType.WEAKNESS, false, false);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        assert potionMeta != null;
        potionMeta.setBasePotionData(potionData);
        potion.setItemMeta(potionMeta);
        return potion;
    }

}
