package mynearbymaps.chanthanhandroid.com.mynearbymaps;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qhcth on 18-Feb-16.
 */
public class Photo {
    public static final String urlRequest = "https://maps.googleapis.com/maps/api/place/photo?";

    public String getPhoto_reference() {
        return photo_reference;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    private String photo_reference ;
    private double height;
    private double width;

    public Photo()
    {
        photo_reference = "";
        height = width = 0;
    }
    public Photo(JSONObject jsonObject)
    {
        if(jsonObject !=null)
        {
            try {
                this.photo_reference =  jsonObject.getString("photo_reference");
                this.height =  jsonObject.getDouble("height");
                this.width = jsonObject.getDouble("width");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String GetStringUrlLoadPhoto()
    {
        String url = urlRequest;
        if(!photo_reference.equals("") && width != 0 && height != 0)
        {
            url += "maxwidth=" + (int)width +"&"+
                    "maxheight=" + (int)height +"&"+
                    "photoreference="+ photo_reference+"&"+
                    "key=" + MapsActivity.GOOGLE_API_KEY;
        }
        return  url;
    }

}
