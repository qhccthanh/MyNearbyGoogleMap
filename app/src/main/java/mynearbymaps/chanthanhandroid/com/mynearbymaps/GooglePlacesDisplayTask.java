package mynearbymaps.chanthanhandroid.com.mynearbymaps;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by qhcth on 15-Feb-16.
 */
public class GooglePlacesDisplayTask extends AsyncTask<Object,Integer,ArrayList<Places>> {

    GoogleMap googleMap;
    ListView listView;
    static public IconGenerator iconFactory = null;
    @Override
    protected ArrayList<Places> doInBackground(Object... params) {

        googleMap = (GoogleMap)params[0];
        listView = (ListView)params[1];
        ArrayList listPlaces = null;
        JSONArray jsonArray = null;

        try {
            JSONObject jsonObject = new JSONObject((String)params[2]);
            jsonArray = jsonObject.getJSONArray("results");

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return ListPlaces.parse(jsonArray);
    }

    @Override
    protected void onPostExecute(ArrayList<Places> arrayList) {
        googleMap.clear();
        if(arrayList == null)
            return;
        for (Places places: arrayList
             ) {
            MarkerOptions markerOptions = new MarkerOptions();

            Bitmap icon = iconFactory.makeIcon(ProcessStringIcon(places.getName()));

            LatLng latLng = new LatLng(places.getGeometry().getLatitue(), places.getGeometry().getLongtitue());

            markerOptions.position(latLng);
            markerOptions.title(places.getName() + " : " + places.getVicinity());
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

            googleMap.addMarker(markerOptions);
        }
        iconFactory.setStyle(IconGenerator.STYLE_BLUE);
        Bitmap icon = iconFactory.makeIcon("Your Location");
        iconFactory.setStyle(IconGenerator.STYLE_GREEN);


        googleMap.addMarker(new MarkerOptions().
                position(ListPlaces.currentLocation).
                title("This is my home").
                icon(BitmapDescriptorFactory.fromBitmap(icon)));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ListPlaces.currentLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        ((ListView_PlaceNearby)listView.getAdapter()).updateResults(arrayList);

    }
    private String ProcessStringIcon(String placeName)
    {
        String newName;
        if(placeName.length()<18)
            newName = placeName;
        else
        {
            StringBuffer text = new StringBuffer(placeName);
            text.replace(12,text.length(),"...");
            newName = text.toString();
        }
        return  newName;
    }
}
