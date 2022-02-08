package fr.kunze.coscanneps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintJob;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

public class ListeFichiersCSV extends AppCompatActivity {

    ListView liste;
    ArrayList<File> listFichiers;
    ArrayList<String>listnomFichiers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_fichiers_csv);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ListeFichiersCSV.this,MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slideleft,R.anim.slideoutright);
                finish();
            }
        });

        listFichiers=new ArrayList<>();
        listnomFichiers=new ArrayList<>();

        File folder= new File(String.valueOf(getExternalFilesDir(null)));

        File[]file=folder.listFiles();

        if (file!=null){
            for (int i=0; i<file.length;i++){
                if (file[i].getName().endsWith("csv")){
                listFichiers.add(file[i]);
                listnomFichiers.add(file[i].getName());
                }

            }
        }

        FloatingActionButton fab2=findViewById(R.id.floatingActionButton3);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater factory = LayoutInflater.from(ListeFichiersCSV.this);
                final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_partager, null);

                final Spinner spinner = (Spinner) alertDialogView.findViewById(R.id.spinnerFichier);
                ArrayAdapter adapter=new ArrayAdapter(alertDialogView.getContext(),android.R.layout.simple_spinner_dropdown_item,listnomFichiers);
                spinner.setAdapter(adapter);

                //Création de l'AlertDialog
                final AlertDialog.Builder adb = new AlertDialog.Builder(ListeFichiersCSV.this);

                //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
                adb.setView(alertDialogView);

                //On donne un titre à l'AlertDialog
                adb.setTitle("Choisissez le fichier que vous souhaitez partager");


                //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        if (spinner.getSelectedItem()!=null) {
                            String fichier = spinner.getSelectedItem().toString();
                            File folder = new File(String.valueOf(getExternalFilesDir(null)));
                            File fi = new File(folder, fichier);
                            Uri uri = FileProvider.getUriForFile(alertDialogView.getContext(), "fr.kunze.coscanneps.provider", fi);

                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("text/csv");
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            startActivity(sharingIntent);
                        }else{
                            Toast.makeText(ListeFichiersCSV.this,"Vous n'avez pas de fichier à partager.",Toast.LENGTH_LONG).show();
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

        liste=findViewById(R.id.listViewFichiercsv);

            ArrayAdapter adapter=new ArrayAdapter(ListeFichiersCSV.this,android.R.layout.simple_list_item_1,listnomFichiers);
            liste.setAdapter(adapter);

            liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    String fichier=(String)adapterView.getItemAtPosition(i);
                    File folder=new File(String.valueOf(getExternalFilesDir(null)));
                    File fi=new File(folder,fichier);
                    Uri uri= FileProvider.getUriForFile(ListeFichiersCSV.this,"fr.kunze.coscanneps.provider",fi);

                    Intent j=new Intent(Intent.ACTION_VIEW);
                    j.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    j.setDataAndType(uri,"text/csv");
                    startActivity(j);

                }
            });

    }

}
