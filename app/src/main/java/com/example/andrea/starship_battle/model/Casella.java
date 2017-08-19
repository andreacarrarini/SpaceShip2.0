package com.example.andrea.starship_battle.model;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * Created by Diletta on 16/07/2017.
 */

public class Casella implements Parcelable {

    ImageView imageView;
    Boolean occupata = false;
    Boolean affondata = false;

    public Casella(ImageView imageView, Boolean o, Boolean x) {
        this.imageView = imageView;
        this.occupata = o;
        this.affondata = x;
    }

    protected Casella(Parcel in) {
        Bitmap btm = (Bitmap) in.readParcelable(Bitmap.class.getClassLoader());

        this.imageView.setImageDrawable( new BitmapDrawable(btm));
        this.occupata = in.readByte() != 0;
        this.affondata = in.readByte() != 0;
    }

    //PARCABLE CREATOR for Casella
    public static final Creator<Casella> CREATOR = new Creator<Casella>() {
        @Override
        public Casella createFromParcel(Parcel in) {
            return new Casella(in);
        }

        @Override
        public Casella[] newArray(int size) {
            return new Casella[size];
        }
    };

    public void setDrawable(Drawable d) { imageView.setImageDrawable(d); }

    public Drawable getDrawable() { return imageView.getDrawable(); }

    public int getImageViewId() { return imageView.getId(); }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Boolean getOccupata() {
        return occupata;
    }

    public void setOccupata(Boolean occupata) {
        this.occupata = occupata;
    }

    public Boolean getAffondata() {
        return affondata;
    }

    public void setAffondata(Boolean affondata) {
        this.affondata = affondata;
    }

    @Override
    public String toString() {
        return "Casella [imageView " + this.getImageView() + "occupata " + this.getOccupata() + "affondata " + this.getAffondata() + "]";
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     * @see #CONTENTS_FILE_DESCRIPTOR
     */
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

        Bitmap btm =  ((BitmapDrawable)getDrawable()).getBitmap();

        dest.writeParcelable(btm, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeByte((byte) (occupata ? 1 : 0));
        dest.writeByte((byte) (affondata ? 1 : 0));
    }
}
