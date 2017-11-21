package io.github.utshaw.blooddonor_v3;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Utshaw on 9/28/2017.
 */

public class UserDetails extends AppCompatActivity {

    private TextView nameTextView;
    private TextView bloodTextView;
    private TextView genderTextView;
    private TextView weightTextView;
    private TextView mobileTextView;
    private TextView addressTextView;
    private TextView dateTextView;
    private TextView disableNoticeTextView;
    String donorNumber;

    boolean ableDonor = false; // check if donor can donate blood as it is more than 4 months since he last donated blood

    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        nameTextView = (TextView) findViewById(R.id.detail_name);
        bloodTextView = (TextView) findViewById(R.id.detail_blood_group);
        genderTextView = (TextView) findViewById(R.id.detail_gender);
        weightTextView = (TextView) findViewById(R.id.detail_weight);
        mobileTextView = (TextView) findViewById(R.id.detail_mobile);
        addressTextView = (TextView) findViewById(R.id.detail_address);
        dateTextView = (TextView) findViewById(R.id.detail_date);
        disableNoticeTextView = (TextView) findViewById(R.id.disable_notice);


        user = (User)getIntent().getSerializableExtra("userObject");


        setTitle("Donor " + user.getmUsername());
        nameTextView.setText(user.getmUsername());
        bloodTextView.setText(user.getmBloodGroup());
        boolean genderValue = user.ismGender(); // true for Male, false for Female
        if(genderValue){
            genderTextView.setText("Male");
        }
        else{
            genderTextView.setText("Female");
        }
        weightTextView.setText(user.getmWeight() + " Kg");
        mobileTextView.setText(user.getmContactNumber());
        addressTextView.setText(user.getmStringLocation());


        Date date = new Date(user.getmDate());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        String strDate = dateFormatter.format(date);
        dateTextView.setText(strDate);


        long timeForDonation = Long.valueOf(user.getmDate()) + 10368000000L; // 10368000000L = 120 * 24 * 60 * 60 * 1000
        Log.e("Utshaw",user.getmDate() + "");
        Log.e("Utshaw",timeForDonation + "");
        Log.e("Utshaw",System.currentTimeMillis() + "");

        if(timeForDonation <= System.currentTimeMillis()){
            disableNoticeTextView.setText("This user is fit for donation");
            disableNoticeTextView.setTextColor(ContextCompat.getColor(this,R.color.able_donor));
            ableDonor = true;
        }else{
            ableDonor = false;
            disableNoticeTextView.setTextColor(ContextCompat.getColor(this,R.color.unable_donor));
            disableNoticeTextView.append(dateFormatter.format(new Date(timeForDonation)));
        }




        donorNumber = user.getmContactNumber();

//        long timeNow = System.currentTimeMillis();
//
//        int diffDayes = (int)(timeNow - user.getmDate()) / (1000*60*60*24);
//
//        if(diffDayes >= 120)
//            ableDonor = true;
//        else
//            ableDonor = false;



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact,menu);
        if(!ableDonor){

            MenuItem callItem = menu.findItem(R.id.action_call);
            callItem.setEnabled(false);
            Drawable callDrawable = callItem.getIcon();
            callDrawable.mutate();
            callDrawable.setAlpha(130);

            MenuItem messageItem =  menu.findItem(R.id.action_message);
            messageItem.setEnabled(false);
            Drawable messageDrawable = messageItem.getIcon();
            messageDrawable.mutate();
            messageDrawable.setAlpha(130);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_call:
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+donorNumber));
                startActivity(callIntent);
                break;
            case R.id.action_message:
                Intent messageIntent = new Intent(Intent.ACTION_VIEW);
                messageIntent.setType("vnd.android-dir/mms-sms");
                messageIntent.putExtra("address",user.getmContactNumber() );
                messageIntent.putExtra("sms_body","Hello! I'm in need of " + user.getmBloodGroup() + " blood. Can you help me ?");
                startActivity(messageIntent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
