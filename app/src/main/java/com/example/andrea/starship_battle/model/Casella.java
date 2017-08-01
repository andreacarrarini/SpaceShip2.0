package com.example.andrea.starship_battle.model;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by utente on 16/07/2017.
 */

public class Casella implements Serializable {

    ImageView imageView;
    Boolean occupata=false;
    Boolean affondata =false;

    public Casella(ImageView imageView, Boolean o, Boolean x){
        this.imageView = imageView;
        this.occupata = o;
        this.affondata = x;
    }

    public void setDrawable( Drawable d){
        imageView.setImageDrawable(d);
    }

    public Drawable getDrawable() {return imageView.getDrawable();}

    public int getImageViewId(){return imageView.getId();}

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

}
