package dev.sthorne.hytale.plugins.quickstacker;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.meta.state.ItemContainerState;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class StackToNearbyCommand extends AbstractPlayerCommand {
    protected static final int RADIUS = 10;
    public StackToNearbyCommand() {
        super("stack", "stack to nearby chests");
        setPermissionGroup(GameMode.Adventure);
    }
    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        if (!commandContext.isPlayer()) return;
        getNearbyChests((Player)commandContext.sender());
    }

    protected void getNearbyChests(Player player)
    {
        var world = player.getWorld();
        if (world == null) return;

        var transform = GetPlayerTransform(player);
        if (transform == null) return;

        var posX = (int)transform.getPosition().x;
        var posY = (int)transform.getPosition().y;
        var posZ = (int)transform.getPosition().z;
        var inventory = player.getInventory();
        if (inventory == null) return;

        int chestCount = 0;
        for (int x = posX - RADIUS; x < posX + RADIUS; x++) {
            for (int y = posY - RADIUS; y < posY + RADIUS; y++) {
                for (int z = posZ - RADIUS; z < posZ + RADIUS; z++) {
                    WorldChunk chunk = world.getChunk(ChunkUtil.indexChunkFromBlock(x, z));
                    if (chunk == null) continue;

                    var blockState = chunk.getState(x, y, z);
                    if (blockState instanceof ItemContainerState containerState) {
                        inventory.getCombinedHotbarFirst().quickStackTo(containerState.getItemContainer());
                        chestCount++;
                    }
                }
            }
        }
        player.sendMessage(Message.raw("Successfully quick stacked to " + chestCount + " nearby chests."));
    }

    protected TransformComponent GetPlayerTransform(Player player) {
        var playerRef = player.getReference();
        var world = player.getWorld();
        if (world == null || playerRef == null) return null;

        var store = world.getEntityStore().getStore();
        return store.getComponent(playerRef, TransformComponent.getComponentType());
    }
}
