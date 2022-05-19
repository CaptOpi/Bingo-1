import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

public class LobbySystem {

    public static boolean seconds = false;
    public static int CountDownTime = Bingo.countDownTimeSet;

    private static Scoreboard board;
    private static BukkitTask Counter;


    public static void CountDown() {
        if (Counter != null)
            return;
        Counter = Bukkit.getScheduler().runTaskTimer((Plugin)Bingo.plugin, new Runnable() {
            public void run() {
                if (LobbySystem.CountDownTime > 1) {
                    LobbySystem.CountDownTime--;
                    if (LobbySystem.CountDownTime % 10 == 0 || LobbySystem.CountDownTime < 10)
                        Bingo.plugin.broadcast(CustomFiles.starting_in.replace("{time}", String.valueOf(LobbySystem.CountDownTime)));
                    if (LobbySystem.CountDownTime == Bingo.countDownTimeSet / 2)
                        LobbySystem.AnnounceReminder();
                    LobbySystem.CheckStart();
                } else {
                    for(int i = 0; i < BingoPlayer.getPlayers().size(); i++) {
                        if (!(CustomFiles.getScoreConfig().contains(BingoPlayer.getPlayers().get(i)))) {
                            CustomFiles.getScoreConfig().set(BingoPlayer.getPlayers().get(i), 0);
                            CustomFiles.scoreConfigSave();
                        }
                    }
                    LobbySystem.Counter.cancel();
                    LobbySystem.Counter = null;
                    LobbySystem.CountDownTime = Bingo.countDownTimeSet;
                    LobbySystem.StartRound();
                }
            }
        }, 0L, 20L);
        Bingo.plugin.log(String.valueOf(Counter));
    }



    public static void AnnounceReminder() {
        Bingo.plugin.serverBroadcast(CustomFiles.almost_starting.replace("{time}", String.valueOf(CountDownTime)));
    }
    public static String getCurrentLeader() {
        HashMap<String, Integer> h = LobbySystem.manualPush(BingoPlayer.getPlayers());
        HashMap<String, Integer> sorted = LobbySystem.sortByValue(h);
        Object[] keys = LobbySystem.getKey(sorted);
        return (keys[keys.length - 1]).toString();
    }
    public static void manualScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("Scavenger Hunt", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.WHITE + ">Current Round<");
        HashMap<String, Integer> h = LobbySystem.manualPush(BingoPlayer.getPlayers());
        HashMap<String, Integer> sorted = LobbySystem.sortByValue(h);
        Object[] keys = LobbySystem.getKey(sorted);
        Score score = objective.getScore(ChatColor.GOLD + keys[keys.length - 1].toString());
        score.setScore(h.get(keys[keys.length - 1].toString()));
        Score score1 = objective.getScore(ChatColor.GOLD +  keys[keys.length - 2].toString());
        score1.setScore(h.get(keys[keys.length - 2].toString()));
        Score score2 = objective.getScore(ChatColor.GOLD + keys[keys.length - 3].toString());
        score2.setScore(h.get(keys[keys.length - 3].toString()));
        /*Score score3 = objective.getScore(ChatColor.GOLD +  keys[keys.length - 4].toString());
        score3.setScore(h.get(keys[keys.length - 4].toString()));*/
        /*Score score4 = objective.getScore(ChatColor.GOLD +  keys[keys.length - 5].toString());
        score4.setScore(h.get(keys[keys.length - 5].toString()));
        Score score5 = objective.getScore(ChatColor.GOLD +  keys[keys.length - 6].toString());
        score5.setScore(h.get(keys[keys.length - 6].toString()));
        Score score6 = objective.getScore(ChatColor.GOLD +  keys[keys.length - 7].toString());
        score6.setScore(h.get(keys[keys.length - 7].toString()));
        Score score7 = objective.getScore(ChatColor.GOLD +  keys[keys.length - 8].toString());
        score7.setScore(h.get(keys[keys.length - 8].toString()));*/
        for (Map.Entry<String, BingoPlayer> entry : Bingo.allPlayers.entrySet()) {
            Player ply = ((BingoPlayer) entry.getValue()).getPlayer();
            ply.setScoreboard(board);
        }
    }
    public static void scoreboardUpdate() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("Scavenger Hunt", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.WHITE + ">Top Players<");
        HashMap<String, Integer> h = LobbySystem.pushToMap(BingoPlayer.getPlayers());
        HashMap<String, Integer> sorted = LobbySystem.sortByValue(h);
        Object[] keys = LobbySystem.getKey(sorted);
        Score score = objective.getScore(ChatColor.GOLD + keys[keys.length - 1].toString());
        score.setScore(h.get(keys[keys.length - 1].toString()));
        Score score1 = objective.getScore(ChatColor.GOLD + keys[keys.length - 2].toString());
        score1.setScore(h.get(keys[keys.length - 2].toString()));
        Score score2 = objective.getScore(ChatColor.GOLD + keys[keys.length - 3].toString());
        score2.setScore(h.get(keys[keys.length - 3].toString()));
        /*Score score3 = objective.getScore(ChatColor.GOLD + keys[keys.length - 4].toString());
        score3.setScore(h.get(keys[keys.length - 4].toString()));*/
        /*Score score4 = objective.getScore(ChatColor.GOLD + keys[keys.length - 5].toString());
        score4.setScore(h.get(keys[keys.length - 5].toString()));*/
        /*Score score5 = objective.getScore(ChatColor.GOLD + keys[keys.length - 6].toString());
        score5.setScore(h.get(keys[keys.length - 6].toString()));
        Score score6 = objective.getScore(ChatColor.GOLD + keys[keys.length - 7].toString());
        score6.setScore(h.get(keys[keys.length - 7].toString()));
        Score score7 = objective.getScore(ChatColor.GOLD + keys[keys.length - 8].toString());
        score7.setScore(h.get(keys[keys.length - 8].toString()));*/
        for (Map.Entry<String, BingoPlayer> entry : Bingo.allPlayers.entrySet()) {
            Player ply = ((BingoPlayer)entry.getValue()).getPlayer();
            ply.setScoreboard(board);
        }
    }
    public static void CheckStart() {
        if (Bingo.plugin.allPlayers.size() >= Bingo.minimumplayers) {
            if (Counter == null) {
                Bingo.plugin.broadcast(CustomFiles.starting_in.replace("{time}", String.valueOf(CountDownTime)));
                CountDown();
            }
        } else if (Counter != null) {
            Counter.cancel();
            Counter = null;
            CountDownTime = Bingo.countDownTimeSet;
            Bingo.plugin.broadcast(CustomFiles.has_stopped);
        }
    }
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
    public static HashMap<String, Integer> pushToMap(ArrayList<String> players) {
        HashMap<String, Integer> playersScores = new HashMap<String,Integer>();
        for(int i = 0; i < players.size(); i++) {
            playersScores.put(players.get(i), CustomFiles.getScoreConfig().getInt(players.get(i)));
        }
        return playersScores;
    }
    public static Object[] getKey(HashMap<String, Integer> players) {
        Object[] keys = players.keySet().toArray();
        return keys;
    }
    public static HashMap<String, Integer> manualPush(ArrayList<String> players) {
        HashMap<String, Integer> playersScores = new HashMap<String,Integer>();
        for(int i = 0; i < players.size(); i++) {
            playersScores.put(players.get(i), CustomFiles.getRoundConfig().getInt(players.get(i)));
        }
        return playersScores;
    }
    public static void StartRound() {
        Bingo.plugin.startTime = Calendar.getInstance();
        Location spawn;
        if (Bingo.gameNum == 1){
            spawn = new Location(Bingo.spawnWorld, Bingo.plugin.getConfig().getDouble(String.format("lobby-location-x-1" )),Bingo.plugin.getConfig().getDouble(String.format("lobby-location-y-1" )),Bingo.plugin.getConfig().getDouble(String.format("lobby-location-z-1" )));

        }else if (Bingo.gameNum == 2){
            spawn = new Location(Bingo.spawnWorld, Bingo.plugin.getConfig().getDouble(String.format("lobby-location-x-2" )),Bingo.plugin.getConfig().getDouble(String.format("lobby-location-y-2" )),Bingo.plugin.getConfig().getDouble(String.format("lobby-location-z-2" )));

        }else if (Bingo.gameNum == 3){
            spawn = new Location(Bingo.spawnWorld, Bingo.plugin.getConfig().getDouble(String.format("lobby-location-x-3" )),Bingo.plugin.getConfig().getDouble(String.format("lobby-location-y-3" )),Bingo.plugin.getConfig().getDouble(String.format("lobby-location-z-3" )));

        }else {
            spawn = new Location(Bingo.spawnWorld, Bingo.plugin.getConfig().getDouble(String.format("lobby-location-x-4" )),Bingo.plugin.getConfig().getDouble(String.format("lobby-location-y-4" )),Bingo.plugin.getConfig().getDouble(String.format("lobby-location-z-4", Bingo.gameNum )));

        }
        for (Map.Entry<String, BingoPlayer> entry : Bingo.plugin.allPlayers.entrySet()) {
            Player ply = ((BingoPlayer)entry.getValue()).getPlayer();
            ply.getInventory().clear();
            if(!CustomFiles.getRoundConfig().contains(ply.getName())) {
                CustomFiles.getRoundConfig().set(ply.getName(),0);
            }
            CustomFiles.getRoundConfig().set(ply.getName(),0);
            CustomFiles.roundConfigSave();
            manualScoreboard();
            Bingo.plugin.getBingoPlayer(ply).restoreItem2();
            ply.getInventory().setItem(8, Bingo.bingoCard);
            double random = new Random().nextDouble();
            double randomSecond = new Random().nextDouble();
            double randomX = Bingo.plugin.getConfig().getInt("teleport-distance-x") + (random * 10);
            double randomZ = Bingo.plugin.getConfig().getInt("teleport-distance-z") + (randomSecond * 10);
            int changeAxis = new Random().nextInt();
            Location teleport;
            if (changeAxis <= 0.5 ) {
                teleport = spawn.add(randomX, 0, randomZ);

            } else {
                teleport = spawn.subtract(randomX, 0, randomZ);
            }
            Block block = spawn.getWorld().getHighestBlockAt(teleport);
            teleport.setY(block.getY());
            ply.teleport(teleport);
            
            Bingo.gameStarted = true;
            ply.updateInventory();
            if (!Bingo.clearInv)
                ply.sendMessage(String.valueOf(Bingo.prefix) + CustomFiles.replaced_item);
            (Bingo.plugin.getBingoPlayer(ply)).inLobby = false;
            ply.sendMessage(String.valueOf(Bingo.prefix) + CustomFiles.has_started);
            
        }
        Bingo.plugin.countDown();
    }
} 