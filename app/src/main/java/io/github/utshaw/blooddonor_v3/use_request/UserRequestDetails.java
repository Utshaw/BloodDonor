package io.github.utshaw.blooddonor_v3.use_request;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.github.utshaw.blooddonor_v3.R;


/**
 * Created by Utshaw on 9/30/2017.
 */

public class UserRequestDetails  extends AppCompatActivity{
    private TextView nameTextView;
    private TextView bloodTextView;
    private TextView mobileTextView;
    private TextView addressTextView;
    private LinearLayout weightContainerLView;
    private LinearLayout genderContainerLView;
    String donorNumber;

    UserRequest user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        nameTextView = (TextView) findViewById(R.id.detail_name);
        bloodTextView = (TextView) findViewById(R.id.detail_blood_group);
        mobileTextView = (TextView) findViewById(R.id.detail_mobile);
        addressTextView = (TextView) findViewById(R.id.detail_address);
        weightContainerLView = (LinearLayout) findViewById(R.id.details_container_weight);
        genderContainerLView = (LinearLayout) findViewById(R.id.details_container_gender);


        user = (UserRequest) getIntent().getSerializableExtra("userObject");

        setTitle("Request From " + user.getmName());
        nameTextView.setText(user.getmName());
        bloodTextView.setText(user.getmBloodGroup());
        genderContainerLView.setVisibility(LinearLayout.GONE);
        weightContainerLView.setVisibility(LinearLayout.GONE);

        mobileTextView.setText(user.getmPhoneNumber());
        addressTextView.setText(user.getmAddress());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact,menu);
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
                messageIntent.putExtra("address",user.getmPhoneNumber() );
                messageIntent.putExtra("sms_body","Hello! I'm interested in donating " + user.getmBloodGroup() + " blood. ");
                startActivity(messageIntent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
