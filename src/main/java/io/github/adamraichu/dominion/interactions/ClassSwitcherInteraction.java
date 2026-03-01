package io.github.adamraichu.dominion.interactions;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.BlockPosition;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import io.github.adamraichu.dominion.DominionPlugin;
import io.github.adamraichu.dominion.systems.EquipmentManager;
import io.github.adamraichu.dominion.systems.FighterClass;
import io.github.adamraichu.dominion.systems.FighterClassComponent;

public class ClassSwitcherInteraction extends SimpleInstantInteraction {

  @Override
  protected void firstRun(InteractionType arg0, InteractionContext ctx, CooldownHandler arg2) {
    // Get prerequisite data
    Ref<EntityStore> ref = ctx.getEntity();
    World world = Universe.get().getDefaultWorld();
    world.execute(() -> {
      BlockPosition pos = ctx.getTargetBlock();
      Player player = world.getEntityStore().getStore().getComponent(ref,
          Player.getComponentType());
      PlayerRef pRef = player.getPlayerRef();
      FighterClassComponent fighterClass = world.getEntityStore().getStore().getComponent(ref,
          DominionPlugin.get().getFighterClassComponentType());

      // Cycle to the next class. In the future we may add UI, but for now this is
      // fine.
      int currentIndex = FighterClass.values.indexOf(fighterClass.getFighterClass());
      int nextIndex = (currentIndex + 1) % FighterClass.values.size();
      FighterClass nextClass = FighterClass.values.get(nextIndex);
      fighterClass.setFighterClass(nextClass);
      EquipmentManager.equip(player, nextClass);
    });
  }

}
