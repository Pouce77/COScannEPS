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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class ListeCourse extends AppCompatActivity {

    ListView listeCourses;
    ArrayList<HashMap<String,String>> liste;
    CourseDAO base;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        base=new CourseDAO(this);
        base.openDb();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(ListeCourse.this,MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slideleft,R.anim.slideoutright);

            }
        });

        listeCourses= findViewById(R.id.listViewCourses);
        liste=base.getListeCourse();
        SimpleAdapter adapter=new SimpleAdapter(ListeCourse.this,liste,android.R.layout.simple_list_item_2,new String[]{"nomCourse","date"},new int[]{android.R.id.text1,android.R.id.text2});
        listeCourses.setAdapter(adapter);


        listeCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                HashMap<String,String> list= (HashMap<String, String>) adapterView.getItemAtPosition(i);
                String nom=list.get("nomCourse");

                Intent j=new Intent(ListeCourse.this, ListeBalise.class);
                j.putExtra("nomCourse",nom);
                startActivity(j);
                overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listcourse, menu);
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

            Intent i=new Intent(ListeCourse.this,MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideleft,R.anim.slideoutright);
            finish();

            return true;
        }
        if (id == R.id.supprimer) {

            if (liste.isEmpty()) {
                Toast.makeText(ListeCourse.this,"Vous n'avez pas de course à supprimer.",Toast.LENGTH_LONG).show();
            } else {
                LayoutInflater factory = LayoutInflater.from(ListeCourse.this);
                @SuppressLint("InflateParams") final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_supprimer_course, null);
                spinner = alertDialogView.findViewById(R.id.spinner2);
                ArrayList<String> arrayList = base.getListeCourseSansDate();
                ArrayAdapter arrayAdapter = new ArrayAdapter(ListeCourse.this, android.R.layout.simple_spinner_dropdown_item, arrayList);
                spinner.setAdapter(arrayAdapter);
                //Création de l'AlertDialog
                final AlertDialog.Builder adb = new AlertDialog.Builder(ListeCourse.this);

                //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
                adb.setView(alertDialogView);

                //On donne un titre à l'AlertDialog
                adb.setTitle("Supprimer la course");

                //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String nmCourse = spinner.getSelectedItem().toString();
                        base.supprimerCourse(nmCourse);
                        liste = base.getListeCourse();
                        SimpleAdapter adapter = new SimpleAdapter(ListeCourse.this, liste, android.R.layout.simple_list_item_2, new String[]{"nomCourse", "date"}, new int[]{android.R.id.text1, android.R.id.text2});
                        listeCourses.setAdapter(adapter);


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


                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        base.closeDb();
    }
}
