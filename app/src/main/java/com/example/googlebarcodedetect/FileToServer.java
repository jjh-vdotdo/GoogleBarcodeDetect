package com.example.googlebarcodedetect;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.googlebarcodedetect.AsyncTask.SendFileTest;
import com.example.googlebarcodedetect.AsyncTask.SendStringToServer;

import java.io.File;

public class FileToServer extends AppCompatActivity {

    /**
     * 지정된 zip파일을 Post방식으로 서버에 저장한다
     * @method sendTest = 스트링값을 전달하는 테스트 메소드
     * @param zipFile = 전송할 zip파일의 절대경로
     */

    String TAG = "Send_To_Server";

    String pref_serverIp = "192.168.1.214";
    String pref_serverPort = "8787";
    int connTimeout = 30000;

    File zipFile = new File(Environment.getExternalStorageDirectory() // 전송할 파일의 경로
            .toString()+"/Upload_testtest.zip");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Button zip_to_server = (Button)findViewById(R.id.move_to_server);

        Log.d(TAG,"Check zipFile: "+zipFile);

        zip_to_server.setOnClickListener(v -> {
//                sendTest();
            HttpFileSend(pref_serverIp,pref_serverPort,zipFile);
        });
    }

    public void sendTest(){ //String 값을 서버에 전달하는 테스트 메소드
        new SendStringToServer(pref_serverIp, pref_serverPort, connTimeout).execute("hello");
        Log.d("Sending File","Sending msg Test");
    }

    private void HttpFileSend(final String svrIp, final String svrPort, final File file){
        SendFileTest sendFileTest = new SendFileTest(svrIp, svrPort, file, (s)->{ // SendFileTest 라는 Async로 파라메터(서버 아이피, 포트, 전송할 파일)을 넘겨준다
            if( "".equals(s) ) // Async 로 부터 리턴된 값이 "성공" 이라면 토스트 메세지를 띄운다
                s = "성공";
            Toast.makeText(FileToServer.this, s, Toast.LENGTH_SHORT).show();
        });
        sendFileTest.execute();
    }
}
