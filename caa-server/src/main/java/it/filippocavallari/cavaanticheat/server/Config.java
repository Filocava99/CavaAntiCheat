package it.filippocavallari.cavaanticheat.server;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {

    private final File configFile;
    private final Logger logger = Bukkit.getLogger();
    private final JavaPlugin plugin;
    private FileConfiguration config;

    /**
     * Use this constructor in case you want to copy an internal file (or template)
     *
     * @param configName Name of the config file
     * @param plugin     Instance of the plugin
     */
    public Config(@NotNull String configName, @NotNull JavaPlugin plugin) throws IOException {
        this(new File(plugin.getDataFolder(), configName), plugin);
    }

    /**
     * Use this constructor in case you want to load an existing file
     *
     * @param configFile The file you want to load
     */
    public Config(@NotNull File configFile, @NotNull JavaPlugin plugin) throws IOException {
        this.configFile = configFile;
        this.plugin = plugin;
        if(!exists()){
            createConfig();
        }
        loadConfig();
    }

    /**
     * Returns the FileConfiguration instance
     *
     * @return FileCongifuration
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Saves current config values
     */
    public void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while saving the config file " + ChatColor.RED + configFile.getName());

            e.printStackTrace();
        }
    }

    private boolean exists(){
        if(configFile.exists()){
            logger.info(configFile.getName() + " has been found");
            return true;
        }else{
            logger.info(configFile.getName() + " " +
                    "has not been found");
            return false;
        }
    }

    private void createConfig() throws IOException {
        logger.info("Creating " + configFile.getName() + "...");
        if (!configFile.getParentFile().exists()) {
            boolean result = configFile.getParentFile().mkdirs();
            if (!result) throw new IOException("Error creating " + configFile.getParentFile().getName());
        }
        if(plugin.getResource(configFile.getName()) != null){
            plugin.saveResource(configFile.getName(), false);
        }else{
            if (!configFile.exists()) {
                boolean result = configFile.createNewFile();
                if(result){
                    logger.log(Level.INFO, configFile.getName() + "has been created");
                }else{
                    logger.log(Level.SEVERE, ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "Error creating " + configFile.getName());
                    throw new IOException("Error creating " + configFile.getName());
                }
            }
        }
    }

    private void loadConfig(){
        config = YamlConfiguration.loadConfiguration(configFile);
        logger.info(configFile.getName() + " has been loaded");
    }
}