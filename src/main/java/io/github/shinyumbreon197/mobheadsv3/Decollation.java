package io.github.shinyumbreon197.mobheadsv3;

import io.github.shinyumbreon197.mobheadsv3.data.Key;
import io.github.shinyumbreon197.mobheadsv3.function.HeadItemDrop;
import io.github.shinyumbreon197.mobheadsv3.function.Util;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;

public class Decollation implements Listener {

    private static final NamespacedKey nsk = Key.decollation;

    private static final List<Material> baseMaterials = Arrays.asList(
            Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
            Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD,
            Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
            Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE,
            Material.BOW, Material.CROSSBOW, Material.TRIDENT, Material.ENDER_PEARL
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
        return decollationValue != null && decollationValue != 0;
    }

    private static final List<String> decollationStrings = List.of(
            "The next kill with this weapon",
            "will guarantee a Head drop.",
            "Throw this pearl at a creature",
            "you wish to gain the head of.",
            "It will not be painless."
    );
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
        data.set(nsk, PersistentDataType.INTEGER,1);
        List<String> loreAdditions = Arrays.asList(
                ChatColor.RESET+""+ChatColor.DARK_PURPLE+decollationStrings.get(0),
                ChatColor.RESET+""+ChatColor.DARK_PURPLE+decollationStrings.get(1)
        );
        List<String> pearlLoreAdditions = List.of(
                ChatColor.RESET + "" + ChatColor.DARK_PURPLE + decollationStrings.get(2),
                ChatColor.RESET + "" + ChatColor.DARK_PURPLE + decollationStrings.get(3),
                ChatColor.RESET + "" + ChatColor.DARK_RED + decollationStrings.get(4)
        );
        if (weapon.getType().equals(Material.ENDER_PEARL)){
            loreAdditions = pearlLoreAdditions;
            resultMeta.addEnchant(Enchantment.BINDING_CURSE,1,true);
            resultMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            resultMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Decollation Pearl");
        }
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
                boolean hit = false;
                for (String decollationString:decollationStrings){
                    if (loreString.contains(decollationString)){
                        hit = true;
                        break;
                    }
                }
                if (hit)continue;
                newLore.add(loreString);
            }
        }
        weaponMeta.setLore(newLore);
        weapon.setItemMeta(weaponMeta);
        return weapon;
    }

    @EventHandler
    public static void onDecollationPrepareSmith(PrepareSmithingEvent e){
        //if (!Config.decollationAbility)return;
        if (debug) System.out.println("onDecollationPrepareSmith() " + e.getView().getTopInventory().toString()); //debug
        SmithingInventory inv = e.getInventory();
        ItemStack template = inv.getItem(0);
        if (template == null) template = new ItemStack(Material.AIR);
        ItemStack base = inv.getItem(1);
        if (base == null) base = new ItemStack(Material.AIR);
        ItemStack addition = inv.getItem(2);
        if (debug) System.out.println("template: " + template + "\nbase: " + base + "\naddition: " + addition); //debug
        if (base.getType().equals(Material.AIR) ||
                addition == null || addition.getType().equals(Material.AIR) ||
                !template.getType().equals(Material.DIRT))return;
        if (!baseMaterials.contains(base.getType()) || !addition.getType().equals(Material.WITHER_SKELETON_SKULL))return;
        ItemStack result = base.clone();
        result.setAmount(1);
        result = addDecollation(result);
        e.setResult(result);
    }

    private static List<SmithingTrimRecipe> recipesSmithingDecollation(){
        List<SmithingTrimRecipe> smithingRecipes = new ArrayList<>();
        RecipeChoice templateItem = new RecipeChoice.MaterialChoice(Material.DIRT);
        RecipeChoice additionItem = new RecipeChoice.MaterialChoice(Material.WITHER_SKELETON_SKULL);
        for (Material material:baseMaterials){
            NamespacedKey key = new NamespacedKey(MobHeadsV3.getPlugin(),"smith_decollation_"+material.toString().toLowerCase());
            RecipeChoice baseItem = new RecipeChoice.MaterialChoice(material);
            //ItemStack result = new ItemStack(material);
            smithingRecipes.add(new SmithingTrimRecipe(key,templateItem,baseItem,additionItem));
        }
        return smithingRecipes;
    }

    public static boolean isDecollationPearlItem(ItemStack itemStack){
        if (itemStack == null || !itemStack.getType().equals(Material.ENDER_PEARL))return false;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)return false;
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        return data.has(nsk,PersistentDataType.INTEGER);
    }
    public static boolean isDecollationPearlEntity(Entity entity){
        if (!(entity instanceof EnderPearl))return false;
        EnderPearl enderPearl = (EnderPearl) entity;
        ItemStack pearlItem = enderPearl.getItem();
        return isDecollationPearlItem(pearlItem);
    }

    public static void runDecollationPearlHit(EnderPearl pearl, Entity targetEnt, Block targetBlock){
        if (debug) System.out.println("runDecollationPearlHit() targetEnt: " + targetEnt + "\ntargetBlock: " + targetBlock); //debug
        Location dropLoc = pearl.getLocation();
        ProjectileSource source = pearl.getShooter();
        if (targetEnt instanceof EnderDragonPart){
            targetEnt = ((EnderDragonPart) targetEnt).getParent();
        }
        if (targetEnt instanceof LivingEntity && !targetEnt.getType().equals(EntityType.ARMOR_STAND)){
            LivingEntity livTarget = (LivingEntity) targetEnt;
            double health = livTarget.getHealth();
            if (health < 5){
                health = 5;
            }else if (health > 20) health = 20;
            double damage = health * 0.8;
            livTarget.damage(damage, pearl);

            ItemStack headItem = MobHead.getHeadItemOfEntity(livTarget);
            if (source instanceof Player){
                Player player = (Player) source;
                Map<Integer,ItemStack> overflow = player.getInventory().addItem(headItem);
                if (overflow.size() > 0){
                    World world = player.getWorld();
                    for (ItemStack itemStack: overflow.values()){
                        world.dropItem(player.getLocation(),itemStack);
                    }
                }
                AVFX.playDecollationPearlTeleportEffect(player.getEyeLocation().add(player.getLocation().getDirection()));
            }else HeadItemDrop.dropHead(livTarget.getEyeLocation(),livTarget);
            AVFX.playHeadDropEffect(livTarget.getEyeLocation());
            AVFX.playDecollationPearlEffect(livTarget.getLocation());
        }else{
            if (source instanceof Player){
                Player player = (Player) source;
                Map<Integer,ItemStack> overflow = player.getInventory().addItem(pearl.getItem());
                if (overflow.size() > 0){
                    World world = player.getWorld();
                    for (ItemStack itemStack: overflow.values()){
                        world.dropItem(player.getLocation(),itemStack);
                    }
                }
                AVFX.playDecollationPearlTeleportEffect(player.getEyeLocation().add(player.getLocation().getDirection()));
            }else pearl.getWorld().dropItem(dropLoc,pearl.getItem());
        }
        pearl.remove();
    }

}
