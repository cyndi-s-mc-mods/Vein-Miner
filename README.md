# Vein Miner

A Fabric mod for **Minecraft 26.3** that mines a whole ore vein at once.

## How to use

Hold **sneak** (Shift) and break an ore with a tool that can harvest it. Every connected
block of the same ore breaks along with it, diagonals included. Stone and deepslate
variants count as the same ore, so a vein that crosses the deepslate line comes out in one go.

- Works on every vanilla ore (coal, iron, copper, gold, redstone, lapis, diamond, emerald,
  nether gold, nether quartz) and ancient debris, plus modded ores tagged `c:ores`.
- Each block goes through the normal mining path, so Fortune, Silk Touch, XP, tool
  durability, hunger and stats all work the way they would if you mined it by hand.
- You need the right tool. Bare hands or a wooden pickaxe on diamond ore won't vein mine.
- By default it stops when your tool reaches 1 durability, so it won't break your pickaxe.
- Runs on the server. Players don't need it on their client to use it on a server that has it.
  In singleplayer, just install it as usual.

## Install

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 26.3.
2. Put [Fabric API](https://modrinth.com/mod/fabric-api) and `vein-miner-<version>.jar`
   in your `mods` folder.

## Config

The config file `config/veinminer.properties` is created the first time the game starts:

| Key            | Default | Meaning                                                        |
|----------------|---------|----------------------------------------------------------------|
| `maxBlocks`    | `64`    | Most extra blocks broken per vein                              |
| `requireSneak` | `true`  | Only vein mine while sneaking (`false` = always vein mine ores) |
| `protectTool`  | `true`  | Stop before your tool would break                              |

Restart the game after editing it.

## Building

Needs **JDK 25** (Minecraft 26.x requires Java 25).

```sh
./gradlew build
```

The mod jar is written to `build/libs/`.

Before building, check `gradle.properties` against <https://fabricmc.net/develop> and update
`loader_version`, `loom_version` and `fabric_api_version` to the current versions for 26.3.
