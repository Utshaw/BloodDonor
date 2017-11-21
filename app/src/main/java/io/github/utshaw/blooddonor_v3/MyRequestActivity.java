package io.github.utshaw.blooddonor_v3;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.github.utshaw.blooddonor_v3.use_request.UserRequest;

/**
 * Created by Utshaw on 11/18/2017.
 */

public class MyRequestActivity extends AppCompatActivity {


    String authNumber ;

    private TextView nameTextView;
    private TextView bloodTextView;
    private TextView mobileTextView;
    private TextView addressTextView;

    private String bloodGroup;

    private UserRequest mRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_request);

        nameTextView = (TextView) findViewById(R.id.detail_name);
        bloodTextView = (TextView) findViewById(R.id.detail_blood_group);
        mobileTextView = (TextView) findViewById(R.id.detail_mobile);
        addressTextView = (TextView) findViewById(R.id.detail_address);

        mRequest = (UserRequest) getIntent().getSerializableExtra("requestObject");
        authNumber = getIntent().getStringExtra("phoneNumber");

        nameTextView.setText(mRequest.getmName());
        bloodTextView.setText(mRequest.getmBloodGroup());
        bloodGroup = mRequest.getmBloodGroup();
        mobileTextView.setText(mRequest.getmPhoneNumber());
        addressTextView.setText(mRequest.getmAddress());

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
            case R.id.action_delete:
                if(isNetworkAvailable(getApplicationContext())) {
                    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                    mDatabaseReference.child("requests_accounts").child(authNumber).removeValue();
                    mDatabaseReference.child("requests").child(bloodGroup).child(authNumber).removeValue();
                    Toast.makeText(this, "Request deleted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(this, "Internet is not available", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_request,menu);
        return true;
    }

    private boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
