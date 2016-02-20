package mynearbymaps.chanthanhandroid.com.mynearbymaps;

/**
 * Created by qhcth on 04-Feb-16.
 */
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;

public class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {
    String googlePlacesData = null;
    GoogleMap googleMap;
    ListView listView;
    @Override
    protected String doInBackground(Object... inputObj) {
        try {
            googleMap = (GoogleMap) inputObj[0];
            String googlePlacesUrl = (String) inputObj[1];
            listView = (ListView)inputObj[2];
            Http http = new Http();
            googlePlacesData = http.read(googlePlacesUrl);
        } catch (Exception e) {
            Log.d("Google Place Read Task", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        if(result == null)
            return;
        GooglePlacesDisplayTask googlePlacesDisplayTask = new GooglePlacesDisplayTask();
        Object[] toPass = new Object[3];
        toPass[0] = googleMap;
        toPass[1] = listView;
        toPass[2] = result;
        // parse result

        googlePlacesDisplayTask.execute(toPass);
    }
}