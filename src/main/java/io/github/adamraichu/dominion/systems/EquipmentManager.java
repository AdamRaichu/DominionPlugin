package io.github.adamraichu.dominion.systems;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.io.handlers.game.InventoryPacketHandler;

public class EquipmentManager {

  public static void equip(Player player, FighterClass fighterClass) {
    switch (fighterClass) {
      case FighterClass.CIVILIAN:
        break;
      default:
        break;
    }
  }

  public static void clearInventory(Player player) {
    player.getInventory().clear();
  }

  // #region Holy Roman Empire
  // #endregion
}
