package io.github.adamraichu.dominion.blocks;

import static io.github.adamraichu.dominion.DominionPlugin.LOGGER;

import java.util.Map;
import java.util.Objects;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.EnumCodec;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.StateData;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.chunk.state.TickableBlockState;
import com.hypixel.hytale.server.core.universe.world.meta.BlockState;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import io.github.adamraichu.dominion.DominionPlugin;
import io.github.adamraichu.dominion.systems.Faction;
import io.github.adamraichu.dominion.systems.FactionComponent;

@SuppressWarnings("removal")
public class SacredSiteMarkerBlockState extends BlockState implements TickableBlockState {
    public static final Codec<SacredSiteMarkerBlockState> CODEC = BuilderCodec
            .builder(SacredSiteMarkerBlockState.class, SacredSiteMarkerBlockState::new, BlockState.BASE_CODEC)
            // .append(new KeyedCodec<>("MarkerReference", PersistentRef.CODEC), (spawn, o)
            // -> spawn.spawnMarkerReference = o, spawn -> spawn.spawnMarkerReference)
            // .add()
            .append(new KeyedCodec<>("Status", new EnumCodec<>(Status.class, EnumCodec.EnumStyle.CAMEL_CASE)),
                    (o, v) -> o.status = v, o -> o.status)
            .add()
            .build();

    public Status status = Status.NoGame;
    public Vector3i zoneOffset = new Vector3i(-2, -1, -2);
    public Vector3i zooneSize = new Vector3i(5, 3, 5);
    private PlayerRef[] trackedPlayers;
    public Map<Faction, Vector3d> spawnPoints = Map.of(
            Faction.HRE, new Vector3d(-26.95, 124, 166.82),
            Faction.OE, new Vector3d(4, 0, 4));

    public void setSpawnPoint(Faction faction, Vector3d point) {
        spawnPoints.put(faction, point);
    }

    @Override
    public void tick(float v, int i, ArchetypeChunk<ChunkStore> archetypeChunk, Store<ChunkStore> store,
            CommandBuffer<ChunkStore> commandBuffer) {
        switch (this.status) {
            case Status.NoGame:
                // No game in progress.
                Universe.get().getPlayers();
                break;

            default:
                break;
        }
    }

    public void startGame(PlayerRef[] players, Store<EntityStore> store) {
        LOGGER.atInfo().log("Starting game at " + this.getBlockPosition().toString());
        // #region Assign players to factions
        // TODO: For now we'll do random team assignment.
        // In the future we will want to allow preferences.

        // FIXME: For debug purposes, we are skipping the playerCount > 1 check.
        trackedPlayers = players;
        int playerIdx = 0;
        for (PlayerRef playerRef : trackedPlayers) {
            FactionComponent faction = store.getComponent(playerRef.getReference(),
                    DominionPlugin.get().getFactionComponentType());
            if (Objects.isNull(faction)) {
                LOGGER.atInfo().log("Player had no faction component, adding...");
                faction = new FactionComponent();
                store.addComponent(playerRef.getReference(), DominionPlugin.get().getFactionComponentType(), faction);
            }
            if (playerIdx % 2 == 0) {
                faction.setFaction(Faction.HRE);
            } else {
                faction.setFaction(Faction.OE);
            }
            playerRef.sendMessage(Message.raw("You are now on faction " + faction.getFaction()));
            playerRef.getReference().getStore().addComponent(playerRef.getReference(), Teleport.getComponentType(),
                    Teleport.createForPlayer(spawnPoints.get(faction.getFaction()), new Vector3f((float) -3.14, 0, 0)));
            playerIdx++;
        }
        // #endregion
    }

    public static class Data extends StateData {
        public static final BuilderCodec<SacredSiteMarkerBlockState.Data> CODEC = BuilderCodec
                .builder(SacredSiteMarkerBlockState.Data.class, SacredSiteMarkerBlockState.Data::new,
                        StateData.DEFAULT_CODEC)
                .build();
    }

    public static enum Status {
        NoGame,
        Unowned,
        Capturing,
        Owned
    }
}
