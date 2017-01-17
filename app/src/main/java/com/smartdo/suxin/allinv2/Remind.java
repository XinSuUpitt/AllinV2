package com.smartdo.suxin.allinv2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by suxin on 9/9/16.
 */
public class Remind implements Parcelable{

    public String id;
    public String text;
    public boolean done;
    public Date remindDate;


    public Remind(String text, Date remindDate) {
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.done = false;
        this.remindDate = remindDate;
    }

    protected Remind(Parcel in) {
        id = in.readString();
        text = in.readString();
        done = in.readByte() != 0;
        long date = in.readLong();
        remindDate = date == 0 ? null : new Date(date);
    }

    public static final Creator<Remind> CREATOR = new Creator<Remind>() {
        @Override
        public Remind createFromParcel(Parcel in) {
            return new Remind(in);
        }

        @Override
        public Remind[] newArray(int size) {
            return new Remind[size];
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
        parcel.writeByte((byte) (done ? 1 : 0));
        parcel.writeLong(remindDate != null ? remindDate.getTime() : 0);
    }
}
