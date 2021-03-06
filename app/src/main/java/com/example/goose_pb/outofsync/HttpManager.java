package com.example.goose_pb.outofsync;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by goose_pb on 08/02/2017.
 */

public class HttpManager {

    public static String getData(String uri){
        //import the buffered reader class and instatiate and object of it "reader" to null
        BufferedReader reader = null;

        try {
            //import the java url class, create a url object called "url" and the url we will accept is uri
            URL url = new URL(uri);

            //open up said connection above using the HTTPURLCONNECTION
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;

            //while there is still data left to read the reader will continue to write to the input stream
            while ((line = reader.readLine()) != null) {
                sb.append(line + " \n");
            }

            //now convert the stringbuilder to a string then return it to the Main Activity
            return sb.toString();

        } catch (Exception e) {
            //if there is a poor url print the stack trace and return nothing
            e.printStackTrace();
            return null;
        }


        finally {
            if (reader != null) {
                try {
                    //close the reader
                    reader.close();
                } catch (IOException e) {
                    //if there is a i/o exception then print the stack trace and retun nothing
                    e.printStackTrace();
                    return null;
                }

            }
        }
    }

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

