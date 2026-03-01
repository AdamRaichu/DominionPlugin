package io.github.adamraichu.dominion;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.adamraichu.dominion.blocks.SacredSiteMarkerBlockState;
import io.github.adamraichu.dominion.interactions.SacredSiteMarkerInteraction;
import io.github.adamraichu.dominion.systems.faction.FactionComponent;
import io.github.adamraichu.dominion.systems.faction.FactionQueryCommand;
import io.github.adamraichu.dominion.systems.fighterclass.FighterClassComponent;
import io.github.adamraichu.dominion.systems.points.PointSystem;
import io.github.adamraichu.dominion.systems.points.PointsComponent;
import io.github.adamraichu.dominion.systems.points.PointsTestCommand;

import javax.annotation.Nonnull;

/**
 * This class serves as the entrypoint for your plugin. Use the setup method to
 * register into game registries or add
 * event listeners.
 */
public class DominionPlugin extends JavaPlugin {

  private static DominionPlugin instance;
  private ComponentType<EntityStore, PointsComponent> pointsComponent;
  private ComponentType<EntityStore, FactionComponent> factionComponent;
  private ComponentType<EntityStore, FighterClassComponent> fighterClassComponent;
  public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

  public DominionPlugin(@Nonnull JavaPluginInit init) {
    super(init);
    LOGGER.atInfo().log(
        "Hello from " +
            this.getName() +
            " version " +
            this.getManifest().getVersion().toString());
    instance = this;
  }

  @Override
  protected void setup() {
    super.setup();
    LOGGER.atInfo().log("Setting up plugin " + this.getName());
    // Commands
    this.getCommandRegistry().registerCommand(new PointsTestCommand());
    this.getCommandRegistry().registerCommand(new FactionQueryCommand());

    // PointsSystem
    this.pointsComponent = this.getEntityStoreRegistry().registerComponent(
        PointsComponent.class,
        "DominionPoints",
        PointsComponent.CODEC);
    this.getEntityStoreRegistry().registerSystem(
        new PointSystem(this.pointsComponent));

    // FactionSystem
    this.factionComponent = this.getEntityStoreRegistry().registerComponent(
        FactionComponent.class,
        "DominionFaction",
        FactionComponent.CODEC);

    // Class system
    this.fighterClassComponent = this.getEntityStoreRegistry().registerComponent(
        FighterClassComponent.class,
        "DominionFighterClass",
        FighterClassComponent.CODEC);

    // Interactions
    this.getCodecRegistry(Interaction.CODEC).register(
        "SacredSiteMarkerInteraction",
        SacredSiteMarkerInteraction.class,
        SacredSiteMarkerInteraction.CODEC);

    this.getBlockStateRegistry().registerBlockState(
        SacredSiteMarkerBlockState.class,
        "Flamingie_DominionGamemode_SacredSiteMarker",
        SacredSiteMarkerBlockState.CODEC,
        SacredSiteMarkerBlockState.Data.class,
        SacredSiteMarkerBlockState.Data.CODEC);
  }

  public ComponentType<EntityStore, PointsComponent> getPointsComponentType() {
    return pointsComponent;
  }

  public ComponentType<EntityStore, FactionComponent> getFactionComponentType() {
    return factionComponent;
  }

  public ComponentType<EntityStore, FighterClassComponent> getFighterClassComponentType() {
    return fighterClassComponent;
  }

  public static DominionPlugin get() {
    return instance;
  }
}
