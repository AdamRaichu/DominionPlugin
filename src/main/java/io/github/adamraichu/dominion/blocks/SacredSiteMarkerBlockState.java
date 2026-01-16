package io.github.adamraichu.dominion.blocks;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.StateData;
import com.hypixel.hytale.server.core.universe.world.chunk.state.TickableBlockState;
import com.hypixel.hytale.server.core.universe.world.meta.BlockState;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;

import static io.github.adamraichu.dominion.DominionPlugin.LOGGER;

public class SacredSiteMarkerBlockState extends BlockState implements TickableBlockState {
    public static final Codec<SacredSiteMarkerBlockState> CODEC = BuilderCodec.builder(SacredSiteMarkerBlockState.class, SacredSiteMarkerBlockState::new, BlockState.BASE_CODEC)
//            .append(new KeyedCodec<>("MarkerReference", PersistentRef.CODEC), (spawn, o) -> spawn.spawnMarkerReference = o, spawn -> spawn.spawnMarkerReference)
//            .add()
            .build();

    @Override
    public void tick(float v, int i, ArchetypeChunk<ChunkStore> archetypeChunk, Store<ChunkStore> store, CommandBuffer<ChunkStore> commandBuffer) {
        LOGGER.atInfo().log(String.valueOf(v));
    }

    public static class Data extends StateData {
        public static final BuilderCodec<SacredSiteMarkerBlockState.Data> CODEC = BuilderCodec.builder(SacredSiteMarkerBlockState.Data.class, SacredSiteMarkerBlockState.Data::new, StateData.DEFAULT_CODEC)
                .build();
    }
}
