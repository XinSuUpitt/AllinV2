package com.smartdo.suxin.allinv2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by suxin on 9/29/16.
 */

public class Secret implements Parcelable {

    public String id;
    public String text;
    public List<String> content;
    public Date date;

    public Secret(String text, List<String> content) {
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.content = content;
    }

    protected Secret(Parcel in) {
        id = in.readString();
        text = in.readString();
        content = in.readArrayList(String.class.getClassLoader());
    }

    public static final Creator<Secret> CREATOR = new Creator<Secret>() {
        @Override
        public Secret createFromParcel(Parcel in) {
            return new Secret(in);
        }

        @Override
        public Secret[] newArray(int size) {
            return new Secret[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(text);
        parcel.writeList(content);
    }
}
