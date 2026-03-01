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

public class FactionQueryCommand extends AbstractPlayerCommand {

  public FactionQueryCommand() {
    super("myfaction", "What is my faction?");
  }

  @Override
  protected void execute(
      @Nonnull CommandContext commandContext,
      @Nonnull Store<EntityStore> store,
      @Nonnull Ref<EntityStore> ref,
      @Nonnull PlayerRef playerRef,
      @Nonnull World world) {
    Player player = store.getComponent(ref, Player.getComponentType());
    FactionComponent faction = store.getComponent(
        ref,
        DominionPlugin.get().getFactionComponentType());
    if (Objects.isNull(faction)) {
      player.sendMessage(Message.raw("You have no faction."));
      return;
    }
    switch (faction.getFaction()) {
      case Faction.HRE:
        player.sendMessage(
            Message.raw("You are a member of the Holy Roman Empire!"));
        break;
      case Faction.OE:
        player.sendMessage(
            Message.raw("You are a member of the Ottoman Empire!"));
        break;
      default:
        LOGGER.atWarning().log(
            "Somehow player " + playerRef.getUsername() + "got to this point.");
        break;
    }
  }
}
