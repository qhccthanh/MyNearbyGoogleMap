package mynearbymaps.chanthanhandroid.com.mynearbymaps;

public class Geometry
{
    public Geometry()
    {
        this.longtitue = 0;
        this.latitue =0;
    }
    public Geometry(double Latitue,double Longtitue)
    {
        this.latitue =Latitue;
        this.longtitue = Longtitue;
    }
    public double getLatitue() {
        return latitue;
    }

    public void setLatitue(double latitue) {
        this.latitue = latitue;
    }

    public double getLongtitue() {
        return longtitue;
    }

    public void setLongtitue(double longtitue) {
        this.longtitue = longtitue;
    }

    private double latitue;
    private  double longtitue;
}
