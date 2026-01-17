package dev.sthorne.hytale.plugins.quickstacker;

import com.hypixel.hytale.server.core.command.commands.player.inventory.InventoryCommand;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class Plugin extends JavaPlugin {
    private final Config<PluginConfig> Config;

    public Plugin(@NonNullDecl JavaPluginInit init) {
        super(init);
        Config = this.withConfig("Quickstacker", PluginConfig.CODEC);
    }

    @Override
    protected void setup() {
        super.setup();
        Config.save();

        getCommandRegistry().registerCommand(new QuickstackCommand(Config));
        getCommandRegistry().registerCommand(new StackToNearbyCommand(Config));
        getCommandRegistry().registerCommand(new StackToPlayerCommand(Config, true));
    }
}