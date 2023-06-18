package net.ldoin.bedwars.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtil {

    public static Location getLocation(String string) {
        String world = null;
        double x = 0;
        double y = 0;
        double z = 0;
        float yaw = 0;
        float pitch = 0;
        String[] rawLoc = string.split(" ");
        try {
            world = rawLoc[0];
            x = Double.parseDouble(rawLoc[1]);
            y = Double.parseDouble(rawLoc[2]);
            z = Double.parseDouble(rawLoc[3]);
            try {
                yaw = Float.parseFloat(rawLoc[4]);
                pitch = Float.parseFloat(rawLoc[5]);
            } catch (IndexOutOfBoundsException exception) {
                yaw = 0;
                pitch = 0;
            }
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }
}