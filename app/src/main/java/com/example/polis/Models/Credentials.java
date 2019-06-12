package com.example.polis.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Credentials implements Parcelable {

    private String email;
    private String password;



    public Credentials(){
        super();
    }

    public Credentials (Parcel parcel){
        this.email = parcel.readString();
        this.password = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString((this.password));
    }

    public static final Creator<Credentials> CREATOR = new Creator<Credentials>() {
        @Override
        public Credentials createFromParcel(Parcel source) {
            return new Credentials(source);
        }

        @Override
        public Credentials[] newArray(int size) {
            return new Credentials[size];
        }
    };

    public void setEmail (String email){this.email = email;}
    public void setPassword (String password) {this.password = password;}

    public String getEmail () { return email;}
    public  String getPassword() {return password;}


}
