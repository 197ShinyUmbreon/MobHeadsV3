package io.github.shinyumbreon197.mobheadsv3;

import io.github.shinyumbreon197.mobheadsv3.data.Data;
import io.github.shinyumbreon197.mobheadsv3.data.Key;
import io.github.shinyumbreon197.mobheadsv3.function.CreatureEvents;
import io.github.shinyumbreon197.mobheadsv3.function.Util;
import io.github.shinyumbreon197.mobheadsv3.head.PlayerHead;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.github.shinyumbreon197.mobheadsv3.MobHeadsV3.debug;
import static io.github.shinyumbreon197.mobheadsv3.data.HeadData.getAllHeads;

public class MobHead implements Listener {

    @EventHandler
    public static void grindstoneHead(PrepareGrindstoneEvent e){
        GrindstoneInventory inv = e.getInventory();
        ItemStack slot0 = inv.getContents()[0];
        ItemStack slot1 = inv.getContents()[1];
        if (skullItemIsMobHead(slot0) || skullItemIsMobHead(slot1)){
            e.setResult(null);
        }
    }
    public static void initialize(){
        mobHeads.addAll(getAllHeads());
        PlayerHead.registerPlayerHistory();
        PlayerHead.registerOnlinePlayers();
    }

    public static boolean isWearingHead(Entity entity){
        if (!(entity instanceof LivingEntity))return false;
        LivingEntity livingEnt = (LivingEntity) entity;
        ItemStack headItem;
        if (entity instanceof Player){
            Player player = (Player) entity;
            headItem = player.getInventory().getHelmet();
        }else{
            EntityEquipment equip = livingEnt.getEquipment();
            if (equip == null)return false;
            headItem = livingEnt.getEquipment().getHelmet();
        }
        if (headItem == null)return false;
        ItemMeta itemMeta = headItem.getItemMeta();
        if (itemMeta == null)return false;
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        NamespacedKey key = Key.getHeadIDStorageType(data);
        if (key == null)return false;
        return data.has(key, PersistentDataType.STRING);
    }

    public static MobHead getMobHeadFromBlock(Block block){
        if (!Data.getHeadBlockMats().contains(block.getType()))return null;
        Skull skull = (Skull) block.getState();
        PersistentDataContainer data = skull.getPersistentDataContainer();
        NamespacedKey key = Key.getHeadIDStorageType(data);
        if (key == null)return null;
        String uuidString = data.get(key,PersistentDataType.STRING);
        if (uuidString == null)return null;
        return MobHead.getMobHeadFromUUID(UUID.fromString(uuidString));
    }

    public static boolean isUUIDRegistered(UUID uuid){
        return getMobHeadFromUUID(uuid) != null;
    }
    public static MobHead getMobHeadOfEntity(LivingEntity target){
        EntityType targetType = target.getType();
        if (targetType.equals(EntityType.PLAYER)){
            return getMobHeadFromUUID(target.getUniqueId());
        }
        List<MobHead> matches = new ArrayList<>();
        for (MobHead mobHead:getMobHeads()){
            EntityType scanType = mobHead.getEntityType();
            if (targetType.equals(scanType)) matches.add(mobHead);
        }
        if (debug) System.out.println("getMobHeadOfEntity(" + target.getType() + ")\nmatches.size() " + matches.size()); //debug
        if (matches.size() == 0){
            return null;
        }else if (matches.size() == 1) return matches.get(0);
        String targetVariant = Util.getVariantString(target);
        if (targetVariant == null) return null;
        for (MobHead mobHead:matches){
            String scanVariant = mobHead.getVariant();
            if (scanVariant == null) continue;
            if (targetVariant.matches(scanVariant)) return mobHead;
        }
        return null;
    }

    public static MobHead getMobHeadFromVanillaType(EntityType entityType){
        if (!Data.vanillaHeadMap().containsKey(entityType))return null;
        for (MobHead mobHead:getMobHeads()){
            if (entityType.equals(mobHead.getEntityType()))return mobHead;
        }
        return null;
    }

    public static ItemStack getHeadItemOfEntity(LivingEntity target){
        MobHead mobHead = getMobHeadOfEntity(target);
        if (mobHead == null)return null;
        return mobHead.getHeadItemStack();
    }
    public static MobHead getMobHeadWornByEntity(Entity entity){
        UUID id = getHeadUUIDFromWearingEntity(entity);
        if (id == null)return null;
        return getMobHeadFromUUID(id);
    }

    private static UUID getUUIDFromMobHeadItem(ItemStack headItem){
        if (headItem == null)return null;
        ItemMeta itemMeta = headItem.getItemMeta();
        if (itemMeta == null)return null;
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        NamespacedKey key = Key.getHeadIDStorageType(data);
        if (key == null)return null;
        String uuidString = data.get(key,PersistentDataType.STRING);
        if (uuidString == null)return null;
        return UUID.fromString(uuidString);
    }
    public static UUID getHeadUUIDFromWearingEntity(Entity entity){
        if (entity == null)return null;
        if (!(entity instanceof LivingEntity))return null;
        if (!isWearingHead(entity))return null;
        LivingEntity livEnt = (LivingEntity) entity;
        assert livEnt.getEquipment() != null;
        ItemStack headItem = livEnt.getEquipment().getHelmet();
        return getUUIDFromMobHeadItem(headItem);
    }

    public static MobHead getMobHeadFromUUID(UUID uuid){
        for (MobHead mobHead:mobHeads){
            UUID headID = mobHead.getUuid();
            if (headID.equals(uuid))return mobHead;
        }
        return null;
    }

    public static boolean skullItemIsMobHead(ItemStack itemStack){
        if (itemStack == null)return false;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)return false;
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        NamespacedKey key = Key.getHeadIDStorageType(data);
        return key != null;
    }

    public static MobHead getMobHeadFromSkullItemStack(ItemStack skullItem){
        if (skullItem == null)return null;
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
        if (skullMeta == null)return null;
        PersistentDataContainer data = skullMeta.getPersistentDataContainer();
        NamespacedKey key = Key.getHeadIDStorageType(data);
        if (key == null)return null;
        String uuidString = data.get(key,PersistentDataType.STRING);
        if (uuidString == null)return null;
        return MobHead.getMobHeadFromUUID(UUID.fromString(uuidString));
    }
    public static ItemStack repairMobheadItemstack(ItemStack held){
        MobHead mobHead = getMobHeadFromSkullItemStack(held);
        if (mobHead == null) mobHead = getMobHeadFromVanillaSkullItem(held);
        if (mobHead == null && !held.getType().equals(Material.PLAYER_HEAD))return null;
        if (mobHead == null){
            SkullMeta meta = (SkullMeta) held.getItemMeta();
            if (meta == null)return null;
            PlayerProfile pp = meta.getOwnerProfile();
            if (pp == null)return null;
            UUID uuid = pp.getUniqueId();
            if (uuid == null)return null;
            for (MobHead head:getMobHeads()){
                ItemStack headItem = head.getHeadItemStack();
                if (!headItem.getType().equals(Material.PLAYER_HEAD))continue;
                SkullMeta savedMeta = (SkullMeta) headItem.getItemMeta();
                assert savedMeta != null;
                PlayerProfile savedPP = savedMeta.getOwnerProfile();
                assert savedPP != null;
                UUID savedUUID = savedPP.getUniqueId();
                assert savedUUID != null;
                if (uuid.equals(savedUUID)){
                    mobHead = head;
                    break;
                }
            }
        }
        if (mobHead == null)return null;
        ItemStack repairedHead = mobHead.getHeadItemStack().clone();
        int count = held.getAmount();
        repairedHead.setAmount(count);
        return repairedHead;
    }
    public static MobHead getMobHeadFromBrokenSkullItem(ItemStack skullItem){
        if (skullItem == null)return null;
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
        if (skullMeta == null)return null;
        PersistentDataContainer data = skullMeta.getPersistentDataContainer();
        NamespacedKey key = Key.getHeadIDStorageType(data);
        if (key != null)return null;
        PlayerProfile pp = skullMeta.getOwnerProfile();
        if (pp == null){
            Material material = skullItem.getType();
            if (!Data.vanillaHeadMats.contains(material))return null;
            return getMobHeadFromVanillaSkullItem(skullItem);
        }
        UUID uuid = pp.getUniqueId();
        return getMobHeadFromUUID(uuid);
    }

    public static MobHead getMobHeadFromVanillaSkullItem(ItemStack skullItem){
        Material skullMat = skullItem.getType();
        EntityType type = Data.vanillaMatEntTypeMap().get(skullMat);
        if (type == null)return null;
        return MobHead.getMobHeadFromVanillaType(type);
    }

    private static final List<MobHead> mobHeads = new ArrayList<>();
    public static List<MobHead> getMobHeads(){
        return mobHeads;
    }
    public static void addMobHead(MobHead mobHead){
        boolean isNotRegistered = !isHeadRegistered(mobHead);
        if (debug) System.out.println("addMobHead(" + mobHead + ") " + isNotRegistered); //debug
        if (isNotRegistered) mobHeads.add(0,mobHead);
    }
    public static void removeMobHead(UUID uuid){
        List<MobHead> heads = new ArrayList<>();
        for (MobHead mobHead:mobHeads){
            UUID headUUID = mobHead.getUuid();
            if (headUUID.equals(uuid))continue;
            heads.add(mobHead);
        }
        mobHeads.clear();
        mobHeads.addAll(heads);
    }
    public static boolean isHeadRegistered(MobHead mobHead){
        return mobHeads.contains(mobHead);
    }

    UUID uuid;
    String headName;
    EntityType entityType;
    ItemStack headItemStack;
    ItemStack headLootItemStack;
    List<String> lore;
    String variant;
    Map<Enchantment, Integer> enchantments;

    public MobHead(UUID uuid, String displayName, EntityType entityType, ItemStack headItemStack, ItemStack headLootItemStack, List<String> lore) {
        this.uuid = uuid;
        this.headName = displayName;
        this.entityType = entityType;
        this.headItemStack = headItemStack;
        this.headLootItemStack = headLootItemStack;
        this.lore = lore;
        this.variant = null;
        this.enchantments = null;
    }
    public MobHead(UUID uuid, String displayName, EntityType entityType, ItemStack headItemStack, ItemStack headLootItemStack, List<String> lore, String variant) {
        this.uuid = uuid;
        this.headName = displayName;
        this.entityType = entityType;
        this.headItemStack = headItemStack;
        this.headLootItemStack = headLootItemStack;
        this.lore = lore;
        this.variant = variant;
        this.enchantments = null;
    }
    public MobHead(UUID uuid, String displayName, EntityType entityType, ItemStack headItemStack, ItemStack headLootItemStack, List<String> lore, String variant, Map<Enchantment, Integer> enchantments) {
        this.uuid = uuid;
        this.headName = displayName;
        this.entityType = entityType;
        this.headItemStack = headItemStack;
        this.headLootItemStack = headLootItemStack;
        this.lore = lore;
        this.variant = variant;
        this.enchantments = enchantments;
    }
    public MobHead() {

    }


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public ItemStack getHeadItemStack() {
        return headItemStack;
    }

    public void setHeadItemStack(ItemStack headItemStack) {
        this.headItemStack = headItemStack;
    }

    public ItemStack getHeadLootItemStack() {
        return headLootItemStack;
    }

    public void setHeadLootItemStack(ItemStack headLootItemStack) {
        this.headLootItemStack = headLootItemStack;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public String getVariant(){
        return variant;
    }

    public void setVariant(String string){
        this.variant = string;
    }

    public Map<Enchantment, Integer> getEnchantments(){return enchantments;}

    public void setEnchantments(Map<Enchantment, Integer> enchantments){this.enchantments = enchantments;}


}
