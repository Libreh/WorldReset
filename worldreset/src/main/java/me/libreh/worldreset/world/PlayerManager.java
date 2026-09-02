package me.libreh.worldreset.world;

import me.libreh.worldreset.WorldReset;
import me.libreh.worldreset.api.ServerTaskExecutor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class PlayerManager {
    private final MinecraftServer server;
    private final ServerTaskExecutor taskExecutor;

    public PlayerManager(MinecraftServer server, ServerTaskExecutor taskExecutor) {
        this.server = server;
        this.taskExecutor = taskExecutor;
    }

    public void preparePlayerForReset(ServerPlayer player) {
        if (!player.isAlive()) {
            respawnPlayer(player);
        }
        teleportToLobby(player);
    }

    public void teleportToLobby(ServerPlayer player) {
        ServerLevel lobbyWorld = server.getLevel(WorldReset.LOBBY_WORLD);
        if (lobbyWorld == null) {
            WorldReset.LOGGER.warn("Lobby world not found");
            return;
        }
        player.teleportTo(
                lobbyWorld,
                1, 65, 1,
                Set.of(),
                0.0F, 0.0F
        );
    }

    public void teleportToOverworldSpawn(ServerPlayer player, ServerLevel overworld) {
        BlockPos worldSpawnPos = overworld.getSharedSpawnPos();
        Vec3 spawnPos = Vec3.atBottomCenterOf(player.adjustSpawnLocation(overworld, worldSpawnPos));
        player.teleportTo(
                overworld,
                spawnPos.x(),
                spawnPos.y(),
                spawnPos.z(),
                Set.of(),
                0.0F,
                overworld.getSharedSpawnAngle()
        );
        player.setRespawnPosition(overworld.dimension(), worldSpawnPos, 0.0F, true, false);
    }

    // Dead players can't be teleported, so this forges the client's respawn-button packet to force
    // a respawn server-side. Vanilla replaces the ServerPlayer instance during respawn, so the
    // follow-up runs a tick later via the executor and re-reads connection.player to get the new one.
    public void respawnPlayer(ServerPlayer player) {
        var connection = player.connection;
        connection.handleClientCommand(
                new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN)
        );
        taskExecutor.execute(() -> preparePlayerForReset(connection.player));
    }

    public ServerPlayer respawnInto(ServerPlayer player, ServerLevel level, BlockPos spawnPos) {
        player.setRespawnPosition(level.dimension(), spawnPos, 0.0F, true, false);
        var connection = player.connection;
        connection.handleClientCommand(
                new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN)
        );
        return connection.player;
    }

} 