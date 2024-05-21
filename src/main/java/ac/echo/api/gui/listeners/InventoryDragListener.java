package ac.echo.api.gui.listeners;

import ac.echo.api.gui.MainMenu;
import ac.echo.api.gui.gui.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryDragListener implements Listener {

    private JavaPlugin plugin;
    private MainMenu menu;

    public InventoryDragListener(JavaPlugin plugin, MainMenu menu) {
        this.plugin = plugin;
        this.menu = menu;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();
        if(inventory.getHolder() != null && inventory.getHolder() instanceof Menu) {
            Menu gui = (Menu) inventory.getHolder();
            if(gui.getInstanceId() == this.menu.getInstanceId()) {
                event.setCancelled(true);
            }
        }
    }
}
