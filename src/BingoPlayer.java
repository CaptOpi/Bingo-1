import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;

public class BingoPlayer {
    public Inventory inv;

    public Inventory invTeams;

    public String name;

    private Player player;

    private Bingo plugin;

    private ItemStack tempItem;

    private ItemStack tempItem2;

    public boolean inLobby;

    public String Team;

    private Scoreboard board;

    private static ArrayList<String> players = new ArrayList<String>();



    public BingoPlayer(Player p, Bingo plugin) {
        if (Bingo.clearInv) {
            p.getInventory().clear();
            p.sendMessage(String.valueOf(Bingo.prefix) + CustomFiles.inventory_has_cleared);
        }
        this.player = p;
        this.name = p.getName();
        this.plugin = plugin;
        (this.inv = Bukkit.createInventory((InventoryHolder)p, 45, String.valueOf(String.valueOf(String.valueOf(this.name))) + "'s " + CustomFiles.bingo_board)).clear();
        int i;
        for (i = 0; i < Bingo.bingoItemstack.size(); i++)
            this.inv.setItem(Bingo.boardPosition[i], Bingo.bingoItemstack.get(i));
        this.tempItem = p.getInventory().getItem(8);
        if (Bingo.usingTeams) {
            this.Team = String.valueOf(Bingo.soloname) + " Team";
            (this.invTeams = Bukkit.createInventory((InventoryHolder)p, 9, CustomFiles.team_selection_menu)).clear();
            this.invTeams.setItem(0, Bingo.teamItemStacks.get(0));
            for (i = 1; i < Bingo.teamItemStacks.size(); i++)
                this.invTeams.setItem(Bingo.teamPositions[i - 1], Bingo.teamItemStacks.get(i));
            this.tempItem2 = p.getInventory().getItem(7);
            p.getInventory().setItem(7, Bingo.teamChest);
        }
        plugin.serverBroadcast(CustomFiles.joined_game.replace("{player}", p.getName()));
        p.teleport(Bingo.spawn);
        CustomFiles.saveToLog(CustomFiles.joined_game.replace("{player}", p.getName()));
        if (Bingo.usingLobby) {
            this.inLobby = true;
        } else {
            p.getInventory().setItem(8, Bingo.bingoCard);
            p.updateInventory();
            if (!Bingo.clearInv)
                p.sendMessage(String.valueOf(Bingo.prefix) + CustomFiles.replaced_item);

        }
    }
    public static ArrayList<String> getPlayers() {
        return players;
    }
    public void gotItem(ItemStack is) {
        if (!this.plugin.CheckWhiteListedWorld(this.player)) {
            this.player.sendMessage(String.valueOf(Bingo.prefix) + CustomFiles.wrong_world);
            return;
        }
        String name = is.getItemMeta().hasDisplayName() ? is.getItemMeta().getDisplayName() : is.getType().toString().replace("_", " ").toLowerCase();
        StringBuilder b = new StringBuilder(name);
        int i = 0;
        do {
            b.replace(i, i + 1, b.substring(i, i + 1).toUpperCase());
            i = b.indexOf(" ", i) + 1;
        } while (i > 0 && i < b.length());
        CustomFiles.saveToLog(CustomFiles.has_obtained.replace("{player}", this.name).replace("{item}", b));
        if (Bingo.announceNew)
            this.plugin.broadcast(CustomFiles.has_obtained.replace("{player}", this.name).replace("{item}", b));
        for (int j = 0; j < Bingo.bingoItemstack.size(); j++) {
            if (this.inv.getItem(Bingo.boardPosition[j]).equals(is))
                this.inv.setItem(Bingo.boardPosition[j], Bingo.starItem);

        }
        if (Bingo.usingTeams)
            for (Map.Entry<String, BingoPlayer> entry : this.plugin.allPlayers.entrySet()) {
                Player ply = ((BingoPlayer)entry.getValue()).getPlayer();
                BingoPlayer p = entry.getValue();
                if (p != this &&
                        p.Team.equalsIgnoreCase(this.Team) && !this.Team.equalsIgnoreCase(String.valueOf(Bingo.soloname) + " Team")) {
                    for (int k = 0; k < Bingo.bingoItemstack.size(); k++) {
                        if (p.inv.getItem(Bingo.boardPosition[k]).equals(is))
                            p.inv.setItem(Bingo.boardPosition[k], Bingo.starItem);
                    }
                    ply.sendMessage(String.valueOf(Bingo.prefix) + CustomFiles.obtained_team.replace("{item}", b).replace("{player}", this.name));
                }
            }
        if (checkBingo()) {
            this.plugin.getPlayersFinished().add(this.player);
            this.plugin.broadcast(CustomFiles.bingo.replace("{player}", this.name));
            this.plugin.handleWinner(this.player);
        }
        int previous = CustomFiles.getScoreConfig().getInt(player.getName());
        CustomFiles.getScoreConfig().set(player.getName(), previous + 1);
        CustomFiles.scoreConfigSave();
        int previous1 = CustomFiles.getRoundConfig().getInt(player.getName());
        CustomFiles.getRoundConfig().set(player.getName(), previous1 + 1);
        CustomFiles.roundConfigSave();
        LobbySystem.manualScoreboard();
    }

    public void showCard() {
        this.player.openInventory(this.inv);
    }

    public void showTeams() {
        this.player.openInventory(this.invTeams);
    }

    public void reCreateInv() {
        this.inv.clear();
        for (int i = 0; i < Bingo.bingoItemstack.size(); i++)
            this.inv.setItem(Bingo.boardPosition[i], Bingo.bingoItemstack.get(i));
    }

    public boolean checkItem(ItemStack is) {
        return this.inv.contains(is.getType());
    }

    public boolean checkBingo() {
        return BingoChecker.bingoChecker(this.inv);
    }

    public void restoreItem() {
        this.player.getInventory().clear(8);
        if (this.player.getInventory().contains(Bingo.bingoCard)) {
            int slot = this.player.getInventory().first(Bingo.bingoCard);
            this.player.getInventory().clear(slot);
        }
        if (!Bingo.clearInv)
            this.player.getInventory().setItem(8, this.tempItem);
    }

    public void restoreItem2() {
        this.player.getInventory().clear(7);
        this.player.getInventory().setItem(7, this.tempItem2);
        this.tempItem2 = null;
    }

    public Player getPlayer() {
        return this.player;
    }
}