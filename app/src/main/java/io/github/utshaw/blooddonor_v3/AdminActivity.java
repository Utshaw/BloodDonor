package io.github.utshaw.blooddonor_v3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Utshaw on 10/13/2017.
 */

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_call:
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+getResources().getString(R.string.admin_phone)));
                startActivity(callIntent);
                break;
            case R.id.action_message:
                Intent messageIntent = new Intent(Intent.ACTION_VIEW);
                messageIntent.setType("vnd.android-dir/mms-sms");
                messageIntent.putExtra("address",getResources().getString(R.string.admin_phone) );
                startActivity(messageIntent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
