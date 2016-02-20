package mynearbymaps.chanthanhandroid.com.mynearbymaps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by qhcth on 12-Feb-16.
 */
public class ListView_PlaceNearby extends ArrayAdapter<Places>{

    static public Context keyContext = null;
    private ArrayList<Places> arrayList;
    public ListView_PlaceNearby(Context context, int resource, ArrayList<Places> objects) {
        super(context, resource, objects);
        arrayList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v==null)
        {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.activity_listview_nearbyplace,null);
        }
        final Places place = getItem(position);
        if(place!=null)
        {
            TextView tv_Name = (TextView)v.findViewById(R.id.tv_Name);
            TextView tv_Vicitiny = (TextView)v.findViewById(R.id.tv_vicitiny);
            TextView tv_Distance = (TextView)v.findViewById(R.id.tv_Distance);
            ImageView img = (ImageView)v.findViewById(R.id.imageView);


            tv_Name.setText(place.getName());
            tv_Vicitiny.setText(place.getVicinity());
            if(place.getIcon() != null)
                img.setImageBitmap(place.getIcon().bmp);

            double distance = place.getDistance();
            tv_Distance.setText(ProcessDistance(distance));

        }
        return v;
    }
    View.OnClickListener yourClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //put your desired action here
            v.callOnClick();
        }
    };
    private String ProcessDistance(double d)
    {
        String result = "";
        d = Math.round(d);
        if(d>=1000)
        {
            d/=1000;
            result = Double.toString(d) + "km";
        }
        else
        {
            result = Double.toString(d) + "m";
        }
        return  result;
    }
    public void updateResults(ArrayList<Places> results) {
        arrayList = results;
        //Triggers the list update
        notifyDataSetChanged();
    }
}
