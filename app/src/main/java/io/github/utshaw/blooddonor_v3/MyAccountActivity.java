package io.github.utshaw.blooddonor_v3;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Utshaw on 10/1/2017.
 */

public class MyAccountActivity extends AppCompatActivity {


    String authNumber ;

    private TextView nameTextView;
    private TextView bloodTextView;
    private TextView genderTextView;
    private TextView weightTextView;
    private TextView mobileTextView;
    private TextView addressTextView;
    private TextView dateTextView;
    private TextView statusTextView;
    
    private String bloodGroup;

    private User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        nameTextView = (TextView) findViewById(R.id.detail_name);
        bloodTextView = (TextView) findViewById(R.id.detail_blood_group);
        genderTextView = (TextView) findViewById(R.id.detail_gender);
        weightTextView = (TextView) findViewById(R.id.detail_weight);
        mobileTextView = (TextView) findViewById(R.id.detail_mobile);
        addressTextView = (TextView) findViewById(R.id.detail_address);
        dateTextView = (TextView) findViewById(R.id.detail_date);
        statusTextView = (TextView) findViewById(R.id.detail_status);

        mUser = (User) getIntent().getSerializableExtra("userObject");
        authNumber = getIntent().getStringExtra("phoneNumber");

        ((TextView)(findViewById(R.id.detail_acc_id))).setText(authNumber);

        nameTextView.setText(mUser.getmUsername());
        bloodTextView.setText(mUser.getmBloodGroup());
        bloodGroup = mUser.getmBloodGroup();
        boolean genderValue = mUser.ismGender(); // true for Male, false for Female
        if(genderValue){
            genderTextView.setText("Male");
        }
        else{
            genderTextView.setText("Female");
        }
        weightTextView.setText(mUser.getmWeight() + " Kg");
        mobileTextView.setText(mUser.getmContactNumber());
        addressTextView.setText(mUser.getmStringLocation());


        Date date = new Date(mUser.getmDate());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        String strDate = dateFormatter.format(date);
        dateTextView.setText(strDate);


        if(mUser.isActiveDonor()){
            statusTextView.setText("Active");
        }else{
            statusTextView.setText("Disabled");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit:
                Intent intent = new Intent(MyAccountActivity.this,EditAccountActivity.class);
                intent.putExtra("userObject",mUser);
                startActivity(intent);
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
            case R.id.action_delete:
                if(isNetworkAvailable(getApplicationContext())) {
                    FirebaseDatabase.getInstance().getReference().child("accounts").child(authNumber).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("users").child(bloodGroup).child(authNumber).removeValue();


                    FirebaseDatabase.getInstance().getReference().child("user_tokens").child(bloodGroup).child(authNumber).removeValue();
                    Toast.makeText(this, "Account deleted!!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(this, "Internet is not available", Toast.LENGTH_SHORT).show();
                }
                
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_myacc_details,menu);
        return true;
    }
}
