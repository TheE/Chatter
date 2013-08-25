package de.minehattan.chatter;

import org.bukkit.entity.Player;

/**
 * Provides all methods that metadata-providers need to have source the plugin's
 * logic
 */
public interface MetadataProvider {

    /**
     * Gets the player's prefix
     * 
     * @param player
     *            the player
     * @return the effective prefix
     */
    public String getPrefix(Player player);

    /**
     * Gets the player's suffix
     * 
     * @param player
     *            the player
     * @return the effective suffix
     */
    public String getSuffix(Player player);

}
