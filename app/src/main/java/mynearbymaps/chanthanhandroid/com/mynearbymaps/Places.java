package mynearbymaps.chanthanhandroid.com.mynearbymaps;

/**
 * Created by qhcth on 04-Feb-16.
 */

import android.graphics.Bitmap;

public class Places {

    static  public  ListIcon listIcon = null;
    public Places(){
        this.icon = null;
        this.id = null;
        this.name = null;
        this.place_id = null;
        this.rating = 0;
        this.vicinity = null;
        this.distance = 0;
        photo = null;
    }
    public Places(Icon icon, String id, String name, String place_id, int rating, String vicinity) {
        this.icon = icon;
        this.id = id;
        this.name = name;
        this.place_id = place_id;
        this.rating = rating;
        this.vicinity = vicinity;
    }








    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    private Icon icon;
    private Bitmap image;
    private String id;
    private String name;
    private String place_id;
    private  int rating;
    private String vicinity;
    private Geometry geometry;

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    private  Photo photo;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    private double distance = 0;

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }




    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


}
