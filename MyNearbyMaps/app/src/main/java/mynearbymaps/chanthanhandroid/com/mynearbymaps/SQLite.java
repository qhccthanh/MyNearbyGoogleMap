package mynearbymaps.chanthanhandroid.com.mynearbymaps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by qhcth on 13-Feb-16.
 */
public class SQLite extends SQLiteOpenHelper {

    public static Context context = null;
    private static SQLite _instace = null;
    static public SQLite get_instace()
    {
        if(_instace == null)
            _instace = new SQLite(context);
        return _instace;
    }
    private SQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
        static private SQLite _instance = null;
        SQLiteDatabase sqliteDatabase =null;

        private  SQLite(){
            sqliteDatabase = SQLiteDatabase.openOrCreateDatabase("placemap", null);
            sqliteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ICON(URL TEXT,IMG IMAGE);");

    }
        static public  SQLite GetSQLite()
        {
            if(_instance==null)
                _instance = new SQLite();
            return  _instance;
        }
        */
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "google_placesmap";

    //  table name
    private static final String TABLE_ICON = "icon";
    // key
    private static final String KEY_URL = "URL";
    private static final String KEY_BITMAP = "bmp";


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_ICON + "("
                + KEY_URL + " String PRIMARY KEY," + KEY_BITMAP + " blob"
                 + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ICON);

        // Create tables again
        onCreate(db);
    }

    public ArrayList<Icon> getIcon()
    {
        ArrayList<Icon> listIcon = new ArrayList<>();
        String query = "Select * from " + TABLE_ICON;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do {
                Icon icon = new Icon();
                icon.url = cursor.getString(0);

                byte[] arr = cursor.getBlob(1);
                icon.bmp = BitmapFactory.decodeByteArray(arr,0,arr.length);
                listIcon.add(icon);
            }while (cursor.moveToNext());
        }

        return  listIcon;
    }

    public void insertIcon(Icon icon)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_URL,icon.url);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        icon.bmp.compress(Bitmap.CompressFormat.PNG,0,stream);
        contentValues.put(KEY_BITMAP, stream.toByteArray());

        db.insert(TABLE_ICON, null, contentValues);
        db.close();
    }
}
