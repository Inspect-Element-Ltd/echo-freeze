package ac.echo.api.gui.tasks;

import ac.echo.api.gui.MainMenu;
import ac.echo.api.gui.gui.Menu;
import ac.echo.api.gui.gui.PaginatedGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class Refresher {

    public Refresher(JavaPlugin plugin, MainMenu menu) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> {
            for(Player player : plugin.getServer().getOnlinePlayers()) {
                if(player.getOpenInventory().getTopInventory() != null) {
                    Inventory inventory = player.getOpenInventory().getTopInventory();
                    if(inventory.getHolder() instanceof Menu) {
                        Menu gui = (Menu) inventory.getHolder();
                        if(gui.getUpdate() != null && gui.isAutoRefresh()) {
                            if (gui.getInstanceId() == menu.getInstanceId()) {
                                gui.update();
                                if(!(gui instanceof PaginatedGUI)) {
                                    gui.open(player);
                                }
                            }
                        }
                    }
                }
            }
        }, 0, 20);
    }
}
