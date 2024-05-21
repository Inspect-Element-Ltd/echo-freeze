package ac.echo.api.command.exception;

import org.bukkit.ChatColor;

public class CommandException extends IllegalArgumentException {

    public CommandException(String message) {
        super(message);
        ChatColor.translateAlternateColorCodes('&', "&c" + message);
    }
}
