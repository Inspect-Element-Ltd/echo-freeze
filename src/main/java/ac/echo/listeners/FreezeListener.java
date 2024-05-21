package ac.echo.listeners;

import ac.echo.Main;
import ac.echo.api.gui.action.ButtonAction;
import ac.echo.api.gui.button.Button;
import ac.echo.api.gui.gui.GUICloseUpdate;
import ac.echo.api.gui.gui.Menu;
import ac.echo.config.Config;
import ac.echo.event.FreezePlayerEvent;
import ac.echo.handler.FreezeHandler;
import ac.echo.util.Echo;
import ac.echo.util.Messages;
import ac.echo.util.item.ItemBuilder;
import ac.echo.util.item.ItemDataColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class FreezeListener implements Listener {

    private Menu gui = null;

    @EventHandler
    public void onPlayerFrozen(FreezePlayerEvent event) {
        Player target = event.getPlayerFrozen();
        Player staff = event.getPlayer();

        gui = new Menu(Config.Default.getConfig(false).getString("Echo.Frozen.Gui.Title"), Config.Default.getConfig(false).getInt("Echo.Frozen.Gui.Rows"));
        gui.setBlockAllInteractions(true);
        Button proceed = new Button(new ItemBuilder(Material.WOOL).data(ItemDataColor.ORANGE.getValue()).build(), "&6Proceed");
        Button admit = new Button(new ItemBuilder(Material.WOOL).data(ItemDataColor.GREEN.getValue()).build(), "&aAdmit");
        Button refuse = new Button(new ItemBuilder(Material.WOOL).data(ItemDataColor.RED.getValue()).build(), "&cRefuse");

        proceed.setCloseOnClick(true);
        refuse.setCloseOnClick(true);
        admit.setCloseOnClick(true);

        proceed.setLore("", "&aProceed with the screen-share.");
        admit.setLore("", "&7Admit to cheating and receive a shorter ban.");
        refuse.setLore("", "&c&lNOTE: &cRefusing will result in a ban.");

        gui.setBackground(new ItemBuilder(Material.STAINED_GLASS_PANE).data(ItemDataColor.BLACK.getValue()).name("&f").build());

        gui.setButton(11, admit);
        gui.setButton(13, proceed);
        gui.setButton(15, refuse);

        refuse.withListener(new ButtonAction() {
            @Override
            public void run(Player player, Menu gui, Button button, InventoryClickEvent event) {
                staff.sendMessage(Main.getInstance().c(Messages.FREEZE_PLAYER_REFUSED).replace("{PLAYER}", target.getName()));
                FreezeHandler.getInstance().setGuiLocked(target, false);
                FreezeHandler.getInstance().removeFrozenPlayer(target);
                staff.performCommand(Config.Default.getConfig(false).getString("Echo.Frozen.Ban.Refusal").replace("{TARGET}", target.getName()));
            }
        });

        admit.withListener(new ButtonAction() {
            @Override
            public void run(Player player, Menu gui, Button button, InventoryClickEvent event) {
                staff.sendMessage(Main.getInstance().c(Messages.FREEZE_PLAYER_ADMITTED).replace("{PLAYER}", target.getName()));
                FreezeHandler.getInstance().setGuiLocked(target, false);
                FreezeHandler.getInstance().removeFrozenPlayer(target);
                staff.performCommand(Config.Default.getConfig(false).getString("Echo.Frozen.Ban.Admitted").replace("{TARGET}", target.getName()));
            }
        });

        proceed.withListener(new ButtonAction() {
            @Override
            public void run(Player player, Menu gui, Button button, InventoryClickEvent event) {
                staff.sendMessage(Main.getInstance().c(Messages.FREEZE_PLAYER_PROCEED).replace("{PLAYER}", target.getName()));
                FreezeHandler.getInstance().setGuiLocked(target, false);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.sendMessage(Main.getInstance().c(Messages.FREEZE_PLAYER_DOWNLOAD).replace("{LINK}", Echo.getDownloadLink()));
                    }
                }.runTaskAsynchronously(Main.getInstance());
            }
        });

        gui.setGuiCloseUpdate(new GUICloseUpdate() {
            @Override
            public void onClose(Player player, Menu gui, InventoryCloseEvent event) {
                if (FreezeHandler.getInstance().isGuiLocked(target)) {
                    gui.open(target);
                }
            }
        });

        gui.open(target);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (FreezeHandler.getInstance().isFrozen(player)) {
            if (event.getTo().getBlockX() != event.getFrom().getBlockX() || event.getTo().getBlockZ() != event.getFrom().getBlockZ()) {
                event.setTo(event.getFrom());
            }

            if (FreezeHandler.getInstance().isGuiLocked(player)) {
                event.setTo(event.getFrom());
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (FreezeHandler.getInstance().isFrozen(player)) {
            for (String cmd : Config.Default.getConfig(false).getStringList("Echo.Allowed-Commands")) {
                if (event.getMessage().split(" ")[0].equalsIgnoreCase("/" + cmd)) {
                    return;
                }
            }
            event.setCancelled(true);
            player.sendMessage(Main.getInstance().c(Messages.FREEZE_BLOCK_COMMAND));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (FreezeHandler.getInstance().isFrozen(player)) {
                event.getDamager().sendMessage(Main.getInstance().c(Messages.FREEZE_BLOCK_DAMAGE).replace("{TARGET}", player.getName()));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamageFrozen(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (FreezeHandler.getInstance().isFrozen(player)) {
                event.getDamager().sendMessage(Main.getInstance().c(Messages.FREEZE_BLOCK_DAMAGE_FROZEN).replace("{TARGET}", event.getEntity().getName()));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamageAll(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (FreezeHandler.getInstance().isFrozen(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent breakEvent) {
        if (FreezeHandler.getInstance().isFrozen(breakEvent.getPlayer())) {
            breakEvent.setCancelled(true);
            breakEvent.getPlayer().sendMessage(Main.getInstance().c(Messages.FREEZE_BLOCK_BREAK_BLOCK));
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent placeEvent) {
        if (FreezeHandler.getInstance().isFrozen(placeEvent.getPlayer())) {
            placeEvent.setBuild(false);
            placeEvent.getPlayer().sendMessage(Main.getInstance().c(Messages.FREEZE_BLOCK_PLACE_BLOCK));
        }
    }

    @EventHandler
    public void onDropItemEvent(PlayerDropItemEvent event) {
        if (FreezeHandler.getInstance().isFrozen(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Main.getInstance().c(Messages.FREEZE_BLOCK_DROP_ITEM));
        }
    }

    @EventHandler
    public void onTeleportEvent(PlayerTeleportEvent event) {
        if (FreezeHandler.getInstance().isFrozen(event.getPlayer()) && event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Main.getInstance().c(Messages.FREEZE_BLOCK_TELEPORT));
        }
    }

    @EventHandler
    public void onPickUpItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (FreezeHandler.getInstance().isFrozen(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (FreezeHandler.getInstance().isFrozen(event.getPlayer())) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Main.getInstance().c(Messages.FREEZE_BLOCK_INTERACTIONS));
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (FreezeHandler.getInstance().isFrozen(player)) {
            for (Player staff : Bukkit.getOnlinePlayers()) {
                if (staff.hasPermission(Config.Default.getConfig(false).getString("Echo.Staff"))) {
                    staff.sendMessage(Main.getInstance().c(Messages.FREEZE_PLAYER_QUIT_FROZEN).replace("{TARGET}", player.getName()));
                }
            }
        }
    }
}
