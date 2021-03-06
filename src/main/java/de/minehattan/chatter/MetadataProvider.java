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

import org.bukkit.entity.Player;

/**
 * Provides all methods that metadata-providers need to have source the plugin's
 * logic
 */
public interface MetadataProvider {

  /**
   * Gets the player's prefix
   *
   * @param player the player
   * @return the effective prefix
   */
  String getPrefix(Player player);

  /**
   * Gets the player's suffix
   *
   * @param player the player
   * @return the effective suffix
   */
  String getSuffix(Player player);

}
