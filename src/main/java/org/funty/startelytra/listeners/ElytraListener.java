package org.funty.startelytra.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.funty.startelytra.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class ElytraListener implements Listener {

    ItemStack Elytra = new ItemStack(Material.ELYTRA);
    ItemMeta ElytraMeta = this.Elytra.getItemMeta();

    private static final ArrayList<UUID> glider = new ArrayList<>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Location location = event.getPlayer().getWorld().getSpawnLocation();
        location.setX(Double.parseDouble(Main.getPlugin().getConfig().getString("Center.X")));
        location.setY(Double.parseDouble(Main.getPlugin().getConfig().getString("Center.Y")));
        location.setZ(Double.parseDouble(Main.getPlugin().getConfig().getString("Center.Z")));
        int radiusC = Integer.parseInt(Main.getPlugin().getConfig().getString("Radius"));
        int radius = radiusC * radiusC;

        if (location.distanceSquared(event.getPlayer().getLocation()) <= radius) {
            if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                if (player.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.AIR)) {
                    if (!(glider.contains(player.getUniqueId()))) {
                        glider.add(uuid);
                        if(player.getName().startsWith(Main.getPlugin().getConfig().getString("Geysermc.Prefix"))){
                                if(player.getInventory().getChestplate() == null){
                                    this.ElytraMeta.setDisplayName(Main.getPlugin().getConfig().getString("Geysermc.Elytra.DisplayName"));
                                    this.ElytraMeta.setLore(Collections.singletonList(Main.getPlugin().getConfig().getString("Geysermc.Elytra.Lore")));
                                    this.ElytraMeta.setUnbreakable(true);
                                    this.ElytraMeta.addEnchant(Enchantment.DURABILITY, 1000, true);
                                    this.ElytraMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
                                    this.ElytraMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
                                    this.ElytraMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
                                    this.Elytra.setItemMeta(this.ElytraMeta);
                                    player.getInventory().setChestplate(this.Elytra);
                                }else {
                                    player.sendMessage(Main.getPlugin().getConfig().getString("Geysermc.Messages.ChestOccupied"));
                                }
                        }
                        player.setGliding(true);
                        player.setAllowFlight(true);
                    }
                }
            }
        }

        if (!(player.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.AIR))) {
            glider.remove(uuid);
            if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) player.setAllowFlight(false);
            if(player.getName().startsWith(Main.getPlugin().getConfig().getString("Geysermc.Prefix"))){
                if(!(player.getInventory().getChestplate() == null)){
                    if(player.getInventory().getChestplate().equals(this.Elytra)){
                        player.getInventory().setChestplate((ItemStack)null);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (glider.contains(uuid)) {
            event.setCancelled(true);
            Vector velocity = player.getLocation().getDirection().multiply(2).add(new Vector(0, Double.parseDouble((Main.getPlugin().getConfig().getString("Boost"))), 0));
            player.setVelocity(velocity);
        }
    }

    @EventHandler
    public void onSneakItems(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (event.getPlayer().getName().startsWith(Main.getPlugin().getConfig().getString("Geysermc.Prefix"))) {
            if (glider.contains(uuid)) {
                event.setCancelled(true);
                Vector velocity = player.getLocation().getDirection().multiply(2).add(new Vector(0, Double.parseDouble((Main.getPlugin().getConfig().getString("Boost"))), 0));
                player.setVelocity(velocity);
            }
        }
    }

    @EventHandler
    public void onFlightToggle(PlayerToggleFlightEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (event.getPlayer().getGameMode() == GameMode.SURVIVAL && glider.contains(uuid)) event.setCancelled(true);
    }

    @EventHandler
    public void onGlideToggle(EntityToggleGlideEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            UUID uuid = player.getUniqueId();
            if (glider.contains(uuid)) event.setCancelled(true);
        }
    }
}