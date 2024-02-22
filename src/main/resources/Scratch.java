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
        case PANDA -> {}
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

        double x = velocity.getX()*10;
        double y = velocity.getY()*2;
        double z = velocity.getZ()*10;
        if (x > 0.5) x = 0.5; if (x < -0.5) x = -0.5;
        if (z > 0.5) z = 0.5; if (z < -0.5) z = -0.5;

private static void runStandby(){
        new BukkitRunnable(){
@Override
public void run() {
        List<Mob> remove = new ArrayList<>();
        List<Mob> foundTarget = new ArrayList<>();
        for (Mob summon:standby){
        if (summon == null)continue;
        if (summon.isDead()) {
        remove.add(summon);
        continue;
        }
        if (summon.getTarget() != null){
        foundTarget.add(summon);
        continue;
        }
        LivingEntity owner = (LivingEntity) getSummonOwner(summon);
        if (owner == null){
        remove.add(summon);
        continue;
        }
        LivingEntity target = findNewTarget(owner,summon);
        if (target == null){
        remove.add(summon);
        continue;
        }else foundTarget.add(summon);
        }
        for (Mob found:foundTarget){
        standby.remove(found);
        }
        removeSummons(remove);
        }
        }.runTaskLater(MobHeadsV3.getPlugin(), 20);
        }

        double fromX = from.getX();
        double fromY = from.getY();
        double fromZ = from.getZ();
        double toX = to.getX();
        double toY = to.getY();
        double toZ = to.getZ();
        double xDif = 0;
        double yDif = 0;
        double zDif = 0;
        if (toX > fromX){xDif = toX - fromX;
        }else{xDif = (toX - fromX)*-1;}
        if (toY > fromY){yDif = toY - fromY;
        }else{yDif = (toY - fromY)*-1;}
        if (toZ > fromZ){zDif = toZ - fromZ;
        }else{zDif = (toZ - fromZ)*-1;}

        System.out.println("xDif: "+xDif);
        System.out.println("yDif: "+yDif);
        System.out.println("zDif: "+zDif);

        if (xDifAbs > zDifAbs && xDifAbs > yDifAbs){
        if (xDif >= 0){
        if (isOrdinal){
        if (zDif < 0){
        moving = BlockFace.SOUTH_EAST;
        }else moving = BlockFace.NORTH_EAST;
        }else moving = BlockFace.EAST;
        }else{
        if (isOrdinal){
        if (zDif < 0){
        moving = BlockFace.SOUTH_WEST;
        }else moving = BlockFace.NORTH_WEST;
        }else moving = BlockFace.WEST;
        }
        }else if (zDifAbs > xDifAbs && zDifAbs > yDifAbs){
        if (zDif >= 0){
        moving = BlockFace.SOUTH;
        }else{
        moving = BlockFace.NORTH;
        }
        }else{
        if (yDif >= 0){
        moving = BlockFace.UP;
        }else{
        moving = BlockFace.DOWN;
        }
        }

private static Map<EntityType,List<List<Integer>>> entityTypePotionEffectListMap(){
        Map<EntityType,List<List<Integer>>> map = new HashMap<>();

        map.put(EntityType.ZOMBIE, List.of(List.of(17,0,-1), List.of(2,0,-1)));
        map.put(EntityType.WITHER_SKELETON, List.of(List.of(20,0,-1)));
        map.put(EntityType.CHICKEN, List.of(List.of(28,0,-1)));
        map.put(EntityType.DOLPHIN, List.of(List.of(30,0,-1)));
        map.put(EntityType.COD, List.of(List.of(1, 0, -1)));
        map.put(EntityType.SALMON, List.of(List.of(1, 0, -1)));
        map.put(EntityType.PUFFERFISH, List.of(List.of(1, 0, -1)));
        map.put(EntityType.TROPICAL_FISH, List.of(List.of(1, 0, -1)));
        map.put(EntityType.SQUID, List.of(List.of(1, 0, -1))); //

        return map;
        }

private static Map<Integer, PotionEffectType> potionTypeMap;
private static Map<Integer, PotionEffectType> getPotionTypeMap(){
        if (potionTypeMap != null)return potionTypeMap;
        potionTypeMap = buildPotionTypeMap();
        return getPotionTypeMap();
        }
private static Map<Integer, PotionEffectType> buildPotionTypeMap(){
        Map<Integer, PotionEffectType> map = new HashMap<>();
        map.put(1,PotionEffectType.SPEED);
        map.put(2,PotionEffectType.SLOW);
        map.put(3,PotionEffectType.FAST_DIGGING);
        map.put(4,PotionEffectType.SLOW_DIGGING);
        map.put(5,PotionEffectType.INCREASE_DAMAGE);
        map.put(6,PotionEffectType.HEAL);
        map.put(7,PotionEffectType.HARM);
        map.put(8,PotionEffectType.JUMP);
        map.put(9,PotionEffectType.CONFUSION);
        map.put(10,PotionEffectType.REGENERATION);
        map.put(11,PotionEffectType.DAMAGE_RESISTANCE);
        map.put(12,PotionEffectType.FIRE_RESISTANCE);
        map.put(13,PotionEffectType.WATER_BREATHING);
        map.put(14,PotionEffectType.INVISIBILITY);
        map.put(15,PotionEffectType.BLINDNESS);
        map.put(16, PotionEffectType.NIGHT_VISION);
        map.put(17,PotionEffectType.HUNGER);
        map.put(18,PotionEffectType.WEAKNESS);
        map.put(19,PotionEffectType.POISON);
        map.put(20,PotionEffectType.WITHER);
        map.put(21,PotionEffectType.HEALTH_BOOST);
        map.put(22,PotionEffectType.ABSORPTION);
        map.put(23,PotionEffectType.SATURATION);
        map.put(24,PotionEffectType.GLOWING);
        map.put(25,PotionEffectType.LEVITATION);
        map.put(26,PotionEffectType.LUCK);
        map.put(27,PotionEffectType.UNLUCK);
        map.put(28,PotionEffectType.SLOW_FALLING);
        map.put(29,PotionEffectType.CONDUIT_POWER);
        map.put(30,PotionEffectType.DOLPHINS_GRACE);
        map.put(31,PotionEffectType.BAD_OMEN);
        map.put(32,PotionEffectType.HERO_OF_THE_VILLAGE);
        map.put(33,PotionEffectType.DARKNESS);
        return map;
        }
private static Map<PotionEffectType,Integer> potionTypeNumberMap;
private static Map<PotionEffectType,Integer> getPotionTypeNumberMap(){
        if (potionTypeNumberMap != null)return potionTypeNumberMap;
        potionTypeNumberMap = buildPotionTypeNumberMap();
        return getPotionTypeNumberMap();
        }
private static Map<PotionEffectType,Integer> buildPotionTypeNumberMap(){
        Map<PotionEffectType,Integer> map = new HashMap<>();
        for (int i = 0; i < getPotionTypeMap().size(); i++) {
        int value = i + 1;
        PotionEffectType type = potionTypeMap.get(value);
        map.put(type,value);
        }
        return map;
        }

private static PotionEffectType getPotionTypeFromNumber(int i){
        if (!getPotionTypeMap().containsKey(i))return PotionEffectType.CONFUSION;
        return getPotionTypeMap().get(i);
        }
private static List<PotionEffectType> getPotionTypesFromNumberList(List<Integer> values){
        List<PotionEffectType> types = new ArrayList<>();
        for (Integer value:values){
        types.add(getPotionTypeFromNumber(value));
        }
        return types;
        }
private static int getNumberFromPotionType(PotionEffectType type){
        if (!getPotionTypeNumberMap().containsKey(type))return -1;
        return getPotionTypeNumberMap().get(type);
        }
private static List<Integer> getNumbersFromPotionTypes(List<PotionEffectType> types){
        List<Integer> values = new ArrayList<>();
        for (PotionEffectType type:types){
        values.add(getNumberFromPotionType(type));
        }
        return values;
        }


private static void writePotionEffectToEntity(Entity target, PotionEffectType type, int strength, int duration){
        writePotionEffectsToEntity(target, List.of(type), List.of(strength), List.of(duration));
        }
private static void writePotionEffectsToEntity(Entity target, List<PotionEffectType> types, List<Integer> strengths, List<Integer> durations){
        PersistentDataContainer data = target.getPersistentDataContainer();

        List<Integer> typesValues = getNumbersFromPotionTypes(types);
        int[] typesValuesArray = new int[typesValues.size()];
        for (int i = 0; i < typesValues.size(); i++) {
        typesValuesArray[i] = typesValues.get(i);
        }
        data.set(type, PersistentDataType.INTEGER_ARRAY,typesValuesArray);

        int[] strengthsArray = new int[strengths.size()];
        for (int i = 0; i < strengths.size(); i++) {
        strengthsArray[i] = strengths.get(i);
        }
        data.set(strength,PersistentDataType.INTEGER_ARRAY,strengthsArray);

        int[] durationsArray = new int[durations.size()];
        for (int i = 0; i < durations.size(); i++) {
        durationsArray[i] = durations.get(i);
        }
        data.set(duration,PersistentDataType.INTEGER_ARRAY,durationsArray);
        }
private static List<PotionEffect> readPotionEffectsFromEntity(Entity target){
        List<PotionEffect> effects = new ArrayList<>();
        PersistentDataContainer data = target.getPersistentDataContainer();
        boolean hasTypeData = data.has(type,PersistentDataType.INTEGER_ARRAY);
        boolean hasStrengthData = data.has(strength,PersistentDataType.INTEGER_ARRAY);
        boolean hasDurationData = data.has(duration,PersistentDataType.INTEGER_ARRAY);
        if (!hasTypeData || !hasStrengthData || !hasDurationData) return effects;

        int[] typeValuesArray = data.get(type,PersistentDataType.INTEGER_ARRAY);
        assert typeValuesArray != null;
        List<Integer> typeValues = new ArrayList<>();
        for (int value:typeValuesArray){
        typeValues.add(value);
        }

        int[] strengthArray = data.get(strength,PersistentDataType.INTEGER_ARRAY);
        assert strengthArray != null;
        List<Integer> strengths = new ArrayList<>();
        for (int strength:strengthArray){
        strengths.add(strength);
        }

        int[] durationArray = data.get(duration,PersistentDataType.INTEGER_ARRAY);
        assert durationArray != null;
        List<Integer> durations = new ArrayList<>();
        for (int duration:durationArray){
        durations.add(duration);
        }

        int amount = typeValues.size();
        for (int i = 0; i < amount; i++) {
        PotionEffectType pet = getPotionTypeFromNumber(typeValues.get(i));
        int s = strengths.get(i);
        int d = durations.get(i);
        effects.add(new PotionEffect(pet,d,s,false,false,true));
        }
        return effects;
        }
private static void removePotionEffectDataFromEntity(Entity target){
        PersistentDataContainer data = target.getPersistentDataContainer();
        data.remove(type);
        data.remove(strength);
        data.remove(duration);
        }

public static void initialize(){
        populateHeadMatList();
        foodMats = buildFoodMats();
        for (MobHead mobHead:mobHeads){
        registerHead(mobHead);
        }
        }

public static void registerHead(MobHead mobHead){
        if (!mobHeads.contains(mobHead)) mobHeads.add(0, mobHead);
        mobHeadByUUID.put(mobHead.getUuid(), mobHead);
        if (!entityTypes.contains(mobHead.getEntityType())) entityTypes.add(mobHead.getEntityType());
        if (mobHead.getDisplayName() == null) vanillaHeadUUIDs.put(mobHead.getEntityType(), mobHead.getUuid());
        }

private static final List<MobHead> mobHeads = new ArrayList<>();
public static List<MobHead> getMobHeads(){
        return mobHeads;
        }
public static void addMobHead(MobHead mobHead){
        mobHeads.add(mobHead);
        }
public static void addMobHeads(List<MobHead> mobHeads){
        for (MobHead mobHead:mobHeads){
        addMobHead(mobHead);
        }
        }
public static Map<UUID, MobHead> mobHeadByUUID = new HashMap<>();
public static Map<EntityType, UUID> vanillaHeadUUIDs = new HashMap<>();

        int maxStack = overflow.getMaxStackSize();
        int overflowCount = overflow.getAmount();
        ItemStack[] contents = inv.getStorageContents();
        int firstEmpty = -1;
        for (int i = 0; i < contents.length; i++) {
        ItemStack slotItem = contents[i];
        if (slotItem == null && firstEmpty == -1){
        firstEmpty = i;
        }
        if (slotItem == null)continue;
        if (!slotItem.isSimilar(overflow))continue;
        int matchCount = slotItem.getAmount();
        if (matchCount == maxStack)continue;
        int newCount = matchCount + overflowCount;
        if (newCount > maxStack){
        int space = maxStack - matchCount;
        overflowCount = overflowCount - space;
        newCount = maxStack;
        }
        slotItem.setAmount(newCount);

        contents[i] = slotItem;
        }
        if (overflowCount > 0){
        overflow.setAmount(overflowCount);
        contents[firstEmpty] = overflow;
        }
        inv.setStorageContents(contents);


        new BukkitRunnable(){
@Override
public void run() {
        boolean sneaking = player.isSneaking();
        boolean continuous = enderDragonContinuousBoost.contains(player);
        boolean allowBoost = player.isGliding() && (((continuous && sneaking) || (sneaking && enderDragonBoostAboveMinimum(player))));
        if (!sneaking){
        enderDragonContinuousBoost.remove(player);
        }else if (enderDragonBoostAboveMinimum(player)){
        enderDragonContinuousBoost.add(player);
        }
        int boost = getEnderDragonBoostValue(player);
        if (boost == 0){
        enderDragonContinuousBoost.remove(player);
        allowBoost = false;
        }
        boolean onMap = isOnEnderDragonBoostMap(player);
        if (debug) System.out.println("enderDragonBoostClock() -> run():\nboost: " + boost + "\nonMap: " + onMap); //debug
        MobHead mobHead = MobHead.getMobHeadWornByEntity(player);
        if (mobHead == null || !mobHead.getEntityType().equals(EntityType.ENDER_DRAGON) || !onMap || (!player.isGliding() && boost == enderDragonMaxBoost)){
        if (onMap) removeFromEnderDragonBoostMap(player);
        Hud.progressBarEnd(player);
        cancel();
        return;
        }
        boost++;
        if (boost > enderDragonMaxBoost) boost = enderDragonMaxBoost;
        updateEnderDragonBoostMap(player,boost);

        Hud.progressBar(player,enderDragonMaxBoost,boost,true,"Boost Charge:",true);
        if (debug) System.out.println("sneaking: " + player.isSneaking()); //debug
        if (allowBoost){
        if (debug) System.out.println("-> enderDragonElytraBoost() "); //debug
        enderDragonElytraBoost(player, false);
        if (!continuous) playEnderDragonBoostSound(player);
        }
        }
        }.runTaskTimer(MobHeadsV3.getPlugin(),0,1);

public static void slimeBounce(LivingEntity target){
        Vector lastVel = slimeLastVelocity.get(target);
        if (debug) System.out.println("slimeBounce() lastVel: " + lastVel); //debug
        if (lastVel == null)return;
        double x = lastVel.getX()*5;
        double z = lastVel.getZ()*5;
        double y = Math.abs(lastVel.getY()*0.8);
        Vector velocity = new Vector(x,y,z);
        target.setVelocity(velocity);
        //AVFX.playSlimeBounceEffect();
        }

public static void doRightClick(Player player) {
        ServerPlayer serverPlayer = null;
        try {
        serverPlayer = (ServerPlayer) player.getClass().getMethod("getHandle").invoke(player);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        e.printStackTrace();
        }
        InteractionHand hand = getHandWithRod(player);
        if (hand == null) return;
        serverPlayer.gameMode.useItem(serverPlayer, serverPlayer.getLevel(), serverPlayer.getItemInHand(hand), hand);
        serverPlayer.swing(hand, true);
        }

private static InteractionHand getHandWithRod(Player player) {
        return player.getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD) ?
        InteractionHand.MAIN_HAND : player.getInventory().getItemInOffHand().getType().equals(Material.FISHING_ROD) ?
        InteractionHand.OFF_HAND : null;
        }

public static void milkCow(Player milkingPlayer, LivingEntity milkedEnt){
        if (isOnMilkingCooldown(milkingPlayer))return;
        ItemStack mainHand = milkingPlayer.getInventory().getItemInMainHand();
        ItemStack offHand = milkingPlayer.getInventory().getItemInOffHand();
        EquipmentSlot hand = EquipmentSlot.HAND;
        ItemStack bucketItem = milkingPlayer.getItemInUse();
        if (mainHand.getType().equals(Material.BUCKET)){
        bucketItem = mainHand;
        }else if (offHand.getType().equals(Material.BUCKET)){
        bucketItem = offHand;
        hand = EquipmentSlot.OFF_HAND;
        }
        Location location = milkedEnt.getLocation();
        if (bucketItem == null){
        List<PotionEffectType> types = new ArrayList<>();
        for (PotionEffect effect:milkingPlayer.getActivePotionEffects())types.add(effect.getType());
        for (PotionEffectType type:types) milkingPlayer.removePotionEffect(type);
        runMilkingCooldown(milkingPlayer);
        AVFX.playCowMilkingSounds(location,false);
        }else if (bucketItem.getType().equals(Material.BUCKET)){
        giveMilkBucket(milkingPlayer, hand);
        runMilkingCooldown(milkingPlayer);
        AVFX.playCowMilkingSounds(location,true);
        }
        }
public static void soupMooshroom(Player soupingPlayer, LivingEntity soupedEnt){
        if (isOnMilkingCooldown(soupingPlayer))return;
        ItemStack mainHand = soupingPlayer.getInventory().getItemInMainHand();
        ItemStack offHand = soupingPlayer.getInventory().getItemInOffHand();
        EquipmentSlot hand = EquipmentSlot.HAND;
        ItemStack bowlItem = soupingPlayer.getItemInUse();
        if (mainHand.getType().equals(Material.BOWL)){
        bowlItem = mainHand;
        }else if (offHand.getType().equals(Material.BOWL)){
        bowlItem = offHand;
        hand = EquipmentSlot.OFF_HAND;
        }
        List<PotionEffect> effects = new ArrayList<>();
        MobHead mobHead = MobHead.getMobHeadWornByEntity(soupedEnt);
        assert mobHead != null;
        boolean brown = mobHead.getVariant().matches("BROWN");
        if (brown) effects = getBrownMooshroomEffects(soupedEnt);
        Location location = soupedEnt.getLocation();
        if (bowlItem == null){
        //Give 2 Hunger && 1 Saturation
        int hunger = soupingPlayer.getFoodLevel();
        float saturation = soupingPlayer.getSaturation();
        if (hunger >= 20)return;
        hunger = hunger + 2;
        if (hunger > 20) hunger = 20;
        saturation = saturation + 1f;
        if (saturation > 20f)saturation = 20f;
        soupingPlayer.setFoodLevel(hunger);
        soupingPlayer.setSaturation(saturation);
        PotionEffectManager.addEffectsToEntity(soupingPlayer, effects);
        runMilkingCooldown(soupingPlayer);
        AVFX.playMooshroomSoupingSounds(location,false);
        }else if (bowlItem.getType().equals(Material.BOWL)){
        giveSoupBowl(soupingPlayer, hand, effects);
        runMilkingCooldown(soupingPlayer);
        AVFX.playMooshroomSoupingSounds(location,true);
        }
        }

        public static final boolean a = config.getBoolean("");

        if (container instanceof Chest){
        if (debug) System.out.println("Container instanceof Chest");
        Chest chest = (Chest) container;
        container.getSnapshotInventory().setContents(items);
        chest.update(true);
        }else if (container instanceof Barrel){
        if (debug) System.out.println("Container instanceof Barrel");
        Barrel barrel = (Barrel) container;
        barrel.getSnapshotInventory().setContents(items);
        barrel.update(true);
        }
