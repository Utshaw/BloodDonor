package io.github.utshaw.blooddonor_v3;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.github.utshaw.blooddonor_v3.use_request.UserRequest;

/**
 * Created by Utshaw on 10/1/2017.
 */

public class RequestActivity extends AppCompatActivity {


    int PLACE_PICKER_REQUEST = 1;


    //Firebase instances
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    private EditText mNameEditText;
    private EditText mMobileNoEditText;
    private EditText mAddressEditText;
    private Spinner mBloodGroupSpinner;
    private CheckBox mCheckBox;
    private KeyListener editTextKeyListener;
    private Button locationChooserButton ;
    private String authMobileNo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        mNameEditText = (EditText) findViewById(R.id.request_name);
        mMobileNoEditText = (EditText) findViewById(R.id.request_mobile_no);
        mAddressEditText = (EditText) findViewById(R.id.request_address);
        mBloodGroupSpinner = (Spinner) findViewById(R.id.request_spinner_blood_group);
        locationChooserButton = (Button) findViewById(R.id.location_btn);

        authMobileNo = getIntent().getExtras().getString("phoneNumber");
        ((TextView)(findViewById(R.id.edit_auth_mobile))).setText(authMobileNo);

        setUpSpinner();




        editTextKeyListener = mAddressEditText.getKeyListener();




        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

    }

    public void addressClick(View view) {
        mAddressEditText.setKeyListener(null);
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(RequestActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data );
                locationChooserButton.setText("Change Location");
                mAddressEditText.setVisibility(View.VISIBLE);
                mAddressEditText.setText(place.getName() + ", " + place.getAddress());
                UserLocationInfo.setUserLatitude(place.getLatLng().latitude);
                UserLocationInfo.setUserLongitude(place.getLatLng().longitude);
            }
        }
    }

    private void setUpSpinner() {
        ArrayAdapter bloodGroupSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_blood_groups, android.R.layout.simple_spinner_item);
        bloodGroupSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mBloodGroupSpinner.setAdapter(bloodGroupSpinnerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_request, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_request_save:
                if (isNetworkAvailable(getApplicationContext())) {
                    String accName = mNameEditText.getText().toString().trim();
                    String accMobile = mMobileNoEditText.getText().toString().trim();
                    String accBlood = mBloodGroupSpinner.getSelectedItem().toString();
                    String location = mAddressEditText.getText().toString();
                    if (accName.length() < 1 || accMobile.length() < 1 || accBlood.length() < 1 || location.length() < 1) {
                        Toast.makeText(this, "Please fill up all the field!", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    UserRequest user = null;
                    user = new UserRequest(accName, location, accMobile, accBlood, UserLocationInfo.userLatitude, UserLocationInfo.getUserLongitude());
                    mDatabaseReference.child("requests_accounts").child(authMobileNo).setValue(accBlood);
                    mDatabaseReference.child("requests").child(accBlood).child(authMobileNo).setValue(user);
                    finish();
                } else {
                    Toast.makeText(this, "Check internet connection", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_sign_out:
                if(isNetworkAvailable(getApplicationContext())) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(this, "Signing Out From Account", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(this, "Signing out failed.Check internet connection!", Toast.LENGTH_SHORT).show();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


}
