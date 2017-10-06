package com.mymobile.zadanie2.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by tomek on 04.10.17.
 */

public class UserTask implements Parcelable {

    private String description;
    private Date dateOfTask;

    public UserTask(String description, Date dateOfTask) {
        this.description = description;
        this.dateOfTask = dateOfTask;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateOfTask() {
        return dateOfTask;
    }

    public void setDateOfTask(Date dateOfTask) {
        this.dateOfTask = dateOfTask;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeSerializable(dateOfTask);
    }

    public static final Parcelable.Creator<UserTask> CREATOR = new Parcelable.Creator<UserTask>() {
        @Override
        public UserTask createFromParcel(Parcel parcel) {
            return new UserTask(parcel);
        }

        @Override
        public UserTask[] newArray(int size) {
            return new UserTask[size];
        }
    };

    private UserTask(Parcel in) {
        this.description = in.readString();
        this.dateOfTask = (Date) in.readSerializable();
    }
}
