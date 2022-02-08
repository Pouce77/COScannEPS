package fr.kunze.coscanneps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ScannerCourse extends AppCompatActivity {

    TextView titre;
    Button scanner;
    Button listeBalise;
    String tit;
    CourseDAO base;
    Balise b;
    String lat;
    String longi;
    String date;
    LocationManager locationManager;
    LocationListener locationListener;
    String provider;
    Location location;


    HandlerThread otherthread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_course);

        titre = findViewById(R.id.titreCourse);
        scanner = findViewById(R.id.boutonScanner);
        listeBalise = findViewById(R.id.listeBalise);

        tit = getIntent().getStringExtra("NOM_COURSE");
        titre.setText(tit);
        Date now = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        date = formatter.format(now);

        base = new CourseDAO(this);
        base.openDb();

        otherthread = new HandlerThread("Traitement localisation");
        otherthread.start();


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {
                location = loc;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
                Log.e("message", s);
            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 2.0f, locationListener, otherthread.getLooper());

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(ScannerCourse.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ScannerCourse.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.e("Provider", "GPS");

                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Log.e("Provider", "Network");

                    if (location == null) {
                        location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                        Log.e("Provider", "Passive");

                    }
                }
                if (location != null) {
                    lat = String.valueOf(location.getLatitude());
                    longi = String.valueOf(location.getLongitude());
                    new IntentIntegrator(ScannerCourse.this).initiateScan();

                } else {
                    Toast.makeText(ScannerCourse.this, "Pas de donnée de localisation reçue. Vérifiez que vous avez bien autorisé ou activé la localisaton.", Toast.LENGTH_LONG).show();

                }
            }
        });

        listeBalise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ScannerCourse.this, ListeBalise.class);
                i.putExtra("nomCourse", tit);
                startActivity(i);
                overridePendingTransition(R.anim.slideright, R.anim.slideoutleft);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

// nous utilisons la classe IntentIntegrator et sa fonction parseActivityResult pour parser le résultat du scan
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {

// nous récupérons le contenu du code barre
            String scanContent = scanningResult.getContents();
            if (scanContent != null) {

                b = new Balise();
                b.nomCourse = tit;
                b.nombalise = scanContent;

                if ((lat == null) && (longi==null)){
                    b.latitude="Non trouvée";
                    b.longitude="Non trouvée";
                    Toast.makeText(ScannerCourse.this,"La localisation n'a pas été trouvée, vous ne pourrez pas obtenir la vitesse pour cette course!",Toast.LENGTH_LONG).show();
                    }else {
                    b.latitude = lat;
                    b.longitude = longi;
                }
                b.setDate(date);
                base.ajouterBalise(b);

                Toast.makeText(ScannerCourse.this, "Vous avez enregistré la balise : " + b.getnomBalise(), Toast.LENGTH_LONG).show();

                Intent i = new Intent(ScannerCourse.this, ListeBalise.class);
                i.putExtra("nomCourse", tit);
                startActivity(i);
                overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
                finish();


            } else {
                Toast.makeText(ScannerCourse.this,
                        "Aucune donnée reçue!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(ScannerCourse.this,
                    "Aucune donnée reçu!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        base.closeDb();
        if (locationManager!=null && locationListener!=null) {
            locationManager.removeUpdates(locationListener);
        }
    }

}
