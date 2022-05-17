package me.senoropi.bingo.main;

import org.bukkit.inventory.Inventory;

public class BingoChecker {
    public static boolean bingoChecker(Inventory inv) {
        return boardCheck(inv);
    }

    private static boolean boardCheck(Inventory inv) {
        if ((inv.getItem(Bingo.boardPosition[0]).equals(Bingo.starItem)) && (inv.getItem(Bingo.boardPosition[1]).equals(Bingo.starItem)) && (inv.getItem(Bingo.boardPosition[2]).equals(Bingo.starItem)) && (inv.getItem(Bingo.boardPosition[3]).equals(Bingo.starItem)) && (inv.getItem(Bingo.boardPosition[4]).equals(Bingo.starItem)) && (inv.getItem(Bingo.boardPosition[5]).equals(Bingo.starItem)) && (inv.getItem(Bingo.boardPosition[6]).equals(Bingo.starItem)) && (inv.getItem(Bingo.boardPosition[7]).equals(Bingo.starItem)) && (inv.getItem(Bingo.boardPosition[8]).equals(Bingo.starItem))) {
            return true;
        }
        return false;
    }
}
