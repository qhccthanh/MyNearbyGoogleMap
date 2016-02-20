package mynearbymaps.chanthanhandroid.com.mynearbymaps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by qhcth on 15-Feb-16.
 */
public class ListPlaces {
    static public ArrayList<Places> listPlace;
    static public LatLng currentLocation = new LatLng(10.772890,106.648321);
    private ListPlaces()
    {

    }
    static  public ArrayList<Places> getListPlace()
    {
        if(listPlace==null)
            listPlace = new ArrayList<>();
        return  listPlace;
    }
    static public ArrayList<Places> parse(JSONArray jsonArray)
    {
        if(listPlace == null)
            listPlace = new ArrayList<>();
        listPlace.clear();

        for(int i =0 ;i<jsonArray.length();i++)
        {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Places newPlace = getPlaces(jsonObject);
            listPlace.add(newPlace);
        }

        return listPlace;
    }
    static public Places getPlaces(JSONObject placeJson)
    {
        Places places = new Places();
        try
        {
            if (!placeJson.isNull("name")) {
                places.setName(placeJson.getString("name"));
            }
            if (!placeJson.isNull("vicinity")) {
                places.setVicinity(placeJson.getString("vicinity"));
            }
            String lat = placeJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            String lng = placeJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            places.setGeometry(
                    new Geometry(Double.parseDouble(lat), Double.parseDouble(lng))
            );
            places.setId(placeJson.getString("id"));
            places.setPlace_id(placeJson.getString("place_id"));

            LatLng from = new LatLng(places.getGeometry().getLatitue(),places.getGeometry().getLongtitue());
            double distance = SphericalUtil.computeDistanceBetween(ListPlaces.currentLocation,from);

            places.setDistance(distance);

            if(!placeJson.isNull("rating"))
            {
                places.setRating(placeJson.getInt("rating"));
            }

            if(!placeJson.isNull("icon"))
            {
                ListIcon listIcon = ListIcon.GetListIcon();
                String url = placeJson.getString("icon");
                if(listIcon.IsHave(url))
                {
                    // get bitmap
                    places.setIcon(listIcon.GetIcon(url));
                }
                else
                {

                    URL urlLink = new URL(url);
                    Bitmap bmp = BitmapFactory.decodeStream(urlLink.openConnection().getInputStream());
                    Icon iconTemp = new Icon(url,bmp);
                    places.setIcon(iconTemp);
                    listIcon.AddIconToDatabase(iconTemp);
                    //photoReadTask.get();
                    // dowload bitmap
                }
            }
            if(!placeJson.isNull("photos"))
            {
                JSONArray jsonPhotos = placeJson.getJSONArray("photos");
                if(jsonPhotos.length() > 0) {
                    JSONObject jsonPhoto = jsonPhotos.getJSONObject(0);
                    Photo photo = new Photo();
                    photo.setPhoto_reference(jsonPhoto.getString("photo_reference"));
                    photo.setHeight(jsonPhoto.getInt("height"));
                    photo.setWidth(jsonPhoto.getInt("width"));
                    places.setPhoto(photo);
                }
            }

        }
        catch(JSONException e){
        }
        finally {
            return places;
        }
    }
}
