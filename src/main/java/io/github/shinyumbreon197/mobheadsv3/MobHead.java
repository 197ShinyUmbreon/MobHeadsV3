package io.github.shinyumbreon197.mobheadsv3;

import io.github.shinyumbreon197.mobheadsv3.data.Data;
import io.github.shinyumbreon197.mobheadsv3.data.Key;
import io.github.shinyumbreon197.mobheadsv3.function.Util;
import io.github.shinyumbreon197.mobheadsv3.head.PlayerHead;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
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
import org.bukkit.profile.PlayerTextures;

import java.io.FileWriter;
import java.io.IOException;
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
        if (debug){
            outputAllHeadsToFile();
        }
    }

    private static void outputAllHeadsToFile(){
        List<String> strings = new ArrayList<>();
        strings.add("------Debug Output of All Heads after Initialization------");
        strings.add(Util.getFriendlyDateAndTimeString(Util.getCurrentDateAndTime()));
        strings.add("Heads Total: " + mobHeads.size());
        EntityType lastType = null;
        for (MobHead mobHead:mobHeads){
            EntityType headType = mobHead.getEntityType();
            if (!headType.equals(lastType)){
                strings.add("\nMobHeads of Type: " + headType);
            }
            lastType = headType;
            String headName = mobHead.getHeadName();
            strings.add("\tHead Name: " + headName);
            String headUUID = mobHead.getUuid().toString();
            strings.add("\tHead UUID: " + headUUID);
            String headVariant = mobHead.getVariant();
            strings.add("\tHead Variant: " + headVariant);
            ItemStack headLootItemStack = mobHead.getHeadLootItemStack();
            String headLootItemString = null;
            if (headLootItemStack != null){
                int count = headLootItemStack.getAmount();
                String materialName = Util.friendlyMaterialName(headLootItemStack.getType());
                headLootItemString = materialName + " x" + count;
            }
            strings.add("\tHead Loot ItemStack: " + headLootItemString);
            Map<Enchantment,Integer> headEnchantments = mobHead.getEnchantments();
            if (headEnchantments != null){
                int enchantCount = headEnchantments.size();
                strings.add("\tHead Enchantments: " + enchantCount);
                for (Enchantment enchantment:headEnchantments.keySet()){
                    int lv = headEnchantments.get(enchantment);
                    strings.add("\t\t" + enchantment.getTranslationKey().toString() + " Lv" + lv);
                }
            }
            strings.add("\tHead Lore:");
            for (String loreString:mobHead.getLore()){
                strings.add("\t\t" + loreString);
            }
            Sound headSound = mobHead.getNoteblockSound();
            String headSoundString = null;
            if (headSound != null){
                headSoundString = headSound.toString();
            }
            strings.add("\tHead Noteblock Sound: " + headSoundString);
            strings.add("");
        }
//        File file = new File(MobHeadsV3.getPlugin().getDataFolder() + "Debug Mobhead Export.txt");
        String fileName = MobHeadsV3.getPlugin().getDataFolder() + "/DebugMobheadExport.txt";
        try{
            FileWriter fileWriter = new FileWriter(fileName);
            for (String string:strings)fileWriter.write(string + System.lineSeparator());
            fileWriter.close();
        }catch (IOException ignored){}
        MobHeadsV3.cOut("Exported All Heads to file: " + fileName);
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
        if (debug) System.out.println("getMobHeadOfEntity(LivingEntity target):String targetVariant = " + targetVariant); //debug
        if (targetVariant == null){
            System.out.println("The correct variant could not be found for killed entity " + targetType + ". Dropped first matching head in array.");
            return matches.get(0);
        }
        for (MobHead mobHead:matches){
            String scanVariant = mobHead.getVariant();
            if (debug) System.out.println("scanVariant: " + scanVariant); //debug
            if (scanVariant == null) continue;
            if (targetVariant.matches(scanVariant)){
                return mobHead;
            }
        }
        MobHeadsV3.cOut("The proper variant head item was not dropped.");
        MobHeadsV3.cOut("\t\t" + target.toString());
        return null; // matches.get(0);
    }

    public static MobHead getMobHeadFromVanillaType(EntityType entityType){
        if (!Data.vanillaHeadMap().containsKey(entityType))return null;
        for (MobHead mobHead:getMobHeads()){
            if (entityType.equals(mobHead.getEntityType()))return mobHead;
        }
        return null;
    }

    public static ItemStack getHeadItemOfEntity(LivingEntity target){
        if (target.getType().equals(EntityType.ARMOR_STAND)) return new ItemStack(Material.ARMOR_STAND);
        MobHead mobHead = getMobHeadOfEntity(target);
        if (mobHead == null){
            MobHeadsV3.cOut("An error occurred fetching the head item of an entity.");
            MobHeadsV3.cOut("\t\t" + target.getAsString());
            return new ItemStack(Material.SKELETON_SKULL);
        }
        return mobHead.getHeadItemStack();
    }
    public static MobHead getMobHeadWornByEntity(Entity entity){
        UUID id = getHeadUUIDFromWearingEntity(entity);
        if (id == null)return null;
        return getMobHeadFromUUID(id);
    }
    public static boolean isEntityWearingCertainHeadType(Entity entity, EntityType targetType){
        MobHead mobHead = getMobHeadWornByEntity(entity);
        return mobHead != null && mobHead.getEntityType().equals(targetType);
    }
    public static boolean isEntityWearingCertainHead(Entity entity, UUID headID){
        MobHead mobHead = getMobHeadWornByEntity(entity);
        return mobHead != null && mobHead.getUuid().equals(headID);
    }
    public static UUID getUUIDFromMobHeadItem(ItemStack headItem){
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
        if (skullItem.getItemMeta() == null || !(skullItem.getItemMeta() instanceof SkullMeta))return null;
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
    public static ItemStack getMobHeadItemFromBrokenSkullItem(ItemStack brokenSkullItem){
        if (brokenSkullItem == null)return null;
        SkullMeta brokenSkullMeta = (SkullMeta) brokenSkullItem.getItemMeta();
        if (brokenSkullMeta == null)return null;
        PersistentDataContainer data = brokenSkullMeta.getPersistentDataContainer();
        NamespacedKey key = Key.getHeadIDStorageType(data);
        if (key != null)return null;
        PlayerProfile pp = brokenSkullMeta.getOwnerProfile();
        if (pp == null){
            Material material = brokenSkullItem.getType();
            if (!Data.vanillaHeadMats.contains(material))return null;
            MobHead mobHead = getMobHeadFromVanillaSkullItem(brokenSkullItem);
            if (mobHead == null)return null;
            return mobHead.getHeadItemStack();
        }
        UUID uuid = pp.getUniqueId();
        MobHead mobHead = getMobHeadFromUUID(uuid);
        if (mobHead == null)return null;
        PlayerTextures headTexture = pp.getTextures();
        ItemStack newHeadItem = mobHead.getHeadItemStack();
        SkullMeta newSkullMeta = (SkullMeta) newHeadItem.getItemMeta();
        assert newSkullMeta != null;
        PlayerProfile newPP = newSkullMeta.getOwnerProfile();
        assert newPP != null;
        newPP.setTextures(headTexture);
        newSkullMeta.setOwnerProfile(newPP);
        newHeadItem.setItemMeta(newSkullMeta);
        return newHeadItem;
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
    Sound noteblockSound;

    public MobHead(UUID uuid, String displayName, EntityType entityType, ItemStack headItemStack, ItemStack headLootItemStack, List<String> lore) {
        this.uuid = uuid;
        this.headName = displayName;
        this.entityType = entityType;
        this.headItemStack = headItemStack;
        this.headLootItemStack = headLootItemStack;
        this.lore = lore;
        this.variant = null;
        this.enchantments = null;
        this.noteblockSound = null;
    }
    public MobHead(UUID uuid, String displayName, EntityType entityType, ItemStack headItemStack, ItemStack headLootItemStack, List<String> lore, Sound noteblockSound) {
        this.uuid = uuid;
        this.headName = displayName;
        this.entityType = entityType;
        this.headItemStack = headItemStack;
        this.headLootItemStack = headLootItemStack;
        this.lore = lore;
        this.variant = null;
        this.enchantments = null;
        this.noteblockSound = noteblockSound;
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
        this.noteblockSound = null;
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
        this.noteblockSound = null;
    }
    public MobHead(UUID uuid, String displayName, EntityType entityType, ItemStack headItemStack, ItemStack headLootItemStack, List<String> lore, String variant, Map<Enchantment, Integer> enchantments, Sound noteblockSound) {
        this.uuid = uuid;
        this.headName = displayName;
        this.entityType = entityType;
        this.headItemStack = headItemStack;
        this.headLootItemStack = headLootItemStack;
        this.lore = lore;
        this.variant = variant;
        this.enchantments = enchantments;
        this.noteblockSound = noteblockSound;
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

    public Sound getNoteblockSound(){return noteblockSound;}

    public void setNoteblockSound(Sound noteblockSound){
        this.noteblockSound = noteblockSound;
    }


}
