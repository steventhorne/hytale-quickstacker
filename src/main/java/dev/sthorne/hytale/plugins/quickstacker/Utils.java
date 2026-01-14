package dev.sthorne.hytale.plugins.quickstacker;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public class Utils {
    public static TransformComponent GetPlayerTransform(Player player) {
        var playerRef = player.getReference();
        var world = player.getWorld();
        if (world == null || playerRef == null) return null;

        var store = world.getEntityStore().getStore();
        return store.getComponent(playerRef, TransformComponent.getComponentType());
    }

    public static Player GetPlayerFromRef(PlayerRef playerRef) {
        var ref = playerRef.getReference();
        if (ref == null) return null;

        var store = ref.getStore();
        return store.getComponent(ref, Player.getComponentType());
    }
}
