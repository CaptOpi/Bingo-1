import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

public class CustomFiles {
    private static File itemsFile1;
    private static File itemsFile2;
    private static File itemsFile3;
    private static File itemsFile4;

    private static FileConfiguration itemsConfig1;
    private static FileConfiguration itemsConfig2;
    private static FileConfiguration itemsConfig3;
    private static FileConfiguration itemsConfig4;

    private static File messagesFile;

    public static File scoreFile;

    private static FileConfiguration messagesConfig;

    public static File logFile;
    public static File roundFile;
    private static FileConfiguration roundConfig;
    private static FileConfiguration scoreConfig;

    private static final SimpleDateFormat TimeFormat = new SimpleDateFormat("dd/MMM/yyyy - HH.mm.ss", Locale.US);

    public static FileConfiguration getItemsConfig1() {
        return itemsConfig1;
    }

    public static FileConfiguration getItemsConfig2() {
        return itemsConfig2;
    }

    public static FileConfiguration getItemsConfig3() {
        return itemsConfig3;
    }

    public static FileConfiguration getItemsConfig4() {
        return itemsConfig4;
    }

    public static FileConfiguration getMessagesConfig() {
        return messagesConfig;
    }

    public static FileConfiguration getScoreConfig() {
        return scoreConfig;
    }

    public static FileConfiguration getRoundConfig() {
        return roundConfig;
    }

    public static void createItemsConfig1() {
        itemsFile1 = new File(Bingo.plugin.getDataFolder(), "items1.yml");
        if (!itemsFile1.exists()) {
            itemsFile1.getParentFile().mkdirs();
            Bingo.plugin.saveResource("items1.yml", false);
        }
        itemsConfig1 = (FileConfiguration)new YamlConfiguration();
        try {
            itemsConfig1.load(itemsFile1);
        } catch (IOException|org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public static void createItemsConfig2() {
        itemsFile2 = new File(Bingo.plugin.getDataFolder(), "items2.yml");
        if (!itemsFile2.exists()) {
            itemsFile2.getParentFile().mkdirs();
            Bingo.plugin.saveResource("items2.yml", false);
        }
        itemsConfig2 = (FileConfiguration)new YamlConfiguration();
        try {
            itemsConfig2.load(itemsFile2);
        } catch (IOException|org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public static void createItemsConfig3() {
        itemsFile3 = new File(Bingo.plugin.getDataFolder(), "items3.yml");
        if (!itemsFile3.exists()) {
            itemsFile3.getParentFile().mkdirs();
            Bingo.plugin.saveResource("items3.yml", false);
        }
        itemsConfig3 = (FileConfiguration)new YamlConfiguration();
        try {
            itemsConfig3.load(itemsFile3);
        } catch (IOException|org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public static void createItemsConfig4() {
        itemsFile4 = new File(Bingo.plugin.getDataFolder(), "items4.yml");
        if (!itemsFile4.exists()) {
            itemsFile4.getParentFile().mkdirs();
            Bingo.plugin.saveResource("items4.yml", false);
        }
        itemsConfig4 = (FileConfiguration)new YamlConfiguration();
        try {
            itemsConfig4.load(itemsFile4);
        } catch (IOException|org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void createScoreConfig() {
        scoreFile = new File(Bingo.plugin.getDataFolder(), "score.yml");
        if (!scoreFile.exists()) {
            scoreFile.getParentFile().mkdirs();
            Bingo.plugin.saveResource("score.yml",false);
        }
        scoreConfig = (FileConfiguration) new YamlConfiguration();
        try {
            scoreConfig.load(scoreFile);
        } catch (IOException|org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public static void scoreConfigSave() {
        try {
            scoreConfig.save(scoreFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void createManualConfig() {
        roundFile = new File(Bingo.plugin.getDataFolder(), "round.yml");
        if(!roundFile.exists()) {
            roundFile.getParentFile().mkdirs();
            Bingo.plugin.saveResource("round.yml",false);
        }
        roundConfig = (FileConfiguration) new YamlConfiguration();
        try {
            roundConfig.load(roundFile);
        } catch (IOException|org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public static void roundConfigSave() {
        try {
            roundConfig.save(roundFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void reloadScore() {
        scoreConfig = YamlConfiguration.loadConfiguration(scoreFile);
    }
    public static void createMessagesConfig() {
        messagesFile = new File(Bingo.plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdirs();
            Bingo.plugin.saveResource("messages.yml", false);
        }
        messagesConfig = (FileConfiguration)new YamlConfiguration();
        try {
            messagesConfig.load(messagesFile);
            InitMessages();
        } catch (IOException|org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void createLogFile() throws IOException {
        logFile = new File(Bingo.plugin.getDataFolder(), "bingolog.txt");
        try {
            if (logFile.createNewFile())
                saveToLog("Log generated");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveToLog(String message) {
        if (Bingo.bingolog) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            message = "[" + TimeFormat.format(timestamp).toUpperCase() + "] " + message + "\r\n";
            message = ChatColor.stripColor(message);
            try {
                Files.write(logFile.toPath(), message.getBytes(), new OpenOption[] { StandardOpenOption.APPEND });
            } catch (IOException iOException) {}
        }
    }

    public static String expectedMessagesConfigVestion = "1.0";

    public static String version_str;

    public static List<String> helpmsg;

    public static List<String> adminhelpmsg;

    public static String no_permission;

    public static String unknown_command;

    public static String bingo_board;

    public static String bingo_card;

    public static String not_whitelisted_world;

    public static String wrong_world;

    public static String game_has_started;

    public static String has_started_game;

    public static String game_succesfully_started;

    public static String inventory_will_clear;

    public static String inventory_has_cleared;

    public static String non_player_error;

    public static String option_disabled;

    public static String use_own_card;

    public static String checked_other_card;

    public static String player_not_found;

    public static String fill_out_other_player;

    public static String no_game_found;

    public static String game_already_started;

    public static String already_in_game;

    public static String not_in_game;

    public static String left_game;

    public static String need_survivalmode;

    public static String need_fee;

    public static String you_won;

    public static String duration;

    public static String joined_game;

    public static String player_left_game;

    public static String replaced_item;

    public static String has_obtained;

    public static String bingo;

    public static String bingo_team;

    public static String has_won;

    public static String bingo_game_ongoing;

    public static String has_died;

    public static String almost_starting;

    public static String starting_in;

    public static String has_started;

    public static String has_stopped;

    public static String team_selection_menu;

    public static String select_team;

    public static String obtained_team;

    public static String not_in_team;

    public static String my_team;

    public static void InitMessages() {
        if (!getMessagesConfig().getString("version").equals(expectedMessagesConfigVestion)) {
            Bingo.plugin.log("Your messages configuration file is out of date, it will be overwritten with the new one now.");
            messagesFile.delete();
            messagesFile.getParentFile().mkdirs();
            Bingo.plugin.saveResource("messages.yml", false);
            createMessagesConfig();
            return;
        }
        version_str = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("version_str"));
        helpmsg = getMessagesConfig().getStringList("help");
        adminhelpmsg = getMessagesConfig().getStringList("adminhelp");
        no_permission = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("no_permission"));
        unknown_command = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("unknown_command"));
        bingo_board = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("bingo_board"));
        bingo_card = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("bingo_card"));
        not_whitelisted_world = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("not_whitelisted_world"));
        wrong_world = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("wrong_world"));
        game_has_started = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("game_has_started"));
        has_started_game = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("has_started_game"));
        game_succesfully_started = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("game_succesfully_started"));
        inventory_will_clear = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("inventory_will_clear"));
        inventory_has_cleared = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("inventory_has_cleared"));
        non_player_error = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("non_player_error"));
        option_disabled = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("option_disabled"));
        use_own_card = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("use_own_card"));
        checked_other_card = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("checked_other_card"));
        player_not_found = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("player_not_found"));
        fill_out_other_player = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("fill_out_other_player"));
        no_game_found = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("no_game_found"));
        game_already_started = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("game_already_started"));
        already_in_game = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("already_in_game"));
        not_in_game = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("not_in_game"));
        left_game = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("left_game"));
        need_survivalmode = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("need_survivalmode"));
        need_fee = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("need_fee"));
        you_won = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("you_won"));
        duration = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("duration"));
        joined_game = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("joined_game"));
        player_left_game = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("player_left_game"));
        replaced_item = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("replaced_item"));
        has_obtained = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("has_obtained"));
        bingo = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("bingo"));
        bingo_team = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("bingo_team"));
        has_won = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("has_won"));
        bingo_game_ongoing = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("bingo_game_ongoing"));
        has_died = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("has_died"));
        almost_starting = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("almost_starting"));
        starting_in = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("starting_in"));
        has_started = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("has_started"));
        has_stopped = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("has_stopped"));
        team_selection_menu = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("team_selection_menu"));
        select_team = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("select_team"));
        obtained_team = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("obtained_team"));
        not_in_team = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("not_in_team"));
        my_team = ChatColor.translateAlternateColorCodes('&', getMessagesConfig().getString("my_team"));
    }
}