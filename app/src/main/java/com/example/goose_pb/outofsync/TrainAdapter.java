package com.example.goose_pb.outofsync;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goose_pb.outofsync.model.Trains;
import java.util.List;

/**
 * Created by goose_pb on 01/03/2017.
 */

public class TrainAdapter extends ArrayAdapter<Trains> {
    private Context context;
    private List<Trains> trainList;
    public TrainAdapter(Context context, int resource, List<Trains> objects) {
        super(context, resource, objects);
        this.context = context;
        this.trainList = objects;
    }
    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.single_train, parent, false);

        Trains trains = trainList.get(position);

        TextView tv = (TextView) view.findViewById(R.id.trainText);
        tv.setText(trains.getPubMsg());

        ImageView iv = (ImageView) view.findViewById(R.id.trainImage);
        iv.setImageBitmap(trains.getBitmap());
//        iv.setImageURI(Uri.parse(trains.getTrainImg()));

        return view;
    }
}
