package ac.echo.api.gui.action;

import ac.echo.api.gui.button.Button;
import ac.echo.api.gui.gui.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OpenGUIAction implements ButtonAction {

    private final Menu gui;

    @Override
    public void run(Player player, Menu gui, Button button, InventoryClickEvent event) {
        player.openInventory(this.gui.getInventory());
    }

    /**
     * Opens a GUI.
     * @param gui The GUI to be opened for the executor.
     */
    public OpenGUIAction(Menu gui) {
        this.gui = gui;
    }
}
