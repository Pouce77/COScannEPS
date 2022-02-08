package fr.kunze.coscanneps;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ResultatsEleves extends AppCompatActivity {

    ListView liste;
    String chaine;
    ArrayList<Resultats> resultatsArrayList;
    String nomGroupe;
    CourseDAO base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultats_eleves);
        Toolbar toolbar = findViewById(R.id.toolbar);

        base=new CourseDAO(this);
        base.openDb();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ResultatsEleves.this,MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slideleft,R.anim.slideoutright);
                finish();
            }
        });

        chaine=getIntent().getStringExtra("CHAINE");
        liste=findViewById(R.id.listeresultat);
        resultatsArrayList=new ArrayList<>();

        String[]course=chaine.split("[\n]");

        for (int i = 0; i < course.length; i++) {
            String[]resultats=course[i].split(";");

            if (resultats.length <= 1) {

                Toast.makeText(ResultatsEleves.this, "Le QR code scanné n'est pas valide !", Toast.LENGTH_LONG).show();
                finish();

            } else {

                nomGroupe = resultats[0];
                String nomBalise = resultats[1];
                String tempsBalise = resultats[2];
                String tempsTotal = resultats[3];
                String date = resultats[4];
                String vitesse = resultats[5];

                Resultats res = new Resultats(nomBalise, tempsTotal, tempsBalise, date, nomGroupe, vitesse);


                if (base.QRExiste(chaine)) {

                } else {

                    base.ajouterResultats(res);
                }

                resultatsArrayList.add(res);
            }
        }
        base.ajouterChaineQR(chaine);
        toolbar.setTitle("Résultats pour le groupe : "+nomGroupe);
        setSupportActionBar(toolbar);
        ListAdapterResultats adapterResultats=new ListAdapterResultats(ResultatsEleves.this,R.layout.listersultatsadapter,resultatsArrayList);
        liste.setAdapter(adapterResultats);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        base.closeDb();
    }


}
