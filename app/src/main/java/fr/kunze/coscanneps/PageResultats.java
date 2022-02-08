package fr.kunze.coscanneps;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PageResultats extends AppCompatActivity {

    Spinner spinnerDate;
    Spinner spinnerGroupe;
    ListView liste;
    TextView texttempstotal;

    CourseDAO base;
    ArrayList<String> listGroupe;
    ArrayList<Resultats> listResultats;
    ArrayList<String> listdate;
    String chainegenerer="";
    FloatingActionButton fab;
    FloatingActionButton fabDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_resultats);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinnerDate=findViewById(R.id.spinnerdate);
        spinnerGroupe=findViewById(R.id.spinnerGroupe);
        liste=findViewById(R.id.listresultatsrecherche);
        texttempstotal=findViewById(R.id.textTempstotal);
        fab = findViewById(R.id.fab);
        fabDelete=findViewById(R.id.fabDelete);

        base=new CourseDAO(this);
        base.openDb();

        listGroupe=new ArrayList<>();
        listdate=new ArrayList<>();
        listResultats=new ArrayList<Resultats>();
        listdate=base.getlisteDate();
        ArrayAdapter adapter=new ArrayAdapter(PageResultats.this,android.R.layout.simple_spinner_dropdown_item,listdate);
        spinnerDate.setAdapter(adapter);

        if (listdate.isEmpty()){

            texttempstotal.setText("--:--");
        }

        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                listGroupe=base.getlistenomGroupeparDate((String) adapterView.getItemAtPosition(i));
                ArrayAdapter arrayAdapter=new ArrayAdapter(adapterView.getContext(),android.R.layout.simple_spinner_dropdown_item,listGroupe);
                spinnerGroupe.setAdapter(arrayAdapter);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerGroupe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                listResultats=base.getListResultat((String) adapterView.getItemAtPosition(i));
                if (!listResultats.isEmpty()) {
                    texttempstotal.setText(listResultats.get(0).getTempstotal());
                }else{
                    texttempstotal.setText("--:--");
                }
                ListAdapterResultats adapterResultats=new ListAdapterResultats(adapterView.getContext(),R.layout.listersultatsadapter,listResultats);
                liste.setAdapter(adapterResultats);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if (listdate.isEmpty()){

            texttempstotal.setText("--:--");
            fab.setEnabled(false);
            fabDelete.setEnabled(false);
        }else{
            fab.setEnabled(true);
            fab.setEnabled(true);
        }
        fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater factory = LayoutInflater.from(PageResultats.this);
                final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_creer_csv, null);

                final EditText editnomFichier = (EditText) alertDialogView.findViewById(R.id.editTextCSV);

                //Création de l'AlertDialog
                final AlertDialog.Builder adb = new AlertDialog.Builder(PageResultats.this);

                //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
                adb.setView(alertDialogView);

                //On donne un titre à l'AlertDialog
                adb.setTitle("Créer un fichier CSV pour cette course ?");


                //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String data="";
                        if (!listResultats.isEmpty()) {
                            String tempstot = texttempstotal.getText().toString();
                            data = "Date;"+listResultats.get(0).getDate()+"\n"+"Nom du groupe;"+spinnerGroupe.getSelectedItem().toString()+"\n"+"Temps de course total;"+tempstot+"\n";
                        }
                        for(int i=0;i<listResultats.size();i++){

                            String nomBalise=listResultats.get(i).getNomBalise();
                            String tempsBalise=listResultats.get(i).getTempsBalise();
                            String vitesse=listResultats.get(i).getVitesse();

                            data=data+nomBalise+";"+tempsBalise+";"+vitesse+" km/h"+"\n";
                        }

                        if (editnomFichier.getText().toString().matches("")||editnomFichier.getText().toString().contains("/")){
                            Toast.makeText(alertDialogView.getContext(),"Vous devez entrer un nom de fichier valide",Toast.LENGTH_LONG).show();
                        }else {
                            String filname = (editnomFichier.getText().toString()) + ".csv";
                            ecrireDansCSV(data, filname);

                            Toast.makeText(PageResultats.this, "Course enregistrée dans le dossier : "+getExternalFilesDir(null)+File.separator+ filname, Toast.LENGTH_LONG).show();

                        }
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

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater factory = LayoutInflater.from(PageResultats.this);
                final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_supprimer_resultat, null);

                final TextView editsuppressionFichier = (TextView) alertDialogView.findViewById(R.id.textView13);

                editsuppressionFichier.setText("Vous allez supprimer tous les résultats du groupe : "+spinnerGroupe.getSelectedItem().toString()+"\n\nSouhaitez-vous continuer ?");
                //Création de l'AlertDialog
                final AlertDialog.Builder adb = new AlertDialog.Builder(PageResultats.this);

                //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
                adb.setView(alertDialogView);

                //On donne un titre à l'AlertDialog
                adb.setTitle("Supprimer un résultat");


                //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        base.deleteResultat(spinnerGroupe.getSelectedItem().toString());
                        base.SupprimerChaine(spinnerGroupe.getSelectedItem().toString());
                        listdate=base.getlisteDate();
                        ArrayAdapter adapter=new ArrayAdapter(PageResultats.this,android.R.layout.simple_spinner_dropdown_item,listdate);
                        spinnerDate.setAdapter(adapter);
                        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                listGroupe=base.getlistenomGroupeparDate((String) adapterView.getItemAtPosition(i));
                                ArrayAdapter arrayAdapter=new ArrayAdapter(adapterView.getContext(),android.R.layout.simple_spinner_dropdown_item,listGroupe);
                                spinnerGroupe.setAdapter(arrayAdapter);


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {


                            }
                        });

                        if (spinnerDate.getSelectedItem()==null){
                            Intent i=new Intent (PageResultats.this,MainActivity.class);
                            overridePendingTransition(R.anim.slideleft,R.anim.slideoutright);
                            startActivity(i);
                            finish();
                        }

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
    }


    public void ecrireDansCSV(String data, String filename) {

        File folder= new File(String.valueOf(getExternalFilesDir(null)));
        if(!folder.exists()){

            folder.mkdirs();
        }

        File file=new File(folder,filename);
        int n=0;
        try{
            FileOutputStream out=new FileOutputStream(file);
            out.write(data.getBytes());

            if (out != null)
                out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

            Intent i=new Intent(PageResultats.this,MainActivity.class);
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
