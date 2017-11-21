package io.github.utshaw.blooddonor_v3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Created by Utshaw on 9/30/2017.
 */

public class FAQActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.d("FFFFF");
    }
}
