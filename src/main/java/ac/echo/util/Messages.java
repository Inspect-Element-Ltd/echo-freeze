package ac.echo.util;

import ac.echo.config.Config;

import java.util.List;

public class Messages {
    public static final String NOT_PLAYER = Config.Message.getConfig(false).getString("Not-Player");
    public static final String NO_PERMS = Config.Message.getConfig(false).getString("No-Permission");
    public static final String INVALID_PLAYER = Config.Message.getConfig(false).getString("Invalid-Player");

    public static final String FREEZE_FROZEN = Config.Message.getConfig(false).getString("Freeze.Frozen");
    public static final String FREEZE_UNFROZEN = Config.Message.getConfig(false).getString("Freeze.Unfrozen");
    public static final String FREEZE_PLAYER_DOWNLOAD = Config.Message.getConfig(false).getString("Freeze.Player.Download");
    public static final String FREEZE_PLAYER_FREEZE_SELF = Config.Message.getConfig(false).getString("Freeze.Player.Freeze-Self");
    public static final String FREEZE_PLAYER_QUIT_FROZEN = Config.Message.getConfig(false).getString("Freeze.Player.Quit-Frozen");
    public static final String FREEZE_PLAYER_ADMITTED = Config.Message.getConfig(false).getString("Freeze.Player.Admitted");
    public static final String FREEZE_PLAYER_REFUSED = Config.Message.getConfig(false).getString("Freeze.Player.Refused");
    public static final String FREEZE_PLAYER_PROCEED = Config.Message.getConfig(false).getString("Freeze.Player.Proceeding");
    public static final String FREEZE_BLOCK_COMMAND = Config.Message.getConfig(false).getString("Freeze.Player.Block-Command");
    public static final String FREEZE_BLOCK_DAMAGE = Config.Message.getConfig(false).getString("Freeze.Player.Block-Damage");
    public static final String FREEZE_BLOCK_DAMAGE_FROZEN = Config.Message.getConfig(false).getString("Freeze.Player.Block-Damage-Frozen");
    public static final String FREEZE_BLOCK_DROP_ITEM = Config.Message.getConfig(false).getString("Freeze.Player.Block-Drop-Item");
    public static final String FREEZE_BLOCK_BREAK_BLOCK = Config.Message.getConfig(false).getString("Freeze.Player.Block-Break-Block");
    public static final String FREEZE_BLOCK_PLACE_BLOCK = Config.Message.getConfig(false).getString("Freeze.Player.Block-Place-Block");
    public static final String FREEZE_BLOCK_TELEPORT = Config.Message.getConfig(false).getString("Freeze.Player.Block-Teleport");
    public static final String FREEZE_BLOCK_INTERACTIONS = Config.Message.getConfig(false).getString("Freeze.Player.Block-Interactions");

    public static final List<String> FREEZE_MESSAGE = Config.Message.getConfig(false).getStringList("Freeze.Player.Freeze-Message");
}
