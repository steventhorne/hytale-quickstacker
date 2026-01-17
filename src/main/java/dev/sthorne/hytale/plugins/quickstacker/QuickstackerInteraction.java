package dev.sthorne.hytale.plugins.quickstacker;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.Vector3i;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class QuickstackerInteraction extends SimpleBlockInteraction {
    public static final BuilderCodec<QuickstackerInteraction> CODEC = BuilderCodec.builder(QuickstackerInteraction.class, QuickstackerInteraction::new).build();

    @Override
    protected void interactWithBlock(@NonNullDecl World world, @NonNullDecl CommandBuffer<EntityStore> commandBuffer, @NonNullDecl InteractionType interactionType, @NonNullDecl InteractionContext interactionContext, @NullableDecl ItemStack itemStack, @NonNullDecl com.hypixel.hytale.math.vector.Vector3i vector3i, @NonNullDecl CooldownHandler cooldownHandler) {
        var ref = interactionContext.getEntity();
        var store = ref.getStore();
        var player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;
        StackToNearbyCommand.quickStackToNearbyChests(player);
    }

    @Override
    protected void simulateInteractWithBlock(@NonNullDecl InteractionType interactionType, @NonNullDecl InteractionContext interactionContext, @NullableDecl ItemStack itemStack, @NonNullDecl World world, @NonNullDecl com.hypixel.hytale.math.vector.Vector3i vector3i) {

    }
}
