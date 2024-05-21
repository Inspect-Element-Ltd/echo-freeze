package ac.echo.api.gui.listeners;

import ac.echo.api.gui.MainMenu;
import ac.echo.api.gui.button.Button;
import ac.echo.api.gui.gui.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class InventoryClickListener implements Listener {

    private JavaPlugin plugin;
    private MainMenu menu;

    public InventoryClickListener(JavaPlugin plugin, MainMenu menu) {
        this.plugin = plugin;
        this.menu = menu;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        Inventory topInventory = event.getView().getTopInventory() == null ? null : event.getView().getTopInventory();
        ItemStack item = event.getCurrentItem();

        if(inventory != null) {
            if(inventory.getHolder() != null && inventory.getHolder() instanceof Menu) {
                Menu gui = (Menu) inventory.getHolder();
                if(gui.getInstanceId() == this.menu.getInstanceId()) {
                    event.setCancelled(true);
                    Button button;
                    if(item != null) {
                        button = gui.getButton(event.getSlot());
                        if(button != null) {
                            if(button.isCloseOnClick()) {
                                player.closeInventory();
                            }

                            if(button.getButtonAction() != null) {
                                button.getButtonAction().run(player, gui, button, event);
                            }
                        } else {
                            event.setCancelled(gui.isBlockAllInteractions() || Arrays.stream(gui.getBlockedMenuActions()).anyMatch(action -> action == event.getAction())
                                    || (Arrays.stream(gui.getBlockedMenuAdjacentActions()).anyMatch(action -> action == event.getAction()) && event.getClickedInventory() == event.getView().getTopInventory())
                                    || Arrays.stream(gui.getPermittedClickTypes()).noneMatch(clickType -> clickType == event.getClick()));
                        }
                    }
                }
            }
        }
    }
}