package com.example.googlebarcodedetect.Recyclerview_model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * SQLite DataBase에 이용되는 데이터 모델
 * Parcelable : 하나 이상의 데이터 모음을 한꺼번에 전달하고 받아볼수있다.(직렬화 개념)
 */

public class Contact implements Parcelable {

    private int id;
    private String name;
    private String content;

    public Contact(){
    }

    protected Contact(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>(){

        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String Content){
        this.content = Content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) { //데이터들을 직렬화 시켜주는 메소드, dest에 순차적으로 데이터들을 저장시킨다
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.content);
    }
}
