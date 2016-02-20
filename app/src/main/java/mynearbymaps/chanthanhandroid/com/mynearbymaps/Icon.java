package mynearbymaps.chanthanhandroid.com.mynearbymaps;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by qhcth on 12-Feb-16.
 */
public class Icon {
    public String url;
    public Bitmap bmp;
    static public Context context;
    public Icon(){}

    public Icon(String url, Bitmap bmp) {
        this.url = url;
        this.bmp = bmp;
    }
}
