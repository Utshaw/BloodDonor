package io.github.utshaw.blooddonor_v3;

/**
 * Created by Utshaw on 10/1/2017.
 */

public class UserLocationInfo {
    public static final double BAD_VALUE = -9999.99;

    public static double userLatitude = BAD_VALUE;
    public static double userLongitude = BAD_VALUE;

    public static double getUserLatitude() {
        return userLatitude;
    }

    public static void setUserLatitude(double userLatitude) {
        UserLocationInfo.userLatitude = userLatitude;
    }

    public static double getUserLongitude() {
        return userLongitude;
    }

    public static void setUserLongitude(double userLogitude) {
        UserLocationInfo.userLongitude = userLogitude;
    }

    public static boolean isValueSet(){
        return userLatitude != BAD_VALUE && userLongitude != BAD_VALUE;
    }
}
