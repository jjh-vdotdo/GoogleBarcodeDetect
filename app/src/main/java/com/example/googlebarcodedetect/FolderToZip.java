package com.example.googlebarcodedetect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cognex.mobile.barcode.sdk.ReadResult;
import com.cognex.mobile.barcode.sdk.ReadResults;
import com.cognex.mobile.barcode.sdk.ReaderDevice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FolderToZip extends AppCompatActivity {

    /**
     * 지정된 폴더를 zip 파일로 압축한다
     * @param folder = 압축 대상 디렉토리 && 저장 zip 파일 이름
     */

    Button zip_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        zip_btn = (Button)findViewById(R.id.zip_images);

        zip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zipImages();
            }
        });

    }

    public void zipImages(){

        File folder = new File(Environment.getExternalStorageDirectory().toString()+"/Upload_test/");

        if (folder.exists()){
            File[] allFiles = folder.listFiles(); // Upload_test 폴더에 존재하는 파일 리스트를 allFiles 배열에 저장
            String path = folder.getAbsolutePath(); // Upload_test 폴더의 절대경로

            int size = allFiles.length; //
            String[] fullpath = new String[size]; // allFiles 의 크기만큼 배열 생성

            for (int i = 0; i<allFiles.length; i++){ // 파일들의 절대경로를 구해서 배열에 저장
                fullpath[i] = allFiles[i].getAbsolutePath();
            }
            zip(fullpath, path+".zip"); // zip 파일 생성
        }
    }

    public void zip(String[] inputFilePath, String zipFileName){ // 실제로 압축하는 메소드, 압축을 진행할 파일 경로들과 압축파일 명을 전달받는다
        try {
            BufferedInputStream bufferedInputStream = null;
            FileOutputStream fileOutputStream = new FileOutputStream(zipFileName);
            ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream( // 압축파일이 생성될 경로 지정
                    fileOutputStream));
            byte[] data = new byte[1024];

            for (int i = 0; i < inputFilePath.length; i++) {
                Log.v("compress", "Adding: " + inputFilePath[i]);
                FileInputStream fileInputStream = new FileInputStream(inputFilePath[i]);
                bufferedInputStream = new BufferedInputStream(fileInputStream, 1024);

                ZipEntry entry = new ZipEntry(inputFilePath[i].substring(inputFilePath[i].lastIndexOf("/") + 1));
                zipOutputStream.putNextEntry(entry); // ZipEntry 객체를 이용해 파일들을 압축할 공간에 집어넣는다
                int count;

                while ((count = bufferedInputStream.read(data, 0, 1024)) != -1) { // 압축 진행할 파일이 존재하지 않을 때까지 진행
                    zipOutputStream.write(data, 0, count);
                }
                bufferedInputStream.close();
            }
            zipOutputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
