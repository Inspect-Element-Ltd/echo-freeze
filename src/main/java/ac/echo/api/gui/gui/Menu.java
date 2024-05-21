package ac.echo.api.gui.gui;

import ac.echo.api.gui.MainMenu;
import ac.echo.api.gui.button.Button;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Data
public class Menu implements InventoryHolder {

    private UUID instanceId;
    private String title;
    private int rows;
    private boolean autoRefresh;
    private Map<Integer, Button> buttons;
    private Map<Integer, ItemStack> items;
    private ItemStack background;
    private ItemStack topAndBottom;
    private GUIUpdate update;
    private GUICloseUpdate guiCloseUpdate;
    private boolean blockAllInteractions;

    private ClickType[] permittedClickTypes = new ClickType[] {
            ClickType.LEFT, ClickType.RIGHT
    };

    private InventoryAction[] blockedMenuActions = new InventoryAction[] {
            InventoryAction.MOVE_TO_OTHER_INVENTORY, InventoryAction.COLLECT_TO_CURSOR
    };

    private InventoryAction[] blockedMenuAdjacentActions = new InventoryAction[] {
            InventoryAction.MOVE_TO_OTHER_INVENTORY, InventoryAction.COLLECT_TO_CURSOR
    };


    public void addButton(Button button) {
        if(buttons.isEmpty()) {
            this.buttons.put(0, button);
        } else {
            this.buttons.put(buttons.size(), button);
        }
    }

    public void addButtons(Button... buttons) {
        for(Button button : buttons) {
            this.addButton(button);
        }
    }

    public void setButton(int slot, Button button) {
        this.buttons.put(slot, button);
    }

    public void removeButton(int slot) {
        this.buttons.remove(slot);
    }

    /**
     * This method is useless within this class.
     * @param button The button to be removed.
     */
    public void removeButton(Button button) {
    }

    public Button getButton(int slot) {
        return this.buttons.get(slot);
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, rows * 9, ChatColor.translateAlternateColorCodes('&', title));

        if(this.background != null) {
            for(int x = 0; x < getSize(); x++) {
                inventory.setItem(x, this.background);
            }
        }

        if(this.topAndBottom != null) {
            for(int x = 0; x < 9; x++) {
                inventory.setItem(x, this.topAndBottom);
            }

            for(int x = getSize() - 9; x < getSize(); x++) {
                inventory.setItem(x, this.topAndBottom);
            }
        }

        for(Map.Entry<Integer, ItemStack> entry : this.items.entrySet()) {
            ItemStack item = entry.getValue();
            inventory.setItem(entry.getKey(), item);
        }

        for(Map.Entry<Integer, Button> entry : this.buttons.entrySet()) {
            Button button = entry.getValue();
            button.update();
            inventory.setItem(entry.getKey(), button);
        }

        return inventory;
    }

    public void setItem(int slot, ItemStack itemStack) {
        items.put(slot, itemStack);
    }

    public int getSize() {
        return rows * 9;
    }

    public void addPermittedClickType(ClickType... clickType) {
        List<ClickType> permittedClickTypes = new ArrayList<>(Arrays.asList(getPermittedClickTypes()));
        permittedClickTypes.addAll(Arrays.asList(clickType));
        setPermittedClickTypes(permittedClickTypes.toArray(new ClickType[0]));
    }

    public void removePermittedClickType(ClickType... clickType) {
        List<ClickType> permittedClickTypes = new ArrayList<>(Arrays.asList(getPermittedClickTypes()));
        permittedClickTypes.removeAll(Arrays.asList(clickType));
        setPermittedClickTypes(permittedClickTypes.toArray(new ClickType[0]));
    }

    public void addBlockedMenuAction(InventoryAction... action) {
        List<InventoryAction> blockedActions = new ArrayList<>(Arrays.asList(getBlockedMenuActions()));
        blockedActions.addAll(Arrays.asList(action));
        setBlockedMenuActions(blockedActions.toArray(new InventoryAction[0]));
    }

    public void removeBlockedMenuAction(InventoryAction... action) {
        List<InventoryAction> blockedActions = new ArrayList<>(Arrays.asList(getBlockedMenuActions()));
        blockedActions.removeAll(Arrays.asList(action));
        setBlockedMenuActions(blockedActions.toArray(new InventoryAction[0]));
    }

    public void addBlockedMenuActionAdjacent(InventoryAction... action) {
        List<InventoryAction> blockedActions = new ArrayList<>(Arrays.asList(getBlockedMenuAdjacentActions()));
        blockedActions.addAll(Arrays.asList(action));
        setBlockedMenuAdjacentActions(blockedActions.toArray(new InventoryAction[0]));
    }

    public void removeBlockedMenuActionAdjacent(InventoryAction... action) {
        List<InventoryAction> blockedActions = new ArrayList<>(Arrays.asList(getBlockedMenuAdjacentActions()));
        blockedActions.removeAll(Arrays.asList(action));
        setBlockedMenuAdjacentActions(blockedActions.toArray(new InventoryAction[0]));
    }

    /**
     * Opens the GUI for a player.
     * @param player The player that will see the GUI.
     */
    public void open(Player player) {
        player.openInventory(this.getInventory());
    }

    public void open(Player player, boolean update) {
        if(update && this.getUpdate() != null) {
            this.update();
        }
        open(player);
    }

    /**
     * Updates the GUI with whatever GUIUpdate method was set for this GUI.
     */
    public void update() {
        this.update.onUpdate(this);
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.getOpenInventory().getTopInventory() != null) {
                Inventory inventory = player.getOpenInventory().getTopInventory();
                if(inventory.getHolder() instanceof Menu) {
                    Menu gui = (Menu) inventory.getHolder();
                    if(gui.equals(this)) {
                        gui.open(player);
                    }
                }
            }
        }
    }

    public Menu(String title, int rows) {
        this.instanceId = MainMenu.instance.getInstanceId();
        this.title = title;
        this.rows = rows;
        this.autoRefresh = true;
        this.buttons = new HashMap<>();
        this.items = new HashMap<>();
        this.blockAllInteractions = true;
    }
}
