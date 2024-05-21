package ac.echo.api.command.manager;

import ac.echo.Main;
import ac.echo.api.command.Command;
import ac.echo.api.command.exception.CommandException;
import org.reflections.Reflections;

import java.util.Set;

public class CommandManager {

    public CommandManager() {
        this.init();
    }

    private void init() {
        try {
            this.register();
        } catch (CommandException e) {
            throw new CommandException("Failed to register commands.");
        }
    }

    private void register() throws CommandException {
        final Reflections reflections = new Reflections("ac.echo.commands");
        final Set<Class<? extends Command>> commandClasses = reflections.getSubTypesOf(Command.class);

        for (Class<? extends Command> command : commandClasses) {
            try {
                final Command com = command.newInstance();
                Main.getInstance().getCommand(com.getName()).setExecutor(com);
                Main.getInstance().getCommand(com.getName()).setAliases(com.getAliases());
            } catch (IllegalAccessException | InstantiationException ignore) {}
        }
    }
}
