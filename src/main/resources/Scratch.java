public static final Map<UUID, EntityType> entityTypeLookupMap = new HashMap<>();
public static final Map<EntityType, List<UUID>> uuidFromEntityTypeMap = new HashMap<>();
public static final Map<UUID, String> variantLookupMap = new HashMap<>();
public static final Map<UUID, ItemStack> headItemLookupMap = new HashMap<>();
public static final Map<String, UUID> uuidFromNameLookupMap = new HashMap<>();

public static void entityTypeSwitch(EntityType entityType){
        switch (entityType){
        default -> {}
        case PLAYER -> {}

        case ZOMBIE -> {}
        case SKELETON -> {}
        case CREEPER -> {}
        case WITHER_SKELETON -> {}
        case ENDER_DRAGON -> {}

        case COW -> {}
        case PIG -> {}
        case CHICKEN -> {}
        case WOLF -> {}
        case DONKEY -> {}
        case MULE -> {}
        case DOLPHIN -> {}
        case COD -> {}
        case SALMON -> {}
        case PUFFERFISH -> {}
        case TROPICAL_FISH -> {}
        case TURTLE -> {}
        case STRIDER -> {}
        case GOAT -> {}
        case SQUID -> {}
        case BEE -> {}
        case BAT -> {}
        case OCELOT -> {}
        case SNOWMAN -> {}
        case PANDA -> {}
        case POLAR_BEAR -> {}
        case SKELETON_HORSE -> {}
        case ZOMBIE_HORSE -> {}
        case WANDERING_TRADER -> {}
        case IRON_GOLEM -> {}
        case GLOW_SQUID -> {}
        case ALLAY -> {}
        case TADPOLE -> {}

        case SILVERFISH -> {}
        case STRAY -> {}
        case SHULKER -> {}
        case PHANTOM -> {}
        case HUSK -> {}
        case DROWNED -> {}
        case HOGLIN -> {}
        case ZOGLIN -> {}
        case PIGLIN -> {}
        case PIGLIN_BRUTE -> {}
        case WITCH -> {}
        case GUARDIAN -> {}
        case RAVAGER -> {}
        case VEX -> {}
        case EVOKER -> {}
        case SPIDER -> {}
        case ENDERMAN -> {}
        case GHAST -> {}
        case BLAZE -> {}
        case CAVE_SPIDER -> {}
        case MAGMA_CUBE -> {}
        case ZOMBIFIED_PIGLIN -> {}
        case SLIME -> {}
        case ENDERMITE -> {}
        case PILLAGER -> {}
        case VINDICATOR -> {}
        case ILLUSIONER -> {}

        case ELDER_GUARDIAN -> {}
        case WITHER -> {}
        case WARDEN -> {}

        case RABBIT -> {}
        case AXOLOTL -> {}
        case CAT -> {}
        case HORSE -> {}
        case LLAMA -> {}
        case TRADER_LLAMA -> {}
        case PARROT -> {}
        case FOX -> {}
        case SHEEP -> {}
        case MUSHROOM_COW -> {}
        case FROG -> {}
        case VILLAGER -> {}
        case ZOMBIE_VILLAGER -> {}
    }
}

public static void unregisterPlayers(List<Player> players){
        if (players.size() == 0)return;
        List<String> playerUUIDs = new ArrayList<>();
        List<String> headUUIDs = new ArrayList<>();
        List<String> matchedUUIDs = new ArrayList<>();
        for (Player player:players){
        playerUUIDs.add(player.getUniqueId().toString());
        }
        for (MobHead mobHead:HeadData.mobHeads){
        headUUIDs.add(mobHead.getUuid().toString());
        }
        for (String playerUUID:playerUUIDs) {
        if (headUUIDs.contains(playerUUID)) {
        matchedUUIDs.add(playerUUID);
        }
        }
        if (matchedUUIDs.size() == 0)return;
        List<MobHead> headsToRemove = new ArrayList<>();
        for (MobHead mobHead:HeadData.mobHeads){
        String headUUID = mobHead.getUuid().toString();
        if (matchedUUIDs.contains(headUUID)){
        headsToRemove.add(mobHead);
        }
        }
        if (headsToRemove.size() == 0)return;
        HeadData.mobHeads.removeAll(headsToRemove);
        }

        EntityType entityType;
        if (HeadData.playerHeadMats.contains(clickedBlock.getType())){
        Skull skull = (Skull) clickedBlock.getState();
        PersistentDataContainer data = skull.getPersistentDataContainer();
        if (!data.has(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING))return;
        String uuidString = data.get(MobHeadsV3.getPluginNSK(), PersistentDataType.STRING);
        if (uuidString == null)return;
        UUID uuid = UUID.fromString(uuidString);
        entityType = HeadData.mobHeadByUUID.get(uuid).getEntityType();
        }else{
        entityType = HeadData.vanillaMatEntTypeMap().get(clickedBlock.getType());
        }
        if (entityType == null)return;