package io.github.shinyumbreon197.mobheadsv3;

import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;

public class Packets {

//    @EventHandler
//    public static void test(PlayerDropItemEvent e){
//        Player player = e.getPlayer();
//        testPacket(player);
//    }

    public  static void frogEdibleUpdatePacket(Player player, LivingEntity target){
        int targetID = target.getEntityId();
        MobEffect glowing = MobEffect.byId(24);
        System.out.println("glowing = "+glowing); //debug
        if (glowing == null)return;
        MobEffectInstance mobEffectInstance = new MobEffectInstance(
               glowing , -1, 0, false, true, false
        );

        ClientboundUpdateMobEffectPacket packet = new ClientboundUpdateMobEffectPacket(
                targetID, mobEffectInstance
        );
        sendPacket(player, packet);
    }

    public static void frogEdibleUpdatePacket1(Player player, LivingEntity target){
        int targetID = target.getEntityId();
        CraftLivingEntity craftTarget = ((CraftLivingEntity) target);

        SynchedEntityData data = new SynchedEntityData(craftTarget.getHandle());
        //data.set(new EntityDataAccessor<>(targetID, ));

        List<SynchedEntityData.DataValue<?>> dataValues = data.getNonDefaultValues();
        System.out.println(dataValues); //debug

        ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(
                targetID, dataValues
        );


    }
    public static void frogEdibleUpdatePacket2(Player player, LivingEntity target){
        //((CraftPlayer) player).getHandle().;
    }
    public  static void frogEdibleRemovePacket(Player player, LivingEntity target){
        for (PotionEffect potionEffect: target.getActivePotionEffects()){
            if (potionEffect.getType().equals(PotionEffectType.GLOWING))return;
        }
        int targetID = target.getEntityId();
        MobEffect glowing = MobEffect.byId(24);
        if (glowing == null)return;

        ClientboundRemoveMobEffectPacket packet = new ClientboundRemoveMobEffectPacket(targetID, glowing);
        sendPacket(player, packet);
    }

    private static void testPacket(Player player){
        ResourceLocation sound = new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, Sound.BLOCK_BEACON_ACTIVATE.getKey().getKey());
        SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(sound);
        Holder<SoundEvent> holder = Holder.direct(soundEvent);
        SoundSource soundSource = SoundSource.AMBIENT;
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();

        Random random = new Random();
        ClientboundSoundPacket packet = new ClientboundSoundPacket(holder, soundSource, x, y, z, 1.0f, 1.0f, random.nextLong());

        sendPacket(player, packet);
    }

    private static void sendPacket(Player player, net.minecraft.network.protocol.Packet<?> packet){
        ServerGamePacketListenerImpl listener = ((CraftPlayer)player).getHandle().connection;
        listener.send(packet);
    }

}
