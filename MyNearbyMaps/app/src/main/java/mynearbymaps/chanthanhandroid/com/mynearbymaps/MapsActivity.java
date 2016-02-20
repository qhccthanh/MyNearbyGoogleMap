package mynearbymaps.chanthanhandroid.com.mynearbymaps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.ui.IconGenerator;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity
        implements LocationListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationChangeListener,
        OnMapReadyCallback,
        SwipeRefreshLayout.OnRefreshListener
{
     //private static final String GOOGLE_API_KEY = "AIzaSyClI0fUYf_ASmMkAzb2PHQDndBWyW0CZNU";
    public static final String GOOGLE_API_KEY = "AIzaSyDQ3PFhRCFb-cxOW7sqFveOnp-HfuftHb0";

    GoogleMap googleMap;

    //10.772901, 106.648314
    LatLng currentLocation = ListPlaces.currentLocation;

    private int PROXIMITY_RADIUS = 2000;

    private GoogleMap mMap;
    private Spinner spinner;
    private ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;

    IconGenerator iconGenerator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_maps);

        SQLite.context = this;
        ListView_PlaceNearby.keyContext = getApplicationContext();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        fragment.getMapAsync(this);

        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        CreateTypesPlace();
        spinner = (Spinner)findViewById(R.id.spinner);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout) ;


       // spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 findPlaces();
            }
            @Override

            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        listView = (ListView)findViewById(R.id.listView);
        ListAdapter adapter = new ListView_PlaceNearby(this,R.layout.activity_listview_nearbyplace,ListPlaces.getListPlace());
        listView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MapsActivity.this, Details_Place.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });


        googleMap = fragment.getMap();
        SetupLocationService();


    }
    private  void SetupLocationService() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
         Location location = locationManager.getLastKnownLocation(bestProvider);
        if(location !=null)
        {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
    }
    public boolean isInternetOn() {

        Context mContext = getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else {
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
    private void findPlaces()
    {
        if(spinner.getSelectedItem() == null)
            return;

        if(!isInternetOn()) return;
        try {
            String type = spinner.getSelectedItem().toString();
            type = types_Place.get(type);
            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            googlePlacesUrl.append("location=" + currentLocation.latitude + "," + currentLocation.longitude);
            googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
            googlePlacesUrl.append("&types=" + type);
            googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);

            final GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
            Object[] toPass = new Object[3];
            toPass[0] = googleMap;
            toPass[1] = googlePlacesUrl.toString();
            toPass[2] = listView;
            googlePlacesReadTask.execute(toPass);

            //Add my location

        }catch (Exception e)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("The Internet is not connected. Please try again!");
            builder.setPositiveButton("OKE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }finally {

        }
    }

    public void computerDistanceList()
    {
        for (Places places: ListPlaces.listPlace
             ) {
            LatLng to    = currentLocation;
            LatLng from = new LatLng(places.getGeometry().getLatitue(),places.getGeometry().getLongtitue());
            double distance = SphericalUtil.computeDistanceBetween(from,to);
            places.setDistance(distance);
        }
    }


    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }




    @Override
    public void onMapReady(GoogleMap googleMaps) {
        googleMap = googleMaps;
        //Add market

       iconGenerator = new IconGenerator(this);

        iconGenerator.setStyle(IconGenerator.STYLE_GREEN);

        iconGenerator.setContentRotation(90);

        GooglePlacesDisplayTask.iconFactory = iconGenerator;

        Bitmap icon = iconGenerator.makeIcon("My Home");

        googleMap.addMarker(new MarkerOptions().
                position(currentLocation).
                title("This is my home").
                icon(BitmapDescriptorFactory.fromBitmap(icon)));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        enableMyLocation();
        googleMap.setOnMyLocationChangeListener(this);
    }
    private void enableMyLocation() {
        // Permission to access the location is missing.

        if (googleMap != null) {
            // Access to the location has been granted to the app.
            googleMap.setMyLocationEnabled(true);
        }
    }
    @Override
    public boolean onMyLocationButtonClick() {

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        return false;
    }

    @Override
    public void onMyLocationChange(Location location) {
        currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
    }

    @Override
    public void onLocationChanged(Location location) {
       currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
       googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    private HashMap<String,String> types_Place = new HashMap<>();
    private void CreateTypesPlace()
    {
        types_Place.put("Cảnh sát","police|post_office");
        types_Place.put("Công viên","park|rv_park");
        types_Place.put("Rạp","movie_rental|movie_theater|moving_company");
        types_Place.put("Thư viện","library|book_store");
        types_Place.put("Bệnh viện","hospital");
        types_Place.put("Bác sĩ","doctor");
        types_Place.put("Trung tâm mua sắm","electronics_store");
        types_Place.put("Thức ăn","food|bakery|restaurant" );
        types_Place.put("Cửa hàng tiện ích","convenience_store");
        types_Place.put("ATM","atm");
        types_Place.put("Ngân hàng","bank");
        types_Place.put("Tiệm Bánh","bakery");
        types_Place.put("Bar","bar");
        types_Place.put("Cafe","cafe");
        types_Place.put("Trạm xe buýt","bus_station|gas_station|train_station");
        types_Place.put("Nhà thờ","church");
        types_Place.put("Nhà hàng","restaurant");
        types_Place.put("Trường","school");
        types_Place.put("Cửa hàng","Store|shopping_mall|bakery");
        types_Place.put("Spa","spa");
        types_Place.put("Sân vận động","stadium");
        types_Place.put("Trường đại học","school|university");
        types_Place.put("Gym","gym");
        types_Place.put("Shopping","shopping_mall");


    }

    @Override
    public void onRefresh() {
        findPlaces();
        swipeRefreshLayout.setRefreshing(false);
    }

}
