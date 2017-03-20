package com.example.goose_pb.outofsync.parser;

import android.provider.Settings;
import android.util.Log;
import android.util.StringBuilderPrinter;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by ultra on 20-Mar-17.
 */

public class BikesJSONParser {
    //varibles for JSONParser
    static InputStream is = null;
    static JSONObject jObj = null;
    //default constructor
    public BikesJSONParser(){

    }
    //fucntion to actually recieve the JSON from a URL
    public String callApi(String reqURL){
        String res = null;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //read the res
            InputStream in = new BufferedInputStream(conn.getInputStream());
            res = convertStreamToString(in);

        }  catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return res;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while((line = reader.readLine()) != null){
                sb.append(line).append('\n');
            }

        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
    }
}
