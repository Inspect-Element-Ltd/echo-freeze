package ac.echo.api.gui.action;

import ac.echo.api.gui.button.Button;
import ac.echo.api.gui.gui.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface ButtonAction {
    void run(Player player, Menu gui, Button button, InventoryClickEvent event);
}
