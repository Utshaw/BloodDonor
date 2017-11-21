package io.github.utshaw.blooddonor_v3.hospitals;

/**
 * Created by Utshaw on 9/29/2017.
 */

public class Hospital {
    private String mName;
    private double mLatitude;
    private double mLongitude;
    private String vicinity;
    private double distanceFromCurrentLocation;

    public Hospital(String mName, double mLatitude, double mLongitude, String vicinity, double distanceFromCurrentLocation) {
        this.mName = mName;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.vicinity = vicinity;
        this.distanceFromCurrentLocation = distanceFromCurrentLocation;
    }

    public String getmName() {
        return mName;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public String getVicinity() {
        return vicinity;
    }

    public double getDistanceFromCurrentLocation() {
        return distanceFromCurrentLocation;
    }


    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


}