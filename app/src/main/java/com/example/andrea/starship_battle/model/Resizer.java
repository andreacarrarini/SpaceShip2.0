package com.example.andrea.starship_battle.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;


public class Resizer {

    private Context context;
    public Resizer(Context currentContext){
        this.context = currentContext;
    }

    public void resize(View view, int dim) {
        for (int index = 0; index < ((ViewGroup) view).getChildCount(); ++index) {
            View nextChild = ((ViewGroup) view).getChildAt(index);
            ViewGroup.LayoutParams lp = nextChild.getLayoutParams();
            //getting screen dimen
            Display d = ((Activity)context).getWindowManager().getDefaultDisplay();
            Point outsize = new Point();
            d.getSize(outsize);
            lp.width = outsize.y / dim;
            lp.height = outsize.y / dim;
            nextChild.setLayoutParams(lp);
        }
    }
}
