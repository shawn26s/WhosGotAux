package com.example.shawn.whosgotaux;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.button.MaterialButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Fragment that represents the main screen where last user is recorded and displayed.
 */
public class TrackingFragment extends android.support.v4.app.Fragment
{
    private SharedPreferences sharedPref;
    private boolean lastPerson;
    private String lastPersonData;
    private de.hdodenhof.circleimageview.CircleImageView userImage;
    private MaterialButton recordUserButton;
    private TextView infoText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tracking, container, false);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        // Get the last saved user and the time/date for the last time they played music
        lastPerson = sharedPref.getBoolean(getResources().getString(R.string.LAST_PERSON), true);
        lastPersonData = sharedPref.getString(getResources().getString(R.string.LAST_PERSON_DATA), "");

        userImage = view.findViewById(R.id.user_image);
        infoText = view.findViewById(R.id.text);
        recordUserButton = view.findViewById(R.id.recordUser);
        recordUserButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SharedPreferences.Editor editor = sharedPref.edit();
                // switch the last person to have had their music played in the car.
                lastPerson = !lastPerson;
                editor.putBoolean(getResources().getString(R.string.LAST_PERSON), lastPerson);
                // get date and time for the last record;
                String cd = new SimpleDateFormat("yyyyMMdd_HHmmss")
                        .format(Calendar.getInstance().getTime());
                // Switch the string value for the last user's name
                String name = (lastPerson) ?
                        getResources().getString(R.string.user_shawn) : getResources().getString(R.string.user_katie);
                int hour = Integer.parseInt(cd.substring(9, 11));
                String amPM = (hour / 12 == 0) ? "AM" : "PM";
                hour %= 12;
                hour = (hour == 0) ? 12 : hour;
                String timeStamp = hour + ":" + cd.substring(11, 13) + " " + amPM
                        + " on " + cd.substring(4, 6) + "/" + cd.substring(6, 8);
                lastPersonData = name + " last played music at " + timeStamp;
                editor.putString(getResources().getString(R.string.LAST_PERSON_DATA), lastPersonData);
                editor.commit();
                updateTextAndImage();
            }
        });

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        // Updates the display with the most recent information
        updateTextAndImage();
    }

    /**
     * Sets the infoText with the most recent user and time played, and updates the userImage
     * with the corresponding user.
     */
    private void updateTextAndImage()
    {
        infoText.setText(lastPersonData);
        if (lastPerson) // If the last user was Shawn, show Katie's image for next user
        {
            userImage.setImageDrawable(getResources()
                    .getDrawable(R.drawable.profile_katie, getResources().newTheme()));
        }
        else // If the last user was Katie, show Shawn's image for next user
        {
            userImage.setImageDrawable(getResources()
                    .getDrawable(R.drawable.profile_shawn, getResources().newTheme()));
        }
    }
}
