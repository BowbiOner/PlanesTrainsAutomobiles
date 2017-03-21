package com.example.goose_pb.outofsync;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.example.goose_pb.outofsync.model.Trains;
import com.example.goose_pb.outofsync.parser.TrainsJSONParser;
import com.example.goose_pb.outofsync.parser.TrainsXMLParser;


public class MainActivity extends ListActivity {
    TextView do_task;
    ListView bikeLV;
    ProgressBar pb;
    List<NSync> nsyncs;
    // We will now retrieve a list of Flowers rather than raw XML
    List<Trains> trainList;
    //constant string to hold the url to where the images are stored on localhost
    //one for testing on my pc and laptop
    private static final String PHOTOS_BASE_URL = "http://10.0.2.2:8888/planes-trains-automobiles/img/";
//    private static final String PHOTOS_BASE_URL = "http://10.0.2.2:8888/planes-trains-automobiles/img/";
    //URL for JSON
    private static String jURL = "https://api.jcdecaux.com/vls/v1/stations?contract=Dublin&apiKey=ec447add626cfb0869dd4747a7e50e21d39d1850";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //create an instance of the toolbaer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //set the visibility of the above toolbar to visible
        toolbar.setVisibility(View.VISIBLE);
        //setSupportActionBar(toolbar);
        //create a progress bar and get it by the id defined in the conent_main.xml
        pb = (ProgressBar) findViewById(R.id.pb);
        //set the visiblity of the progress bar to being visible
        pb.setVisibility(View.VISIBLE);

        // This array list of tasks allows for control over parallel tasks
        // you know when there are tasks still left to do
        // the application will add and remove AsyncTasks from this list
        nsyncs = new ArrayList<>();

//        requestData("http://api.irishrail.ie/realtime/realtime.asmx/getCurrentTrainsXML");
        //one for testing on my pc and laptop
//        requestData("http://10.0.2.2:80/planes-trains-automobiles/pta.xml");
        requestData("http://10.0.2.2:8888/planes-trains-automobiles/pta.json");
//        requestData("http://10.0.2.2:8888/planes-trains-automobiles/pta.xml");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void requestData(String uri){
        NSync nSync = new NSync();
        nSync.execute(uri);
    }

    public void getData(View v) {
//        requestData("http://api.irishrail.ie/realtime/realtime.asmx/getCurrentTrainsXML");
        //one for testing on my pc and laptop
//        requestData("http://10.0.2.2:80/planes-trains-automobiles/pta.xml");
        requestData("http://10.0.2.2:8888/planes-trains-automobiles/pta.json");
//        requestData("http://10.0.2.2:8888/planes-trains-automobiles/pta.xml");
    }

    //function to check to see if the device has a valid internet connection
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public class NSync extends AsyncTask<String, String, List<Trains>>{

        private final String TAG = MainActivity.class.getSimpleName() ;

        @Override
        protected void onPreExecute(){
//            updateDisplay();
            if(nsyncs.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            nsyncs.add(this);
        }
        @Override
        protected List<Trains> doInBackground(String... params) {
            publishProgress(params);
            // ask the HttpManager to go to this uri and get the data
            // getData should return a string of XML (the URI above request xml)
            String content = HttpManager.getData(params[0]);
//            return content;
            trainList = TrainsJSONParser.parseFeed(content);
//            return content;
//            loop through each train object do a network reuqest for each image
//            store each of the phots in a bitmap var in the train obeject
            for (Trains train : trainList) {
                try {
                    String imgUrl = PHOTOS_BASE_URL + train.getTrainImg();
                    InputStream in = (InputStream) new URL(imgUrl).getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    train.setBitmap(bitmap);
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return trainList;
        }


//        @Override
        protected void onPostExecute(List<Trains> train) {
            for (Trains trains : trainList) {
                Log.i("Train", trains.toString());
            }
            // pass result that was received from doInBackground() into the Parser
        //    trainList = TrainsXMLParser.parseFeed(s);
            updateDisplay();
            nsyncs.remove(this);
            if(nsyncs.isEmpty()) {
                pb.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onProgressUpdate(String... values){
//            super.onProgressUpdate();
//            for(int i = 0; i < 3; i++){
//                updateDisplay("We are working with : " + values[i]);
//            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            NSync syncup = new NSync();
////            //link for live data
//            syncup.execute("http://api.irishrail.ie/realtime/realtime.asmx/getCurrentTrainsXML");
//            //link for static data so i can add images
////            syncup.execute("http://10.0.2.2:8888/planes-trains-automobiles/pta.xml");
//
//            nsyncs.add(syncup);
//            requestData("http://api.irishrail.ie/retime/realtime.asmx/getCurrentTrainsXML");al
            //one for testing on my pc and laptop
//            requestData("http://10.0.2.2:80/planes-trains-automobiles/pta.xml");
//            requestData("http://10.0.2.2:8888/planes-trains-automobiles/pta.xml");
            requestData("http://10.0.2.2:8888/planes-trains-automobiles/pta.json");
        }

        return false;
    }


    protected void updateDisplay(){
//        // Loop through the trainList displaying the name of each flower.
//        if (trainList != null){
//            for (Trains train : trainList) {
//                do_task.append(train.getPubMsg() +"\n");
//            }
//        }
        TrainAdapter adapter = new TrainAdapter(this, R.layout.single_train, trainList);
        setListAdapter(adapter);
    }

//    public View getView (int Position, View convertView, ViewGroup parent) {
//        Context context = null;
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.single_train, parent, false);
//
//        int position = 0;
//        Trains trains = trainList.get(position);
//
//        TextView tv = (TextView) view.findViewById(R.id.trainText);
//        tv.setText(trains.getTrainStatus());
//
//        ImageView iv = (ImageView) findViewById(R.id.trainImage);
//        iv.setImageBitmap(trains.getBitmap());
//
//    return view;
//    }
}
