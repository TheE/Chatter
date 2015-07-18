/*
 * Copyright (C) 2013 - 2015, Chatter team and contributors
 *
 * This file is part of Chatter.
 *
 * Chatter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Chatter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Chatter. If not, see <http://www.gnu.org/licenses/>.
 */

package de.minehattan.chatter;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Chatter extends JavaPlugin implements Listener {

  private static final String CONFIG_FILE = "config.yml";

  MetadataProvider metadataProvider;
  FileConfiguration config;

  @Override
  public void onEnable() {
    File configFile = new File(getDataFolder(), CONFIG_FILE);
    config = getYAMLConfig(configFile, YamlConfiguration.loadConfiguration(getResource(CONFIG_FILE)));

    // register events
    getServer().getPluginManager().registerEvents(this, this);

    // setup the metadata provider
    metadataProvider = new bPermissionsProvider();
  }

  // async cannot be used, we need api methods!
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPlayerChatEvent(PlayerChatEvent event) {
    Player player = event.getPlayer();

    // replace message color codes
    if (player.hasPermission("chatter.colour")) {
      String message = event.getMessage();
      message = replaceColorCodes(message);
      event.setMessage(message);
    }

    // set the format
    String format = config.getString("chat.format");
    format = replacePlayerPlaceholders(player, format);
    format = replaceColorCodes(format);
    event.setFormat(format);
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPlayerJoin(PlayerJoinEvent event) {
    event.setJoinMessage(replaceColorCodes(StringUtils.replace(
        event.getPlayer().hasPlayedBefore() ? config.getString("messages.onJoin")
                                            : config.getString("messages.onFirstJoin"), "%player",
        event.getPlayer().getDisplayName())));
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPlayerQuit(PlayerQuitEvent event) {
    event.setQuitMessage(replaceColorCodes(
        StringUtils.replace(config.getString("messages.onQuit"), "%player", event.getPlayer().getDisplayName())));
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
    if (!command.getName().equalsIgnoreCase("chatter")) {
      return false;
    }
    if (args.length != 1 || !args[0].equals("reload")) {
      sender.sendMessage(ChatColor.RED + "Unknown command.");
      return true;
    }
    if (!sender.hasPermission("chatter.reload")) {
      sender.sendMessage(ChatColor.RED + "Insufficient permission!");
      return true;
    }
    reloadConfig();
    sender.sendMessage(ChatColor.GREEN + "Chatter's configuration has been reloaded.");
    return true;
  }

  /**
   * Replaces all message-placeholder found in the given string with the one
   * applicable for the given player
   *
   * @param player the player
   * @param str    the string
   * @return the string with replaced placeholders
   */
  private String replacePlayerPlaceholders(Player player, String str) {
    return str.replace("%prefix", metadataProvider.getPrefix(player))
        .replace("%suffix", metadataProvider.getSuffix(player)).replace("%world", player.getWorld().getName())
        .replace("%player", player.getDisplayName()).replace("%message", "%2$s");
  }

  /**
   * replaces all coulour-codes found in the given string.
   *
   * @param str the string
   * @return the string with replaced colour-macros
   */
  private String replaceColorCodes(String str) {
    return ChatColor.translateAlternateColorCodes(config.getString("chat.colourChar").charAt(0), str);
  }

  /**
   * Returns the FileCOnfiguration found in the given configFile. Returns the
   * given defaults if any errors occur.
   *
   * @param configFile    the configuration-file
   * @param defaultConfig the default file-configuration
   * @return the file-configuration as found in the given file
   */
  private FileConfiguration getYAMLConfig(File configFile, FileConfiguration defaultConfig) {
    FileConfiguration config = defaultConfig;

    try {
      // create the configuration path if it does not exist
      if (!configFile.getParentFile().exists()) {
        configFile.getParentFile().mkdirs();
      }
      // create the configuration file if it does not exist
      if (!configFile.exists()) {
        configFile.createNewFile();
        getLogger().info("Default " + configFile.getName() + " created successfully!");
      }

      // load the configuration-file
      config = YamlConfiguration.loadConfiguration(configFile);

      // copy defaults for missing values
      config.setDefaults(defaultConfig);
      config.options().copyDefaults(true);
      config.save(configFile);
    } catch (IOException e) {
      getLogger()
          .log(Level.SEVERE, "Failed to create default " + configFile.getName() + " , using build-in defaults: ", e);
    }
    return config;
  }
}
