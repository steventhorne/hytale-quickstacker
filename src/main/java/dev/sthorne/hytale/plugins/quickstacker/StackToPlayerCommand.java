package dev.sthorne.hytale.plugins.quickstacker;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class StackToPlayerCommand extends AbstractPlayerCommand {
    private static final Message MESSAGE_COMMANDS_ERRORS_UNKNOWN_ERROR = Message.raw("An unknown error has occurred.");

    private final Config<PluginConfig> Config;
    private final RequiredArg<PlayerRef> Argument;

    public StackToPlayerCommand(Config<PluginConfig> config) {
        super("playerstack", "stack to a nearby player");

        Config = config;

        this.addAliases("pstack", "stackplayer", "stackp");
        this.Argument = this.withRequiredArg("p", "Quick stacks to this player's inventory if nearby.", ArgTypes.PLAYER_REF);
        setPermissionGroup(GameMode.Adventure);
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        if (!commandContext.isPlayer()) return;
        var srcPlayer = (Player) commandContext.sender();
        var targetPlayerRef = commandContext.get(this.Argument);
        if (targetPlayerRef == null) return;
        var targetPlayer = Utils.GetPlayerFromRef(targetPlayerRef);
        if (targetPlayer == null) {
            commandContext.sendMessage(Message.translation("server.commands.errors.playerNotInWorld"));
            return;
        }

        var result = quickStackToNearbyPlayer(srcPlayer, targetPlayer);
        if (result != null) {
            commandContext.sendMessage(result);
        }
    }

    protected Message quickStackToNearbyPlayer(Player src, Player target) {
        var srcWorld = src.getWorld();
        var targetWorld = target.getWorld();
        if (srcWorld == null || targetWorld == null) return MESSAGE_COMMANDS_ERRORS_UNKNOWN_ERROR;
        if (!srcWorld.equals(targetWorld)) return MESSAGE_COMMANDS_ERRORS_UNKNOWN_ERROR;

        var srcTransform = Utils.GetPlayerTransform(src);
        var targetTransform = Utils.GetPlayerTransform(target);
        if (srcTransform == null || targetTransform == null) return MESSAGE_COMMANDS_ERRORS_UNKNOWN_ERROR;

        var srcInv = src.getInventory();
        var targetInv = target.getInventory();
        if (srcInv == null || targetInv == null) return MESSAGE_COMMANDS_ERRORS_UNKNOWN_ERROR;

        var srcPos = srcTransform.getPosition();
        var targetPos = targetTransform.getPosition();

        var maxRadius = Config.get().GetStackToPlayerRange();
        if (maxRadius > 0) {
            var dx = srcPos.x - targetPos.x;
            var dy = srcPos.y - targetPos.y;
            var dz = srcPos.z - targetPos.z;
            var distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distance > Config.get().GetStackToPlayerRange()) return Message.raw("Target player is too far.");
        }

        srcInv.getCombinedHotbarFirst().quickStackTo(targetInv.getCombinedHotbarFirst());
        if (Config.get().GetIncludeHotbar())
            srcInv.getCombinedHotbarFirst().quickStackTo(targetInv.getCombinedHotbarFirst());
        else
            srcInv.getStorage().quickStackTo(targetInv.getCombinedHotbarFirst());
        return Message.raw("Quick stacked to " + target.getDisplayName() + "'s inventory.");
    }
}
