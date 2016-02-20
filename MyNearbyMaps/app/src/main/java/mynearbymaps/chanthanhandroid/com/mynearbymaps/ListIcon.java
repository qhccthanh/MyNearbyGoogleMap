package mynearbymaps.chanthanhandroid.com.mynearbymaps;

import java.util.ArrayList;

/**
 * Created by qhcth on 13-Feb-16.
 */
public class ListIcon {
    private static SQLite sqLite = null;
    public static ArrayList<Icon> listIcon = new ArrayList<Icon>();
    private  ListIcon()
    {
        sqLite = SQLite.get_instace();
        listIcon = sqLite.getIcon();
    }
    private  static  ListIcon _instance;
    static  public  ListIcon GetListIcon()
    {
        if(_instance==null)
        {
            _instance = new ListIcon();
        }
        return _instance;
    }
    public  boolean IsHave(String url)
    {
        for (Icon icon:listIcon) {
            if(icon.url.equals(url))
            return true;
        }
        return  false;
    }

    public  Icon GetIcon(String url)
    {
        for (Icon icon: listIcon
             ) {
            if(icon.url.equals(url))
            {
                return  icon;
            }
        }
        return  null;
    }
    public  void AddIconToDatabase(Icon icon)
    {
        listIcon.add(icon);
        sqLite.insertIcon(icon);
    }

}
