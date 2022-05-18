import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.*;


public class Bingo extends JavaPlugin implements Listener {
    public static int count = 1501;
    private Server server;
    private Scoreboard board;
    public static ArrayList<ItemStack> bingoItems;

    public static ArrayList<ItemStack> bingoItemstack;

    public static boolean announceNew;

    public static boolean clearInv;

    public ArrayList<String> welcomemsg;

    public static boolean gameIsSetup;

    public static boolean gameStarted;

    public static int[] boardPosition;

    public static ItemStack bingoCard;

    public static ItemStack teamChest;

    public static ItemStack starItem;

    public static HashMap<String, BingoPlayer> allPlayers;

    public static String prefix;

    public static String expectedConfigVersion;

    public double entryFee;

    public double winningPrize;

    public Material entryFeeItem;

    public int entryFeeItemQuantity;

    public Material winningPrizeItem;

    public int winningPrizeQuantity;

    public Calendar startTime;

    public static Bingo plugin;

    public static String lastUpdated;

    public static boolean startoverondeath;

    public static boolean sendtospawn;

    public static Location spawn;

    public static World spawnWorld;

    public static boolean whitelistworlds;

    public static List<String> whitelistedworlds;

    public static boolean bingolog;

    public static boolean seeotherscards;

    public static boolean newVersionAvailable;

    public static boolean announceseeothercard;

    public static boolean usingLobby;

    public static boolean usingTeams;

    public static int minimumplayers;

    public static int countDownTimeSet;

    public static String soloname;

    public static int[] teamPositions = new int[] { 2, 3, 4, 5, 6 };

    public static ArrayList<ItemStack> teamItems = new ArrayList<>();

    public static ArrayList<ItemStack> teamItemStacks = new ArrayList<>();

    public static List<String> teamnames;

    public static int scheduler;

    public static int gameNum = 1;

    public static boolean firstPlace = false;
    public static boolean secondPlace = false;
    public static boolean thirdPlace = false;

    public static ArrayList<String> registeredPlayerNames = new ArrayList<String>();

    public static ArrayList<Player> registeredPlayers = new ArrayList<Player>();

    public static ArrayList<Player> playersFinished = new ArrayList<Player>();

    public static ArrayList<Player> registeredPlayersOnline = new ArrayList<Player>();

    public static final String TEXT_RESET = "\u001B[0m";

    public static final String TEXT_YELLOW = "\u001B[33m";
    
    static {
        bingoItemstack = new ArrayList<>();
        gameIsSetup = false;
        boardPosition = new int[] {
                12,13,14,21,22,23,30,31,32 };
        prefix = ChatColor.DARK_AQUA + "[" + ChatColor.GOLD + "Bingo" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;
        expectedConfigVersion = "1.4";
        lastUpdated = "28/OCT/2019";
    }

    public Bingo() {
        bingoItems = new ArrayList<>();
        this.welcomemsg = new ArrayList<>();
        this.allPlayers = new HashMap<>();
        this.entryFee = 0.0D;
        this.winningPrize = 0.0D;
        this.entryFeeItem = null;
        this.entryFeeItemQuantity = 0;
        this.winningPrizeItem = null;
        this.winningPrizeQuantity = 0;
        clearInv = false;
        startoverondeath = false;
        bingolog = false;
        seeotherscards = false;
        announceseeothercard = false;
        usingLobby = false;
        minimumplayers = 2;
        countDownTimeSet = 120;
        gameStarted = false;
    }

    public void log(String message) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(message.replaceAll("&([0-9a-fk-or])", "hi"));
    }

    private void loadPlayerNames(){
        try {
            File players = new File("./plugins/Bingo/players.txt");
            Scanner scan = new Scanner(players);
            System.out.println(" ");
            while (scan.hasNextLine()){
                String data = scan.nextLine();
                if (!registeredPlayerNames.contains(data)){
                    registeredPlayerNames.add(data);
                }
                System.out.println( String.format("Added %s to the registered player list.", data) );

            }
            System.out.println(" ");
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getPlayerNames(){
        return registeredPlayerNames;
    }
    public ArrayList<Player> getPlayersFinished() {
        return playersFinished;
    }


    public void onEnable() {
        plugin = this;
        CustomFiles.createItemsConfig1();
        CustomFiles.createItemsConfig2();
        CustomFiles.createItemsConfig3();
        CustomFiles.createItemsConfig4();
        CustomFiles.createMessagesConfig();
        CustomFiles.createScoreConfig();
        CustomFiles.createManualConfig();
        loadPlayerNames();
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "team remove TEAMNAME");
        File file = new File(getDataFolder() + File.separator + "config.yml");
        if (!file.exists())
            saveDefaultConfig();
        if (!getConfig().getString("version").equals(expectedConfigVersion)) {
            log("Your configuration file is out of date, it will be overwritten with the new one now.");
            file.delete();
            saveDefaultConfig();
        }
        this.server = getServer();
        loadEntryAndPrize();
        bingolog = getConfig().getBoolean("bingo_log");
        announceNew = getConfig().getBoolean("announce_new_item");
        clearInv = getConfig().getBoolean("clear_inventory_on_join");
        startoverondeath = getConfig().getBoolean("start_over_on_death");
        sendtospawn = getConfig().getBoolean("teleport_to_spawn_on_finish");
        whitelistworlds = getConfig().getBoolean("whitelist-worlds");
        whitelistedworlds = getConfig().getStringList("whitelisted-worlds");
        seeotherscards = getConfig().getBoolean("see_others_card");
        announceseeothercard = getConfig().getBoolean("announce_see_others_card");
        usingLobby = getConfig().getBoolean("lobby-system.enabled");
        minimumplayers = getConfig().getInt("lobby-system.minimum_players");
        countDownTimeSet = getConfig().getInt("lobby-system.time_to_start");
        usingTeams = getConfig().getBoolean("teams.enabled");
        teamnames = getConfig().getStringList("teams.team-names");
        soloname = getConfig().getString("teams.solo-name");
        List<String> welcomemsg = getConfig().getStringList("welcomemessage");
        for (String s : welcomemsg)
            this.welcomemsg.add(ChatColor.translateAlternateColorCodes('&', s));
        List<String> configintlist1 = CustomFiles.getItemsConfig1().getStringList("items1");
        List<String> configintlist2 = CustomFiles.getItemsConfig2().getStringList("items2");
        List<String> configintlist3 = CustomFiles.getItemsConfig3().getStringList("items3");
        List<String> configintlist4 = CustomFiles.getItemsConfig4().getStringList("items4");
        boolean PluginNeedsToDisable = false;
        for (String item : configintlist1) {
            if (Material.matchMaterial(item) == null) {
                log("Item " + item + " specified in the items.yml file is not supported, please remove it!");
                PluginNeedsToDisable = true;
            }
        }

        List<String> configTeams = getConfig().getStringList("teams.team-items");
        if (configTeams.size() != teamnames.size()) {
            log("The amount of team items compared to the amount of team names do not match up!");
            PluginNeedsToDisable = true;
        }
        if (configTeams.size() > 5 || teamnames.size() > 5) {
            log("You can only have a maximum of 5 teams.");
            PluginNeedsToDisable = true;
        }
        if (usingTeams && !usingLobby) {
            log("If you want to use teams, you need to enable lobby!");
            PluginNeedsToDisable = true;
        }
        if (PluginNeedsToDisable) {
            this.server.getPluginManager().disablePlugin((Plugin)this);
        } else {
            if (bingolog)
                try {
                    CustomFiles.createLogFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            log("[Bingo] " + (usingLobby ? "Lobby Enabled" : "Lobby Disabled" ));
            log("[Bingo] " + (usingTeams ? ("Teams with " + teamnames.size() + " teams") : "Teams Disabled" ));
            log("Bingo has successfully loaded!");
            Bukkit.getPluginManager().registerEvents(this, (Plugin)this);
        }
    }
    public void onDisable() {
        if (gameIsSetup && gameStarted)
            onGameFinish();
        log("Bingo has been disabled");
    }
    public static HashMap<String, BingoPlayer> getPlayerList() {
        return allPlayers;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String c = cmd.getName().toLowerCase();
        String response = null;
        if (c.equals("bingo"))
            response = commandProcessor(args, sender);
        if (response != null)
            sender.sendMessage(response);
        return true;
    }

    public String commandProcessor(String[] args, CommandSender s) {
        String HelpString = String.valueOf(prefix) + ChatColor.AQUA + "Bingo " + CustomFiles.version_str + " " + getDescription().getVersion() + " Command Help";
        for (String str : CustomFiles.helpmsg)
            HelpString = String.valueOf(HelpString) + "\n" + ChatColor.translateAlternateColorCodes('&', str);
        String AdminHelpString = ChatColor.AQUA + "\nAdmin Command:";
        for (String str : CustomFiles.adminhelpmsg)
            AdminHelpString = String.valueOf(AdminHelpString) + "\n" + ChatColor.translateAlternateColorCodes('&', str);
        if (args.length < 1)
            return HelpString;
        String cmd = args[0].toLowerCase();
        if (cmd.equals("help")) {
            if (s.hasPermission("bingo.admin"))
                return String.valueOf(HelpString) + AdminHelpString;
            return HelpString;
        }
        if (cmd.equals("info"))
            return String.valueOf(prefix) + ChatColor.AQUA + "Bingo Version " + getDescription().getVersion() + "\n" + ChatColor.DARK_AQUA + "Plugin made by " + ChatColor.GOLD + "St3fan[NL]" + ChatColor.DARK_AQUA + " - www.st3fannl.nl\n" + ChatColor.DARK_AQUA + "Last updated: " + ChatColor.GOLD + lastUpdated;
        if (cmd.equals("start")) {

            /**Checks for start argument */
            if ( args.length < 3 ) {
                try{
                    gameNum = Integer.parseInt( args[1] );
                    if ( (gameNum < 1) && (4 < gameNum) ){
                        ((Player)s).sendMessage(String.valueOf(prefix) + ChatColor.DARK_RED + "Invalid game number. Choose games only [1-4].");
                        return HelpString;
                    }

                }catch(Exception e){
                    return String.format(String.valueOf(prefix) + ChatColor.GREEN + "No game number specified. Choose game number [1-4].", gameNum);
                }
            }else if( args.length > 3 ) {
                ((Player)s).sendMessage(String.valueOf(prefix) + ChatColor.DARK_RED + "Invalid Command. Correct Usage:\n/bingo start [GAME NUMBER]");
                return HelpString;
            }

            if (s.hasPermission("bingo.admin")) {
                if (s instanceof Player) {
                    if (newVersionAvailable)
                        ((Player)s).sendMessage(String.valueOf(prefix) + ChatColor.DARK_RED + "A new version of this plugin is available, visit SpigotMC.org!");
                    if (!CheckWhiteListedWorld((Player)s))
                        return String.valueOf(prefix) + CustomFiles.not_whitelisted_world;
                    startGame((Player)s, gameNum);
                    serverBroadcast(CustomFiles.game_has_started);
                    if (clearInv)
                        serverBroadcast(CustomFiles.inventory_will_clear);
                    return String.valueOf(prefix) + CustomFiles.game_succesfully_started;
                }
                return String.valueOf(prefix) + CustomFiles.non_player_error;
            }
            return String.valueOf(prefix) + CustomFiles.no_permission;
        }
        if (cmd.equals("see")) {
            if (!seeotherscards)
                return String.valueOf(prefix) + CustomFiles.option_disabled;
            if (s instanceof Player) {
                if (!this.allPlayers.containsKey(s.getName()))
                    return String.valueOf(prefix) + CustomFiles.not_in_game;
                if (args[1].length() > 0) {
                    Player target = Bukkit.getServer().getPlayer(args[1]);
                    if (target != null && this.allPlayers.containsKey(target.getName())) {
                        Player ply = (Player)s;
                        if (target == ply)
                            return String.valueOf(prefix) + CustomFiles.use_own_card;
                        ply.openInventory((getBingoPlayer(target)).inv);
                        if (announceseeothercard)
                            broadcast(CustomFiles.checked_other_card.replace("{player}", ply.getName()).replace("{otherplayer}", target.getName()));
                        CustomFiles.saveToLog(CustomFiles.checked_other_card.replace("{player}", ply.getName()).replace("{otherplayer}", target.getName()));
                        return null;
                    }
                    return String.valueOf(prefix) + CustomFiles.player_not_found;
                }
                return String.valueOf(prefix) + CustomFiles.fill_out_other_player;
            }
        }
        if (cmd.equals("team")) {
            if (!usingTeams)
                return String.valueOf(prefix) + CustomFiles.option_disabled;
            if (s instanceof Player) {
                if (!this.allPlayers.containsKey(s.getName()))
                    return String.valueOf(prefix) + CustomFiles.not_in_game;
                if ((getBingoPlayer((Player)s)).Team.equalsIgnoreCase(String.valueOf(soloname) + " Team"))
                    return String.valueOf(prefix) + CustomFiles.not_in_team;
                s.sendMessage(String.valueOf(prefix) + CustomFiles.my_team);
                for (Map.Entry<String, BingoPlayer> entry : this.allPlayers.entrySet()) {
                    Player ply = ((BingoPlayer)entry.getValue()).getPlayer();
                    if (ply != s &&
                            ((BingoPlayer)entry.getValue()).Team.equalsIgnoreCase((getBingoPlayer((Player)s)).Team))
                        s.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b" + ply.getName()));
                }
            }
            return null;
        }
        if (cmd.equals("join")) {
            if (s instanceof Player) {
                CommandJoin((Player)s);
                return null;
            }
            return String.valueOf(prefix) + CustomFiles.non_player_error;
        }
        if (cmd.equals("leave")) {
            if (!gameIsSetup)
                return String.valueOf(prefix) + CustomFiles.not_in_game;
            if (s instanceof Player) {
                if (this.allPlayers.containsKey(s.getName())) {
                    onPlayerLeave((Player)s);
                    return String.valueOf(prefix) + CustomFiles.left_game;
                }
                return String.valueOf(prefix) + CustomFiles.not_in_game;
            }
        }
        return String.valueOf(prefix) + CustomFiles.unknown_command;
    }

    public void updateOnlineRegisteredPlayers(){

        try {
            File players = new File("./plugins/Bingo/players.txt");
            Scanner scan = new Scanner(players);
            System.out.println(" ");
            while (scan.hasNextLine()){
                String data = scan.nextLine();
                Player p = Bukkit.getServer().getPlayerExact(data);
                if (p != null && !registeredPlayersOnline.contains(p)){
                    registeredPlayersOnline.add(p);
                    System.out.println( String.format("Added %s to the online registered player list.", data) );
                }else {
                    registeredPlayersOnline.remove(p);
                    System.out.println( String.format("Removed %s from the online registered player list.", data) );
                }
            }
            System.out.println(" ");
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addPlayersToGame(){
        for ( Player p : registeredPlayersOnline ){
            p.performCommand("bingo join");
        }
    }

    public void startGame( Player ply, int gameNum ) {
        firstPlace = false;
        secondPlace = false;
        thirdPlace = false;

        updateOnlineRegisteredPlayers();
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "team add TEAMNAME");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "team add TEAMNAME @a");
        if (gameNum != 4){
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "team modify TEAMNAME friendlyFire false");

        }else {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "team modify TEAMNAME friendlyFire true");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "effect clear @a minecraft:conduit_power");

        }
        System.out.println("Working Directory = " + System.getProperty("user.dir\n"));
        for (String playerName : registeredPlayerNames){
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "clear " + playerName);
            System.out.println("Cleared inventory of " + playerName);
        }
        this.allPlayers.clear();
        bingoItemstack.clear();
        bingoItems.clear();
        teamItems.clear();
        teamItemStacks.clear();
        spawnWorld = ply.getWorld();
        CustomFiles.saveToLog(CustomFiles.has_started_game.replace("{player}", ply.getName()));
        List<String> configintlist;
        if (gameNum == 1){
            configintlist = CustomFiles.getItemsConfig1().getStringList("items");
            spawn = new Location(spawnWorld, getConfig().getDouble(String.format("lobby-location-x-1", gameNum )),getConfig().getDouble(String.format("lobby-location-y-1", gameNum )),getConfig().getDouble(String.format("lobby-location-z-1", gameNum )));

        }else if (gameNum == 2){
            configintlist = CustomFiles.getItemsConfig2().getStringList("items");
            spawn = new Location(spawnWorld, getConfig().getDouble(String.format("lobby-location-x-2", gameNum )),getConfig().getDouble(String.format("lobby-location-y-2", gameNum )),getConfig().getDouble(String.format("lobby-location-z-2", gameNum )));

        }else if (gameNum == 3){
            configintlist = CustomFiles.getItemsConfig3().getStringList("items");
            spawn = new Location(spawnWorld, getConfig().getDouble(String.format("lobby-location-x-3", gameNum )),getConfig().getDouble(String.format("lobby-location-y-3", gameNum )),getConfig().getDouble(String.format("lobby-location-z-3", gameNum )));

        }else {
            configintlist = CustomFiles.getItemsConfig4().getStringList("items");
            spawn = new Location(spawnWorld, getConfig().getDouble(String.format("lobby-location-x-4", gameNum )),getConfig().getDouble(String.format("lobby-location-y-4", gameNum )),getConfig().getDouble(String.format("lobby-location-z-4", gameNum )));

        }
        spawn.setWorld(spawnWorld);
        System.out.println("Game number " + gameNum);
        System.out.println("configintlist size " + configintlist.size());
        if (configintlist.size() >= 9) {
            ShuffleList.shuffleList(configintlist);
            Iterator<String> it = configintlist.iterator();
            for (int i = 0; i < 9; i++) {
                String item = it.next();
                bingoItems.add(NameToItem.fromName(item));
            }
            if (bingoItems.size() >= 9) {
                String itemLore = getConfig().getString("itemlore");
                ArrayList<String> lores = new ArrayList<>();
                lores.add(ChatColor.translateAlternateColorCodes('&', itemLore));
                for (ItemStack is : bingoItems) {
                    ItemMeta im = is.getItemMeta();
                    im.setLore(lores);
                    is.setItemMeta(im);
                    bingoItemstack.add(is);
                }
                initializeBingoCard();
                initializeStarItem();
                initializeTeamItem();
                this.server.getPluginManager().registerEvents(new BingoListener(), (Plugin)this);
                this.server.getPluginManager().registerEvents(new ItemListener(), (Plugin)this);
                this.startTime = Calendar.getInstance();
                gameIsSetup = true;
                if (!usingLobby)
                    gameStarted = true;
                System.out.println(String.format("[Bingo] Scavenger Hunt game #%d has started", gameNum));
                addPlayersToGame();
            } else {
                System.out.println("[Bingo] Bingo cannot be enabled because some items are not supported and the adjusted list is less than 9 items!");
                this.server.getPluginManager().disablePlugin((Plugin)this);
            }
        } else {
            System.out.println("[Bingo] Bingo cannot be enabled because the items list did not match the requirement of 9 or more items!");
            System.out.println("Game being used: " + gameNum);
            System.out.println(configintlist.size());
            this.server.getPluginManager().disablePlugin((Plugin)this);
        }
        if (usingTeams) {
            List<String> configTeams = getConfig().getStringList("teams.team-items");
            Iterator<String> it = configTeams.iterator();
            teamItems.add(NameToItem.fromName(getConfig().getString("teams.solo-item")));
            for (int i = 0; i < configTeams.size(); i++) {
                String item = it.next();
                teamItems.add(NameToItem.fromName(item));
            }
            ItemStack is = teamItems.get(0);
            ItemMeta im = is.getItemMeta();
            ArrayList<String> lores = new ArrayList<>();
            lores.add(ChatColor.translateAlternateColorCodes('&', String.valueOf(soloname) + " Team"));
            im.setLore(lores);
            is.setItemMeta(im);
            teamItemStacks.add(is);
            for (int j = 1; j < teamItems.size(); j++) {
                ItemStack is2 = teamItems.get(j);
                ItemMeta im2 = is2.getItemMeta();
                ArrayList<String> lores2 = new ArrayList<>();
                lores2.add(ChatColor.translateAlternateColorCodes('&', String.valueOf(teamnames.get(j - 1)) + " Team"));
                im2.setLore(lores2);
                is2.setItemMeta(im2);
                teamItemStacks.add(is2);
            }
        }
    }

    public void loadEntryAndPrize() {
        if (getConfig().getBoolean("entry-fee-enabled")) {
            this.entryFeeItem = Material.getMaterial(getConfig().getString("entry-fee"));
            this.entryFeeItemQuantity = getConfig().getInt("entry-fee-quantity");
        }
        this.winningPrizeItem = Material.getMaterial(getConfig().getString("reward"));
        this.winningPrizeQuantity = getConfig().getInt("reward-quantity");
    }

    public void initializeBingoCard() {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GREEN.toString() + ChatColor.AQUA + CustomFiles.bingo_card);
        String bingoLore = getConfig().getString("bingolore");
        ArrayList<String> lores = new ArrayList<>();
        lores.add(ChatColor.translateAlternateColorCodes('&', bingoLore));
        im.setLore(lores);
        is.setItemMeta(im);
        bingoCard = is;
    }

    public void initializeTeamItem() {
        ItemStack is = new ItemStack(Material.CHEST);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + CustomFiles.team_selection_menu);
        String teamlore = getConfig().getString("teams.teamlore");
        ArrayList<String> lores = new ArrayList<>();
        lores.add(ChatColor.translateAlternateColorCodes('&', teamlore));
        im.setLore(lores);
        is.setItemMeta(im);
        teamChest = is;
    }

    public void initializeStarItem() {
        ItemStack is = new ItemStack(Material.NETHER_STAR);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Completed");
        String starLore = getConfig().getString("starlore");
        ArrayList<String> lores = new ArrayList<>();
        lores.add(ChatColor.translateAlternateColorCodes('&', starLore));
        im.setLore(lores);
        is.setItemMeta(im);
        starItem = is;
    }
    public void onPlayerFinish(BingoPlayer p) {

    }
    public void onPlayerJoin(Player p) {
        p.getInventory().clear();
        if (this.entryFeeItem != null && this.entryFeeItemQuantity > 0) {
            if (p.getInventory().contains(this.entryFeeItem)) {
                int slotID = p.getInventory().first(this.entryFeeItem);
                int amount = p.getInventory().getItem(slotID).getAmount();
                if (amount == this.entryFeeItemQuantity) {
                    p.getInventory().clear(slotID);
                    for (String s2 : this.welcomemsg)
                        p.sendMessage(s2);
                    this.allPlayers.put(p.getName(), new BingoPlayer(p, this));
                } else if (amount > this.entryFeeItemQuantity) {
                    p.getInventory().getItem(slotID).setAmount(amount - this.entryFeeItemQuantity);
                    p.updateInventory();
                    for (String s2 : this.welcomemsg)
                        p.sendMessage(s2);
                    this.allPlayers.put(p.getName(), new BingoPlayer(p, this));
                } else {
                    p.sendMessage(String.valueOf(prefix) + CustomFiles.need_fee.replace("{amount}", String.valueOf(this.entryFeeItemQuantity)).replace("{item}", getConfig().getString("entry-fee")));
                }
            } else {
                p.sendMessage(String.valueOf(prefix) + CustomFiles.need_fee.replace("{amount}", String.valueOf(this.entryFeeItemQuantity)).replace("{item}", getConfig().getString("entry-fee")));
            }
        } else {
            for (String s3 : this.welcomemsg)
                p.sendMessage(s3);
            this.allPlayers.put(p.getName(), new BingoPlayer(p, this));

        }
        if (this.allPlayers.size() == 1)
            this.startTime = Calendar.getInstance();
        if (usingLobby)
            LobbySystem.CheckStart();
        BingoPlayer.getPlayers().add(p.getName());
    }

    public void onPlayerLeave(Player p) {
        BingoPlayer bp = this.allPlayers.get(p.getName());
        bp.restoreItem();
        bp.restoreItem2();
        this.allPlayers.remove(p.getName());
        serverBroadcast(CustomFiles.player_left_game.replace("{player}", p.getName()));
    }
    public void handleWinner(Player p) {
        if(BingoPlayer.getPlayers().size() == playersFinished.size()) {
            onGameFinish();
            return;
        }
        if(!firstPlace) {
            p.sendMessage(String.valueOf(prefix) + ChatColor.GOLD + "Congratulations, you were victorious!");
            Bingo.plugin.broadcast(p.getDisplayName() + ChatColor.GOLD + "has finished in first place!");
            int previous = CustomFiles.getScoreConfig().getInt(p.getName());
            CustomFiles.getScoreConfig().set(p.getName(), previous + 5);
            firstPlace = true;
            return;
        } else if (!secondPlace && firstPlace) {
            Bingo.plugin.broadcast(p.getDisplayName() + ChatColor.GOLD + "has finished in second place!");
            int previous = CustomFiles.getScoreConfig().getInt(p.getName());
            CustomFiles.getScoreConfig().set(p.getName(), previous + 3);
            secondPlace = true;
            return;
        } else if (!thirdPlace && secondPlace) {
            Bingo.plugin.broadcast(p.getDisplayName() + ChatColor.GOLD + "has finished in third place!");
            int previous = CustomFiles.getScoreConfig().getInt(p.getName());
            CustomFiles.getScoreConfig().set(p.getName(), previous + 2);
            thirdPlace = true;
            return;
        } else {
            Bingo.plugin.broadcast(p.getDisplayName() + ChatColor.GOLD + "has finished their board!");
            return;
        }
    }
    public void onGameFinish() {
        broadcast(CustomFiles.duration.replace("{time}", Tools.formatDuration(this.startTime, Calendar.getInstance(), true)));
        for (Map.Entry<String, BingoPlayer> entry : this.allPlayers.entrySet()) {
            ((BingoPlayer)entry.getValue()).restoreItem();
            Player ply = ((BingoPlayer)entry.getValue()).getPlayer();
            if (clearInv)
                ply.getInventory().clear();
            ply.teleport(spawn);
        }
        gameIsSetup = false;
        gameStarted = false;
        LobbySystem.seconds = false;
        HandlerList.unregisterAll((Plugin)this);
        plugin.getServer().getScheduler().cancelTask(scheduler);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                LobbySystem.scoreboardUpdate();
            }
        },60L);
        count = 1501;
    }

    public boolean CheckWhiteListedWorld(Player p) {
        if (whitelistworlds) {
            boolean IsInWhiteListedWorld = false;
            for (String world : whitelistedworlds) {
                if (p.getWorld().getName().equalsIgnoreCase(world))
                    IsInWhiteListedWorld = true;
            }
            if (!IsInWhiteListedWorld)
                return false;
        }
        return true;
    }

    public BingoPlayer getBingoPlayer(Player p) {
        return getBingoPlayer(p.getName());
    }

    public BingoPlayer getBingoPlayer(String name) {
        return this.allPlayers.get(name);
    }

    public void serverBroadcast(String msg) {
        this.server.broadcastMessage(String.valueOf(prefix) + msg);
    }

    public boolean isPartOfGame(String name) {
        if (this.allPlayers.containsKey(name))
            return true;
        return false;
    }
    public void countDown() {
        scheduler = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                count--;
                switch (count) { // Yes I know this can be done in a different way, I just want it to be finished
                    case 1500:
                        Bingo.plugin.broadcast(ChatColor.AQUA + "You Have 25 Minutes To Finish Your Board!");
                        break;
                    case 900:
                        Bingo.plugin.broadcast(ChatColor.AQUA + "There is 15 Minutes Remaining!");
                        break;
                    case 600:
                        Bingo.plugin.broadcast(ChatColor.AQUA + "There is 10 Minutes Remaining!");
                        break;
                    case 300:
                        Bingo.plugin.broadcast(ChatColor.AQUA + "There is 5 Minutes Remaining!");
                        break;
                    case 120:
                        Bingo.plugin.broadcast(ChatColor.AQUA + "There is 2 Minutes Remaining!");
                        break;
                    case 60:
                        LobbySystem.seconds = true;
                        Bingo.plugin.broadcast(ChatColor.AQUA + "There is 1 Minute Remaining!");
                        break;
                    case 30:
                        Bingo.plugin.broadcast(ChatColor.AQUA + "There is 30 Seconds Remaining!");
                        break;
                    case 15:
                        Bingo.plugin.broadcast(ChatColor.AQUA + "There is 15 Seconds Remaining!");
                        break;
                    case 10:
                        Bingo.plugin.broadcast(ChatColor.AQUA + "There is 10 Seconds Remaining!");
                        break;
                    case 9:
                        Bingo.plugin.broadcast(ChatColor.AQUA + "9!");
                        break;
                    case 8:
                        Bingo.plugin.broadcast(ChatColor.AQUA + "8!");
                        break;
                    case 7:
                        Bingo.plugin.broadcast(ChatColor.AQUA + "7!");
                        break;
                    case 6:
                        Bingo.plugin.broadcast(ChatColor.AQUA + "6!");
                        break;
                    case 5:
                        Bingo.plugin.broadcast(ChatColor.AQUA + "5!");
                        break;
                    case 4:
                        Bingo.plugin.broadcast(ChatColor.AQUA + "4!");
                        break;
                    case 3:
                        Bingo.plugin.broadcast(ChatColor.AQUA + "3!");
                        break;
                    case 2:
                        Bingo.plugin.broadcast(ChatColor.AQUA + "2!");
                        break;
                    case 1:
                        Bingo.plugin.broadcast(ChatColor.AQUA + "1!");
                        break;
                    case 0:
                        plugin.getServer().getScheduler().cancelTasks(plugin);
                        onGameFinish();
                        break;
                }
            }
        },0L, 20L);
    }


    public String CommandJoin(Player p) {

        if (!gameIsSetup)
            return String.valueOf(prefix) + CustomFiles.no_game_found;
        Player ply = p;
        p.setFoodLevel(20);
        p.setSaturation(99999);
        p.setGameMode(GameMode.SURVIVAL);
        if (ply.getGameMode().name().equalsIgnoreCase("survival")) {
            if (!CheckWhiteListedWorld(p))
                return String.valueOf(prefix) + CustomFiles.wrong_world;
            if (!this.allPlayers.containsKey(p.getName())) {
                if (usingLobby &&
                        gameStarted)
                    return String.valueOf(prefix) + CustomFiles.game_already_started;
                onPlayerJoin(p);
                return null;
            }
            return String.valueOf(prefix) + CustomFiles.already_in_game;
        }
        return String.valueOf(prefix) + CustomFiles.need_survivalmode;
    }

    public void broadcast(String msg) {
        for (Map.Entry<String, BingoPlayer> entry : this.allPlayers.entrySet()) {
            Player ply = ((BingoPlayer)entry.getValue()).getPlayer();
            ply.sendMessage(String.valueOf(prefix) + msg);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignChange(SignChangeEvent event) {
        if (event.getBlock().getState() instanceof org.bukkit.block.Sign) {
            String[] lines = event.getLines();
            if (lines[0].equalsIgnoreCase("[Bingo]") &&
                    event.getPlayer().hasPermission("bingo.admin") &&
                    !CheckWhiteListedWorld(event.getPlayer())) {
                event.getPlayer().sendMessage(String.valueOf(prefix) + CustomFiles.wrong_world);
                event.setCancelled(true);
            }
        }
    }
    public void onPlayerMove(PlayerMoveEvent event) {
        if (this.allPlayers.containsKey(event.getPlayer().getName()) &&
                 (getBingoPlayer(event.getPlayer())).inLobby && !gameStarted)
             event.setCancelled(true);
     }

    @EventHandler
    public void onPlayerTakeDamage(EntityDamageEvent event) {
        if (this.allPlayers.containsKey(event.getEntity().getName()) &&
                (getBingoPlayer((Player)event.getEntity())).inLobby && !gameStarted)
            event.setCancelled(true);
    }

    @EventHandler
    public void onHungerDeplete(FoodLevelChangeEvent event) {
        event.setCancelled(true);
        event.setFoodLevel(20);
    }

    @EventHandler
    public void onPlayerJoinServer(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        p.setGameMode(GameMode.SURVIVAL);

        String pName = p.getName();
        if (registeredPlayerNames.contains(pName)){
            registeredPlayers.add(p);
            System.out.println("Added " + pName + " to registered Players array.");
            log("Added " + pName + " to registered Players array.");
        }
    }
}