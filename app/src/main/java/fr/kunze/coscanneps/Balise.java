package fr.kunze.coscanneps;

import java.io.Serializable;

public class Balise implements Serializable {

    String nomCourse;
    String nombalise;
    String latitude;
    String longitude;
    String date;

    public Balise(){

    }

    public Balise(String nomCourse, String nombalise, String latitude, String longitude,String date) {
        this.nomCourse = nomCourse;
        this.nombalise = nombalise;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date=date;
    }

    public String getNomCourse() {
        return nomCourse;
    }

    public void setNomCourse(String nomCourse) {
        this.nomCourse = nomCourse;
    }

    public String getnomBalise() {
        return nombalise;
    }

    public void setnomBalise(String nombalise) {
        this.nombalise = nombalise;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
