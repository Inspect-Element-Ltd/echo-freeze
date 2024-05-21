package ac.echo.api.gui.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

public interface GUICloseUpdate {

    void onClose(Player player, Menu gui, InventoryCloseEvent event);
}
