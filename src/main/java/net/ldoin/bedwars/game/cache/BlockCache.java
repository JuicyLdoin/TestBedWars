package net.ldoin.bedwars.game.cache;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class BlockCache {

    private final Map<Location, Block> placedBlocks = new HashMap<>();

    public boolean containsBlock(Block block) {
        return placedBlocks.containsKey(block.getLocation());
    }

    public void addBlock(Block block) {
        placedBlocks.put(block.getLocation(), block);
    }

    public void removeBlock(Block block) {
        placedBlocks.remove(block.getLocation());
    }

    public void clear() {
        placedBlocks.values().forEach(block -> block.setType(Material.AIR));
        placedBlocks.clear();
    }
}