package com.example.googlebarcodedetect.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.googlebarcodedetect.Interface.StringCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendFileTest extends AsyncTask<Void, Void, String> {

    /**
     * AsyncTask가 동작하는 클래스
     * @param svrIp = zip파일을 저장할 서버 ip
     * @param svrPort = zip파일을 저장할 서버 port
     * @param file = FileToServer 클래스에서 넘겨받은 zip파일
     */

    private String TAG = "SendFileTest";
    private String svrIp;
    private String svrPort;
    private File file;
    private StringCallback callback;

    public SendFileTest(String svrIp, String svrPort, File file, StringCallback callback){
        this.svrIp = svrIp;
        this.svrPort = svrPort;
        this.file = file;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Void... voids) {

        String inputLine = "";

        String boundary = "^-----^";
        String LINE_FEED = "\r\n";
        String charset = "UTF-8";
        OutputStream outputStream;
        PrintWriter writer;

        try{

                    URL url = new URL(String.format("http://%s:%s/UploadFile2", svrIp, svrPort));

            Log.d("TAG","detail URL"+url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Multipart/form-data형식에 대한 RequestBody를 만들어주는 과정.
            connection.setRequestProperty("Content-Type", "multipart/form-data;charset=utf-8;boundary=" + boundary);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(15000);
            connection.connect();


            // 경로로 부터 읽어들인 파일을 byte단위로 변환 후 통신을 통해 외부로 내보내는 과정.
            outputStream = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);

            FileInputStream inputStream = new FileInputStream(file); // InputStream == byte단위로 데이터를 읽어들인다.
            byte[] buffer = new byte[(int)file.length()];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) { //buffer값이 0일때까지 데이터를 write
                outputStream.write(buffer, 0, bytesRead); // OutputStream == 외부로(서버) 데이터를 내보내는 기능.
            }
            outputStream.flush();
            inputStream.close();
            writer.append(LINE_FEED);
            writer.flush();
            writer.close();

            int responseCode = connection.getResponseCode(); // 요청에 대한 응답코드
            Log.d(TAG,"Check responseCode"+responseCode);

            //요청에 대한 응답(리턴값)을 처리하는 작업
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) { // http 코드가 200 / 201인 경우
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

            } else { // response 코드가 200 / 201아닌 경우 에러 메세지를 담는다
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }

        } catch (ConnectException e) {
            Log.e(TAG, "ConnectException");
            e.printStackTrace();
            inputLine = e.getMessage();
        } catch (Exception e){
            e.printStackTrace();
            inputLine = e.getMessage();
        }

        return inputLine;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // main activity
        callback.callDelegate(result);
    }
}
