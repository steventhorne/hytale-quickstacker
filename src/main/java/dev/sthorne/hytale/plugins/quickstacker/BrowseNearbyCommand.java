package dev.sthorne.hytale.plugins.quickstacker;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.windows.ContainerWindow;
import com.hypixel.hytale.server.core.entity.entities.player.windows.Window;
import com.hypixel.hytale.server.core.inventory.container.CombinedItemContainer;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.meta.state.ItemContainerState;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.*;
import java.util.HashSet;

public class BrowseNearbyCommand extends AbstractPlayerCommand {
    private final Config<PluginConfig> Config;

    public BrowseNearbyCommand(Config<PluginConfig> config) {
        super("browse", "Allows the player to browse all nearby chests at once.");

        Config = config;

        setPermissionGroup(GameMode.Adventure);
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        if (!commandContext.isPlayer()) return;

        var srcPlayer = (Player)commandContext.sender();
        var inv = GetCombinedInventory(srcPlayer);
        if (inv != null) {
            srcPlayer.getPageManager().setPageWithWindows(ref, store, Page.Inventory, true, new Window[]{new ContainerWindow(inv)});
        }
    }

    protected CombinedItemContainer GetCombinedInventory(Player player)
    {
        var world = player.getWorld();
        if (world == null) return null;

        var transform = Utils.GetPlayerTransform(player);
        if (transform == null) return null;

        var posX = (int)transform.getPosition().x;
        var posY = (int)transform.getPosition().y;
        var posZ = (int)transform.getPosition().z;
        var inventory = player.getInventory();
        if (inventory == null) return null;

        int maxRadius = Config.get().GetStackToChestRange();
        var containers = new HashSet<ItemContainer>();
        for (int x = posX - maxRadius; x < posX + maxRadius; x++) {
            for (int y = posY - maxRadius; y < posY + maxRadius; y++) {
                for (int z = posZ - maxRadius; z < posZ + maxRadius; z++) {
                    WorldChunk chunk = world.getChunk(ChunkUtil.indexChunkFromBlock(x, z));
                    if (chunk == null) continue;

                    var blockState = chunk.getState(x, y, z);
                    if (blockState instanceof ItemContainerState containerState) {
                        var dx = posX - x;
                        var dy = posY - y;
                        var dz = posZ - z;
                        var distance = Math.sqrt(dx*dx + dy*dy + dz*dz);
                        if (distance > maxRadius) continue;

                        containers.add(containerState.getItemContainer());
                    }
                }
            }
        }

        if (containers.isEmpty()) return null;
        return new CombinedItemContainer(containers.toArray(new ItemContainer[] {}));
    }
}
