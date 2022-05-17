package me.senoropi.bingo.main;

import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class BingoListener implements Listener {
    public Bingo plugin = Bingo.plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(String.valueOf(Bingo.prefix) + CustomFiles.bingo_game_ongoing);
        if (Bingo.clearInv)
            event.getPlayer().sendMessage(String.valueOf(Bingo.prefix) + CustomFiles.inventory_will_clear);
        LobbySystem.scoreboardUpdate();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (this.plugin.allPlayers.containsKey(event.getPlayer().getName()))
            this.plugin.onPlayerLeave(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (this.plugin.allPlayers.containsKey(event.getEntity().getName())) {
            Player ply = event.getEntity();
            List<ItemStack> DroppedItems = event.getDrops();
            for (int i = 0; i < DroppedItems.size(); i++) {
                if (((ItemStack)DroppedItems.get(i)).equals(Bingo.bingoCard))
                    DroppedItems.remove(Bingo.bingoCard);
            }
            if (Bingo.startoverondeath) {
                this.plugin.getBingoPlayer(ply).reCreateInv();
                ply.sendMessage(String.valueOf(Bingo.prefix) + CustomFiles.has_died);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (this.plugin.allPlayers.containsKey(event.getPlayer().getName())) {
            final Player ply = event.getPlayer();
            if (Bingo.gameStarted) {
                ply.getInventory().setItem(8, Bingo.bingoCard);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, new Runnable() {
                    public void run() {
                        ply.teleport(Bingo.spawn);
                    }
                },  5L);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRightCLick(PlayerInteractEvent e) {
        if (this.plugin.allPlayers.containsKey(e.getPlayer().getName())) {
            if (e.getItem() != null && e.getItem().equals(Bingo.bingoCard)) {
                BingoPlayer p = this.plugin.allPlayers.get(e.getPlayer().getName());
                p.showCard();
                e.setCancelled(true);
            }
            if (e.getItem() != null && e.getItem().equals(Bingo.teamChest)) {
                BingoPlayer p = this.plugin.allPlayers.get(e.getPlayer().getName());
                p.showTeams();
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player &&
                this.plugin.allPlayers.containsKey(e.getWhoClicked().getName())) {
            if (e.getView().getTitle().contains(CustomFiles.bingo_board))
                e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().equals(Bingo.bingoCard))
                e.setCancelled(true);
            if (e.getView().getTitle().contains(CustomFiles.team_selection_menu)) {
                ItemStack is = e.getCurrentItem();
                if (is != null) {
                    ItemMeta im = is.getItemMeta();
                    List<String> lores = im.getLore();
                    BingoPlayer p = this.plugin.allPlayers.get(e.getWhoClicked().getName());
                    p.Team = lores.get(0);
                    this.plugin.broadcast(CustomFiles.select_team.replace("{player}", p.name).replace("{team}", ChatColor.translateAlternateColorCodes('&', lores.get(0))));
                }
                e.setCancelled(true);
            }
            if (e.getCurrentItem() != null && e.getCurrentItem().equals(Bingo.teamChest))
                e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDrop(PlayerDropItemEvent e) {
        if (this.plugin.allPlayers.containsKey(e.getPlayer().getName())) {
            if (e.getItemDrop().getItemStack().equals(Bingo.bingoCard))
                e.setCancelled(true);
            if (e.getItemDrop().getItemStack().equals(Bingo.teamChest))
                e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null)
            return;
        if (event.getClickedBlock().getState() instanceof Sign) {
            Sign sign = (Sign)event.getClickedBlock().getState();
            String[] lines = sign.getLines();
            if (lines[0].equalsIgnoreCase("[Bingo]") &&
                    lines[2].equalsIgnoreCase("Join Game") &&
                    !this.plugin.isPartOfGame(event.getPlayer().getName()))
                this.plugin.CommandJoin(event.getPlayer());
        } else {
            return;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSwitchItem(PlayerSwapHandItemsEvent e) {
        if (this.plugin.allPlayers.containsKey(e.getPlayer().getName())) {
            if (e.getMainHandItem().equals(Bingo.bingoCard) || e.getOffHandItem().equals(Bingo.bingoCard))
                e.setCancelled(true);
            if (e.getMainHandItem().equals(Bingo.teamChest) || e.getOffHandItem().equals(Bingo.teamChest))
                e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemMerge(ItemMergeEvent e) {
        if (this.plugin.allPlayers.containsKey(e.getEntity().getName())) {
            if (e.getTarget().getItemStack().equals(Bingo.bingoCard))
                e.setCancelled(true);
            if (e.getTarget().getItemStack().equals(Bingo.teamChest))
                e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCraftItem(CraftItemEvent e) {
        if (this.plugin.allPlayers.containsKey(e.getWhoClicked().getName()))
            for (ItemStack is : Bingo.bingoItems) {
                if (is.getType().equals(e.getCurrentItem().getType()) && this.plugin.getBingoPlayer((Player)e.getWhoClicked()).checkItem(is))
                    this.plugin.getBingoPlayer((Player)e.getWhoClicked()).gotItem(is);
            }
    }
}