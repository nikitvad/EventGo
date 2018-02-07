package com.ghteam.eventgo.util;

import android.util.Log;

import com.facebook.GraphResponse;
import com.ghteam.eventgo.data.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nikit on 27.11.2017.
 */

public class FacebookUserJsonConverter {
    private static final String TAG = FacebookUserJsonConverter.class.getSimpleName();

    public static User getUser(GraphResponse graphResponse) {
        String firstName;
        String lastName;
        String pictureUrl;

        User result = new User();
        if (graphResponse.getError() == null) {
            JSONObject response = graphResponse.getJSONObject();
            Log.d(TAG, "getOwner: " + response.toString());
            try {
                firstName = response.getString("first_name");
                lastName = response.getString("last_name");
                pictureUrl = response.getJSONObject("picture").getJSONObject("data").getString("url");

                result.setFirstName(firstName);
                result.setLastName(lastName);
                result.setProfileImageUrl(pictureUrl);

                return result;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
