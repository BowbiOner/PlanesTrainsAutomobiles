package com.example.goose_pb.outofsync;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.example.goose_pb.outofsync.model.Trains;
import com.example.goose_pb.outofsync.parser.TrainsXMLParser;


public class MainActivity extends AppCompatActivity {
    TextView do_task;
    ProgressBar pb;
    List<NSync> nsyncs;
    // We will now retrieve a list of Flowers rather than raw XML
    List<Trains> trainList;
    //constant string to hold the url to where the images are stored on localhost
    private static final String PHOTOS_BASE_URL = "http://10.0.2.2:8888/planes-trains-automobiles/img/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pb = (ProgressBar) findViewById(R.id.pb);
        pb.setVisibility(View.INVISIBLE);


        do_task = (TextView) findViewById(R.id.do_task);
        do_task.setMovementMethod(new ScrollingMovementMethod());

        nsyncs = new ArrayList<>();


    }
    public class NSync extends AsyncTask<String, String, List<Trains>>{

        @Override
        protected void onPreExecute(){
            updateDisplay();
            if(nsyncs.size() == 1) {
                pb.setVisibility(View.VISIBLE);
            }
        }
        @Override
        protected List<Trains> doInBackground(String... params) {
            publishProgress(params);
            // ask the HttpManager to go to this uri and get the data
            // getData should return a string of XML (the URI above request xml)
            String content = HttpManager.getData(params[0]);
            trainList = TrainsXMLParser.parseFeed(content);

            //loop through each train object do a network reuqest for each image
            //store each of the phots in a bitmap var in the train obeject
            for (Trains train : trainList){
                try{
                    String imgUrl = PHOTOS_BASE_URL + train.getTrainImg();
                    InputStream in = (InputStream) new URL(imgUrl).getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    train.setBitmap(bitmap);
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //now we return the trainlist not just content, so the whole array of objects
            return trainList;

        }



        protected void onPostExecute(String s) {

            // pass result that was received from doInBackground() into the Parser
            trainList = TrainsXMLParser.parseFeed(s);
            updateDisplay();
            nsyncs.remove(this);
            if(nsyncs.isEmpty()) {
                pb.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onProgressUpdate(String... values){
            super.onProgressUpdate();
//            for(int i = 0; i < 3; i++){
//                updateDisplay("We are working with : " + values[i]);
//            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            NSync syncup = new NSync();
//            //link for live data
            syncup.execute("http://api.irishrail.ie/realtime/realtime.asmx/getCurrentTrainsXML");
            //link for static data so i can add images
//            syncup.execute("http://10.0.2.2:8888/planes-trains-automobiles/pta.xml");

            nsyncs.add(syncup);
        }

        return false;
    }

    protected void updateDisplay(){
        // Loop through the trainList displaying the name of each flower.
        if (trainList != null){
            for (Trains train : trainList) {
                do_task.append(train.getPubMsg() +"\n");
            }
        }
    }
    private void requestData(String uri) {
        NSync syncup = new NSync();
        syncup.execute(uri);
    }
    public View getView (int Position, View convertView, ViewGroup parent) {
        Context context = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.single_train, parent, false);

        int position = 0;
        Trains trains = trainList.get(position);

        TextView tv = (TextView) view.findViewById(R.id.trainText);
        tv.setText(trains.getTrainStatus());

        ImageView iv = (ImageView) findViewById(R.id.trainImage);
        iv.setImageBitmap(trains.getBitmap());

    return view;
    }
}
