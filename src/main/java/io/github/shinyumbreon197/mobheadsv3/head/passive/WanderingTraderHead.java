package io.github.shinyumbreon197.mobheadsv3.head.passive;

import io.github.shinyumbreon197.mobheadsv3.Data;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.tool.HeadUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class WanderingTraderHead {

    private static final EntityType entityType = EntityType.WANDERING_TRADER;
    private static final ItemStack lootItem = new ItemStack(Material.DEEPSLATE_EMERALD_ORE, 4);
    private static final Sound interactSound = Sound.ENTITY_WANDERING_TRADER_NO;
    private static final String headName = "Wandering Trader Head";
    private static final UUID headUUID = UUID.fromString("19412170-5fac-11ed-9b6a-0242ac120002");

    public static void initialize(){
        URL textureURL;
        try{
            textureURL = new URL("http://textures.minecraft.net/texture/5f1379a82290d7abe1efaabbc70710ff2ec02dd34ade386bc00c930c461cf932");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ItemStack headItem = HeadUtil.customHead(headName, headUUID, textureURL);
        MobHead mobHead = new MobHead(entityType, null, headName, headItem, lootItem, headUUID, interactSound);
        Data.addMobHead(mobHead);
    }

}
