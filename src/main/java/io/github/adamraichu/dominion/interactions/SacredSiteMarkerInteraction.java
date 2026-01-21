package io.github.adamraichu.dominion.interactions;

import static io.github.adamraichu.dominion.DominionPlugin.LOGGER;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.BlockPosition;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.meta.BlockState;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import io.github.adamraichu.dominion.blocks.SacredSiteMarkerBlockState;
import io.github.adamraichu.dominion.ui.SacredSiteMarker_OpMenu;

public class SacredSiteMarkerInteraction extends SimpleInstantInteraction {
  public static final BuilderCodec<SacredSiteMarkerInteraction> CODEC = BuilderCodec
      .builder(SacredSiteMarkerInteraction.class, SacredSiteMarkerInteraction::new, SimpleInstantInteraction.CODEC)
      .build();

  @SuppressWarnings({ "deprecation", "removal" })
  @Override
  protected void firstRun(InteractionType iType, InteractionContext ctx, CooldownHandler arg2) {
    // TODO Auto-generated method stub
    LOGGER.atInfo().log("SacredSiteMarkerInteraction executed.");
    // arg1
    // LOGGER.atInfo().log(arg0.toString());
    // LOGGER.atInfo().log(ctx.toString());
    // LOGGER.atInfo().log(arg2.toString());

    Ref<EntityStore> ref = ctx.getEntity();

    World world = Universe.get().getDefaultWorld();
    // FIXME: For now we are just starting the game. In the future, we will handle
    // who activates this, and if they are configuring.
    world.execute(() -> {
      BlockPosition pos = ctx.getTargetBlock();
      BlockState state = world.getState(pos.x, pos.y, pos.z, true);
      if (state instanceof SacredSiteMarkerBlockState) {
        SacredSiteMarkerBlockState markerState = (SacredSiteMarkerBlockState) state;
        // Start the game with the interacting player.
        Player player = world.getEntityStore().getStore().getComponent(ref,
            Player.getComponentType());
        // LOGGER.atInfo().log("Starting Sacred Site game for player " +
        // player.getDisplayName());
        PlayerRef pRef = player.getPlayerRef();

        MovementStatesComponent movementState = world.getEntityStore().getStore().getComponent(ref,
            MovementStatesComponent.getComponentType());
        boolean isCrouching = movementState.getMovementStates().crouching;

        if (isCrouching) {
          // Configure mode
          CompletableFuture.runAsync(() -> {
            player.getPageManager().openCustomPage(ref,
                world.getEntityStore().getStore(),
                new SacredSiteMarker_OpMenu(pRef, CustomPageLifetime.CanDismiss));
            pRef.sendMessage(Message.raw("UI Page Shown"));
          }, world);
          return;
        }

        Collection<PlayerRef> pRefs = world.getPlayerRefs();
        PlayerRef[] players = pRefs.toArray(new PlayerRef[0]);
        markerState.startGame(players, world.getEntityStore().getStore());

        // // legacy UI code for testing

      } else {
        LOGGER.atWarning()
            .log("SacredSiteMarkerInteraction executed on non-SacredSiteMarkerBlockState at " + pos.toString());
      }
    });

    ctx.getState().state = InteractionState.Finished;
  }
}
