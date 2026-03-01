package io.github.adamraichu.dominion.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.adamraichu.dominion.blocks.SacredSiteMarkerBlockState;
import io.github.adamraichu.dominion.systems.faction.Faction;

import javax.annotation.Nonnull;

public class SacredSiteMarker_OpMenu
        extends InteractiveCustomUIPage<SacredSiteMarker_OpMenu.BindingData> {

    public SacredSiteMarker_OpMenu(
            @Nonnull PlayerRef pRef,
            @Nonnull CustomPageLifetime lifetime,
            SacredSiteMarkerBlockState state) {
        super(pRef, lifetime, BindingData.CODEC);
        this.blockState = state;
    }

    private SacredSiteMarkerBlockState blockState;

    public static class BindingData {

        @SuppressWarnings("deprecation")
        public static final BuilderCodec<BindingData> CODEC = BuilderCodec.builder(
                BindingData.class,
                BindingData::new)
                .addField(
                        new KeyedCodec<>("@NumberFielde973f82b", Codec.FLOAT),
                        (data, s) -> data.NumberFielde973f82b = s,
                        data -> data.NumberFielde973f82b)
                .addField(
                        new KeyedCodec<>("@NumberField39d3c936", Codec.FLOAT),
                        (data, s) -> data.NumberField39d3c936 = s,
                        data -> data.NumberField39d3c936)
                .addField(
                        new KeyedCodec<>("@NumberField260af0dd", Codec.FLOAT),
                        (data, s) -> data.NumberField260af0dd = s,
                        data -> data.NumberField260af0dd)
                .addField(
                        new KeyedCodec<>("@NumberField31af9960", Codec.FLOAT),
                        (data, s) -> data.NumberField31af9960 = s,
                        data -> data.NumberField31af9960)
                .addField(
                        new KeyedCodec<>("@NumberField035daaf7", Codec.FLOAT),
                        (data, s) -> data.NumberField035daaf7 = s,
                        data -> data.NumberField035daaf7)
                .addField(
                        new KeyedCodec<>("@NumberField953f6c26", Codec.FLOAT),
                        (data, s) -> data.NumberField953f6c26 = s,
                        data -> data.NumberField953f6c26)
                .build();

        // HRE X
        public Float NumberFielde973f82b;
        // HRE Y
        public Float NumberField39d3c936;
        // HRE Z
        public Float NumberField260af0dd;
        // OE X
        public Float NumberField31af9960;
        // OE Y
        public Float NumberField035daaf7;
        // OE Z
        public Float NumberField953f6c26;
    }

    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull UICommandBuilder uiCommandBuilder,
            @Nonnull UIEventBuilder uiEventBuilder,
            @Nonnull Store<EntityStore> store) {
        uiCommandBuilder.append("Pages/CoordinateUpdatePage.ui");

        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                "#NumberFielde973f82b",
                EventData.of("@NumberFielde973f82b", "#NumberFielde973f82b.Value"),
                false);
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                "#NumberField39d3c936",
                EventData.of("@NumberField39d3c936", "#NumberField39d3c936.Value"),
                false);
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                "#NumberField260af0dd",
                EventData.of("@NumberField260af0dd", "#NumberField260af0dd.Value"),
                false);
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                "#NumberField31af9960",
                EventData.of("@NumberField31af9960", "#NumberField31af9960.Value"),
                false);
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                "#NumberField035daaf7",
                EventData.of("@NumberField035daaf7", "#NumberField035daaf7.Value"),
                false);
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                "#NumberField953f6c26",
                EventData.of("@NumberField953f6c26", "#NumberField953f6c26.Value"),
                false);

        Vector3d hre = this.blockState.getSpawnPoint(Faction.HRE);
        Vector3d oe = this.blockState.getSpawnPoint(Faction.OE);

        // // Idk why this doesn't work but it doesn't so.
        uiCommandBuilder.set("#NumberFielde973f82b.Value", hre.getX());
        uiCommandBuilder.set("#NumberField39d3c936.Value", hre.getY());
        uiCommandBuilder.set("#NumberField260af0dd.Value", hre.getZ());
        uiCommandBuilder.set("#NumberField31af9960.Value", oe.getX());
        uiCommandBuilder.set("#NumberField035daaf7.Value", oe.getY());
        uiCommandBuilder.set("#NumberField953f6c26.Value", oe.getZ());
    }

    @Override
    public void handleDataEvent(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull Store<EntityStore> store,
            @Nonnull BindingData data) {
        super.handleDataEvent(ref, store, data);
        boolean changed = false;

        if (data.NumberFielde973f82b != null) {
            // Logic for @NumberFielde973f82b
            this.playerRef.sendMessage(
                    Message.raw(
                            "@NumberFielde973f82b updated to: " + data.NumberFielde973f82b));
            changed = true;
        }
        if (data.NumberField39d3c936 != null) {
            // Logic for @NumberField39d3c936
            this.playerRef.sendMessage(
                    Message.raw(
                            "@NumberField39d3c936 updated to: " + data.NumberField39d3c936));
            changed = true;
        }
        if (data.NumberField260af0dd != null) {
            // Logic for @NumberField260af0dd
            this.playerRef.sendMessage(
                    Message.raw(
                            "@NumberField260af0dd updated to: " + data.NumberField260af0dd));
            changed = true;
        }
        if (data.NumberField31af9960 != null) {
            // Logic for @NumberField31af9960
            this.playerRef.sendMessage(
                    Message.raw(
                            "@NumberField31af9960 updated to: " + data.NumberField31af9960));
            changed = true;
        }
        if (data.NumberField035daaf7 != null) {
            // Logic for @NumberField035daaf7
            this.playerRef.sendMessage(
                    Message.raw(
                            "@NumberField035daaf7 updated to: " + data.NumberField035daaf7));
            changed = true;
        }
        if (data.NumberField953f6c26 != null) {
            // Logic for @NumberField953f6c26
            this.playerRef.sendMessage(
                    Message.raw(
                            "@NumberField953f6c26 updated to: " + data.NumberField953f6c26));
            changed = true;
        }

        if (changed) {
            this.blockState.setSpawnPoint(
                    Faction.HRE,
                    new Vector3d(
                            data.NumberFielde973f82b != null
                                    ? data.NumberFielde973f82b
                                    : this.blockState.getSpawnPoint(Faction.HRE).getX(),
                            data.NumberField39d3c936 != null
                                    ? data.NumberField39d3c936
                                    : this.blockState.getSpawnPoint(Faction.HRE).getY(),
                            data.NumberField260af0dd != null
                                    ? data.NumberField260af0dd
                                    : this.blockState.getSpawnPoint(Faction.HRE).getZ()));
            this.blockState.setSpawnPoint(
                    Faction.OE,
                    new Vector3d(
                            data.NumberField31af9960 != null
                                    ? data.NumberField31af9960
                                    : this.blockState.getSpawnPoint(Faction.OE).getX(),
                            data.NumberField035daaf7 != null
                                    ? data.NumberField035daaf7
                                    : this.blockState.getSpawnPoint(Faction.OE).getY(),
                            data.NumberField953f6c26 != null
                                    ? data.NumberField953f6c26
                                    : this.blockState.getSpawnPoint(Faction.OE).getZ()));
        }
    }
}
