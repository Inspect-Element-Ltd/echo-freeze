package ac.echo.api.command;

import ac.echo.api.command.exception.CommandException;
import ac.echo.api.command.sub.SubCommand;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class Command implements CommandExecutor, TabCompleter {

    private static Command instance;

    private String name, desc, usage, permission;
    private List<String> aliases;

    private ArrayList<SubCommand> subCommands;

    protected Command() {
        instance = this;
        final CommandData data = this.getClass().getAnnotation(CommandData.class);
        Validate.notNull(data, "CONFUSED ANNOTATION EXCEPTION");
        this.subCommands = new ArrayList<>();

        this.name = data.command();
        this.desc = data.desc();
        this.usage = data.usage();
        this.permission = data.permission();
        this.aliases = Arrays.asList(data.aliases());
    }

    public abstract void execute(CommandSender sender, String... args) throws CommandException;

    public final void addSubCommands(SubCommand... subCommand) {
        this.subCommands.addAll(Arrays.asList(subCommand));
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String s, String[] args) {
        this.execute(sender, args);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String s, String[] args) {
        if (getSubCommands().isEmpty()) {
            return null;
        }

        if (args.length == 1) {

            return this.getSubCommands()
                    .stream()
                    .map(SubCommand::getName)
                    .collect(Collectors.toList())
                    .stream()
                    .filter(name1 -> name1.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }

        return null;
    }
}