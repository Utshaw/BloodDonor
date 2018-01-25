package io.github.utshaw.blooddonor_v3;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by lenovo on 11/17/2017.
 */

public class appIntro extends AppIntro
{
    public Boolean first;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        addSlide(AppIntroFragment.newInstance("Sign Up Process","Create a new donor account in less than 10 seconds!",R.drawable.intro_sign_in, ContextCompat.getColor(getApplicationContext(), R.color.Red1)));
        addSlide(AppIntroFragment.newInstance("Nearby Hospitals!","Hospital list is sorted according to distances!",R.drawable.intro_nearby_hospitals, ContextCompat.getColor(getApplicationContext(), R.color.blue1)));
        addSlide(AppIntroFragment.newInstance("Get Notified!","Get notification for blood request if you are a donor!",R.drawable.intro_notification, ContextCompat.getColor(getApplicationContext(), R.color.green1)));
        addSlide(AppIntroFragment.newInstance("Account Deactivation For Donor","Deactivated donor accounts are not found in search list!",R.drawable.intro_account_deactivate, ContextCompat.getColor(getApplicationContext(), R.color.amber11)));
        addSlide(AppIntroFragment.newInstance("Chat with other people!","Get help as a requester and give help as a donor easily!",R.drawable.intro_chat, ContextCompat.getColor(getApplicationContext(), R.color.purple1)));


//        setBarColor(Color.parseColor("#d50000"));
//        setSeparatorColor(Color.parseColor("#2196F3"));
        setFlowAnimation();
        showSkipButton(true);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment)
    {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.

        SharedPreferences sharedPreferences=getSharedPreferences("first", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("first",false);
        editor.apply();

        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment)
    {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.


        SharedPreferences sharedPreferences=getSharedPreferences("first", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("first",false);
        editor.apply();

        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment)
    {
        if(oldFragment != null && newFragment != null){
            Log.e("Utshaw",oldFragment.getActivity() + "   Old frag     " + oldFragment.toString());
            Log.e("Utshaw",newFragment.getActivity() + "   new frag          "  +newFragment.toString());
        }



        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

}
