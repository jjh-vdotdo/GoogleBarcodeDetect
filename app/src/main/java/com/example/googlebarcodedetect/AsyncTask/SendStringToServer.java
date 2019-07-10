package com.example.googlebarcodedetect.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SendStringToServer extends AsyncTask<String, String, String> {

    private String serverIp;
    private String serverPort;
    private int connectTimeout;

    public SendStringToServer(String svrIp, String svrPort, int connectTimeout){
        serverIp = svrIp;
        serverPort = svrPort;
        this.connectTimeout = connectTimeout;
    }

    @Override
    protected String doInBackground(String... params) {

        String result;
        HttpURLConnection urlConnection;
        URL url;
        try {
            url = new URL(String.format("http://%s:%s/pushData?dataString=%s", serverIp, serverPort, URLEncoder.encode(params[0], "utf-8")));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(1000 /* milliseconds */ );
            urlConnection.setConnectTimeout(1000 /* milliseconds */ );
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            int responsecode = urlConnection.getResponseCode();
            Log.d("TAG", "Check responseCode"+responsecode);

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            JSONObject jo = new JSONObject(sb.toString());
            result = jo.getString("pushDataResult");

            urlConnection.disconnect();

        } catch (IOException | JSONException e) {
            Log.e(this.getClass().getSimpleName(), e.toString());
            return "fail";
        }finally {
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
