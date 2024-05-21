package ac.echo.commands;

import ac.echo.Main;
import ac.echo.api.command.Command;
import ac.echo.api.command.CommandData;
import ac.echo.api.command.exception.CommandException;
import ac.echo.event.FreezePlayerEvent;
import ac.echo.handler.FreezeHandler;
import ac.echo.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandData(command = "freeze", permission = "echo.freeze", usage = "&cUsage: /freeze <player>")
public class FreezeCommand extends Command {

    @Override
    public void execute(CommandSender sender, String... args) throws CommandException {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.getInstance().c(Messages.NOT_PLAYER));
            return;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(getPermission())) {
            player.sendMessage(Main.getInstance().c(Messages.NO_PERMS));
            return;
        }

        if (args.length != 1) {
            player.sendMessage(Main.getInstance().c(getUsage()));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Main.getInstance().c(Messages.INVALID_PLAYER));
            return;
        }

        if (FreezeHandler.getInstance().isFrozen(target)) {
            FreezeHandler.getInstance().removeFrozenPlayer(target);
            player.sendMessage(Main.getInstance().c(Messages.FREEZE_UNFROZEN.replace("{PLAYER}", target.getName())));
            return;
        }

        if (target == player) {
            player.sendMessage(Main.getInstance().c(Messages.FREEZE_PLAYER_FREEZE_SELF));
            return;
        }

        FreezeHandler.getInstance().addFrozenPlayer(target, true);
        player.sendMessage(Main.getInstance().c(Messages.FREEZE_FROZEN.replace("{PLAYER}", target.getName())));
        FreezeHandler.getInstance().startTask(target);
        Bukkit.getPluginManager().callEvent(new FreezePlayerEvent(target, player));
    }
}
