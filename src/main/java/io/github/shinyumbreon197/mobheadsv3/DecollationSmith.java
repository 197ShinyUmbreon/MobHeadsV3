package io.github.shinyumbreon197.mobheadsv3;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingInventory;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DecollationSmith implements Listener {
    
    private static final NamespacedKey nsk = new NamespacedKey(MobHeadsV3.getPlugin(), "decollation");

    private static final List<Material> baseMaterials = Arrays.asList(
            Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
            Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD,
            Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
            Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE
    );

    public static void registerSmithingRecipes(){
        for (SmithingRecipe recipe:recipesSmithingDecollation()){
            MobHeadsV3.getPlugin().getServer().addRecipe(recipe);
        }
    }

    public static boolean hasDecollation(ItemStack weapon){
        if (weapon == null)return false;
        if (!baseMaterials.contains(weapon.getType()))return false;
        ItemMeta weaponMeta = weapon.getItemMeta();
        assert weaponMeta != null;
        PersistentDataContainer data = weaponMeta.getPersistentDataContainer();
        Integer decollationValue = data.get(nsk, PersistentDataType.INTEGER);
        if (decollationValue == null || decollationValue == 0)return false;
        return true;
    }

    @Nullable //Returns null if unsuccessful
    public static ItemStack addDecollation(ItemStack weapon){
        if (hasDecollation(weapon))return null;
        ItemMeta resultMeta = weapon.getItemMeta();
        assert resultMeta != null;
        PersistentDataContainer data = resultMeta.getPersistentDataContainer();
        Integer decollationValue = data.get(nsk, PersistentDataType.INTEGER);
        if (decollationValue != null && decollationValue == 1){
            return null;
        }
        data.set(new NamespacedKey(MobHeadsV3.getPlugin(), "decollation"), PersistentDataType.INTEGER,1);
        List<String> loreAdditions = Arrays.asList(
                ChatColor.RESET+""+ChatColor.DARK_PURPLE+"The next kill with this weapon",
                ChatColor.RESET+""+ChatColor.DARK_PURPLE+"will guarantee a Head drop."
        );
        List<String> lore = new ArrayList<>();
        if (resultMeta.getLore() != null) lore.addAll(resultMeta.getLore());
        lore.addAll(loreAdditions);
        resultMeta.setLore(lore);
        weapon.setItemMeta(resultMeta);
        return weapon;
    }

    @Nullable //Returns null if unsuccessful
    public static ItemStack removeDecollation(ItemStack weapon){
        if (!hasDecollation(weapon))return null;
        ItemMeta weaponMeta = weapon.getItemMeta();
        assert weaponMeta != null;
        PersistentDataContainer data = weaponMeta.getPersistentDataContainer();
        //data.set(nsk, PersistentDataType.INTEGER, 0);
        data.remove(nsk);
        List<String> lore = new ArrayList<>();
        List<String> newLore = new ArrayList<>();
        if (weaponMeta.getLore() != null) lore.addAll(weaponMeta.getLore());
        if (lore.size() > 0){
            for (String loreString:lore){
                if (!loreString.contains("The next kill with this weapon") && !loreString.contains("will guarantee a Head drop.")){
                    newLore.add(loreString);
                }
            }
        }
        weaponMeta.setLore(newLore);
        weapon.setItemMeta(weaponMeta);
        return weapon;
    }

    @EventHandler
    public static void onDecollationPrepareSmith(PrepareSmithingEvent e){
        //if (!Config.decollationAbility)return;
        SmithingInventory inv = e.getInventory();
        ItemStack base = inv.getItem(0);
        ItemStack addition = inv.getItem(1);
        if (base == null || base.getType().equals(Material.AIR) || addition == null || addition.getType().equals(Material.AIR))return;
        if (!baseMaterials.contains(base.getType()) || !addition.getType().equals(Material.WITHER_SKELETON_SKULL))return;
        ItemStack result = base.clone();
        result = addDecollation(result);
        e.setResult(result);
    }

    private static List<SmithingRecipe> recipesSmithingDecollation(){
        List<SmithingRecipe> smithingRecipes = new ArrayList<>();
        RecipeChoice additionRC = new RecipeChoice.MaterialChoice(Material.WITHER_SKELETON_SKULL);
        for (Material material:baseMaterials){
            NamespacedKey key = new NamespacedKey(MobHeadsV3.getPlugin(),"smith_decollation_"+material.toString().toLowerCase());
            RecipeChoice baseRC = new RecipeChoice.MaterialChoice(material);

            ItemStack result = new ItemStack(material);
            smithingRecipes.add(new SmithingRecipe(key,result,baseRC,additionRC));
        }
        return smithingRecipes;
    }

}
