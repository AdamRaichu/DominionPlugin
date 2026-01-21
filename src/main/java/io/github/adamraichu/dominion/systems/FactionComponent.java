package io.github.adamraichu.dominion.systems;

import java.util.Objects;

import javax.annotation.Nullable;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class FactionComponent implements Component<EntityStore> {
  public static final BuilderCodec<FactionComponent> CODEC = BuilderCodec
      .builder(FactionComponent.class, FactionComponent::new)
      .append(new KeyedCodec<>("FactionId", Codec.INTEGER), (o, i) -> o.faction = Faction.values.get(i),
          o -> Faction.values.indexOf(o.faction))
      .addValidator(Validators.nonNull())
      .add()
      .afterDecode(v -> {
        if (Objects.isNull(v.faction)) {
          v.faction = Faction.HRE;
        }
      })
      .build();

  private Faction faction;

  public FactionComponent() {
    this(Faction.HRE);
  }

  public FactionComponent(Faction faction) {
    this.faction = faction;
  }

  public FactionComponent(FactionComponent other) {
    this.faction = other.faction;
  }

  public Faction getFaction() {
    return this.faction;
  }

  public void setFaction(Faction f) {
    this.faction = f;
  }

  @Nullable
  @Override
  public Component<EntityStore> clone() {
    return new FactionComponent(this);
  }
}
