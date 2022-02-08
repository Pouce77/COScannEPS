package fr.kunze.coscanneps;

public class Resultats {

    String nomBalise;
    String tempstotal;
    String tempsBalise;
    String date;
    String nomGroupe;
    String vitesse;

    public Resultats(String nomBalise, String tempstotal, String tempsBalise, String date, String nomGroupe, String vitesse) {

        this.nomBalise = nomBalise;
        this.tempstotal = tempstotal;
        this.tempsBalise = tempsBalise;
        this.date=date;
        this.nomGroupe=nomGroupe;
        this.vitesse=vitesse;
    }

    public Resultats() {
    }

    public String getNomBalise() {
        return nomBalise;
    }

    public void setNomBalise(String nomBalise) {
        this.nomBalise = nomBalise;
    }

    public String getTempstotal() {
        return tempstotal;
    }

    public void setTempstotal(String tempstotal) {
        this.tempstotal = tempstotal;
    }

    public String getTempsBalise() {
        return tempsBalise;
    }

    public void setTempsBalise(String tempsBalise) {
        this.tempsBalise = tempsBalise;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    public String getVitesse() {
        return vitesse;
    }

    public void setVitesse(String vitesse) {
        this.vitesse = vitesse;
    }
}
