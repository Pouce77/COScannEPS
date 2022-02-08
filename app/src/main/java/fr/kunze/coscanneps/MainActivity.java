package fr.kunze.coscanneps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    Button scanner;
    Button envoyer;
    Button recuperer;
    Button creerBalise;
    Button courseAutreAppli;
    CourseDAO base;
    ArrayList<String> liste;
    Boolean choice=true;

    public static final int LOCATION_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scanner= findViewById(R.id.nouvelleCourse);
        envoyer= findViewById(R.id.envoyerEleve);
        recuperer= findViewById(R.id.recupererDonnee);
        creerBalise= findViewById(R.id.creerBalise);
        courseAutreAppli=findViewById(R.id.recupAutreCourse);

        base=new CourseDAO(this);
        base.openDb();

        liste=new ArrayList<>();
        liste=base.getListeCourseSansDate();


        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    LOCATION_REQUEST);
        }

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                @SuppressLint("InflateParams") final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_creer_course, null);

                final EditText editGroupe = alertDialogView.findViewById(R.id.editNomCourse);

                //Création de l'AlertDialog
                final AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);

                //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
                adb.setView(alertDialogView);

                //On donne un titre à l'AlertDialog
                adb.setTitle("Donner un nom à votre course pour la retrouver :");


                //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String nomCourse=editGroupe.getText().toString();

                        Intent i=new Intent(MainActivity.this,ScannerCourse.class);
                        i.putExtra("NOM_COURSE",nomCourse);
                        startActivity(i);
                        overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);

                    }

                });

                //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
                adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Lorsque l'on cliquera sur annuler on quittera l'application

                        dialog.dismiss();
                    }
                });
                adb.show();


            }
        });

        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (liste.isEmpty()){

                    Toast.makeText(MainActivity.this,"Vous n'avez pas encore enregistré de course.",Toast.LENGTH_LONG).show();

                }else {
                    Intent i = new Intent(MainActivity.this, GenererQRcourse.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
                }
            }
        });

        recuperer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice=true;
                new IntentIntegrator(MainActivity.this).initiateScan();
            }
        });

        creerBalise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i= new Intent (MainActivity.this,CreerBalise.class);
                startActivity(i);
                overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
            }
        });

        courseAutreAppli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice=false;
                new IntentIntegrator(MainActivity.this).initiateScan();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.aide) {

            Intent i=new Intent(MainActivity.this,Aide.class);
            startActivity(i);

            return true;
        }*/

        if (id == R.id.resultats) {

            Intent i= new Intent(MainActivity.this,PageResultats.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
            return true;
        }

        if (id == R.id.telecharger) {

            String url = "https://play.google.com/store/apps/details?id=fr.kunze.coscanneleve";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

            return true;
        }
        if (id == R.id.listCourses) {

            Intent i=new Intent(MainActivity.this,ListeCourse.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
            return true;
        }

        if (id == R.id.fichiercsv) {

            Intent i=new Intent(MainActivity.this,ListeFichiersCSV.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

// nous utilisons la classe IntentIntegrator et sa fonction parseActivityResult pour parser le résultat du scan
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {

// nous récupérons le contenu du code barre
            String scanContent = scanningResult.getContents();
            if (scanContent != null) {

                if(choice) {
                    Intent i = new Intent(MainActivity.this, ResultatsEleves.class);
                    i.putExtra("CHAINE", scanContent);
                    startActivity(i);
                }else{
                    String[] course = scanContent.split("[\n]");
                    String[] nomDate=course[0].split(";");
                    String nomCourse=nomDate[0];
                    String date=nomDate[1];

                    for (int i = 1; i < course.length; i++) {
                        String[] balise = course[i].split(";");

                        if (balise.length <= 1) {

                            Toast.makeText(MainActivity.this, "Le QR code scanné n'est pas valide !", Toast.LENGTH_LONG).show();

                        } else {
                            Balise b = new Balise();

                            b.setNomCourse(nomCourse);
                            b.setnomBalise(balise[0]);
                            b.setLatitude(balise[1]);
                            b.setLongitude(balise[2]);
                            b.setDate(date);

                            base.ajouterBalise(b);
                            String nom = b.getNomCourse();

                            Toast.makeText(MainActivity.this, "La course : " + nom + " a été ajoutée !", Toast.LENGTH_LONG).show();

                            Intent j = new Intent(MainActivity.this, ListeCourse.class);
                            startActivity(j);
                            overridePendingTransition(R.anim.slideright, R.anim.slideoutleft);

                        }
                    }
                }
            } else {
                Toast.makeText(MainActivity.this,
                        "Aucune donnée reçue!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(MainActivity.this,
                    "Aucune donnée reçu!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Permissions accordées.", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, "Permission refusée !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        base.closeDb();
    }
}
