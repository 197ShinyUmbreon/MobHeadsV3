package io.github.shinyumbreon197.mobheadsv3.tool;

import io.github.shinyumbreon197.mobheadsv3.HeadData;
import io.github.shinyumbreon197.mobheadsv3.MobHeadsV3;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.util.List;
import java.util.UUID;

public class HeadItem {

    public static ItemStack customHead(String headName, UUID uuid, URL texture) {
        PlayerProfile pp = Bukkit.createPlayerProfile(uuid,headName);
        PlayerTextures pt = pp.getTextures();
        pt.setSkin(texture);
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

    public static UUID getHeadUUID(ItemStack input){
        if (input == null || !input.getType().equals(Material.PLAYER_HEAD))return null;
        SkullMeta skullMeta = (SkullMeta) input.getItemMeta();
        if (skullMeta == null)return null;
        PersistentDataContainer data = skullMeta.getPersistentDataContainer();
        String uuidString = data.get(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING);
        if (uuidString == null)return null;
        return UUID.fromString(uuidString);
    }

    public static boolean isMobHead(ItemStack input){
        if (input == null || !input.getType().equals(Material.PLAYER_HEAD))return false;
        SkullMeta skullMeta = (SkullMeta) input.getItemMeta();
        if (skullMeta == null)return false;
        PersistentDataContainer data = skullMeta.getPersistentDataContainer();
        return data.has(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING);
    }

    public static EntityType getHeadEntityType(ItemStack input){
        if (!isMobHead(input))return null;
        UUID uuid = getHeadUUID(input);
        return HeadData.entityTypeLookupMap.get(uuid);
    }

    public static String getHeadVariant(ItemStack input){
        if (!isMobHead(input))return null;
        UUID uuid = getHeadUUID(input);
        return HeadData.variantLookupMap.get(uuid);
    }

    public static ItemStack getHeadFromEntity(Entity entity){
        if (!HeadData.entityTypes.contains(entity.getType()))return null;
        EntityType entityType = entity.getType();
        if (HeadData.vanillaHeadMap().containsKey(entityType)) return HeadData.vanillaHeadMap().get(entityType);
        List<UUID> uuids = HeadData.uuidFromEntityTypeMap.get(entityType);
        if (uuids.size() == 0){
            return null;
        }else if (uuids.size() == 1){
            return HeadData.headItemLookupMap.get(uuids.get(0));
        }else{
            String variant = getVariantFromEntity(entity);
            if (variant == null)return null;
            for (UUID uuid:uuids){
                if (variant.matches(HeadData.variantLookupMap.get(uuid))) return HeadData.headItemLookupMap.get(uuid);
            }
        }
        return null;
    }

    public static String getVariantFromEntity(Entity entity){
        switch (entity.getType()){
            default -> {return null;}
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
    }


}
