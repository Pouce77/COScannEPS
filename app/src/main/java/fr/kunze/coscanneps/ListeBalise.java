package fr.kunze.coscanneps;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ListeBalise extends AppCompatActivity {

    ListView liste;
    CourseDAO base;
    ArrayList<Balise> listeBalise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_balise);
        Toolbar toolbar = findViewById(R.id.toolbar);

        final String nomCourse=getIntent().getStringExtra("nomCourse");
        toolbar.setTitle(nomCourse);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i=new Intent(ListeBalise.this,ScannerCourse.class);
               i.putExtra("NOM_COURSE",nomCourse);
               startActivity(i);
                overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
               finish();
            }
        });

        base=new CourseDAO(this);
        base.openDb();

        liste= findViewById(R.id.listView);

        listeBalise=base.getListeBalise(nomCourse);

        ListAdapter listAdapter=new ListAdapter(ListeBalise.this,R.layout.listebalise,listeBalise);
        liste.setAdapter(listAdapter);

        liste.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

            Balise b= (Balise) adapterView.getItemAtPosition(i);

            final String bali=b.getnomBalise();

                LayoutInflater factory = LayoutInflater.from(ListeBalise.this);
                @SuppressLint("InflateParams") final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_supprimer_balise, null);
                TextView text= alertDialogView.findViewById(R.id.textView18);
                text.setText("Vous allez supprimer la balise "+bali+" ?");
                //Création de l'AlertDialog
                final AlertDialog.Builder adb = new AlertDialog.Builder(ListeBalise.this);

                //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
                adb.setView(alertDialogView);

                //On donne un titre à l'AlertDialog
                adb.setTitle("Supprimer la balise");

                //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        base.supprimerBalise(bali,nomCourse);
                        liste.deferNotifyDataSetChanged();
                        Toast.makeText(alertDialogView.getContext(),"La balise "+bali+" a été supprimée.",Toast.LENGTH_SHORT).show();
                        ArrayList<Balise> listEl = base.getListeBalise(nomCourse);
                        ListAdapter adapter = new ListAdapter(ListeBalise.this,R.layout.listebalise,listEl);
                        liste.setAdapter(adapter);

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



                return false;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listbalise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {

            Intent i=new Intent(ListeBalise.this,MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideleft,R.anim.slideoutright);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        base.closeDb();
    }
}
