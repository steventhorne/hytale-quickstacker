This is a simple quality of life mod that adds a command to quick stack to nearby chests or players.

Simply type `/quickstack` or `/qs` and you will automatically stack to any inventory within a 10 block radius.

Similarly, this mod also contains the ability to quick stack to nearby players. This can be helpful if you're out with another player and want to consolidate inventory space.

Simply type `/qs player <name>` to quick stack to any player within a 10 block radius.

# Installation
Plugins can be installed by placing them in the `%appdata%/Hytale/UserData/Mods` directory. If the folder doesn't exist, you can create this manually.

## Configuration
There are a few config options available once you've run the server with the mod enable at least once.
These options can be found in the `Quickstacker.json` file located in the `steventhorne_Quickstacker` directory within the mods folder.

- `StackToChestRange` (default: 10) - The max range a chest can be from the player to quick stack to.
- `StackToPlayerRage` (default: 10) - The max range a player can be from the player to quick stack to. A negative value gives unlimited range.
- `IncludeHotbar` (default: false) - If true, quick stacking will also quick stack items on the players hot bar.

## Troubleshooting

### I do not have permission to use the command
You do not need OP permissions, this is enabled by default for any Adventure player.

Be sure to enable it for your world by right-clicking on the world and enabling Quickstacker in the Mods list. If it does not appear in that list, you may have copied the file to the wrong location.

If you get a message saying that you don't have permission, this usually means the mod is either not installed in the correct mods folder, or it's not enabled for the world.

### My stuff disappeared but did not go into a nearby chest
If you've increased the radius configuration to a really high number, then it's possible that you quick stacked to a world generated chest that spawns with random items inside.