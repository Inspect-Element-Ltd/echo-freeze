package ac.echo.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class FreezePlayerEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player playerFrozen;
    private final Player player;

    public FreezePlayerEvent(Player frozenPlayer, Player who) {
        this.playerFrozen = frozenPlayer;
        this.player = who;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
