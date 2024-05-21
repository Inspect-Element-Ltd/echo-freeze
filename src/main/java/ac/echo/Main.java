package ac.echo;

import ac.echo.api.command.manager.CommandManager;
import ac.echo.api.gui.MainMenu;
import ac.echo.config.Config;
import ac.echo.handler.FreezeHandler;
import ac.echo.listeners.FreezeListener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Getter @Setter
public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    private String url;
    private String apiKey;

    private CommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;
        Config.init();
        this.url = Config.Default.getConfig(false).getString("Echo.Api.URL");
        this.apiKey = Config.Default.getConfig(false).getString("Echo.Api.Key");

        commandManager = new CommandManager();
        new FreezeHandler();
        new MainMenu(this);

        getServer().getPluginManager().registerEvents(new FreezeListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.SEVERE, "Disabling Echo Freeze");
    }

    public String c(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
