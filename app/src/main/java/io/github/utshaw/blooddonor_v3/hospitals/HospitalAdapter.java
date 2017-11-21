package io.github.utshaw.blooddonor_v3.hospitals;

/**
 * Created by Utshaw on 9/29/2017.
 */

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.ArrayList;

import io.github.utshaw.blooddonor_v3.R;


/**
 * Created by utshaw on 6/29/17.
 */

public class HospitalAdapter extends ArrayAdapter<Hospital> {

    public HospitalAdapter(Activity context, ArrayList<Hospital> quakeArrayList)
    {
        super(context,0,quakeArrayList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Hospital currentHospital = getItem(position);



        TextView hospitalLocation = (TextView) listItemView.findViewById(R.id.list_location);
        hospitalLocation.setText(currentHospital.getVicinity());

        TextView hospitalName = (TextView)listItemView.findViewById(R.id.list_name);
        hospitalName.setText(currentHospital.getmName());


        TextView distanceTextView = (TextView) listItemView.findViewById(R.id.list_distance);

        GradientDrawable distanceCircle = (GradientDrawable) distanceTextView.getBackground();

        double distanceFromUser = currentHospital.getDistanceFromCurrentLocation()/1000;
        int magnitudeColor = getMagnitudeColor(distanceFromUser);
        distanceCircle.setColor(magnitudeColor);

        String formattedMagnitude = formatMagnitude(distanceFromUser);
        distanceTextView.setText(formattedMagnitude+"\nKM");


        return listItemView;
    }

    private int getMagnitudeColor(double distance) {

        int distanceColorResourceId;
        int distanceFloor = (int) Math.floor(distance);
        switch (distanceFloor)
        {
            case 0:
                distanceColorResourceId = R.color.distance1;
                break;
            case 1:
                distanceColorResourceId = R.color.distance2;
                break;
            case 2:
                distanceColorResourceId = R.color.distance3;
                break;
            case 3:
                distanceColorResourceId = R.color.distance4;
                break;
            case 4:
                distanceColorResourceId = R.color.distance5;
                break;
            case 5:
                distanceColorResourceId = R.color.distance6;
                break;
            case 6:
                distanceColorResourceId = R.color.distance7;
                break;
            case 7:
                distanceColorResourceId = R.color.distance8;
                break;
            case 8:
                distanceColorResourceId = R.color.distance9;
                break;
            default:
                distanceColorResourceId = R.color.distance10plus;
                break;
        }

        return ContextCompat.getColor(getContext(),distanceColorResourceId);
    }



    private String formatMagnitude(double magnitude)
    {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.00");
        return magnitudeFormat.format(magnitude);
    }
}