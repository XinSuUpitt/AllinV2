package com.smartdo.suxin.allinv2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * Created by suxin on 9/21/16.
 */
public class Bank implements Parcelable{

    public String id;
    public String credit_categary;
    public String image_id;
    public String holder_first_name;
    public String holder_last_name;
    public String name_Id;
    public String bank_Name;
    public String bank_Account_Number;
    public String expiration_Date;
    public String cvv_Code;
    public String address_1;
    public String address_2;
    public int position;

    public Bank(String expiration_Date) {
        this.id = UUID.randomUUID().toString();
        this.expiration_Date = expiration_Date;
    }

    public Bank(String credit_categary, String holder_first_name, String holder_last_name,
                String name_Id, String bank_Name, String bank_Account_Number,
                String expiration_Date, String cvv_Code, String address_1, String address_2, int position) {
        this.id = UUID.randomUUID().toString();
        this.credit_categary = credit_categary;
        this.holder_first_name = holder_first_name;
        this.holder_last_name = holder_last_name;
        this.name_Id = name_Id;
        this.bank_Name = bank_Name;
        this.bank_Account_Number = bank_Account_Number;
        this.expiration_Date = expiration_Date;
        this.cvv_Code = cvv_Code;
        this.address_1 = address_1;
        this.address_2 = address_2;
        this.position = position;
    }

    protected Bank(Parcel in) {
        id = in.readString();
        credit_categary = in.readString();
        image_id = in.readString();
        holder_first_name = in.readString();
        holder_last_name = in.readString();
        name_Id = in.readString();
        bank_Name = in.readString();
        bank_Account_Number = in.readString();
        expiration_Date = in.readString();
        cvv_Code = in.readString();
        address_1 = in.readString();
        address_2 = in.readString();
        position = in.readInt();
    }

    public static final Creator<Bank> CREATOR = new Creator<Bank>() {
        @Override
        public Bank createFromParcel(Parcel in) {
            return new Bank(in);
        }

        @Override
        public Bank[] newArray(int size) {
            return new Bank[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(credit_categary);
        parcel.writeString(image_id);
        parcel.writeString(holder_first_name);
        parcel.writeString(holder_last_name);
        parcel.writeString(name_Id);
        parcel.writeString(bank_Name);
        parcel.writeString(bank_Account_Number);
        parcel.writeString(expiration_Date);
        parcel.writeString(cvv_Code);
        parcel.writeString(address_1);
        parcel.writeString(address_2);
        parcel.writeInt(position);
    }
}
