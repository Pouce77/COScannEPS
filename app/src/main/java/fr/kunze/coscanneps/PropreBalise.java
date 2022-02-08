package fr.kunze.coscanneps;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.print.PrintHelper;


public class PropreBalise extends AppCompatActivity {

    EditText editText;
    Button generer;
    ImageView imageView;
    Bitmap bitmap;
    FloatingActionButton fab2;
    ProgressDialog progressDialog;

    public final static int QRcodeWidth = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propre_balise);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab2=findViewById(R.id.floatingActionButtonSave);
        fab2.setEnabled(false);

        editText = findViewById(R.id.editTextPropreBalise);
        generer = findViewById(R.id.genererQRPoropre);
        imageView = findViewById(R.id.imageViewPropreQR);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editText.getText().toString().matches("")){

                    Toast.makeText(PropreBalise.this,"Il n'y a pas de QR code à imprimer",Toast.LENGTH_LONG).show();

                }else {
                    // Get the print manager.
                    PrintHelper printHelper = new PrintHelper(PropreBalise.this);

                    // Set the desired scale mode.
                    printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);

                    // Get the bitmap for the ImageView's drawable.
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                    // Print the bitmap.
                    printHelper.printBitmap("Print Bitmap", bitmap);

                }
            }
        });



        generer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                if (editText.getText().toString().matches("")) {

                    Toast.makeText(PropreBalise.this, "Vous devez entrer un nom de balise", Toast.LENGTH_LONG).show();

                } else {
                    generer();
                    fab2.setEnabled(true);
                }

            }
        });


        fab2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                File folder= new File(getExternalFilesDir(null)+File.separator+"Balises_generees");
                if(!folder.exists()) {

                    folder.mkdirs();
                }

                try (FileOutputStream out = new FileOutputStream(getExternalFilesDir(null)+File.separator+"Balises_generees/"+editText.getText().toString()+".png")) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(PropreBalise.this,"Vous avez enregistré ce QR code dans le dossier "+getExternalFilesDir(null)+File.separator+"Balises_generees/"+editText.getText().toString()+".png",Toast.LENGTH_LONG).show();
                fab2.setEnabled(false);
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

            Intent i= new Intent(PropreBalise.this,MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slideleft,R.anim.slideoutright);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }


    public void generer() {
        progressDialog = ProgressDialog.show(PropreBalise.this, "Veuillez patienter...", "Le QRCode est généré", true);
        progressDialog.setCancelable(false);
        new Thread((new Runnable() {
            @Override
            public void run() {

                handler.sendEmptyMessage(0);

            }
        })).start();
    }
    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {

            imageView.setImageResource(R.drawable.coscann);

            try {
                bitmap = TextToImageEncode(editText.getText().toString());
            } catch (WriterException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);
            progressDialog.dismiss();

        }
    };
    }

