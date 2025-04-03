package io.github.shinyumbreon197.mobheadsv3.event;

import io.github.shinyumbreon197.mobheadsv3.entity.Summon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkUnload implements Listener {

    @EventHandler
    public static void onChunkUnload(ChunkUnloadEvent cue){
//        Entity[] unloadingEntities = cue.getChunk().getEntities();
//        for (Entity entity:unloadingEntities){
//            Summon summon = Summon.getSummonFromEntity(entity);
//            if (summon != null) Summon.removeSummon(summon);
//        }
    }

}

/*
[18:25:24] [Server thread/ERROR]: Could not pass event ChunkUnloadEvent to MobHeadsV3 v1.0-SNAPSHOT
org.bukkit.event.EventException: null
	at org.bukkit.plugin.java.JavaPluginLoader$1.execute(JavaPluginLoader.java:310) ~[spigot-api-1.21.4-R0.1-SNAPSHOT.jar:?]
	at org.bukkit.plugin.RegisteredListener.callEvent(RegisteredListener.java:70) ~[spigot-api-1.21.4-R0.1-SNAPSHOT.jar:?]
	at org.bukkit.plugin.SimplePluginManager.fireEvent(SimplePluginManager.java:601) ~[spigot-api-1.21.4-R0.1-SNAPSHOT.jar:?]
	at org.bukkit.plugin.SimplePluginManager.callEvent(SimplePluginManager.java:588) ~[spigot-api-1.21.4-R0.1-SNAPSHOT.jar:?]
	at net.minecraft.world.level.chunk.Chunk.unloadCallback(Chunk.java:625) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.server.level.PlayerChunk.lambda$callEventIfUnloading$7(PlayerChunk.java:341) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.server.level.PlayerChunkMap$CallbackExecutor.run(PlayerChunkMap.java:171) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.server.level.PlayerChunk.callEventIfUnloading(PlayerChunk.java:351) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.server.level.ChunkMapDistance.a(ChunkMapDistance.java:124) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.server.level.ChunkProviderServer.s(ChunkProviderServer.java:304) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.server.level.ChunkProviderServer$b.B(ChunkProviderServer.java:681) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.server.level.ChunkProviderServer.d(ChunkProviderServer.java:300) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.server.MinecraftServer.bv(MinecraftServer.java:1307) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.server.MinecraftServer.B(MinecraftServer.java:1291) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.util.thread.IAsyncTaskHandler.b(SourceFile:147) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.server.MinecraftServer.b(MinecraftServer.java:1248) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.server.MinecraftServer.x_(MinecraftServer.java:1258) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.server.MinecraftServer.y(MinecraftServer.java:1101) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.server.MinecraftServer.lambda$spin$0(MinecraftServer.java:329) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at java.base/java.lang.Thread.run(Thread.java:1570) [?:?]
Caused by: java.lang.NullPointerException: Cannot invoke "it.unimi.dsi.fastutil.objects.ReferenceArrayList.get(int)" because "this.wrapped" is null
	at it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet$SetIterator.next(ReferenceOpenHashSet.java:520) ~[fastutil-8.5.15.jar:?]
	at net.minecraft.server.level.ChunkMapDistance.a(ChunkMapDistance.java:123) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.server.level.ChunkProviderServer.s(ChunkProviderServer.java:304) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.server.level.ChunkProviderServer.c(ChunkProviderServer.java:261) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.server.level.ChunkProviderServer.a(ChunkProviderServer.java:160) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at net.minecraft.world.level.World.a(World.java:299) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at org.bukkit.craftbukkit.v1_21_R3.CraftWorld.getChunkAt(CraftWorld.java:228) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at org.bukkit.craftbukkit.v1_21_R3.CraftChunk.getEntities(CraftChunk.java:127) ~[spigot-1.21.4-R0.1-SNAPSHOT.jar:4431-Spigot-d421948-4f3946f]
	at io.github.shinyumbreon197.mobheadsv3.event.ChunkUnload.onChunkUnload(ChunkUnload.java:14) ~[?:?]
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103) ~[?:?]
	at java.base/java.lang.reflect.Method.invoke(Method.java:580) ~[?:?]
	at org.bukkit.plugin.java.JavaPluginLoader$1.execute(JavaPluginLoader.java:306) ~[spigot-api-1.21.4-R0.1-SNAPSHOT.jar:?]
	... 19 more
 */
