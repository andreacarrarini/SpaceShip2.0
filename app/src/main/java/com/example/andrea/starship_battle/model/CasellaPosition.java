package com.example.andrea.starship_battle.model;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andrea on 17/08/17.
 */

public class CasellaPosition implements Parcelable{

    //to know which drawable is
    private String imageName;

    public CasellaPosition() {}

    protected CasellaPosition(Parcel in) {
        imageName = in.readString();
    }

    public static final Creator<CasellaPosition> CREATOR = new Creator<CasellaPosition>() {
        @Override
        public CasellaPosition createFromParcel(Parcel in) {
            return new CasellaPosition(in);
        }

        @Override
        public CasellaPosition[] newArray(int size) {
            return new CasellaPosition[size];
        }
    };

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(imageName);
    }
}
