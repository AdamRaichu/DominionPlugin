package io.github.adamraichu.dominion.systems.points;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import java.util.Objects;
import javax.annotation.Nullable;

public class PointsComponent implements Component<EntityStore> {

  public static final BuilderCodec<PointsComponent> CODEC = BuilderCodec
      .builder(PointsComponent.class, PointsComponent::new)
      .append(
          new KeyedCodec<>("Points", Codec.INTEGER),
          (o, i) -> o.points = i,
          o -> o.points)
      .addValidator(Validators.nonNull())
      .add()
      .afterDecode(v -> {
        if (Objects.isNull(v.points)) {
          v.points = 0;
        }
      })
      .build();

  private int points;

  public PointsComponent() {
    this(0);
  }

  public PointsComponent(int defaultPoints) {
    this.points = defaultPoints;
  }

  public PointsComponent(PointsComponent other) {
    this.points = other.points;
  }

  @Nullable
  @Override
  public Component<EntityStore> clone() {
    return new PointsComponent(this);
  }

  public int getPoints() {
    return this.points;
  }

  public int addPoints(int amount) {
    this.points += amount;
    return this.points;
  }

  public int setPoints(int amount) {
    this.points = amount;
    return this.points;
  }
}
