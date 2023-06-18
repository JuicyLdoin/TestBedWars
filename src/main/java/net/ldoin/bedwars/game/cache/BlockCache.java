package net.ldoin.bedwars.game.cache;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public final class BlockCache {

    private final Map<Location, Block> placedBlocks = new HashMap<>();

    public final boolean containsBlock(Block block) {
        return placedBlocks.containsKey(block.getLocation());
    }

    public final void addBlock(Block block) {
        placedBlocks.put(block.getLocation(), block);
    }

    public final void removeBlock(Block block) {
        placedBlocks.remove(block.getLocation());
    }

    public final void clear() {
        placedBlocks.values().forEach(block -> block.setType(Material.AIR));
        placedBlocks.clear();
    }
}