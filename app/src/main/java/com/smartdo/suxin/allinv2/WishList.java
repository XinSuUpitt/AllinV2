package com.smartdo.suxin.allinv2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * Created by suxin on 9/7/16.
 */
public class WishList implements Parcelable{


    public String id;
    public String name;
    public boolean done;
    public boolean favorite;

    public WishList(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.done = false;
        this.favorite = false;
    }

    protected WishList(Parcel in) {
        id = in.readString();
        name = in.readString();
        done = in.readByte() != 0;
        favorite = in.readByte() != 0;
    }

    public static final Creator<WishList> CREATOR = new Creator<WishList>() {
        @Override
        public WishList createFromParcel(Parcel in) {
            return new WishList(in);
        }

        @Override
        public WishList[] newArray(int size) {
            return new WishList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeByte((byte) (done ? 1 : 0));
        parcel.writeByte((byte) (favorite ? 1 : 0));
    }
}
