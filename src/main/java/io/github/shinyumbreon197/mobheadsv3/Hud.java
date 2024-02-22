package io.github.shinyumbreon197.mobheadsv3;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Hud {


//    static final List<Player> bars = new ArrayList<>();
//
//    @EventHandler
//    public static void testMove(PlayerMoveEvent e){
//        //newProgressBar(e.getPlayer(), 100, false, "Appendix:");
//
//    }

//    public static void updateShulkerBar(Player player){ // Not in use
//        if (CreatureEvents.isOnLevitationMap(player)){
//            if (!bars.contains(player)){
//                bars.add(player);
//                newProgressBar(player,100,false, "Levitation Time:");
//            }
//        }else{
//            bars.remove(player);
//        }
//    }
//
//    private static void newProgressBar(Player player, int max, boolean fill, String appendix){ // Not in use
//        boolean enabled = bars.contains(player);
//        new BukkitRunnable(){
//            int i = 0;
//            @Override
//            public void run() {
//                System.out.println("i = " + i);
//                if (i >= max){
//                    if (fill){
//                        System.out.println("100%!");
//                        //run success command
//                    }else {
//                        System.out.println("0%!");
//                        //run success command
//                    }
//                }else{
//                    //progressBar(player, max, i, fill, appendix);
//                }
//                if (!enabled || i >= max){
//                    System.out.println("canceled");
//                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder().create());
//                    cancel();
//                    return;
//                }
//                i++;
//            }
//        }.runTaskTimer(MobHeadsV3.getPlugin(), 0, 1);
//    }

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
