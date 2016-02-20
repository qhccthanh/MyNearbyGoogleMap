package mynearbymaps.chanthanhandroid.com.mynearbymaps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.net.URL;

public class Details_Place extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView tv_Name,tv_Victinity,tv_Distance;
    private ImageView imgV_Photo;
    private ImageView[] imgV_Stars = new ImageView[5];
    private LatLng startLatlng;
    private Places places;
    private LatLng currentLatlng = ListPlaces.currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_place);
        //Load element
        tv_Name = (TextView)findViewById(R.id.tv_details_Name);
        tv_Victinity = (TextView)findViewById(R.id.tv_details_Vicitiny);
        tv_Distance = (TextView)findViewById(R.id.tv_details_Distance);
        imgV_Photo = (ImageView)findViewById(R.id.image_Photo);

        imgV_Stars[0] = (ImageView)findViewById(R.id.star_01);
        imgV_Stars[1] = (ImageView)findViewById(R.id.star_02);
        imgV_Stars[2] = (ImageView)findViewById(R.id.star_03);
        imgV_Stars[3] = (ImageView)findViewById(R.id.star_04);
        imgV_Stars[4] = (ImageView)findViewById(R.id.star_05);

        // update value
        Bundle bd = getIntent().getExtras();
        int position = bd.getInt("position");
        try{
            places = ListPlaces.listPlace.get(position);
            if(places != null)
            {
                tv_Name.setText(places.getName());
                tv_Victinity.setText(places.getVicinity());
                tv_Distance.setText(Double.toString(Math.round(places.getDistance())) + "m");

                for(int i =0;i<places.getRating();i++)
                {
                    imgV_Stars[i].setImageResource(R.drawable.starlight);
                }


                Geometry geometry = places.getGeometry();
                startLatlng = new LatLng(geometry.getLatitue(),geometry.getLongtitue());

                Photo photo = places.getPhoto();
                if(photo !=null) {
                    final String urlRequest = photo.GetStringUrlLoadPhoto();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            (new LoadPhoto_GoogleMap()).execute(urlRequest);
                        }
                    });

                }else {
                    imgV_Photo.setImageBitmap(places.getIcon().bmp);
                }

            }
            else
            {
                finishActivity(0);
            }
        }catch (Exception e)
        {
            finishActivity(0);
            return;
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        IconGenerator iconGenerator = new IconGenerator(getApplicationContext());
        iconGenerator.setStyle(IconGenerator.STYLE_BLUE);
        Bitmap iconMyLocation = iconGenerator.makeIcon("My Location");
        iconGenerator.setStyle(IconGenerator.STYLE_RED);
        Bitmap iconDestination = iconGenerator.makeIcon(places.getName());

        mMap.addMarker(new MarkerOptions().position(startLatlng).title(places.getName()).icon(BitmapDescriptorFactory.fromBitmap(iconDestination)));
        mMap.addMarker(new MarkerOptions().position(currentLatlng).title("My Location").icon(BitmapDescriptorFactory.fromBitmap(iconMyLocation)));


        LatLng center = new LatLng((startLatlng.latitude+currentLatlng.latitude)/2,(startLatlng.longitude+currentLatlng.longitude)/2);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));

        int zoom = 15;
        for(int i = (int)places.getDistance();i>=700;i = i / 700)
        {
            zoom--;
        }
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));

    }

    public class LoadPhoto_GoogleMap extends AsyncTask<String,Integer,Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bmp = null;
            String request_URL = params[0];
            try {
                URL url = new URL(request_URL);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap !=null)
            {
                imgV_Photo.setImageBitmap(bitmap);
            }
        }
    }

}
