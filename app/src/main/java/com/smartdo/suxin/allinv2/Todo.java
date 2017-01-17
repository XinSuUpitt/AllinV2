package com.smartdo.suxin.allinv2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * Created by suxin on 9/16/16.
 */
public class Todo implements Parcelable {

    public String id;
    public String name;
    public boolean done;
    //public List<WishList> wishLists;

    public Todo(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.done = false;
        //this.wishLists = new ArrayList<>();
    }

    protected Todo(Parcel in) {
        id = in.readString();
        name = in.readString();
        done = in.readByte() != 0;
        //wishLists = in.readArrayList(WishList.class.getClassLoader());
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
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
        //parcel.writeList(wishLists);
    }
}
