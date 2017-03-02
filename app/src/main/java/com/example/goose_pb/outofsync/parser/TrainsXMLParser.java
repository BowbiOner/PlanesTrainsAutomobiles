package com.example.goose_pb.outofsync.parser;

/**
 * Created by goose_pb on 22/02/2017.
 */
import com.example.goose_pb.outofsync.model.Trains;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


// XmlPullParser - Built in feature of the Android SDK - can do a forward only read of XML data
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
public class TrainsXMLParser {

    public static List<Trains> parseFeed(String content){
        try {
            // Establish Variables that you need to keep track of where you are
            // are you in a data item you care about
            boolean inDataItemTag = false;
            //Which XML tag are you current in
            String currentTagName = "";
            // the Train object you are currently constructing from the XML
            Trains train = null;
            // Full list of trains as you pull them out of the XML
            List<Trains> trainList = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            // content is the complete XML content that was passed in from the calling program
            parser.setInput(new StringReader(content));

            int eventType = parser.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT){

                switch (eventType){
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();

                        // if starting a new product create a new train object to start building it up.
                        if (currentTagName.equals("objTrainPositions")) {
                            inDataItemTag = true;
                            train = new Trains();
                            trainList.add(train);
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        // if leaving current product
                        if (parser.getName().equals("objTrainPositions")) {
                            inDataItemTag = false;
                        }
                        currentTagName = "";
                        break;

                    case XmlPullParser.TEXT:
                        if (inDataItemTag && train != null) {
                            switch (currentTagName) {
                                case "TrainStatus":
                                    train.setTrainStatus(parser.getText());
                                    break;
                                case "TrainLatitude":
                                    train.setTrainLat(Double.parseDouble(parser.getText()));
                                    break;
                                case "TrainLongitude":
                                    train.setTrainLng(Double.parseDouble(parser.getText()));
                                    break;
                                case "TrainCode":
                                    train.setTrainCode(parser.getText());
                                    break;
                                case "TrainDate" :
                                    train.setTrainDate(parser.getText());
                                    break;
                                case "PublicMessage" :
                                    train.setPubMsg(parser.getText());
                                case "Direction" :
                                    train.setDirectionD(parser.getText());
                                default:
                                    break;
                            }
                        }
                        break;
                }

                eventType = parser.next();

            }

            // return the complete list that was generated above
            return trainList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
                }
}


