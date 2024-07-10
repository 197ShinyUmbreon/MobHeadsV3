package io.github.shinyumbreon197.mobheadsv3;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Hud {
    private static final Map<Player,List<String>> headsUpQueue = new HashMap<>();

    public static void headsUp(Player player, List<String> strings){
        StringBuilder stringBuilder = new StringBuilder(MobHeadsV3.getPluginNameColored());
        int i = 0;
        for (String string:strings){
            if (i != 0)stringBuilder.append(" ");
            stringBuilder.append(string);
            i++;
        }
        String message = stringBuilder.toString();
        boolean active = headsUpQueue.containsKey(player);
        List<String> queuedMessages = headsUpQueue.getOrDefault(player, new ArrayList<>());
        queuedMessages.add(message);
        headsUpQueue.put(player, queuedMessages);
        if (!active){
            new BukkitRunnable(){
                @Override
                public void run() {
                    cycleHeadsUpQueue(player);
                }
            }.runTaskLater(MobHeadsV3.getPlugin(),1);
        }
    }

    private static void cycleHeadsUpQueue(Player player){
        List<String> messages = headsUpQueue.getOrDefault(player, List.of());
        if (messages.size() == 0)return;
        sendHeadsUp(player, messages.get(0));
        messages.remove(0);
        new BukkitRunnable(){
            @Override
            public void run() {
                List<String> currentMessages = headsUpQueue.getOrDefault(player, List.of());
                if (currentMessages.size() == 0){
                    headsUpQueue.remove(player);
                }else{
                    headsUpQueue.put(player,currentMessages);
                }
                cycleHeadsUpQueue(player);
            }
        }.runTaskLater(MobHeadsV3.getPlugin(), 85);
    }

    private static void sendHeadsUp(Player player, String message){
        int ticks = 80;
        for (int i = 0; i <= ticks; i++) {
            String sentString;
            if (i < ticks){
                sentString = message;
            }else sentString = "";
            new BukkitRunnable(){
                @Override
                public void run() {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(sentString).create());
                }
            }.runTaskLater(MobHeadsV3.getPlugin(),i);
        }
    }

    public static void progressBarEnd(Player player){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder().create());
    }
    public static void progressBar(Player player, double maxValue, double currentValue, boolean descending, String appendix, boolean critical){
        //if (currentValue >= maxValue)return;
        //if (debug) System.out.println("player: " + player.getDisplayName() + " maxValue: " + maxValue + " currentValue: " + currentValue);
        if (maxValue == 0) maxValue = 0.01;
        double percent = currentValue / maxValue;
        //if (debug) System.out.println("percent: " + percent);
        int bars = 50;
        int filledBars;
        int emptyBars;
        if (descending){
            filledBars = (int) Math.ceil(percent * bars);
            emptyBars = bars - filledBars;
        }else{
            emptyBars = (int) Math.ceil(percent * bars);
            filledBars = bars - emptyBars;
        }
        //if (debug) System.out.println("filledBars: " + filledBars + " emptyBars: " + emptyBars);

        char[] filledCharArray = new char[filledBars];
        char[] emptyCharArray = new char[emptyBars];
        char barChar = '|';
        Arrays.fill(filledCharArray, barChar);
        Arrays.fill(emptyCharArray, barChar);

        String filled = String.copyValueOf(filledCharArray);
        String empty = String.copyValueOf(emptyCharArray);

        ChatColor filledColor = ChatColor.LIGHT_PURPLE;
        ChatColor emptyColor = ChatColor.DARK_PURPLE;
        if (critical && percent < 0.3){
            filledColor = ChatColor.RED;
            emptyColor = ChatColor.DARK_RED;
        }
        String bar = ChatColor.GOLD + appendix + " [" + filledColor + filled + emptyColor + empty + ChatColor.GOLD + "]";

        BaseComponent[] baseComponent;
        baseComponent = new ComponentBuilder(bar).create();

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, baseComponent);
    }

}
