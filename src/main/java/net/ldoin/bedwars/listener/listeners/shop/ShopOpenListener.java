package net.ldoin.bedwars.listener.listeners.shop;

import net.ldoin.bedwars.game.GameManager;
import net.ldoin.bedwars.game.shop.Shop;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public final class ShopOpenListener implements Listener {

    private final Shop shop;

    public ShopOpenListener(GameManager gameManager) {
        shop = gameManager.getGame().getShop();
    }

    @EventHandler
    private void onInteract(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity.getType() != EntityType.VILLAGER) {
            return;
        }
        if (!entity.getCustomName().equals("Магазин")) {
            return;
        }
        event.setCancelled(true);
        shop.open(event.getPlayer());
    }
}