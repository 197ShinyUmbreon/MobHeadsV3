package io.github.shinyumbreon197.mobheadsv3.head;

import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MobHead {

    private EntityType entityType;
    private String variant;
    private String name;
    private ItemStack headItem;
    private ItemStack lootItem;
    private UUID uuid;
    private Sound interactSound;

    public MobHead(){

    }

    public MobHead(EntityType et, String v, String n, ItemStack hi, ItemStack li, UUID id, Sound is){
        entityType = et;
        variant = v;
        name = n;
        headItem = hi;
        lootItem = li;
        uuid = id;
        interactSound = is;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getHeadItem() {
        return headItem;
    }

    public void setHeadItem(ItemStack headItem) {
        this.headItem = headItem;
    }

    public ItemStack getLootItem() {
        return lootItem;
    }

    public void setLootItem(ItemStack lootItem) {
        this.lootItem = lootItem;
    }

    public Sound getInteractSound() {
        return interactSound;
    }

    public void setInteractSound(Sound interactSound) {
        this.interactSound = interactSound;
    }
}