package com.example.googlebarcodedetect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    int FolderName = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button test_btn = (Button)findViewById(R.id.decode_test_button);
        Button Move_to_decoder_Btn = (Button)findViewById(R.id.move_to_decoder);
        Button MoveZipBtn = (Button)findViewById(R.id.move_to_zip);
        Button MoveZipToServerBtn = (Button)findViewById(R.id.zip_to_pc);
        Button Move_to_sqlite = (Button)findViewById(R.id.to_sqlite);

        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView)findViewById(R.id.imgview);
                TextView txtView = (TextView)findViewById(R.id.txtContent);

                Bitmap bitmap = BitmapFactory.decodeResource(
                        getApplicationContext().getResources(),
                        R.drawable.puppy);

                imageView.setImageBitmap(bitmap);

                BarcodeDetector detector =
                        new BarcodeDetector.Builder(getApplicationContext())
                                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                                .build();

                if (!detector.isOperational()){
                    txtView.setText("Could not set up the detector!");
                }

                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<Barcode> barCodes = detector.detect(frame);

                Log.d("TAG","is barcode detected??"+barCodes+"size::"+barCodes.size());

                Barcode thisCode = barCodes.valueAt(0);
                txtView.setText(thisCode.rawValue);

                SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
                Date now = new Date();
                String fileName = format.format(now)+".txt";

                try {
                    File root = new File(Environment.getExternalStorageDirectory(),"First");

                    if (!root.exists()){
                        root.mkdirs();
                    }
                    File Firstfile = new File(root, fileName);

                    FileWriter writer = new FileWriter(Firstfile, true);
                    writer.append(thisCode.rawValue).append("\n").append("test");
                    writer.flush();
                    writer.close();

                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        });

        Move_to_decoder_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GoogleDecoder.class);
                startActivity(intent);
            }
        });

        MoveZipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FolderToZip.class);
                startActivity(intent);
            }
        });

        MoveZipToServerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FileToServer.class);
                startActivity(intent);
            }
        });

        Move_to_sqlite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SQLite_Activity.class);
                startActivity(intent);
            }
        });

    }
}
