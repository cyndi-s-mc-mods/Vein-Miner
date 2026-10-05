package com.cyndi.veinminer.neoforge;

import com.cyndi.veinminer.VeinMinerCore;

import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

@Mod(VeinMinerCore.MOD_ID)
public class VeinMinerNeoForge {
    public VeinMinerNeoForge() {
        VeinMinerCore.init(FMLPaths.CONFIGDIR.get());
        // Lowest priority so protection mods get to cancel the break first.
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, false, BreakBlockEvent.class,
                VeinMinerNeoForge::onBreak);
    }

    private static void onBreak(BreakBlockEvent event) {
        if (event.isCanceled() || !(event.getLevel() instanceof Level level)) return;
        // BreakBlockEvent fires before the block breaks, so the origin's tool damage is still to come.
        VeinMinerCore.onBlockBroken(level, event.getPlayer(), event.getPos(), event.getState(), 1);
    }
}
