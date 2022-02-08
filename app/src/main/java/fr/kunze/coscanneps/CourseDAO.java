package fr.kunze.coscanneps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseDAO {

    protected final static int VERSION = 1;
    protected final static String NAME = "basecoscann.db";
    protected SQLiteDatabase eDb = null;
    protected DBHelper eHandler = null;

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

    public CourseDAO(Context context) {

        eHandler=new DBHelper(context,NAME,null,VERSION);
    }

    public void openDb (){

        eDb=eHandler.getWritableDatabase();

    }

    public void closeDb (){

        eDb.close();
    }

    public void ajouterBalise (Balise balise){

        ContentValues value=new ContentValues();
        value.put(NOM_COURSE,balise.getNomCourse());
        value.put(BALISE,balise.getnomBalise());
        value.put(LATITUDE,balise.getLatitude());
        value.put(LONGITUDE,balise.getLongitude());
        value.put(DATE,balise.getDate());

        eDb.insert(NOM_TABLE, null, value);

    }

    public ArrayList<Balise> getListeBalise(String nomCourse){

        Cursor c = eDb.rawQuery("select distinct balise, latitude, longitude, date from " + NOM_TABLE + " where " + NOM_COURSE + "=?", new String[]{String.valueOf(nomCourse)});
        ArrayList<Balise> el=new ArrayList<>();

        while (c.moveToNext()){
            String balise=c.getString(0);
            String latitude=c.getString(1);
            String longitude=c.getString (2);
            String date=c.getString(3);

            Balise b = new Balise();

                b.setNomCourse(nomCourse);
                b.setnomBalise(balise);
                b.setLatitude(latitude);
                b.setLongitude(longitude);
                b.setDate(date);

                el.add(b);
            }


        c.close();
        return el;
    }

    public void supprimerBalise(String balise,String nomCourse ){

        eDb.delete(NOM_TABLE, "balise=? and nomCourse=?", new String[]{balise,nomCourse});

    }

    public void supprimerCourse(String nomCourse ){

        eDb.delete(NOM_TABLE, "nomCourse=?", new String[]{nomCourse});

    }

    public ArrayList<HashMap<String,String>> getListeCourse(){

        Cursor c=eDb.rawQuery("select distinct nomCourse, date from "+NOM_TABLE+";",null);
        ArrayList<HashMap<String,String >> el=new ArrayList<>();
        HashMap<String,String> element;
        while (c.moveToNext()){
            String nomCourse=c.getString(0);
            String date=c.getString(1);
            element=new HashMap<String,String>();
            element.put("nomCourse",nomCourse);
            element.put("date",date);

            el.add(element);
        }
        c.close();
        return el;
    }

    public ArrayList<String > getListeCourseSansDate(){

        Cursor c=eDb.rawQuery("select distinct nomCourse from "+NOM_TABLE,null);
        ArrayList<String> array=new ArrayList<>();
        while (c.moveToNext()){
            String nomCourse=c.getString(0);

            array.add(nomCourse);
        }
        c.close();
        return array;
    }

    public String getChaine (String nomCourse){
        String chaine="";
        String date="";
        Cursor c=eDb.rawQuery("select distinct balise, latitude, longitude, date from "+NOM_TABLE+ " where " + NOM_COURSE + "=?", new String[]{String.valueOf(nomCourse)});
        while (c.moveToNext()){

            String balise=c.getString(0);
            String lat=c.getString(1);
            String longi=c.getString(2);
            date=c.getString(3);

            chaine=chaine+balise+";"+lat+";"+longi+"\n";
        }
        c.close();
        chaine=nomCourse+";"+date+"\n"+chaine;
        return chaine;
    }
    public void ajouterResultats (Resultats r){

        ContentValues value=new ContentValues();

        value.put(BALISE,r.getNomBalise());
        value.put(TEMPSBALISE,r.tempsBalise);
        value.put(TEMPSTOTAL,r.getTempstotal());
        value.put(DATE,r.getDate());
        value.put(NOMGROUPE,r.getNomGroupe());
        value.put(VITESSE,r.getVitesse());

        eDb.insert(NOM_TABLE_RESULTAT, null, value);

    }
    public void ajouterChaineQR (String chaine){

        ContentValues values=new ContentValues();
        values.put(CHAINE_QR,chaine);

        eDb.insert(NOM_TABLE_QR,null,values);
    }

    public Boolean QRExiste(String chaine){
            Cursor c=eDb.rawQuery("select distinct "+CHAINE_QR+" from "+NOM_TABLE_QR,null);
            Boolean existe=false;
            while (c.moveToNext()) {
               if (c.getString(0).matches(chaine)) {
                    existe=true;
                   break;

                } else {
                    existe=false;
                }
            }

        c.close();
        return existe;

    }
    public void SupprimerChaine (String nomGroupe){

        eDb.execSQL("delete from "+NOM_TABLE_QR+" where "+CHAINE_QR+" like '%"+nomGroupe+"%'");
    }

    public ArrayList<String> getlisteDate(){
        ArrayList<String> list=new ArrayList<>();
        Cursor c=eDb.rawQuery("select distinct "+DATE+" from "+NOM_TABLE_RESULTAT, null);

        while (c.moveToNext()){
            String nom=c.getString(0);
            list.add(nom);
        }

        return list;
    }

    public ArrayList<String> getlistenomGroupeparDate(String date){
        ArrayList<String> list=new ArrayList<>();
        Cursor c=eDb.rawQuery("select distinct "+NOMGROUPE+" from "+NOM_TABLE_RESULTAT+" where date=?",new String[]{String.valueOf(date)});

        while (c.moveToNext()){
            String nom=c.getString(0);
            list.add(nom);

        }

        return list;
    }

    public ArrayList<Resultats> getListResultat (String nomGroupe){
        ArrayList<Resultats>liste=new ArrayList<>();

        Cursor c=eDb.rawQuery("select balise, tempsbalise,tempstotal,date,vitesse from " + NOM_TABLE_RESULTAT+" where " + NOMGROUPE + "=?", new String[]{String.valueOf(nomGroupe)});

        while (c.moveToNext()){
            Resultats r=new Resultats();
            r.setNomBalise(c.getString(0));
            r.setTempsBalise(c.getString(1));
            r.setTempstotal(c.getString(2));
            r.setDate(c.getString(3));
            r.setVitesse(c.getString(4));

            liste.add(r);
        }

        for (int i=0;i<liste.size();i++){
            Log.e("message",liste.get(i).getNomBalise());
        }
        c.close();

        return liste;
    }

    public void deleteResultat (String nomGroupe){

        eDb.delete(NOM_TABLE_RESULTAT, "nomGroupe=?", new String[]{nomGroupe});

    }
}
