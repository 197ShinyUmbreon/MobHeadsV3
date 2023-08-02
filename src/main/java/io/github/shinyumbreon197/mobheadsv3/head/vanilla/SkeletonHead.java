package io.github.shinyumbreon197.mobheadsv3.head.vanilla;

import io.github.shinyumbreon197.mobheadsv3.Data;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SkeletonHead {

    private static final EntityType entityType = EntityType.SKELETON;
    private static final ItemStack lootItem = new ItemStack(Material.BONE, 16);
    private static final Sound interactSound = Sound.ENTITY_SKELETON_AMBIENT;
    private static final UUID headUUID = UUID.fromString("766ca39c-5fe3-11ed-9b6a-0242ac120002");
    private static final ItemStack headItem = new ItemStack(Material.SKELETON_SKULL);

    public static void initialize(){
        MobHead mobHead = new MobHead(entityType, null, null, headItem, lootItem, headUUID, interactSound);
        Data.addMobHead(mobHead);
    }

}
