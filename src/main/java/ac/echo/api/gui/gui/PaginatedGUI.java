package ac.echo.api.gui.gui;

import ac.echo.api.gui.action.pagination.LastPageAction;
import ac.echo.api.gui.action.pagination.NextPageAction;
import ac.echo.api.gui.button.Button;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class PaginatedGUI extends Menu {

    private int currentPage;
    private List<Button> pageButtons;
    private Button separator, backButton, nextButton;

    @Override
    public void addButton(Button button) {
        this.pageButtons.add(button);
    }

    @Override
    public void setButton(int slot, Button button) {
        this.pageButtons.add(slot, button);
    }

    @Override
    public void removeButton(Button button) {
        this.pageButtons.remove(button);
    }

    @Override
    public Button getButton(int slot) {
        Button button = null;
        int i = slot + (this.currentPage * (this.getSize() - 9));

        if (slot < this.getSize() - 9 && this.hasPage(this.currentPage)) {
            if (this.pageButtons.size() > i) {
                button = this.getPageButtons().get(i);
            }
        } else if (slot == this.getSize() - 1 && this.hasPage(this.currentPage + 1)) {
            button = new Button(Material.ARROW, "&eNext Page");
            button.setButtonAction(new NextPageAction());
        } else if (slot == this.getSize() - 9 && this.currentPage != 0) {
            button = new Button(Material.ARROW, "&cPrevious Page");
            button.setButtonAction(new LastPageAction());
        }

        return button;
    }

    @Override
    public Inventory getInventory() {
        return this.getPage(0);
    }

    public Inventory getPage(int pageNumber) {
        Inventory inventory = Bukkit.createInventory(this, this.getSize(), this.getTitle());

        if(this.hasPage(pageNumber) || this.currentPage == 0) {
            this.currentPage = pageNumber;
            final int inv = this.getSize() - 9;
            final int index = this.currentPage * inv;
            for(int x = 0; x <= inv - 1; x++) {
                if(this.pageButtons.size() > index + x) {
                    Button button = this.pageButtons.get(index + x);
                    button.update();
                    inventory.setItem(x, button);
                } else {
                    break;
                }
            }

            if(separator != null) {
                for(int x = this.getSize() - 9; x < this.getSize(); x++) {
                    inventory.setItem(x, separator);
                }
            }

            if(this.hasPage(pageNumber + 1)) {
                Button button = new Button(Material.ARROW, pageNumber + 1, "&eNext Page");
                button.setButtonAction(new NextPageAction());
                inventory.setItem(this.getSize() - 1, button);
            }

            if(pageNumber != 0) {
                Button button = new Button(Material.ARROW, pageNumber - 1 == 0 ? 1 : pageNumber - 1, "&cPrevious Page");
                button.setButtonAction(new LastPageAction());
                inventory.setItem(this.getSize() - 9, button);
            }

            return inventory;
        } else {
            return null;
        }
    }

    public boolean hasPage(int pageNumber) {
        return pageNumber * (this.getSize() - 9) < this.pageButtons.size();
    }

    /**
     * Creates a new PaginatedGUI. Please note that a PaginatedGUI must be instantiated each time it is opened.
     * @param title Inventory title.
     * @param size Inventory size.
     */
    public PaginatedGUI(String title, int size) {
        super(ChatColor.translateAlternateColorCodes('&', title), size);

        if((size * 9) < 18) {
            throw new IllegalArgumentException("Size must be at least 18.");
        }

        this.currentPage = 0;
        this.pageButtons = new ArrayList<>();
    }
}
