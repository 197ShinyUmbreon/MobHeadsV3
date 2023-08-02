package io.github.shinyumbreon197.mobheadsv3.head.hostile;

import io.github.shinyumbreon197.mobheadsv3.Data;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class ShulkerHead {

    private static final EntityType entityType = EntityType.SHULKER;
    private static final ItemStack lootItem = buildShulkerLoot();
    private static final Sound interactSound = Sound.ENTITY_SHULKER_AMBIENT;
    private static final String headName = "Shrunken Shulker";
    private static final UUID headUUID = UUID.fromString("19412f26-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/1433a4b73273a64c8ab2830b0fff777a61a488c92f60f83bfb3e421f428a44");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        Data.addMobHead(mobHead);
    }

    private static ItemStack buildShulkerLoot(){
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

}
