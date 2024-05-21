package ac.echo.api.gui;

import ac.echo.api.gui.listeners.InventoryClickListener;
import ac.echo.api.gui.listeners.InventoryCloseListener;
import ac.echo.api.gui.listeners.InventoryDragListener;
import ac.echo.api.gui.tasks.Refresher;
import lombok.Data;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

@Data
public class MainMenu {
    public static MainMenu instance;
    private JavaPlugin plugin;
    private UUID instanceId;

    public MainMenu(JavaPlugin plugin) {
        instance = this;
        this.plugin = plugin;

        new InventoryClickListener(this.plugin, this);
        new InventoryCloseListener(this.plugin, this);
        new InventoryDragListener(this.plugin, this);

        new Refresher(this.plugin, this);
    }
}
