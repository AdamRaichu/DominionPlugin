package io.github.adamraichu.dominion.systems;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.adamraichu.dominion.DominionPlugin;
import java.util.Objects;
import javax.annotation.Nonnull;

public class PointsTestCommand extends AbstractPlayerCommand {

  public PointsTestCommand() {
    super("addpoints", "Points component test");
  }

  @Override
  protected void execute(
      @Nonnull CommandContext commandContext,
      @Nonnull Store<EntityStore> store,
      @Nonnull Ref<EntityStore> ref,
      @Nonnull PlayerRef playerRef,
      @Nonnull World world) {
    Player player = store.getComponent(ref, Player.getComponentType());
    PointsComponent points = store.getComponent(
        ref,
        DominionPlugin.get().getPointsComponentType());
    if (Objects.isNull(points)) {
      LOGGER.atInfo().log("Player had no points component, adding...");
      points = new PointsComponent();
      store.addComponent(
          ref,
          DominionPlugin.get().getPointsComponentType(),
          points);
    }
    LOGGER.atInfo().log("points previous: " + points.getPoints());
    points.addPoints(2);
    // world.
    // store.addComponent(ref, DominionPlugin.get().getPointsComponentType(),
    // poison);
    LOGGER.atInfo().log("points post: " + points.getPoints());
    player.sendMessage(
        Message.raw("You have been given 2 points!").color("Green").bold(true));
  }
}
