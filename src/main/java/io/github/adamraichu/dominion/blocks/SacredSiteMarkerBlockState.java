package io.github.adamraichu.dominion.blocks;

import static io.github.adamraichu.dominion.DominionPlugin.LOGGER;

import java.util.ArrayList;
import java.util.List;
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
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
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
            // .append(new KeyedCodec<>("Status", new EnumCodec<>(Status.class,
            // EnumCodec.EnumStyle.CAMEL_CASE)),
            // (o, v) -> o.status = v, o -> o.status)
            // .add()
            .append(new KeyedCodec<>("SpawnPoints", SacredSiteMarkerBlockState.SpawnPoints.CODEC),
                    (o, v) -> {
                        o.spawnPoints.clear();
                        o.spawnPoints.put(Faction.HRE, new Vector3d(v.hreX, v.hreY, v.hreZ));
                        o.spawnPoints.put(Faction.OE, new Vector3d(v.oeX, v.oeY, v.oeZ));
                    },
                    o -> {
                        SpawnPoints v = new SpawnPoints();
                        Vector3d h = o.spawnPoints.get(Faction.HRE);
                        Vector3d e = o.spawnPoints.get(Faction.OE);
                        if (h != null) {
                            v.hreX = (float) h.x;
                            v.hreY = (float) h.y;
                            v.hreZ = (float) h.z;
                        }
                        if (e != null) {
                            v.oeX = (float) e.x;
                            v.oeY = (float) e.y;
                            v.oeZ = (float) e.z;
                        }
                        return v;
                    })
            .add()
            .build();

    private Status status = Status.NoGame;
    private Vector3i zoneOffset = new Vector3i(-2, -1, -2);
    private Vector3i zoneSize = new Vector3i(5, 5, 5);
    private PlayerRef[] trackedPlayers_;
    private ArrayList<String> trackedPlayerUsernames = new ArrayList<>();
    private Map<Faction, Vector3d> spawnPoints = new java.util.EnumMap<>(Faction.class);
    {
        spawnPoints.put(Faction.HRE, new Vector3d(-26.95, 124, 166.82));
        spawnPoints.put(Faction.OE, new Vector3d(4, 0, 4));
    }

    public Status getStatus() {
        return status;
    }

    public void setSpawnPoint(Faction faction, Vector3d point) {
        LOGGER.atInfo().log("Setting spawn point for faction " + faction + " to " + point.toString());
        spawnPoints.put(faction, point);
    }

    public Vector3d getSpawnPoint(Faction faction) {
        return spawnPoints.get(faction);
    }

    @Override
    public void tick(float v, int i, ArchetypeChunk<ChunkStore> archetypeChunk, Store<ChunkStore> chunkStore,
            CommandBuffer<ChunkStore> commandBuffer) {

        List<PlayerRef> playersInZone = this.getPlayersWithinZone();
        LOGGER.atInfo().log(playersInZone.size() + " players in zone.");
        boolean hrePlayersPresent = false;
        boolean oePlayersPresent = false;
        for (PlayerRef playerRef : playersInZone) {
            FactionComponent faction = Universe.get().getWorld(playerRef.getWorldUuid()).getEntityStore().getStore()
                    .getComponent(playerRef.getReference(), DominionPlugin.get().getFactionComponentType());
            if (faction.getFaction() == Faction.HRE) {
                hrePlayersPresent = true;
            } else if (faction.getFaction() == Faction.OE) {
                oePlayersPresent = true;
            }
        }

        switch (this.status) {
            case Status.NoGame:
                break;

            case Status.Unowned:
                if (playersInZone.size() < 1) {
                    break;
                }
                if (hrePlayersPresent && oePlayersPresent) {
                    // Both teams present, do nothing.
                    break;
                }
                break;

            case Status.Capturing:
                break;

            case Status.Owned:
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
        this.trackedPlayers_ = players;
        this.trackedPlayerUsernames = new ArrayList<>();
        for (PlayerRef playerRef : players) {
            this.trackedPlayerUsernames.add(playerRef.getUsername());
        }
        int playerIdx = 0;
        for (PlayerRef playerRef : trackedPlayers_) {
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

        this.status = Status.Unowned;
        // #endregion
    }

    private List<PlayerRef> getPlayersWithinZone() {
        List<PlayerRef> playersInZone = new ArrayList<>();

        int minX = this.getBlockPosition().x + zoneOffset.x;
        int minY = this.getBlockPosition().y + zoneOffset.y;
        int minZ = this.getBlockPosition().z + zoneOffset.z;
        int maxX = minX + zoneSize.x;
        int maxY = minY + zoneSize.y;
        int maxZ = minZ + zoneSize.z;

        Store<EntityStore> store = Universe.get().getDefaultWorld().getEntityStore().getStore();

        try {
            World world = Universe.get().getDefaultWorld();
            for (PlayerRef playerRef : this.trackedPlayers_) {
                // Vector3d pos = playerRef.pl;
                if (!playerRef.isValid()) {
                    // this.trackedPlayers = this.trackedPlayers.
                    continue;
                }

                Player player = store.getComponent(playerRef.getReference(), Player.getComponentType());
                TransformComponent transform = store.getComponent(playerRef.getReference(),
                        TransformComponent.getComponentType());
                Vector3d pos = transform.getPosition();
                if (pos.x >= minX && pos.x <= maxX &&
                        pos.y >= minY && pos.y <= maxY &&
                        pos.z >= minZ && pos.z <= maxZ) {
                    playersInZone.add(playerRef);
                }
            }
        } catch (NullPointerException e) {
            // LOGGER.atWarning().withCause(e).log("Error getting player position for " +
            // playerRef.getUsername()
            // + ". (They probably left the game)");
        }

        return playersInZone;
    }

    public static class SpawnPoints extends StateData {
        public static final Codec<SpawnPoints> CODEC = BuilderCodec
                .builder(SpawnPoints.class, SpawnPoints::new, StateData.DEFAULT_CODEC)
                .append(new KeyedCodec<>("HreX", Codec.FLOAT), (o, v) -> o.hreX = v, o -> o.hreX).add()
                .append(new KeyedCodec<>("HreY", Codec.FLOAT), (o, v) -> o.hreY = v, o -> o.hreY).add()
                .append(new KeyedCodec<>("HreZ", Codec.FLOAT), (o, v) -> o.hreZ = v, o -> o.hreZ).add()
                .append(new KeyedCodec<>("OeX", Codec.FLOAT), (o, v) -> o.oeX = v, o -> o.oeX).add()
                .append(new KeyedCodec<>("OeY", Codec.FLOAT), (o, v) -> o.oeY = v, o -> o.oeY).add()
                .append(new KeyedCodec<>("OeZ", Codec.FLOAT), (o, v) -> o.oeZ = v, o -> o.oeZ).add()
                .build();

        public float hreX, hreY, hreZ;
        public float oeX, oeY, oeZ;
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
