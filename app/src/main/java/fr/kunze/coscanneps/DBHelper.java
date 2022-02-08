package fr.kunze.coscanneps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Julien on 19/11/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String NOM_TABLE = "course";
    public static final String NOM_TABLE_RESULTAT="tableresultats";
    public static final String NOM_TABLE_QR="QRcodeChaine";

    public static final String KEY = "_id";
    public static final String NOM_COURSE="nomCourse";
    public static final String BALISE="balise";
    public static final String LATITUDE="latitude";
    public static final String LONGITUDE="longitude";
    public static final String DATE="date";
    public static final String  TEMPSBALISE="tempsbalise";
    public static final String TEMPSTOTAL="tempstotal";
    public static final String NOMGROUPE="nomGroupe";
    public static final String CHAINE_QR="chaineQR";
    public static final String VITESSE="vitesse";

    public  static final String TABLE_CREATE = "CREATE TABLE " + NOM_TABLE + " ("+ KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "+NOM_COURSE+" TEXT, "+BALISE+" TEXT, "+LATITUDE+" TEXT, "+ LONGITUDE+" TEXT, "+DATE+" TEXT);";
    public  static final String TABLE_CREATE_RESULTATS = "CREATE TABLE " + NOM_TABLE_RESULTAT + " ("+ KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "+BALISE+" TEXT, "+TEMPSBALISE+" TEXT, "+TEMPSTOTAL+" TEXT, "+DATE+" TEXT, "+NOMGROUPE+" TEXT, "+VITESSE+" TEXT);";
    public static final String TABLE_CREATE_QR="CREATE TABLE "+NOM_TABLE_QR+"("+KEY+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CHAINE_QR+" TEXT);";
    public static final String TABLE_DROP= "DROP TABLE IF EXISTS " + NOM_TABLE + ";";
    public static final String TABLE_DROP_RESULTATS="DROP TABLE IF EXISTS " + NOM_TABLE_RESULTAT + ";";
    public static final String TABLE_DROP_QR="DROP TABLE IF EXISTS " + NOM_TABLE_QR+ ";";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_CREATE_RESULTATS);
        db.execSQL(TABLE_CREATE_QR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(TABLE_DROP);
        db.execSQL(TABLE_DROP_RESULTATS);
        db.execSQL(TABLE_DROP_QR);
    }
}
