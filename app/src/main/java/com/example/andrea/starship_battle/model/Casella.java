package com.example.andrea.starship_battle.model;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by Diletta on 16/07/2017.
 */

public class Casella {

    private ImageView imageView;
    private Boolean occupata;

    public Casella(ImageView imageView, Boolean occupata) {
        this.imageView = imageView;
        this.occupata = occupata;
    }


    public void setDrawable(Drawable d) {
        imageView.setImageDrawable(d);
    }

    public Drawable getDrawable() {
        return imageView.getDrawable();
    }

    public int getImageViewId() {
        return imageView.getId();
    }

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
}

