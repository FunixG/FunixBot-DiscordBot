package fr.funixgaming.bot.Modules;

import com.google.gson.Gson;
import fr.funixgaming.bot.Utils.ConsoleColors;
import fr.funixgaming.bot.Utils.FileActions;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static fr.funixgaming.bot.Main.dataFolder;

public class BotConfiguration {

    private static final String configVersion = "1.0";
    private static final File configFile = new File(dataFolder, "botConfiguration.json");

    public String discordToken;
    public String bienvenueID;
    public String logID;
    public String twitchID;
    public String followerID;
    public String adminID;

    public String configVersionSet;

    //Used for base config generation
    private BotConfiguration(boolean genConf) {
        if (!genConf) return;
        Scanner scanner = new Scanner(System.in);
        System.out.println(ConsoleColors.GREEN + "Veuillez entrer le token du fr.pacifista.bot discord: ");
        this.discordToken = scanner.nextLine();
        System.out.println(ConsoleColors.GREEN + "Channel id de bienvenue: ");
        this.bienvenueID = scanner.nextLine();
        System.out.println(ConsoleColors.GREEN + "Channel id de log: ");
        this.logID = scanner.nextLine();
        System.out.println(ConsoleColors.GREEN + "Channel id de twitch: ");
        this.twitchID = scanner.nextLine();
        System.out.println(ConsoleColors.GREEN + "Group id du grade follower: ");
        this.followerID = scanner.nextLine();
        System.out.println(ConsoleColors.GREEN + "Group id du grade admin: " + ConsoleColors.WHITE);
        this.adminID = scanner.nextLine();
        this.configVersionSet = configVersion;
    }

    public void saveConfig() throws IOException {
        Gson gson = new Gson();

        if (!dataFolder.exists() && !dataFolder.mkdir())
            throw new IOException("Error while creating data folder");
        if (!configFile.exists()) {
            if (!configFile.createNewFile())
                throw new IOException("Erreur lors de la création de botConfiguration.json");
        }
        String objString = gson.toJson(this);
        FileActions.writeInFile(configFile, objString, false);
    }

    public static void removeConfigFile() {
        configFile.delete();
    }

    public static BotConfiguration getConfiguration() throws IOException, NoSuchElementException {
        if (!dataFolder.exists() && !dataFolder.mkdir())
            throw new IOException("Error while creating data folder");
        if (!configFile.exists()) {
            System.out.println(ConsoleColors.YELLOW_BOLD + "La configuration du fr.pacifista.bot n'existe pas. Veuillez configurer le fr.pacifista.bot." + ConsoleColors.WHITE);
            BotConfiguration config = new BotConfiguration(true);
            config.saveConfig();
            return config;
        } else {
            String fileContent = FileActions.getFileContent(configFile);
            Gson gson = new Gson();
            BotConfiguration config = gson.fromJson(fileContent, BotConfiguration.class);
            if (!config.configVersionSet.equals(configVersion)) {
                System.out.println(ConsoleColors.YELLOW_BOLD + "La configuration du fr.pacifista.bot à changé. Veuillez reconfigurer le fr.pacifista.bot." + ConsoleColors.WHITE);
                config = new BotConfiguration(true);
                config.saveConfig();
            }
            return config;
        }
    }
}
