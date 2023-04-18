package io.github.shinyumbreon197.mobheadsv3.tool;

import io.github.shinyumbreon197.mobheadsv3.Data;
import io.github.shinyumbreon197.mobheadsv3.head.MobHead;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import io.github.shinyumbreon197.mobheadsv3.head.PlayerHead;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.util.UUID;

public class HeadUtil {

    public static ItemStack customHead(String headName, UUID uuid, PlayerProfile pp){
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        assert headMeta != null;
        headMeta.setOwnerProfile(pp);
        headMeta.setDisplayName(ChatColor.YELLOW+headName);
        PersistentDataContainer data = headMeta.getPersistentDataContainer();
        data.set(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING, uuid.toString());
        head.setItemMeta(headMeta);
        return head;
    }

    private static PlayerProfile customPlayerProfile(String headName, UUID uuid, URL texture){
        PlayerProfile pp = Bukkit.createPlayerProfile(uuid,headName);
        PlayerTextures pt = pp.getTextures();
        pt.setSkin(texture);
        return pp;
    }

    public static ItemStack customHead(String headName, UUID uuid, URL texture) {
        PlayerProfile pp = customPlayerProfile(headName, uuid, texture);
        return customHead(headName, uuid, pp);
    }

    public static UUID getHeadUUID(ItemStack input){
        if (input == null)return null;
        UUID uuid;
        if (input.getType().equals(Material.PLAYER_HEAD)){
            SkullMeta skullMeta = (SkullMeta) input.getItemMeta();
            if (skullMeta == null)return null;
            PersistentDataContainer data = skullMeta.getPersistentDataContainer();
            String uuidString = data.get(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING);
            if (uuidString == null)return null;
            uuid = UUID.fromString(uuidString);
        }else{
            EntityType entityType = Data.vanillaMatEntTypeMap().get(input.getType());
            if (entityType == null)return null;
            uuid = Data.vanillaHeadUUIDs.get(entityType);
        }
        return uuid;
    }

    public static boolean isMobHead(ItemStack input){
        if (input == null) return false;
        if (input.getType().equals(Material.PLAYER_HEAD)){
            SkullMeta skullMeta = (SkullMeta) input.getItemMeta();
            if (skullMeta == null)return false;
            PersistentDataContainer data = skullMeta.getPersistentDataContainer();
            boolean isHead = data.has(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING);
            if (!isHead) return false;
            String uuidString = data.get(MobHeadsV3.getPluginNSK(),PersistentDataType.STRING);
            if (uuidString == null)return false;
            UUID uuid = UUID.fromString(uuidString);
            boolean isRegistered = Data.mobHeadByUUID.containsKey(uuid);
            if (!isRegistered){
                MobHead playerHead = PlayerHead.rebuildPlayerHead(input);
                 Data.registerHead(playerHead);
                 MobHeadsV3.playerRegistry.addToRegistry(playerHead.getHeadItem());
            }
            return true;
        }else{
            return Data.headBlockMats.contains(input.getType());
        }
    }

    public static MobHead getMobHeadFromEntity(Entity entity){
        if (entity instanceof LivingEntity && ((LivingEntity) entity).getEquipment() != null){
            if (isMobHead(((LivingEntity) entity).getEquipment().getHelmet())){
                return getMobHeadFromHeadItem(((LivingEntity) entity).getEquipment().getHelmet());
            }
        }
        return null;
    }

    public static MobHead getMobHeadFromHeadItem(ItemStack input){
        if (!isMobHead(input))return null;
        return Data.mobHeadByUUID.get(getHeadUUID(input));
    }

    public static EntityType getHeadEntityType(ItemStack input){
        MobHead mobHead = getMobHeadFromHeadItem(input);
        if (mobHead == null)return null;
        return mobHead.getEntityType();
    }

    public static String getHeadVariant(ItemStack input){
        MobHead mobHead = getMobHeadFromHeadItem(input);
        if (mobHead == null)return null;
        return mobHead.getVariant();
    }

    public static MobHead getHeadFromBlock(Block headBlock){
        MobHead mobHead = null;
        if (Data.headBlockMats.contains(headBlock.getType())){
            Skull skullState = (Skull) headBlock.getState();
            PersistentDataContainer data = skullState.getPersistentDataContainer();
            String uuidString = data.get(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING);
            if (uuidString != null){
                UUID uuid = UUID.fromString(uuidString);
                mobHead = Data.mobHeadByUUID.get(uuid);
            }
        }
        return mobHead;
    }

    public static ItemStack GetHeadItemFromEntity(Entity entity){
        if (!Data.entityTypes.contains(entity.getType()))return null;
        EntityType entityType = entity.getType();
        String variant = getVariantFromEntity(entity);
        boolean hasVariant = variant != null;

        for (MobHead mobHead: Data.getMobHeads()){
            if (!entityType.equals(mobHead.getEntityType()))continue;
            if (hasVariant && !variant.matches(mobHead.getVariant()))continue;
            return mobHead.getHeadItem();
        }

        return null;
    }

    public static EntityType getEntityTypeFromUUID(UUID uuid){
        return Data.mobHeadByUUID.get(uuid).getEntityType();
    }

    public static String getVariantFromEntity(Entity entity){
        switch (entity.getType()){
            case RABBIT -> {
                Rabbit rabbit = (Rabbit) entity;
                return rabbit.getRabbitType().toString();
            }
            case AXOLOTL -> {
                Axolotl axolotl = (Axolotl) entity;
                return axolotl.getVariant().toString();
            }
            case CAT -> {
                Cat cat = (Cat) entity;
                return cat.getCatType().toString();
            }
            case HORSE -> {
                Horse horse = (Horse) entity;
                return horse.getColor().toString();
            }
            case LLAMA -> {
                Llama llama = (Llama) entity;
                return llama.getColor().toString();
            }
            case TRADER_LLAMA -> {
                TraderLlama traderLlama = (TraderLlama) entity;
                return traderLlama.getColor().toString();
            }
            case PARROT -> {
                Parrot parrot = (Parrot) entity;
                return parrot.getVariant().toString();
            }
            case FOX -> {
                Fox fox = (Fox) entity;
                return fox.getFoxType().toString();
            }
            case PANDA -> {
                Panda panda = (Panda) entity;
                if (panda.getMainGene().equals(Panda.Gene.BROWN) && panda.getHiddenGene().equals(Panda.Gene.BROWN)){
                    return Panda.Gene.BROWN.toString();
                }else{
                    return Panda.Gene.NORMAL.toString();
                }
            }
            case SHEEP -> {
                Sheep sheep = (Sheep) entity;
                if (sheep.getColor() == null)return DyeColor.WHITE.toString();
                return sheep.getColor().toString();
            }
            case MUSHROOM_COW -> {
                MushroomCow mushroomCow = (MushroomCow) entity;
                return mushroomCow.getVariant().toString();
            }
            case FROG -> {
                Frog frog = (Frog) entity;
                return frog.getVariant().toString();
            }
            case VILLAGER -> {
                Villager villager = (Villager) entity;
                return villager.getVillagerType().toString();
            }
            case ZOMBIE_VILLAGER -> {
                ZombieVillager zombieVillager = (ZombieVillager) entity;
                return zombieVillager.getVillagerType().toString();
            }
        }
        return null;
    }


}
