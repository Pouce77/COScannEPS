package fr.kunze.coscanneps;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.encoder.Encoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class GenererQRcourse extends AppCompatActivity {

    Spinner spinner;
    Button generer;
    ImageView image;
    public final static int QRcodeWidth = 500;
    Bitmap bitmap;
    CourseDAO base;
    ArrayList<String> liste;
    String chaineGenerer;
    String titre;
    TextView textview;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generer_qrcourse);

        spinner = (Spinner) findViewById(R.id.spinner);
        generer = (Button) findViewById(R.id.generer);
        image = (ImageView) findViewById(R.id.imageView);
        textview = (TextView) findViewById(R.id.nomQR);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(GenererQRcourse.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slideleft, R.anim.slideoutright);
                finish();

            }
        });

        base = new CourseDAO(this);
        base.openDb();

        liste = base.getListeCourseSansDate();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(GenererQRcourse.this, R.layout.support_simple_spinner_dropdown_item, liste);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chaineGenerer = base.getChaine((String) adapterView.getItemAtPosition(i));
                titre = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        generer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("Taille chaine",String.valueOf(chaineGenerer.length()));
                Log.e("Taille chaine",chaineGenerer);

                if (chaineGenerer.length()>2900){
                    Toast.makeText(GenererQRcourse.this,"Il y a trop de balises dans votre course. Le QR code ne peut être généré.",Toast.LENGTH_LONG).show();
                }else {
                    generer();
                }
            }
        });

    }

    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth,null
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        base.closeDb();
    }

    public void generer() {
        progressDialog = ProgressDialog.show(GenererQRcourse.this, "Veuillez patienter...", "Le QRCode est généré", true);
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

            image.setImageResource(R.drawable.coscann);

            try {
                bitmap = TextToImageEncode(chaineGenerer);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            image.setImageBitmap(bitmap);
            textview.setText("QR code de la course : " + titre);
            progressDialog.dismiss();

        }
    };
}
