package io.github.adamraichu.dominion;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import io.github.adamraichu.dominion.blocks.SacredSiteMarkerBlockState;

import javax.annotation.Nonnull;

/**
 * This class serves as the entrypoint for your plugin. Use the setup method to
 * register into game registries or add
 * event listeners.
 */
public class DominionPlugin extends JavaPlugin {

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public DominionPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from " + this.getName() + " version " + this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        super.setup();
        LOGGER.atInfo().log("Setting up plugin " + this.getName());
        this.getCommandRegistry()
                .registerCommand(new ExampleCommand(this.getName(), this.getManifest().getVersion().toString()));

        this.getBlockStateRegistry().registerBlockState(SacredSiteMarkerBlockState.class, "Flamingie_DominionGamemode_SacredSiteMarker", SacredSiteMarkerBlockState.CODEC, SacredSiteMarkerBlockState.Data.class, SacredSiteMarkerBlockState.Data.CODEC);
    }
}