package dev.sthorne.hytale.plugins.quickstacker;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class Plugin extends JavaPlugin {
    public static Config<PluginConfig> Config;

    public Plugin(@NonNullDecl JavaPluginInit init) {
        super(init);
        Config = this.withConfig("Quickstacker", PluginConfig.CODEC);
    }

    @Override
    protected void setup() {
        super.setup();
        Config.save();

        this.getCodecRegistry(Interaction.CODEC).register("Steventhorne_Quickstacker_QuickstackerInteraction", QuickstackerInteraction.class, QuickstackerInteraction.CODEC);

        getCommandRegistry().registerCommand(new QuickstackCommand(Config));
        getCommandRegistry().registerCommand(new StackToNearbyCommand());
        getCommandRegistry().registerCommand(new StackToPlayerCommand(Config, true));
    }
}