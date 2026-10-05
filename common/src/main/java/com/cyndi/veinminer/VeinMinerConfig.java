package com.cyndi.veinminer;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Simple properties-file config stored at config/veinminer.properties. */
public final class VeinMinerConfig {
    /** Maximum number of extra blocks broken per vein (the block you mined is not counted). */
    public int maxBlocks = 64;
    /** Only vein mine while sneaking. Turn off to always vein mine ores. */
    public boolean requireSneak = true;
    /** Stop before the tool would break, leaving it at 1 durability. */
    public boolean protectTool = true;

    public static VeinMinerConfig load(Path configDir) {
        VeinMinerConfig config = new VeinMinerConfig();
        Path path = configDir.resolve("veinminer.properties");
        Properties props = new Properties();

        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                props.load(reader);
            } catch (IOException e) {
                VeinMinerCore.LOGGER.warn("Could not read {}, using defaults", path, e);
            }
        }

        config.maxBlocks = parseInt(props.getProperty("maxBlocks"), config.maxBlocks);
        config.requireSneak = parseBool(props.getProperty("requireSneak"), config.requireSneak);
        config.protectTool = parseBool(props.getProperty("protectTool"), config.protectTool);
        config.maxBlocks = Math.max(0, config.maxBlocks);

        props.setProperty("maxBlocks", Integer.toString(config.maxBlocks));
        props.setProperty("requireSneak", Boolean.toString(config.requireSneak));
        props.setProperty("protectTool", Boolean.toString(config.protectTool));
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                props.store(writer, "Vein Miner config");
            }
        } catch (IOException e) {
            VeinMinerCore.LOGGER.warn("Could not write {}", path, e);
        }
        return config;
    }

    private static int parseInt(String value, int fallback) {
        if (value == null) return fallback;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private static boolean parseBool(String value, boolean fallback) {
        return value == null ? fallback : Boolean.parseBoolean(value.trim());
    }
}
