package io.github.utshaw.blooddonor_v3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.Arrays;

import io.github.utshaw.blooddonor_v3.chat.ChatActivity;
import io.github.utshaw.blooddonor_v3.hospitals.HospitalListActivity;
import io.github.utshaw.blooddonor_v3.use_request.UserRequest;
import io.github.utshaw.blooddonor_v3.use_request.UserRequestListActivity;

/**
 * Created by Utshaw on 10/1/2017.
 */

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN_REQUEST = 2;
    private ValueEventListener valueEventListener;
    private ValueEventListener userValueEventListener;
    private DatabaseReference dReference;
    private DatabaseReference userReference;

    private TextView mBloodSearchTextView;
    private Spinner mRequestListSpinner;
    private Spinner mBloodSearchSpinner;
    private TextView mRequestTextView;
    private TextView mRequestListTextView;
    private TextView donationTextView;
    private TextView mHospitalsTextView;
    private TextView mFAQTextView;
    private TextView mAdminTextView;

    //Firebase variables
    protected FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;


    //Request codes
    public  final int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.d("Main Activity onCreate()");


        if(!UserLocationInfo.isValueSet()) {
            showLocationNotSetDialog();
        }

        //Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        mBloodSearchTextView = (TextView) findViewById(R.id.tview_search);
        mBloodSearchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetworkAvailable(getApplicationContext())){
                    Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(MainActivity.this,DonorSearchActivity.class);
                    intent.putExtra("bloodGroup", mBloodSearchSpinner.getSelectedItem().toString());
                    startActivity(intent);
                }
            }
        });

        mBloodSearchSpinner = (Spinner) findViewById(R.id.blood_search_spinner);
        mRequestListSpinner = (Spinner) findViewById(R.id.request_spinner_blood_group);
        setUpSpinner();

        mAdminTextView = (TextView) findViewById(R.id.admin);
        mAdminTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AdminActivity.class));
            }
        });




        mRequestListTextView = (TextView) findViewById(R.id.see_requests);
        mRequestListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable(getApplicationContext())){
                    Intent intent = new Intent(MainActivity.this,UserRequestListActivity.class);
                    intent.putExtra("bloodGroup",mRequestListSpinner.getSelectedItem().toString());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });





        mRequestTextView = (TextView) findViewById(R.id.request);
        mRequestTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable(getApplicationContext())){
                    mFirebaseAuth = FirebaseAuth.getInstance();
                    if(mFirebaseAuth.getCurrentUser() != null){
                        dReference = mDatabaseReference.child("requests_accounts").child(mFirebaseAuth.getCurrentUser().getPhoneNumber());

                        valueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String  str = dataSnapshot.getValue(String.class);
                                if(str == null){
                                    Intent intent = new Intent(MainActivity.this,RequestActivity.class);
                                    intent.putExtra("phoneNumber",mFirebaseAuth.getCurrentUser().getPhoneNumber().toString());
                                    startActivity(intent);
                                }
                                else{
                                    userReference = mDatabaseReference.child("requests").child(str).child(mFirebaseAuth.getCurrentUser().getPhoneNumber().toString());
                                    userValueEventListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            UserRequest user = dataSnapshot.getValue(UserRequest.class);
                                            Intent intent = new Intent(MainActivity.this,MyRequestActivity.class);
                                            intent.putExtra("requestObject",user);
                                            intent.putExtra("phoneNumber",mFirebaseAuth.getCurrentUser().getPhoneNumber());
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    };
                                    userReference.addValueEventListener(userValueEventListener);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(MainActivity.this, "Database read ERROR!", Toast.LENGTH_SHORT).show();

                            }
                        };
                        dReference.addValueEventListener(valueEventListener);
                    }
                    else {
                        // not signed in
                        startActivityForResult(
                                AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setAvailableProviders(
                                                Arrays.asList(
                                                        new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()))
                                        .build(),
                                RC_SIGN_IN_REQUEST);
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        donationTextView = (TextView) findViewById(R.id.donate);
        donationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable(getApplicationContext())){
                    mFirebaseAuth = FirebaseAuth.getInstance();
                    if(mFirebaseAuth.getCurrentUser() != null){

//                        dReference = mDatabaseReference.child("users").child(spinner.getSelectedItem().toString()).child(mFirebaseAuth.getCurrentUser().getPhoneNumber());
                        dReference = mDatabaseReference.child("accounts").child(mFirebaseAuth.getCurrentUser().getPhoneNumber());
                        valueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String  str = dataSnapshot.getValue(String.class);
                                if(str == null){
                                    Logger.d(str + " Creating Account");
                                    // new User , Create Activity
                                    Intent intent = new Intent(MainActivity.this,CreateUserActivity.class);
                                    intent.putExtra("phoneNumber",mFirebaseAuth.getCurrentUser().getPhoneNumber().toString());
                                    Log.e("KEY",mFirebaseAuth.getCurrentUser().getPhoneNumber());
                                    startActivity(intent);
                                    Log.e("Utshaw","Started Activity");
                                }
                                else{

                                    //TODO: Old User Show MyAccountActivity
                                    userReference = mDatabaseReference.child("users").child(str).child(mFirebaseAuth.getCurrentUser().getPhoneNumber().toString());
                                    userValueEventListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            User user = dataSnapshot.getValue(User.class);
                                            Intent intent = new Intent(MainActivity.this,MyAccountActivity.class);
                                            intent.putExtra("userObject",user);
                                            intent.putExtra("phoneNumber",mFirebaseAuth.getCurrentUser().getPhoneNumber());
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    };
                                    userReference.addValueEventListener(userValueEventListener);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(MainActivity.this, "Database read ERROR!", Toast.LENGTH_SHORT).show();

                            }
                        };
                        dReference.addValueEventListener(valueEventListener);
                    }
                    else {
                        // not signed in
                        startActivityForResult(
                                AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setAvailableProviders(
                                                Arrays.asList(
                                                        new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()))
                                        .build(),
                                RC_SIGN_IN);
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mHospitalsTextView = (TextView) findViewById(R.id.main_hospitals);
        mHospitalsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetworkAvailable(getApplicationContext())){
                    Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(MainActivity.this,HospitalListActivity.class);
                    intent.putExtra("lat", UserLocationInfo.getUserLatitude());
                    intent.putExtra("long", UserLocationInfo.getUserLongitude());
                    startActivity(intent);
                }
            }
        });
        mFAQTextView = (TextView) findViewById(R.id.faq);
        mFAQTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FAQActivity.class));
            }
        });

    }

    private void showLocationNotSetDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Location is not set in your device\nPlease enable it by clicking on the location button")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setUpSpinner() {
        final ArrayAdapter bloodGroupSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_blood_groups, android.R.layout.simple_spinner_item);
        bloodGroupSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mBloodSearchSpinner.setAdapter(bloodGroupSpinnerAdapter);
        mRequestListSpinner.setAdapter(bloodGroupSpinnerAdapter);

        mBloodSearchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(valueEventListener != null){
            dReference.removeEventListener(valueEventListener);
        }
        if(userValueEventListener != null){
            userReference.removeEventListener(userValueEventListener);
        }

    }

    private boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){

                dReference = mDatabaseReference.child("accounts").child(mFirebaseAuth.getCurrentUser().getPhoneNumber());
                valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String  str = dataSnapshot.getValue(String.class);
                        if(str == null){
                            // new User , Create Activity
                            Intent intent = new Intent(MainActivity.this,CreateUserActivity.class);
                            intent.putExtra("phoneNumber",mFirebaseAuth.getCurrentUser().getPhoneNumber().toString());
                            startActivity(intent);
                        }
                        else{

                            //TODO: Old User Show MyAccountActivity
                            userReference = mDatabaseReference.child("users").child(str).child(mFirebaseAuth.getCurrentUser().getPhoneNumber().toString());
                            userValueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    Intent intent = new Intent(MainActivity.this,MyAccountActivity.class);
                                    intent.putExtra("userObject",user);
                                    intent.putExtra("phoneNumber",mFirebaseAuth.getCurrentUser().getPhoneNumber());
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            userReference.addValueEventListener(userValueEventListener);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "Database read ERROR!", Toast.LENGTH_SHORT).show();

                    }
                };
                dReference.addValueEventListener(valueEventListener);
            }
            else{
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == RC_SIGN_IN_REQUEST){
            Log.e("Utshaw", "Request Sign in 1234");
            if(resultCode == RESULT_OK){
                dReference = mDatabaseReference.child("requests_accounts").child(mFirebaseAuth.getCurrentUser().getPhoneNumber());

                valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String  str = dataSnapshot.getValue(String.class);
                        if(str == null){
                            Intent intent = new Intent(MainActivity.this,RequestActivity.class);
                            intent.putExtra("phoneNumber",mFirebaseAuth.getCurrentUser().getPhoneNumber().toString());
                            startActivity(intent);
                        }
                        else{
                            userReference = mDatabaseReference.child("requests").child(str).child(mFirebaseAuth.getCurrentUser().getPhoneNumber().toString());
                            userValueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    UserRequest user = dataSnapshot.getValue(UserRequest.class);
                                    Intent intent = new Intent(MainActivity.this,MyRequestActivity.class);
                                    intent.putExtra("requestObject",user);
                                    intent.putExtra("phoneNumber",mFirebaseAuth.getCurrentUser().getPhoneNumber());
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            userReference.addValueEventListener(userValueEventListener);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "Database read ERROR!", Toast.LENGTH_SHORT).show();

                    }
                };
                dReference.addValueEventListener(valueEventListener);
            }else{
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_main_location:
                startActivity(new Intent(MainActivity.this,LocationRefreshActivity.class));
                break;
            case R.id.action_main_chat:
                    startActivity(new Intent(MainActivity.this, ChatActivity.class));
//                Toast.makeText(this, "Just chat!!", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }
}
