package io.github.utshaw.blooddonor_v3;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;

public class IntroActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String TAG = IntroActivity.class.getSimpleName();

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    final private int REQUEST_LOCATION_CODE = 99;
    final private int REQUEST_GPS_ACTIVATE_CODE = 100;
    final private int REQUEST_INTERNET = 101;


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Logger.d("Intro Activity initiating");

        Logger.addLogAdapter(new AndroidLogAdapter());


        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        if(!isNetworkAvailable(getApplicationContext())){
            showDialogInternet2();
        }



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if(isGPSEnabled(IntroActivity.this)) {
                    mGoogleApiClient.connect();
                }
                else{
                    showDialogForActivatingGPS();
                }
            }
            else{
                // No permission on marshmallow , so get permission from user
                showGetPermissionOnMarshmallow();
            }
        }
        else{// Pre Marshmallow device
            if(isGPSEnabled(IntroActivity.this)) {
                mGoogleApiClient.connect();
            }
            else{
                showDialogForActivatingGPS();
            }
        }

    }

    private void showDialogInternet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                IntroActivity.this);
        alertDialogBuilder
                .setMessage("Internet is disabled in your device. Enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable internet",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                Intent internetIntent = new Intent(
                                        WifiManager.ACTION_PICK_WIFI_NETWORK);
                                startActivityForResult(internetIntent,REQUEST_INTERNET);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Toast.makeText(IntroActivity.this, "Internet is required for this app", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void showDialogInternet2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This App needs activate internet connection & GPS service to initiate.\nPlease Activate internet & restart the app")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if(!mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.connect();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d("onPause gets called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d("onStop()");
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d("onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.d("onStart()");
    }

    private void handleNewLocation(Location location) {

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        UserLocationInfo.setUserLatitude(currentLatitude);
        UserLocationInfo.setUserLongitude(currentLongitude);

        Logger.d(currentLatitude);
        Logger.d(currentLongitude);



        startActivity(new Intent(IntroActivity.this,MainActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
        finish();

        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            Logger.d("location was null initiating updates");
            if(!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }
        else {
            Logger.d("location is not null");
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }catch (IllegalStateException e){
            startActivity(new Intent(IntroActivity.this,MainActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));

        }
    }

    public boolean showGetPermissionOnMarshmallow(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
            ActivityCompat.requestPermissions(this,new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
        }
        else{
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_LOCATION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permission granted
                    if(isGPSEnabled(IntroActivity.this)){
                        mGoogleApiClient.connect();
                    }
                    else{

                        showDialogForActivatingGPS();
                    }

                }
                else {
                    //permission denied so turn off the app
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_GPS_ACTIVATE_CODE:
                if(isGPSEnabled(this)){
                    Log.e("Utshaw","GPS enabled");
                    mGoogleApiClient.connect();
                }
                else{

                    Toast.makeText(this, "Activate GPS & restart the app", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case REQUEST_INTERNET:
                if(!isNetworkAvailable(getApplicationContext())){
                    Toast.makeText(this, "Activate internet & restart the app", Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    if(UserLocationInfo.isValueSet()){
                        if(mGoogleApiClient.isConnected()){
                            mGoogleApiClient.disconnect();
                            startActivity(new Intent(IntroActivity.this,MainActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
                            finish();
                        }
                        else{
                            mGoogleApiClient.connect();
                        }
                    }
                }
                break;

        }
    }

    public boolean isGPSEnabled (Context mContext){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showDialogForActivatingGPS(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                IntroActivity.this);
        alertDialogBuilder
                .setMessage("GPS is disabled in your device. Enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(callGPSSettingIntent,REQUEST_GPS_ACTIVATE_CODE);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Toast.makeText(IntroActivity.this, "GPS is required for this app", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


}