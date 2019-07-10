package com.example.googlebarcodedetect;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;

import com.cognex.mobile.barcode.sdk.ReadResult;
import com.cognex.mobile.barcode.sdk.ReadResults;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 특정 폴더에 있는 이미지를 구글 바코드 디텍터를 이용해서 디텍팅 후
 * 성공(O), 실패(X)를 판별 후 파일이름/성공유무/디텍팅결과 가 담긴 txt파일을 생성하는 클래스
 */

public class GoogleDecoder extends AppCompatActivity {

    int succ = 0;
    int fal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button run = (Button)findViewById(R.id.run);

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImages();
            }
        });
    }

    public void getImages(){ // 특정 폴더에 있는 이미지를 읽어오는 메소드

        BarcodeDetector detector = // 디텍팅에 사용될 구글디텍터를 빌드한다
                new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(Barcode.CODE_39|Barcode.CODE_93|Barcode.CODE_128|
                                Barcode.UPC_A|Barcode.UPC_E|Barcode.EAN_13|Barcode.EAN_8|Barcode.CODABAR|Barcode.AZTEC|Barcode.ITF)
                        .build();

        for(int f=0; f < 20; f++) { // 20개의 폴더를 차례대로 선택 후 이미지들을 읽어들인다

            String folderName = String.format("%03d", f); // 검색할 폴더명 = 000부터 f만큼 채우기

            File folder = new File(Environment.getExternalStorageDirectory().toString()
                    + "/barcode_test/"+folderName+"/"); // barcode_test 폴더 내부에 있는 folderName에 해당하는 특정 폴더를 folder라는 변수에 저장

            if (folder.exists()) { // 찾는 폴더가 있다면

                File[] allFiles = folder.listFiles(); // 폴더 내 이미지 파일 리스트를 배열로 저장
                String txtName = folderName + ".txt"; // 결과들을 저장 할 txt 파일명
                File root = new File(Environment.getExternalStorageDirectory(),"First"); // txt 파일이 저장될 위치

                if (!root.exists()){ // txt 파일을 저장할 폴더가 존재하지 않는다면 폴더를 생성
                    root.mkdirs();
                }

                File Firstfile = new File(root, txtName);

                try {
                    FileWriter writer = new FileWriter(Firstfile, true);

                    for (int i = 0; i < allFiles.length; i++) {
                        String fullPath = allFiles[i].getAbsolutePath();
                        Bitmap bmp = BitmapFactory.decodeFile(fullPath);

                        String fileName = fullPath.substring(fullPath .lastIndexOf("/")+1); // 이미지 경로에서 이미지 파일명을 추출

                        Frame frame = new Frame.Builder().setBitmap(bmp).build();
                        SparseArray<Barcode> barCodes = detector.detect(frame); // frame으로 변환한 이미지들을 디텍터에 전달

                        String orgBarcode = fullPath.substring(fullPath.lastIndexOf('_')+1, fullPath.lastIndexOf('.'));
                        String ox = "";
                        String googleBarcode = "";

                        if( barCodes != null && barCodes.size() > 0 && orgBarcode.contains(barCodes.valueAt(0).rawValue)) { // 디텍팅이 성공이라면 결과값은 o
                            ox = "O";
                            succ++;
                            googleBarcode = barCodes.valueAt(0).rawValue; // 디텍팅결과가 저장된 barCodes에서 결과값을 가져온다
                        }else{ // 디텍팅이 실패했다면 결과는 x
                            ox = "X";
                            fal++;
                        }
                        try {
                            writer.append(String.format("%s\t%s\t%s", fileName, ox, googleBarcode)); // txt 파일에 파일명, 성공유무, 결과값을 차례로 작성한다
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        bmp.recycle();
                    }

                    int per = succ * 100 / (succ+fal);
                    Log.d("SUCC PER", ""+per);

                    writer.flush();
                    writer.close();
                    Log.d("TAG","success_total::"+succ+"false_total::"+fal);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("TAG", "filename ::" + Arrays.toString(allFiles));
            }
        }
    }
}
