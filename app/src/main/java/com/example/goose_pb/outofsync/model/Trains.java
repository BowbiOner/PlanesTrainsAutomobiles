package com.example.goose_pb.outofsync.model;

import android.graphics.Bitmap;

/**
 * Created by goose_pb on 22/02/2017.
 */

public class Trains {
    private String trainStatus;
    private double trainLat;
    private double trainLng;
    private String trainCode;
    private String trainDate;
    private String pubMsg;
    private String directionD;
    private String trainImg;
    //bitmap for mapping text names to the links of the images
    private Bitmap bitmap;

    public String getTrainStatus() {
        return trainStatus;
    }

    public void setTrainStatus(String trainStatus) {
        this.trainStatus = trainStatus;
    }

    public double getTrainLat() {
        return trainLat;
    }

    public void setTrainLat(double trainLat) {
        this.trainLat = trainLat;
    }

    public double getTrainLng() {
        return trainLng;
    }

    public void setTrainLng(double trainLng) {
        this.trainLng = trainLng;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public String getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(String trainDate) {
        this.trainDate = trainDate;
    }

    public String getPubMsg() {
        return pubMsg;
    }

    public void setPubMsg(String pubMsg) {
        this.pubMsg = pubMsg;
    }

    public String getDirectionD() {
        return directionD;
    }

    public void setDirectionD(String directionD) {
        this.directionD = directionD;
    }


    public String getTrainImg() {
        return trainImg;
    }

    public void setTrainImg(String trainImg) {
        this.trainImg = trainImg;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
