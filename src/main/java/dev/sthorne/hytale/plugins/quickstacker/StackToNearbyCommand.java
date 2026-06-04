package dev.sthorne.hytale.plugins.quickstacker;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.transaction.ItemStackTransaction;
import com.hypixel.hytale.server.core.inventory.transaction.ListTransaction;
import com.hypixel.hytale.server.core.inventory.transaction.MoveTransaction;
import com.hypixel.hytale.server.core.modules.block.components.ItemContainerBlock;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.awt.*;
import java.util.Objects;

public class StackToNearbyCommand extends AbstractPlayerCommand {
    public StackToNearbyCommand() {
        super("stack", "Quick stack to all nearby chests.\n\n/stack is deprecated and will be removed, please use one of the other aliases.");
        this.addAliases("cheststack", "cstack", "stackchest", "stackc");
    }

    @NullableDecl
    @Override
    protected String generatePermissionNode() {
        return "quickstack";
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        if (!commandContext.isPlayer()) return;
        if (!commandContext.getInputString().equalsIgnoreCase("quickstack") && !commandContext.getInputString().equalsIgnoreCase("qs")) {
            var deprecationMessage = Message.raw("/" + commandContext.getInputString() + " is deprecated and will be removed. Please use the /qs or /quickstack command.");
            deprecationMessage.color(Color.YELLOW);
            commandContext.sendMessage(deprecationMessage);
        }

        var srcPlayer = (PlayerRef)commandContext.sender();
        quickStackToNearbyChests(srcPlayer);
    }


    public static void quickStackToNearbyChests(PlayerRef playerRef)
    {
        var ref = playerRef.getReference();
        if (ref == null) return;

        var player = ref.getStore().getComponent(ref, Player.getComponentType());
        if (player == null) return;

        var world = player.getWorld();
        if (world == null) return;

        var transform = Utils.GetPlayerTransform(player);
        if (transform == null) return;

        var posX = (int)transform.getPosition().x;
        var posY = (int)transform.getPosition().y;
        var posZ = (int)transform.getPosition().z;
        var inventory = player.getInventory();
        if (inventory == null) return;

        int availableChests = 0;
        int chestCount = 0;
        int itemCount = 0;
        int maxRadius = Plugin.Config.get().GetStackToChestRange();
        for (int x = posX - maxRadius; x < posX + maxRadius; x++) {
            for (int y = posY - maxRadius; y < posY + maxRadius; y++) {
                for (int z = posZ - maxRadius; z < posZ + maxRadius; z++) {
                    WorldChunk chunk = world.getChunk(ChunkUtil.indexChunkFromBlock(x, z));
                    if (chunk == null) continue;

                    var blockRef = chunk.getBlockComponentEntity(x, y, z);
                    if (blockRef == null) continue;

                    var store = blockRef.getStore();
                    var containerComponent = store.getComponent(blockRef, ItemContainerBlock.getComponentType());
                    if (containerComponent == null) continue;

                    var dx = posX - x;
                    var dy = posY - y;
                    var dz = posZ - z;
                    var distance = Math.sqrt(dx*dx + dy*dy + dz*dz);
                    if (distance > maxRadius) continue;

                    ListTransaction<MoveTransaction<ItemStackTransaction>> transaction;
                    if (Plugin.Config.get().GetIncludeHotbar()) {
                        var inv = InventoryComponent.getCombined(ref.getStore(), ref, InventoryComponent.HOTBAR_FIRST);
                        transaction = inv.quickStackTo(containerComponent.getItemContainer());
                    }
                    else {
                        var inv = InventoryComponent.getCombined(ref.getStore(), ref, Objects.requireNonNull(InventoryComponent.getComponentTypeById(InventoryComponent.STORAGE_SECTION_ID)));
                        transaction = inv.quickStackTo(containerComponent.getItemContainer());
                    }

                    var change = Utils.GetMovedItemQuantityFromTransaction(transaction);
                    if (change > 0) chestCount++;

                    itemCount += change;
                    availableChests++;
                }
            }
        }

        if (availableChests <= 0)
            playerRef.sendMessage(Message.raw("There are no nearby chests."));
        else if (itemCount > 0)
            playerRef.sendMessage(Message.raw("Successfully quick stacked " + itemCount + " items to " + chestCount + " nearby chests."));
        else
            playerRef.sendMessage(Message.raw("No items capable of being quick stacked."));
    }
}
