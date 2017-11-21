package io.github.utshaw.blooddonor_v3.use_request;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.text.DecimalFormat;
import java.util.List;

import io.github.utshaw.blooddonor_v3.R;
import io.github.utshaw.blooddonor_v3.UserLocationInfo;

/**
 * Created by Utshaw on 9/30/2017.
 */

public class UserRequestAdapter extends ArrayAdapter<UserRequest> {

    Context context;

    public UserRequestAdapter(Context context, int resource, List<UserRequest> objects){
        super(context, resource, objects);
        this.context = context;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_item, parent, false);
        }
        TextView locationTextView = (TextView) convertView.findViewById(R.id.list_location);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.list_name);
        TextView distanceTextView = (TextView) convertView.findViewById(R.id.list_distance);

        UserRequest currentUser = getItem(position);

        String addressString =  currentUser.getmAddress();
        GradientDrawable distanceCircle = (GradientDrawable) distanceTextView.getBackground();

        locationTextView.setText(addressString);
        if(currentUser.getmLatitude() != UserLocationInfo.BAD_VALUE && UserLocationInfo.getUserLatitude() != UserLocationInfo.BAD_VALUE){

            float[] results = new float[10];
            android.location.Location.distanceBetween(UserLocationInfo.getUserLatitude(), UserLocationInfo.getUserLongitude(),currentUser.getmLatitude(), currentUser.getmLongitude(),results);
            double doubleDistance = results[0]/1000;
            String distance = formatDistance(doubleDistance);
            distanceTextView.setText(distance + "\nKM");


            int magnitudeColor = getMagnitudeColor(doubleDistance);
            distanceCircle.setColor(magnitudeColor);

        }
        else{
            distanceTextView.setText("N/A");
            distanceCircle.setColor(ContextCompat.getColor(getContext(),R.color.distance10plus));
        }

        nameTextView.setText(currentUser.getmName());

        return convertView;
    }

    private String formatDistance(double magnitude)
    {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.00");
        return magnitudeFormat.format(magnitude);
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


}