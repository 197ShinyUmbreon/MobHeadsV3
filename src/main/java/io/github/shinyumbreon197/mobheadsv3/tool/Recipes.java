package io.github.shinyumbreon197.mobheadsv3.tool;

import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.SkullMeta;

public class Recipes {

    public static void registerHeadRecipe(ItemStack head, ItemStack result){
        RecipeChoice.ExactChoice rc = new RecipeChoice.ExactChoice(head);
        String name;
        if (head.getType().equals(Material.PLAYER_HEAD)){
            SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
            assert skullMeta != null;
            assert skullMeta.getOwnerProfile() != null;
            name = skullMeta.getOwnerProfile().getName();
        }else{
            name = StringBuilder.toSimplifiedString(head.getType().name());
        }
        assert name != null;
        NamespacedKey nsk = new NamespacedKey(MobHeadsV3.getPlugin(), StringBuilder.toSimplifiedString(name));
        ShapedRecipe recipe = new ShapedRecipe(nsk, result);
        recipe.shape("H");
        recipe.setIngredient('H', rc);
        MobHeadsV3.getPlugin().getServer().addRecipe(recipe);
    }

}
