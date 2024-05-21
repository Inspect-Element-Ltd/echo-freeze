package ac.echo.api.gui.action.pagination;

import ac.echo.api.gui.action.ButtonAction;
import ac.echo.api.gui.button.Button;
import ac.echo.api.gui.gui.Menu;
import ac.echo.api.gui.gui.PaginatedGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class NextPageAction implements ButtonAction {

    @Override
    public void run(Player player, Menu gui, Button button, InventoryClickEvent event) {
        if(gui instanceof PaginatedGUI) {
            PaginatedGUI paginatedGUI = (PaginatedGUI) gui;
            if(paginatedGUI.hasPage(paginatedGUI.getCurrentPage() + 1)) {
                player.openInventory(paginatedGUI.getPage(paginatedGUI.getCurrentPage() + 1));
            } else {
                player.sendMessage(ChatColor.RED + "There is no next page to show.");
            }
        } else {
            throw new ClassCastException("This action is only for a PaginatedGUI, and will break otherwise.");
        }
    }
}
