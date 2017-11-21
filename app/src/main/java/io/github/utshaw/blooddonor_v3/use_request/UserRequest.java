package io.github.utshaw.blooddonor_v3.use_request;

import java.io.Serializable;

import io.github.utshaw.blooddonor_v3.UserLocationInfo;

/**
 * Created by Utshaw on 9/30/2017.
 */

public class UserRequest implements Serializable {
    private String mName;
    private String mAddress;
    private String mPhoneNumber;
    private String mBloodGroup;
    private double mLatitude;
    private double mLongitude;


    public UserRequest() {}

    public UserRequest(String mName, String mAddress, String mPhoneNumber, String mBloodGroup) {
        this.mName = mName;
        this.mAddress = mAddress;
        this.mPhoneNumber = mPhoneNumber;
        this.mBloodGroup = mBloodGroup;
        this.mLatitude = UserLocationInfo.BAD_VALUE;
        this.mLongitude = UserLocationInfo.BAD_VALUE;
    }

    public UserRequest(String mName, String mAddress, String mPhoneNumber, String mBloodGroup, double mLatitude, double mLongitude) {
        this.mName = mName;
        this.mAddress = mAddress;
        this.mPhoneNumber = mPhoneNumber;
        this.mBloodGroup = mBloodGroup;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmBloodGroup() {
        return mBloodGroup;
    }

    public void setmBloodGroup(String mBloodGroup) {
        this.mBloodGroup = mBloodGroup;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }
}
