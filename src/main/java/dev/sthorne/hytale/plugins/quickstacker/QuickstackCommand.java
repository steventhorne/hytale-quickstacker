package dev.sthorne.hytale.plugins.quickstacker;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class QuickstackCommand extends AbstractPlayerCommand {
    private final StackToNearbyCommand NearbyCommand;

    public QuickstackCommand(Config<PluginConfig> config) {
        super("quickstack", "Quick stack to all nearby chests.\n\n/stack is deprecated and will be removed, please use one of the other aliases.");
        this.addAliases("qs");
        this.addSubCommand(new StackToPlayerCommand(config));
        // commented out until scrolling support added to Inventory window
        // this.addSubCommand(new BrowseNearbyCommand(config));

        NearbyCommand = new StackToNearbyCommand(config);

        setPermissionGroup(GameMode.Adventure);
    }
    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        NearbyCommand.execute(commandContext, store, ref, playerRef, world);
    }
}
