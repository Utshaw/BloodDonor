package io.github.utshaw.blooddonor_v3;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Utshaw on 9/27/2017.
 */

public class User implements Serializable{
    private String mUsername;

    private String mContactNumber;


    private boolean mGender; // true for Male , false for Female

    private String mBloodGroup;

    private String mWeight;

    private String mStringLocation;

    private double mLatitude;

    private double mLongitude;

    private long mDate;

    private boolean activeDonor;

    public long getmDate() {
        return mDate;
    }

    public User() {}

    public void setmStringLocation(String mStringLocation) {
        this.mStringLocation = mStringLocation;
    }

    public String getmStringLocation() {

        return mStringLocation;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public double getmLatitude() {

        return mLatitude;
    }


    public boolean isActiveDonor() {
        return activeDonor;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public User(String mUsername, String mContactNumber, boolean mGender, String mBloodGroup, String mWeight, String mStringLocation, double mLatitude, double mLongitude, long date, boolean activeDonor) {
        this.mUsername = mUsername;
        this.mContactNumber = mContactNumber;
        this.mGender = mGender;
        this.mBloodGroup = mBloodGroup;
        this.mWeight = mWeight;
        this.mStringLocation = mStringLocation;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mDate = date;
        this.activeDonor = activeDonor;
    }

    public User(String mUsername, String mContactNumber, boolean mGender, String mBloodGroup, String mWeight, String mStringLocation /*, Date date*/) {
        this.mUsername = mUsername;
        this.mContactNumber = mContactNumber;
        this.mGender = mGender;
        this.mBloodGroup = mBloodGroup;
        this.mWeight = mWeight;
        this.mStringLocation = mStringLocation;
        this.mLatitude = UserLocationInfo.BAD_VALUE;
        this.mLongitude = UserLocationInfo.BAD_VALUE;
//        this.mDate = date;
    }

    public String getmUsername() {
        return mUsername;
    }

    public String getmContactNumber() {
        return mContactNumber;
    }


    public boolean ismGender() {
        return mGender;
    }

    public String getmBloodGroup() {
        return mBloodGroup;
    }

    public String getmWeight() {
        return mWeight;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public void setmContactNumber(String mContactNumber) {
        this.mContactNumber = mContactNumber;
    }


    public void setmGender(boolean mGender) {
        this.mGender = mGender;
    }

    public void setmBloodGroup(String mBloodGroup) {
        this.mBloodGroup = mBloodGroup;
    }

    public void setmWeight(String mWeight) {
        this.mWeight = mWeight;
    }
}
