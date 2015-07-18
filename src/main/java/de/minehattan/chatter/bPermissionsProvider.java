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

import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;

import org.bukkit.entity.Player;

/**
 * Represents bPermissions2
 */
public class bPermissionsProvider implements MetadataProvider {

  @Override
  public String getPrefix(Player player) {
    String prefix = ApiLayer.getValue(player.getWorld().getName(), CalculableType.USER, player.getName(), "prefix");
    if (prefix.equals("")) {
      prefix = ApiLayer.getValue(player.getWorld().getName(), CalculableType.GROUP, player.getName(), "prefix");
    }
    return prefix;
  }

  @Override
  public String getSuffix(Player player) {
    String suffix = ApiLayer.getValue(player.getWorld().getName(), CalculableType.USER, player.getName(), "suffix");
    if (suffix.equals("")) {
      suffix = ApiLayer.getValue(player.getWorld().getName(), CalculableType.GROUP, player.getName(), "suffix");
    }
    return suffix;
  }

}
