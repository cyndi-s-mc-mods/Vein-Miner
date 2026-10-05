package com.cyndi.veinminer.fabric;

import com.cyndi.veinminer.VeinMinerCore;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.loader.api.FabricLoader;

public class VeinMinerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        VeinMinerCore.init(FabricLoader.getInstance().getConfigDir());
        // AFTER fires once the block is gone, so the origin's tool damage is already applied.
        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) ->
                VeinMinerCore.onBlockBroken(level, player, pos, state, 0));
    }
}
