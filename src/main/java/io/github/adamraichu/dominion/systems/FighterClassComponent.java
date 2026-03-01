package io.github.adamraichu.dominion.systems;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import java.util.Objects;

public class FighterClassComponent implements Component<EntityStore> {

  public static final BuilderCodec<FighterClassComponent> CODEC = BuilderCodec.builder(
      FighterClassComponent.class,
      FighterClassComponent::new)
      .append(
          new KeyedCodec<>("FighterClassId", Codec.INTEGER),
          (o, i) -> o.fighterClass = FighterClass.values.get(i),
          o -> FighterClass.values.indexOf(o.fighterClass))
      .addValidator(Validators.nonNull())
      .add()
      .afterDecode(v -> {
        if (Objects.isNull(v.fighterClass)) {
          v.fighterClass = FighterClass.CIVILIAN;
        }
      })
      .build();

  private FighterClass fighterClass;

  public FighterClassComponent() {
    this(FighterClass.CIVILIAN);
  }

  public FighterClassComponent(FighterClass fighterClass) {
    this.fighterClass = fighterClass;
  }

  public FighterClassComponent(FighterClassComponent other) {
    this.fighterClass = other.fighterClass;
  }

  public FighterClass getFighterClass() {
    return this.fighterClass;
  }

  public void setFighterClass(FighterClass f) {
    this.fighterClass = f;
  }

  @Override
  public Component<EntityStore> clone() {
    return new FighterClassComponent(this);
  }
}
