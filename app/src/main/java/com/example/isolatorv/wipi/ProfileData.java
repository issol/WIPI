package com.example.isolatorv.wipi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by isolatorv on 2017. 10. 19..
 */

public class ProfileData implements Parcelable{
    private String userName;
    private String userEmail;
    private String uniqueID;
    private int sno;

    public String p_name, p_type, p_age;

    public ProfileData(){

    }


    public ProfileData(Parcel in){
        readFromParcel(in);
    }

    public ProfileData(String userName, String userEmail, String uniqueID) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.uniqueID = uniqueID;

    }


    public ProfileData(int sno,String userName, String userEmail, String uniqueID) {
        this.sno = sno;
        this.userName = userName;
        this.userEmail = userEmail;
        this.uniqueID = uniqueID;

    }



    public ProfileData(int sno, String userName, String userEmail, String uniqueID, String p_name, String p_type, String p_age){
        this.sno = sno;
        this.userName = userName;
        this.userEmail = userEmail;
        this.uniqueID = uniqueID;
        this.p_name = p_name;
        this.p_type =p_type;
        this.p_age =p_age;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_type() {
        return p_type;
    }

    public void setP_type(String p_type) {
        this.p_type = p_type;
    }

    public String getP_age() {
        return p_age;
    }

    public void setP_age(String p_age) {
        this.p_age = p_age;
    }


    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(sno);
        parcel.writeString(userName);
        parcel.writeString(userEmail);
        parcel.writeString(uniqueID);
        parcel.writeString(p_name);
        parcel.writeString(p_type);
        parcel.writeString(p_age);


    }

    private void readFromParcel(Parcel in){
        sno = in.readInt();
        userName = in.readString();
        userEmail = in.readString();
        uniqueID = in.readString();
        p_name = in.readString();
        p_type = in.readString();
        p_age = in.readString();


    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ProfileData createFromParcel(Parcel in){
            return new ProfileData(in);
        }

        public ProfileData[] newArray(int size) {
            return new ProfileData[size];
        }
    } ;

}
