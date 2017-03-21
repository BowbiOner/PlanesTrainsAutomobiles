package com.example.goose_pb.outofsync.parser;

import android.provider.Settings;
import android.util.Log;
import android.util.StringBuilderPrinter;

import com.example.goose_pb.outofsync.model.Bikes;
import com.example.goose_pb.outofsync.model.Trains;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by ultra on 20-Mar-17.
 */

public class TrainsJSONParser {

    public static List<Trains> parseFeed(String content) {
        try {
            JSONArray jArray = new JSONArray(content);
            List<Trains> trainList = new ArrayList<>();

            for (int i = 0; i < jArray.length(); i++){
                JSONObject jObj = jArray.getJSONObject(i);
                Trains train = new Trains();

                train.setTrainStatus(jObj.getString("TrainStatus"));
                train.setTrainLat((jObj.getDouble("TrainLatitude")));
                train.setTrainLng((jObj.getDouble("TrainLongitude")));
                train.setTrainCode(jObj.getString("TrainCode"));
                train.setTrainDate((jObj.getString("TrainDate")));
                train.setPubMsg((jObj.getString("PublicMessage")));
                train.setDirectionD(jObj.getString("Direction"));
                train.setTrainImg(jObj.getString("Photo"));
                //add the train object to the list - add the current train object
                trainList.add(train);
            }

            return trainList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }




    }
}
