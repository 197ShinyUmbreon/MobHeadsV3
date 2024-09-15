package io.github.shinyumbreon197.mobheadsv3;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeadStorage implements Listener {
    private static final MobHeadsV3 plugin = MobHeadsV3.getPlugin();
    private static final String storageString = MobHeadsV3.getPluginNameColored() + ChatColor.BLUE + "MobHead Storage";
    /*
     Head storage that could open on Right-Click of the Helmet slot.
     Would open a Double Chest size inventory. Right-Clicking a head will move it to and from the storage and Player's inventory.
     Left-Clicking a head inside the Storage would equip that head, moving the replaced head to storage.
     Left-Clicking on an empty space will unequip the head, putting it in storage.
     The storage data will be UUID based, saved as a serialized Map<Integer,UUID> in a Player-specific .yml.
     */
    @EventHandler
    public static void headStorageActivation(InventoryClickEvent ice){
        if (ice.getViewers().size() == 0)return;
        Player player = (Player) ice.getViewers().get(0);
        if (player == null)return;
        ClickType clickType = ice.getClick();
        if (!clickType.equals(ClickType.RIGHT))return;
        Inventory inv = ice.getClickedInventory();
        if (inv == null)return;
        InventoryType type = inv.getType();
        if (!type.equals(InventoryType.PLAYER))return;
        if (ice.getSlot() != 39)return; //helmet slot
        ice.setCancelled(true);
        openHeadStorageInventoryDelayed(player);
    }
    @EventHandler
    public static void headStorageInteract(InventoryClickEvent ice){
        if (!ice.getView().getTitle().contains(storageString))return;
        if (ice.getViewers().size() == 0)return;
        Player player = (Player) ice.getViewers().get(0);
        if (player == null)return;
        ice.setCancelled(true);
        Inventory inv = ice.getClickedInventory();
        if (inv == null)return;
        InventoryType type = inv.getType();
        ItemStack clickedItem = ice.getCurrentItem();
        int slot = ice.getSlot();
        if (clickedItem != null && clickedItem.getType().equals(Material.BARRIER)){
            player.closeInventory();
            return;
        }
        boolean isHead = MobHead.skullItemIsMobHead(clickedItem);
        ClickType clickType = ice.getClick();
        if (clickType.equals(ClickType.RIGHT)){
            if (!isHead)return;
            Inventory source;
            Inventory destination;
            boolean limitOne;
            if (type.equals(InventoryType.CHEST)){
                source = ice.getView().getTopInventory();
                destination = ice.getView().getBottomInventory();
                limitOne = false;
            }else if (type.equals(InventoryType.PLAYER)){
                source = ice.getView().getBottomInventory();
                destination = ice.getView().getTopInventory();
                limitOne = true;
            }else return;
            if (!moveItemToTargetInventory(source, slot, destination, limitOne))return;
            saveHeadStorageInventory(player, ice.getView().getTopInventory());
            openHeadStorageInventoryDelayed(player);
        }else if (clickType.equals(ClickType.LEFT)){
            if (!type.equals(InventoryType.CHEST))return;
            ItemStack newHead;
            if (isHead){
                newHead = clickedItem.clone();
                clickedItem.setAmount(clickedItem.getAmount() - 1);
                inv.setItem(slot, clickedItem);
                saveHeadStorageInventory(player, ice.getView().getTopInventory());
            }else{
                newHead = null;
            }
            equipHeadToPlayer(player, newHead);
            player.closeInventory();
        }
    }
    private static void equipHeadToPlayer(Player player, ItemStack head){
        PlayerInventory pi = player.getInventory();
        ItemStack helmet = pi.getHelmet();
        if (MobHead.skullItemIsMobHead(helmet)){
            Inventory headStorage = playerHeadStorageInventory(player);
            for (ItemStack dropped:headStorage.addItem(helmet).values()) player.getWorld().dropItem(player.getLocation(),dropped);
            saveHeadStorageInventory(player, headStorage);
        }else if (helmet != null){
            Map<Integer,ItemStack> dropMap = pi.addItem(helmet);
            if (dropMap.size() != 0){
                Hud.headsUp(player, ChatColor.RED + "Your helmet was dropped!");
                for (ItemStack dropped:dropMap.values()) player.getWorld().dropItem(player.getLocation(),dropped);
            }
        }
        pi.setHelmet(head);
        AVFX.playHeadStorageEquipEffect(player.getLocation());
    }
    private static void saveHeadStorageInventory(Player player, Inventory headStorage){
        Map<Integer,UUID> storageMap = new HashMap<>();
        ItemStack[] storageContents = headStorage.getContents();
        for (int i = 0; i < storageContents.length; i++) {
            ItemStack itemStack = storageContents[i];
            if (itemStack == null)continue;
            UUID uuid = MobHead.getUUIDFromMobHeadItem(itemStack);
            if (uuid == null)continue;
            storageMap.put(i, uuid);
        }
        setPlayerHeadStorageMap(player, storageMap);
    }
    private static boolean moveItemToTargetInventory(Inventory source, int slot, Inventory destination, boolean limitOne){
        ItemStack[] sourceItems = source.getStorageContents();
        ItemStack target = sourceItems[slot];
        if (target == null)return false;
        ItemStack[] destinationItems = destination.getStorageContents();
        int first = -1;
        boolean merge = false;
        for (int i = 0; i < destinationItems.length; i++) {
            ItemStack itemStack = destinationItems[i];
            boolean similar = !limitOne && target.isSimilar(itemStack);
            if (similar && itemStack.getAmount() < itemStack.getMaxStackSize()){
                first = i;
                merge = true;
                break;
            }else if (itemStack == null && first == -1){
                first = i;
            }
        }
        if (first == -1)return false;
        ItemStack singleTarget = target.clone();
        singleTarget.setAmount(1);
        int remainder = target.getAmount() - 1;
        if (remainder == 0){
            target = null;
        }else target.setAmount(remainder);
        sourceItems[slot] = target;
        source.setStorageContents(sourceItems);
        ItemStack destinationTarget = destinationItems[first];
        if (merge){
            int destAmount = destinationTarget.getAmount();
            int targetAmount = singleTarget.getAmount();
            int finalAmount = destAmount + targetAmount;
            destinationTarget.setAmount(finalAmount);
            destinationItems[first] = destinationTarget;
        }else{
            destinationItems[first] = singleTarget;
        }
        destination.setStorageContents(destinationItems);
        return true;
    }

    private static void openHeadStorageInventoryDelayed(Player player){
        new BukkitRunnable(){
            @Override
            public void run() {
                AVFX.playOpenHeadStorageEffect(player.getLocation());
                player.openInventory(playerHeadStorageInventory(player));
            }
        }.runTaskLater(plugin, 1);
    }
    private static ItemStack getCloseIcon(){
        ItemStack closeIcon = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = closeIcon.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatColor.RED + "[ Exit Menu ]");
        closeIcon.setItemMeta(itemMeta);
        return closeIcon;
    }
    private static Inventory playerHeadStorageInventory(Player player){
        Map<Integer, UUID> storageMap = getPlayerHeadStorageMap(player);
        Inventory inv = Bukkit.createInventory(null, 54, storageString);
        inv.setItem(53, getCloseIcon());
        for (Integer i: storageMap.keySet()){
            UUID uuid = storageMap.get(i);
            if (uuid == null)continue;
            MobHead mobhead = MobHead.getMobHeadFromUUID(uuid);
            if (mobhead == null)continue;
            ItemStack headItem = mobhead.getHeadItemStack();
            inv.setItem(i, headItem);
        }
        return inv;
    }
    private static final Map<Player, Map<Integer, UUID>> playerHeadStorageInventoryMapMap = new HashMap<>();
    private static Map<Integer, UUID> getPlayerHeadStorageMap(Player player){
        return playerHeadStorageInventoryMapMap.getOrDefault(player, new HashMap<>());
    }
    private static void setPlayerHeadStorageMap(Player player, Map<Integer, UUID> storageMap){
        playerHeadStorageInventoryMapMap.put(player, storageMap);
        updateUserHeadStorageFile(player);
    }
    /*
    Functional. Not currently needed.

    @Nullable
    private static UUID getPlayerHeadStorageSlot(Player player, int slot){
        Map<Integer, UUID> storageMap = getPlayerHeadStorageMap(player);
        assert storageMap != null;
        return storageMap.get(slot);
    }
    private static void setPlayerHeadStorageSlot(Player player, UUID uuid, Integer slot){
        Map<Integer, UUID> storageMap = getPlayerHeadStorageMap(player);
        assert storageMap != null;
        storageMap.put(slot, uuid);
        setPlayerHeadStorageMap(player, storageMap);
    }
    */

    // File handling ---------------------------------------------------------------------------------------------------
    private static void updateUserHeadStorageFile(Player player){
        String uuid = player.getUniqueId().toString();
        File headStorageFile = new File(plugin.getDataFolder() + "/headStorage",uuid + ".yml");
        YamlConfiguration storageData = new YamlConfiguration();
        Map<Integer, UUID> storageMap = getPlayerHeadStorageMap(player);
        for (Integer slot:storageMap.keySet()){
            String headUUID = storageMap.get(slot).toString();
            storageData.set(String.valueOf(slot),headUUID);
        }
        try{
            storageData.save(headStorageFile);
        }catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try{
            headStorageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadAllUsersHeadStorageFromFile(){
        for (Player player:Bukkit.getOnlinePlayers()) loadUserHeadStorageFromFile(player);
    }
    public static void loadUserHeadStorageFromFile(Player player){
        String uuid = player.getUniqueId().toString();
        File headStorageFile = new File(plugin.getDataFolder() + "/headStorage",uuid + ".yml");
        YamlConfiguration storageData = YamlConfiguration.loadConfiguration(headStorageFile);
        Map<Integer, UUID> storageMap = new HashMap<>();
        for (int i = 0; i < 53; i++) {
            String headUUIDString = storageData.getString(String.valueOf(i));
            if (headUUIDString == null)continue;
            UUID headUUID = UUID.fromString(headUUIDString);
            storageMap.put(i,headUUID);
        }
        setPlayerHeadStorageMap(player,storageMap);
    }
    /*
    Functional, not in use.

    // Old Style ---------------------------------------
    private FileConfiguration userHeadStorage = null;

    private File userHeadStorageFile = null;

    public void reloadUserHeadStorage() {
        if (userHeadStorageFile == null) {
            userHeadStorageFile = new File(plugin.getDataFolder(), "uuid.yml"); //replace with player.getUniqueID
        }
        userHeadStorage = YamlConfiguration.loadConfiguration(userHeadStorageFile);

        // Look for defaults in the jar
        Reader defConfigStream = null;
        InputStream inputStream = plugin.getResource("uuid.yml");
        if (inputStream == null)return;
        try {
            defConfigStream = new InputStreamReader(inputStream, "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            userHeadStorage.setDefaults(defConfig);
        }
    }


    public FileConfiguration getUserHeadStorage() {
        if (userHeadStorage == null) {
            reloadUserHeadStorage();
        }
        return userHeadStorage;
    }

    public void saveUserHeadStorage() {
        if (userHeadStorage == null || userHeadStorageFile == null) {
            return;
        }
        try {
            getUserHeadStorage().save(userHeadStorageFile);
        } catch (IOException ex) {
            System.out.println(ex);
            //getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }

    public void saveDefaultUserHeadStorage() {
        if (userHeadStorageFile == null) {
            userHeadStorageFile = new File(plugin.getDataFolder(), "uuid.yml");
        }
        if (!userHeadStorageFile.exists()) {
            plugin.saveResource("uuid.yml", false);
        }
    }
     */

}
