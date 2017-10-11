package com.jarrar.beaconreceiver;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jarrar on 21-Nov-16.
 */

public class Beacon implements Parcelable {
    public String url;
    public String address;
    public int rssi;
    public int txPower;
    public double distance;

    public Beacon() {

    }

    private Beacon(Parcel in) {
        url = in.readString();
        address = in.readString();
        rssi = in.readInt();
        txPower = in.readInt();
        distance = in.readDouble();
    }

    public static final Creator<Beacon> CREATOR = new Creator<Beacon>() {
        @Override
        public Beacon createFromParcel(Parcel in) {
            return new Beacon(in);
        }

        @Override
        public Beacon[] newArray(int size) {
            return new Beacon[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeString(address);
        parcel.writeInt(rssi);
        parcel.writeInt(txPower);
        parcel.writeDouble(distance);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public void setTxPower(int txPower) {
        this.txPower = txPower;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
