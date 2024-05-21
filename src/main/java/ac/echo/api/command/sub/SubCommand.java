package ac.echo.api.command.sub;

import ac.echo.api.command.Command;
import ac.echo.api.command.exception.CommandException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

@Setter @Getter
public abstract class SubCommand {
    private String name, desc, usage, permission;
    private List<String> alias;

    private Command parent;

    public SubCommand(Command parent) {
        final SubCommandData data = this.getClass().getAnnotation(SubCommandData.class);
        Validate.notNull(data, "CONFUSED ANNOTATION EXCEPTION");

        this.name = data.command();
        this.desc = data.desc();
        this.usage = data.usage();
        this.permission = data.permission();
        this.parent = parent;
        this.alias = Arrays.asList(data.alias());
    }

    public abstract void execute(CommandSender sender, String... args) throws CommandException;
}
