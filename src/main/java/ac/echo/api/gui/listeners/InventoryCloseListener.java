package ac.echo.api.gui.listeners;

import ac.echo.api.gui.MainMenu;
import ac.echo.api.gui.gui.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryCloseListener implements Listener {

    private JavaPlugin plugin;
    private MainMenu menu;

    public InventoryCloseListener(JavaPlugin plugin, MainMenu menu) {
        this.plugin = plugin;
        this.menu = menu;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if(inventory.getHolder() != null && inventory.getHolder() instanceof Menu) {
            Menu gui = (Menu) inventory.getHolder();
            if(gui.getInstanceId() == this.menu.getInstanceId() && gui.getGuiCloseUpdate() != null) {
                Bukkit.getScheduler().runTaskLater(plugin, ()-> gui.getGuiCloseUpdate().onClose((Player) event.getPlayer(), gui, event), 1);
            }
        }
    }
}
