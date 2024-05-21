package ac.echo.config;

import ac.echo.Main;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

public enum Config {
    Default(new String[]{"config"}, "config.yml", false),
    Message(new String[]{"messages", "message"}, "messages.yml", false);

    @Getter private final String[] name;
    @Getter private final String path;
    private final boolean overwrite;

    private FileConfiguration configYaml;

    Config(String[] name, String path, boolean overwrite) {
        this.name = name;
        this.path = path;
        this.overwrite = overwrite;
    }

    public static void init() {
        if (!Main.getInstance().getDataFolder().exists()) {
            Main.getInstance().getDataFolder().mkdirs();
        }

        for (Config config : Config.values()) {
            config.load();
        }
    }

    private File getFile() {
        return new File(getFullPath());
    }

    public void save() {
        try {
            configYaml.save(getFile());
        }catch (IOException e) {
            System.out.println("Error.");
        }
    }

    public FileConfiguration getConfig(boolean reload) {
        return reload ? YamlConfiguration.loadConfiguration(getFile()) : configYaml;
    }

    public String getFullPath() {
        return Main.getInstance().getDataFolder() + File.separator + path;
    }

    public void load() {
        saveResource(getPath(), overwrite);
        configYaml = YamlConfiguration.loadConfiguration(getFile());
    }

    public static void reloadConfig(String name, CommandSender sender) {
        for (Config config : Config.values()) {
            for (String n : config.getName()) {
                if (n.equalsIgnoreCase(name)) {
                    config.load();
                    return;
                }
            }
        }
    }

    public void saveResource(String resourcePath, boolean replace) {
        if (resourcePath != null && !resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = Main.getInstance().getResource(resourcePath);
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + this.getFile());
            } else {
                File outFile = new File(Main.getInstance().getDataFolder(), resourcePath);
                int lastIndex = resourcePath.lastIndexOf(47);
                File outDir = new File(Main.getInstance().getDataFolder(), resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }

                try {
                    if (outFile.exists() && !replace) {
                        Main.getInstance().getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
                    } else {
                        OutputStream out = new FileOutputStream(outFile);
                        byte[] buf = new byte[1024];

                        int len;
                        while((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }

                        out.close();
                        in.close();
                    }
                } catch (IOException var10) {
                    IOException ex = var10;
                    Main.getInstance().getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
                }

            }
        } else {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
    }
}
