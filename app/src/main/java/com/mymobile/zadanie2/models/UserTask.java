package com.mymobile.zadanie2.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.mymobile.zadanie2.utils.NotificationID;

import java.util.Date;

/**
 * Created by tomek on 04.10.17.
 */

public class UserTask implements Parcelable {

    private int id;
    private String description;
    private Date dateOfTask;
    private boolean notification;

    public UserTask(String description, Date dateOfTask, boolean notification) {
        this.id = NotificationID.getID();
        this.description = description;
        this.dateOfTask = dateOfTask;
        this.notification = notification;
    }

    public int getId() {
        return id;
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

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(description);
        parcel.writeSerializable(dateOfTask);
        parcel.writeByte((byte)(notification ? 1 : 0));
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
        this.id = in.readInt();
        this.description = in.readString();
        this.dateOfTask = (Date) in.readSerializable();
        this.notification = (in.readByte() > 0);
    }
}
