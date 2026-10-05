# Vein Miner

A mod for **Minecraft 26.3** that mines a whole ore vein at once. Available for **Fabric**, **Forge** and **NeoForge**.

## How to use

Hold **sneak** (Shift) and break an ore with a tool that can harvest it. Every connected
block of the same ore breaks along with it, diagonals included. Stone and deepslate
variants count as the same ore, so a vein that crosses the deepslate line comes out in one go.

- Works on every vanilla ore (coal, iron, copper, gold, redstone, lapis, diamond, emerald,
  nether gold, nether quartz) and ancient debris, plus modded ores tagged `c:ores`
  (the shared ore tag used by Fabric, Forge and NeoForge mods).
- Each block goes through the normal mining path, so Fortune, Silk Touch, XP, tool
  durability, hunger and stats all work the way they would if you mined it by hand.
- You need the right tool. Bare hands or a wooden pickaxe on diamond ore won't vein mine.
- By default it stops when your tool reaches 1 durability, so it won't break your pickaxe.
- Runs on the server. Players don't need it on their client to use it on a server that has it.
  In singleplayer, just install it as usual.

## Install

Download the jar for your loader from the [latest release](https://github.com/cyndi-s-mc-mods/Vein-Miner/releases/latest)
and put it in your `mods` folder:

| Loader   | Jar                              | Also needs                                                      |
|----------|----------------------------------|-----------------------------------------------------------------|
| Fabric   | `vein-miner-fabric-<version>.jar`   | [Fabric Loader](https://fabricmc.net/use/) and [Fabric API](https://modrinth.com/mod/fabric-api) |
| Forge    | `vein-miner-forge-<version>.jar`    | [Forge](https://files.minecraftforge.net) 66 or newer             |
| NeoForge | `vein-miner-neoforge-<version>.jar` | [NeoForge](https://neoforged.net) 26.3.0.48-beta or newer         |

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

The jars are written to `fabric/build/libs/`, `forge/build/libs/` and `neoforge/build/libs/`.

### Project layout

- `common/`: the vein mining logic and config. Plain Minecraft code, shared by every loader.
- `fabric/`, `forge/`, `neoforge/`: a small entry point per loader that hooks its block break
  event, plus that loader's mod metadata.

Loader and plugin versions live in `gradle.properties`.

## Releases

GitHub Actions builds every push. To publish a release with all three jars, bump `mod_version`
in `gradle.properties`, then go to Actions → Build → Run workflow and enter a tag such as `v1.1.0`
(or push a `v*` tag).
