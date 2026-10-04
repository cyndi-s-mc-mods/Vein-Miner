package com.cyndi.veinminer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class VeinMiner implements ModInitializer {
    public static final String MOD_ID = "veinminer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /**
     * Ore groups. Blocks in the same group count as one vein, so stone and deepslate
     * variants of the same ore get mined together.
     */
    private static final List<TagKey<Block>> ORE_GROUPS = List.of(
            oreTag("coal_ores"),
            oreTag("iron_ores"),
            oreTag("copper_ores"),
            oreTag("gold_ores"),
            oreTag("redstone_ores"),
            oreTag("lapis_ores"),
            oreTag("diamond_ores"),
            oreTag("emerald_ores"));

    private static TagKey<Block> oreTag(String name) {
        return TagKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(name));
    }

    private static VeinMinerConfig config;

    /** Set while we're breaking extra blocks so our own breaks don't start new veins. */
    private static boolean veinMining = false;

    @Override
    public void onInitialize() {
        config = VeinMinerConfig.load();
        PlayerBlockBreakEvents.AFTER.register(VeinMiner::onBlockBroken);
        LOGGER.info("Vein Miner loaded (max {} blocks, sneak required: {})", config.maxBlocks, config.requireSneak);
    }

    private static void onBlockBroken(Level level, Player player, BlockPos origin, BlockState state,
                                      BlockEntity blockEntity) {
        if (veinMining || !(player instanceof ServerPlayer serverPlayer)) return;
        if (config.requireSneak && !player.isShiftKeyDown()) return;
        if (!isOre(state)) return;
        // Same rule vanilla uses for drops: no vein mining ores with the wrong tool or bare hands.
        if (!player.isCreative() && !player.hasCorrectToolForDrops(state)) return;

        Predicate<BlockState> sameVein = veinMatcher(state);
        List<BlockPos> vein = findVein(level, origin, sameVein, config.maxBlocks);

        veinMining = true;
        try {
            for (BlockPos pos : vein) {
                if (!player.isCreative()) {
                    ItemStack tool = player.getMainHandItem();
                    if (tool.isEmpty() || !player.hasCorrectToolForDrops(level.getBlockState(pos))) break;
                    if (config.protectTool && tool.isDamageableItem()
                            && tool.getMaxDamage() - tool.getDamageValue() <= 1) break;
                }
                // Goes through the normal survival break path: drops, Fortune/Silk Touch,
                // XP, tool damage, hunger, stats and protection mods all apply.
                serverPlayer.gameMode.destroyBlock(pos);
            }
        } finally {
            veinMining = false;
        }
    }

    private static boolean isOre(BlockState state) {
        if (state.is(Blocks.ANCIENT_DEBRIS) || state.is(ConventionalBlockTags.ORES)) return true;
        for (TagKey<Block> group : ORE_GROUPS) {
            if (state.is(group)) return true;
        }
        return false;
    }

    private static Predicate<BlockState> veinMatcher(BlockState origin) {
        for (TagKey<Block> group : ORE_GROUPS) {
            if (origin.is(group)) return s -> s.is(group);
        }
        Block block = origin.getBlock();
        return s -> s.is(block);
    }

    /** Breadth-first search over all 26 neighbours, so diagonally touching ore counts too. */
    private static List<BlockPos> findVein(Level level, BlockPos origin, Predicate<BlockState> sameVein, int limit) {
        List<BlockPos> found = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        visited.add(origin);
        queue.add(origin);

        while (!queue.isEmpty() && found.size() < limit) {
            BlockPos current = queue.poll();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) continue;
                        BlockPos next = current.offset(dx, dy, dz);
                        if (!visited.add(next) || !level.isLoaded(next)) continue;
                        if (sameVein.test(level.getBlockState(next))) {
                            found.add(next);
                            queue.add(next);
                            if (found.size() >= limit) return found;
                        }
                    }
                }
            }
        }
        return found;
    }
}
