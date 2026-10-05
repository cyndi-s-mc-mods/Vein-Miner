package com.cyndi.veinminer.forge;

import com.cyndi.veinminer.VeinMinerCore;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(VeinMinerCore.MOD_ID)
public class VeinMinerForge {
    public VeinMinerForge() {
        VeinMinerCore.init(FMLPaths.CONFIGDIR.get());
        BlockEvent.BreakEvent.BUS.addListener(VeinMinerForge::onBreak);
    }

    private static void onBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof Level level)) return;
        // BreakEvent fires before the block breaks, so the origin's tool damage is still to come.
        VeinMinerCore.onBlockBroken(level, event.getPlayer(), event.getPos(), event.getState(), 1);
    }
}
