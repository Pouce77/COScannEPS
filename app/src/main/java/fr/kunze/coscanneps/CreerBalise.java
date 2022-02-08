package fr.kunze.coscanneps;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

public class CreerBalise extends AppCompatActivity {

    Button telecharger;
    Button creer;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_balise);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(CreerBalise.this,MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slideleft,R.anim.slideoutright);
                finish();
            }
        });

       File folder= getApplicationContext().getFilesDir();

       if(!folder.exists()) {

            folder.mkdir();
        }
            InputStream in = null;
            OutputStream out = null;
            file = new File(folder,"qrcode60balises.pdf"); //<= PDF file Name

        try {
            in = getAssets().open("qrcode60balises.pdf");
            out = new FileOutputStream(file);
            int read;
            final byte[] buffer = new byte[4096];
            while ((read = in.read(buffer)) > 0)
                out.write(buffer, 0, read);
        } catch(IOException ioe) {
            try {
                throw new IOException(ioe);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        telecharger=findViewById(R.id.telechargerBalise);
        creer=findViewById(R.id.creerPropreBalise);

        telecharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LayoutInflater factory = LayoutInflater.from(CreerBalise.this);
                @SuppressLint("InflateParams") final View alertDialogView = factory.inflate(R.layout.boite_de_dialogue_telecharger_fichierbalise, null);


                //Création de l'AlertDialog
                final AlertDialog.Builder adb = new AlertDialog.Builder(CreerBalise.this);

                //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
                adb.setView(alertDialogView);

                //On donne un titre à l'AlertDialog
                adb.setTitle("Télécharger le fichier PDF");

                //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
                adb.setPositiveButton("Ouvrir seulement", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        File folder=getApplicationContext().getFilesDir();

                        File fi=new File(folder,"qrcode60balises.pdf");

                        Uri uri= FileProvider.getUriForFile(CreerBalise.this,"fr.kunze.coscanneps.provider",fi);


                        Intent j=new Intent(Intent.ACTION_VIEW);
                        j.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        j.setDataAndType(uri,"application/pdf");
                        try {
                            startActivity(j);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(alertDialogView.getContext(), "Vous n'avez pas d'application installée pour lire les fichiers PDF", Toast.LENGTH_SHORT).show();
                        }


                    }

                });
                adb.setNeutralButton("Imprimer", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public void onClick(DialogInterface dialog, int which) {

                        PrintManager printManager=(PrintManager) CreerBalise.this.getSystemService(Context.PRINT_SERVICE);
                        try
                        {
                            File folder=getApplicationContext().getFilesDir();

                            File fi=new File(folder,"qrcode60balises.pdf");
                            PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(CreerBalise.this,fi.getAbsolutePath() );

                            assert printManager != null;
                            printManager.print("Document", printAdapter,new PrintAttributes.Builder().build());
                    }
                        catch (Exception e)
                    {

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

        creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Intent i=new Intent(CreerBalise.this,PropreBalise.class);
            startActivity(i);
                overridePendingTransition(R.anim.slideright,R.anim.slideoutleft);


            }
        });
    }

}
