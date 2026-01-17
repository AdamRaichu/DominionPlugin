package io.github.adamraichu.dominion;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import io.github.adamraichu.dominion.blocks.SacredSiteMarkerBlockState;
import io.github.adamraichu.dominion.systems.PointSystem;
import io.github.adamraichu.dominion.systems.PointsComponent;
import io.github.adamraichu.dominion.systems.PointsTestCommand;

import javax.annotation.Nonnull;

/**
 * This class serves as the entrypoint for your plugin. Use the setup method to
 * register into game registries or add
 * event listeners.
 */
public class DominionPlugin extends JavaPlugin {
    private static DominionPlugin instance;
    private ComponentType<EntityStore, PointsComponent> pointsComponent;
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public DominionPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from " + this.getName() + " version " + this.getManifest().getVersion().toString());
        instance = this;
    }

    @Override
    protected void setup() {
        super.setup();
        LOGGER.atInfo().log("Setting up plugin " + this.getName());
        // Commands
        this.getCommandRegistry()
                .registerCommand(new ExampleCommand(this.getName(), this.getManifest().getVersion().toString()));
        this.getCommandRegistry()
                .registerCommand(new PointsTestCommand());

        // PointsSystem
        this.pointsComponent = this.getEntityStoreRegistry()
                .registerComponent(PointsComponent.class, "DominionPoints", PointsComponent.CODEC);
        this.getEntityStoreRegistry().registerSystem(new PointSystem(this.pointsComponent));

        this.getBlockStateRegistry().registerBlockState(SacredSiteMarkerBlockState.class,
                "Flamingie_DominionGamemode_SacredSiteMarker", SacredSiteMarkerBlockState.CODEC,
                SacredSiteMarkerBlockState.Data.class, SacredSiteMarkerBlockState.Data.CODEC);
    }

    public ComponentType<EntityStore, PointsComponent> getPointsComponentType() {
        return pointsComponent;
    }

    public static DominionPlugin get() {
        return instance;
    }
}