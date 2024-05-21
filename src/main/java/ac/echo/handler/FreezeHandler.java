package ac.echo.handler;

import ac.echo.Main;
import ac.echo.config.Config;
import ac.echo.util.Messages;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

@Setter
public class FreezeHandler {

    @Getter
    private static FreezeHandler instance;

    @Getter
    private HashMap<Player, Boolean> frozenPlayerMap;

    public FreezeHandler() {
        instance = this;
        this.frozenPlayerMap = new HashMap<>();
    }

    public void addFrozenPlayer(Player player, boolean guiLocked) {
        frozenPlayerMap.put(player, guiLocked);
    }

    public void removeFrozenPlayer(Player player) {
        frozenPlayerMap.remove(player);
    }

    public void setGuiLocked(Player player, boolean guiLocked) {
        if (isFrozen(player)) {
            frozenPlayerMap.put(player, guiLocked);
        }
    }

    public boolean isFrozen(Player target) {
        return frozenPlayerMap.get(target) != null;
    }

    public boolean isGuiLocked(Player target) {
        return isFrozen(target) && frozenPlayerMap.get(target);
    }

    public void startTask(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player == null) {
                    this.cancel();
                    return;
                }

                if (isFrozen(player)) {
                    for (String line : Messages.FREEZE_MESSAGE) {
                        player.sendMessage(Main.getInstance().c(line));
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0L, Config.Default.getConfig(false).getInt("Echo.Freeze-Message-Delay") * 20L);
    }
}
