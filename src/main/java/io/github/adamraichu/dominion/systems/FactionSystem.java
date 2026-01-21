package io.github.adamraichu.dominion.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class FactionSystem extends EntityTickingSystem<EntityStore> {
  private final ComponentType<EntityStore, FactionComponent> factionComponentType;

  public FactionSystem(ComponentType<EntityStore, FactionComponent> factionComponentType) {
    this.factionComponentType = factionComponentType;
  }

  @Override
  public Query<EntityStore> getQuery() {
    return Query.and(this.factionComponentType);
  }

  @Override
  public void tick(float arg0, int arg1, ArchetypeChunk<EntityStore> arg2, Store<EntityStore> arg3,
      CommandBuffer<EntityStore> arg4) {
    // Nothing to do right now.
  }
}
