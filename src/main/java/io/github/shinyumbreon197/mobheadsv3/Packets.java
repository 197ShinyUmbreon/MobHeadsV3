package io.github.shinyumbreon197.mobheadsv3;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedParticle;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class Packets {

    //private static final WrappedDataWatcher.Serializer byteSerializer = WrappedDataWatcher.Registry.get(Byte.class);
    private static ProtocolManager pm;
    public static void initialize(){
        pm = ProtocolLibrary.getProtocolManager();
    }

//    public static void makePlayerJump(Player player){
//        PacketContainer packet = pm.createPacket(PacketType.Play.Client.ENTITY_ACTION);
//
//    }
    public static void susParticles(Player player, Location origin){
        float red = 255 / 255f;
        float green = 60 / 255f;
        float blue = 10 / 255f;
        PacketContainer packet = pm.createPacket(PacketType.Play.Server.WORLD_PARTICLES);
        packet.getNewParticles().write(0, WrappedParticle.create(Particle.SPELL_MOB, null));
        packet.getIntegers().write(0,0);
        packet.getDoubles()
                .write(0, origin.getX()) // Loc X
                .write(1, origin.getY()) // Loc Y
                .write(2, origin.getZ()); // Loc Z
        packet.getFloat()
                .write(0, red) // Offset X // Red
                .write(1, green) // Offset Y // Green
                .write(2, blue) // Offset Z // Blue
                .write(3, 1f); // Speed // Must be 1f
        pm.sendServerPacket(player,packet);
    }

    private static final WrappedDataValue entityDataClearAll = new WrappedDataValue(0,WrappedDataWatcher.Registry.get(Byte.class), (byte) 0x00);
    private static final WrappedDataValue entityDataGlow = new WrappedDataValue(0, WrappedDataWatcher.Registry.get(Byte.class), (byte) 0x40);

    private static final Map<Player, List<Entity>> glowingEnts = new HashMap<>();
    public static void nearbyGlow(Player player, int halfRadius){
        List<Entity> wereNearby;
        if (glowingEnts.containsKey(player)){
            wereNearby = glowingEnts.get(player);
        }else wereNearby = new ArrayList<>();
        List<Entity> nearbyEnts = player.getNearbyEntities(halfRadius,halfRadius,halfRadius);
        nearbyEnts.add(player);
        for (Entity nearby:nearbyEnts){
            toggleGlow(player,nearby,true);
        }
        wereNearby.removeAll(nearbyEnts);
        for (Entity were:wereNearby){
            toggleGlow(player,were,false);
        }
        glowingEnts.put(player,nearbyEnts);
    }
    public static void removeGlow(Player player){
        if (!glowingEnts.containsKey(player))return;
        for (Entity remove:glowingEnts.get(player)){
            toggleGlow(player,remove,false);
        }
        glowingEnts.remove(player);
    }
    public static void toggleGlow(Player player, Entity target, boolean enabled){
        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        WrappedDataValue value;
        if (enabled){
            value = entityDataGlow;
        }else value = entityDataClearAll;
        packet.getDataValueCollectionModifier().write(0, Collections.singletonList(value));
        packet.getIntegers().write(0, target.getEntityId());
        pm.sendServerPacket(player,packet);
    }
//                  THIS CODE WORKS VVVVVVVVVVVVVV
//    public static void blindEffect(Player player, boolean enable){
//        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ENTITY_EFFECT);
//        packet.getIntegers()
//                .write(0, player.getEntityId())
//                .write(1, -1);
//        packet.getEffectTypes().writeDefaults().write(0,PotionEffectType.BLINDNESS);
//        pm.sendServerPacket(player, packet);
//    }

//    @EventHandler
//    public static void test(PlayerDropItemEvent e){
//        Player player = e.getPlayer();
//        testPacket(player);
//    }

//    public  static void frogEdibleUpdatePacket(Player player, LivingEntity target){
//        int targetID = target.getEntityId();
//        MobEffect glowing = MobEffect.byId(24);
//        System.out.println("glowing = "+glowing); //debug
//        if (glowing == null)return;
//        MobEffectInstance mobEffectInstance = new MobEffectInstance(
//               glowing , -1, 0, false, true, false
//        );
//
//        ClientboundUpdateMobEffectPacket packet = new ClientboundUpdateMobEffectPacket(
//                targetID, mobEffectInstance
//        );
//        sendPacket(player, packet);
//    }
//
//    public static void frogEdibleUpdatePacket1(Player player, LivingEntity target){
//        int targetID = target.getEntityId();
//        CraftLivingEntity craftTarget = ((CraftLivingEntity) target);
//
//        SynchedEntityData data = new SynchedEntityData(craftTarget.getHandle());
//        //data.set(new EntityDataAccessor<>(targetID, ));
//
//        List<SynchedEntityData.DataValue<?>> dataValues = data.getNonDefaultValues();
//        System.out.println(dataValues); //debug
//
//        ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(
//                targetID, dataValues
//        );
//
//
//    }
//    public static void frogEdibleUpdatePacket2(Player player, LivingEntity target){
//        //((CraftPlayer) player).getHandle().;
//    }
//    public  static void frogEdibleRemovePacket(Player player, LivingEntity target){
//        for (PotionEffect potionEffect: target.getActivePotionEffects()){
//            if (potionEffect.getType().equals(PotionEffectType.GLOWING))return;
//        }
//        int targetID = target.getEntityId();
//        MobEffect glowing = MobEffect.byId(24);
//        if (glowing == null)return;
//
//        ClientboundRemoveMobEffectPacket packet = new ClientboundRemoveMobEffectPacket(targetID, glowing);
//        sendPacket(player, packet);
//    }
//
//    private static void testPacket(Player player){
//        ResourceLocation sound = new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, Sound.BLOCK_BEACON_ACTIVATE.getKey().getKey());
//        SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(sound);
//        Holder<SoundEvent> holder = Holder.direct(soundEvent);
//        SoundSource soundSource = SoundSource.AMBIENT;
//        int x = player.getLocation().getBlockX();
//        int y = player.getLocation().getBlockY();
//        int z = player.getLocation().getBlockZ();
//
//        Random random = new Random();
//        ClientboundSoundPacket packet = new ClientboundSoundPacket(holder, soundSource, x, y, z, 1.0f, 1.0f, random.nextLong());
//
//        sendPacket(player, packet);
//    }
//
//    private static void sendPacket(Player player, net.minecraft.network.protocol.Packet<?> packet){
//        ServerGamePacketListenerImpl listener = ((CraftPlayer)player).getHandle().connection;
//        listener.send(packet);
//    }

}
