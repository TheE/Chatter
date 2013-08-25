package de.minehattan.chatter;

import org.bukkit.entity.Player;

import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;

/**
 * Represents bPermissions2
 */
public class bPermissionsProvider implements MetadataProvider {

    @Override
    public String getPrefix(Player player) {
        String prefix = ApiLayer.getValue(player.getWorld().getName(), CalculableType.USER, player.getName(),
                "prefix");
        if (prefix.equals("")) {
            prefix = ApiLayer.getValue(player.getWorld().getName(), CalculableType.GROUP, player.getName(),
                    "prefix");
        }
        return prefix;
    }

    @Override
    public String getSuffix(Player player) {
        String suffix = ApiLayer.getValue(player.getWorld().getName(), CalculableType.USER, player.getName(),
                "suffix");
        if (suffix.equals("")) {
            suffix = ApiLayer.getValue(player.getWorld().getName(), CalculableType.GROUP, player.getName(),
                    "suffix");
        }
        return suffix;
    }

}
