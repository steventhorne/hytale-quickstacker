package dev.sthorne.hytale.plugins.quickstacker;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class PluginConfig {
    public static final BuilderCodec<PluginConfig> CODEC = BuilderCodec.builder(PluginConfig.class, PluginConfig::new)
            .append(new KeyedCodec<>("StackToChestRange", Codec.INTEGER),
                    (config, val, _) -> config.StackToChestRange = val,
                    (config, _) -> config.StackToChestRange).add()
            .append(new KeyedCodec<>("StackToPlayerRange", Codec.INTEGER),
                    (config, val, _) -> config.StackToPlayerRange = val,
                    (config, _) -> config.StackToPlayerRange).add()
            .append(new KeyedCodec<>("IncludeHotbar", Codec.BOOLEAN),
                    (config, val, _) -> config.IncludeHotbar = val,
                    (config, _) -> config.IncludeHotbar).add()
            .build();

    private int StackToChestRange = 14;
    public int GetStackToChestRange() { return StackToChestRange; }

    private int StackToPlayerRange = 14;
    public int GetStackToPlayerRange() { return StackToPlayerRange; }

    private boolean IncludeHotbar = false;
    public boolean GetIncludeHotbar() { return IncludeHotbar; }
}
