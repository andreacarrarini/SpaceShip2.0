package com.example.andrea.starship_battle.dragNdrop;

import android.view.View;

import com.example.andrea.starship_battle.R;
import com.example.andrea.starship_battle.model.Casella;

import java.util.ArrayList;

/**
 * Created by Diletta on 27/07/2017.
 */

public class ShipFence {

    public ArrayList<Casella> setShipFence (View view, int i, ArrayList<Casella> c){

        //Creating a square around the Shipview, where other ships can not be dropped

        switch (view.getId()) {
            case R.id.id_tie:

                //Case: left
                if( (i+1)%8==1){
                    for (int j = (i - 8); j>=0 && j < (i - 6) && j<63; j++)
                        drawFench(j, c);
                    for (int j = (i + 8); j>=0 && j < (i + 10) && j<63; j++)
                        drawFench(j, c);

                    drawFench(i+1, c);
                }

                //Case: central
                if( (i+1)%8>=2 && (i+1)%8<=7) {
                    for (int j = (i - 9); j>=0 && j < (i - 6) && j<63; j++)
                        drawFench(j, c);
                    for (int j = (i + 7); j>=0 && j < (i + 10)&& j<63; j++)
                        drawFench(j, c);
                    if(i - 1 >= 0) {
                        drawFench(i-1,c);
                    }
                    drawFench(i+1,c);
                }

                //Case: right
                if( (i+1)%8==0){
                    for (int j = (i - 9); j>=0 && j < (i - 7)&& j<63; j++)
                        drawFench(j, c);

                    for (int j = (i + 7); j>=0 && j < (i + 9)&& j<63; j++)
                        drawFench(j, c);

                    drawFench(i-1, c);
                }

                break;

            case R.id.id_star_dest:

                //Case: left
                if( (i+1)%8==1){
                    for (int j = (i - 8); j>=0 && j < (i - 5)&& j<63; j++)
                        drawFench(j, c);
                    for (int j = (i + 8); j>=0 && j < (i + 11)&& j<63; j++)
                        drawFench(j, c);

                    drawFench(i+2, c);
                }

                //Case: central
                if( (i+1)%8>=2 && (i+1)%8<=7) {
                    for (int j = (i - 9); j>=0 && j < (i - 5)&& j<63; j++)
                        drawFench(j, c);
                    for (int j = (i + 7); j>=0 && j < (i + 11)&& j<63; j++)
                        drawFench(j, c);

                    drawFench(i - 1, c);
                    drawFench(i + 2, c);
                }

                //Case: right
                if( (i+1)%8==0){
                    for (int j = (i - 9); j>=0 && j < (i - 8)&& j<63; j++)
                        drawFench(j, c);

                    for (int j = (i + 7); j>=0 && j < (i + 8)&& j<63; j++)
                        drawFench(j, c);

                    drawFench(i-1, c);
                }

                break;




            case R.id.id_death_star:

                //Case: left
                if( (i+1)%8==1){
                    for (int j = (i - 8); j>=0 && j < (i - 5)&& j<63; j++)
                        drawFench(j, c);
                    for (int j = (i + 16); j>=0 && j < (i + 19) && j<63; j++)
                        drawFench(j, c);


                    drawFench(i + 2, c);
                    drawFench(i + 10, c);

                }

                //Case: central
                if( (i+1)%8>=2 && (i+1)%8<7) {
                    for (int j = (i - 9); j>=0 && j < (i - 5) && j<63; j++)
                        drawFench(j, c);
                    for (int j = (i + 15); j>=0 && j < (i + 19)&& j<63; j++)
                        drawFench(j, c);

                    drawFench(i - 1, c);
                    drawFench(i + 2, c);
                    drawFench(i + 7, c);
                    drawFench(i + 10, c);

                }

                //Case: right
                if( (i+1)%8==7){
                    for (int j = (i - 9); j>=0 && j < (i - 6) && j<63; j++)
                        drawFench(j, c);

                    for (int j = (i + 15); j>=0 && j < (i + 17) && j<63; j++)
                        drawFench(j, c);

                    drawFench(i-1, c);
                    drawFench(i + 7, c);

                }

                break;
        }
        return c;


    }

    private void drawFench(int x, ArrayList<Casella> c){
        c.get(x).getImageView().setOnDragListener(null);
        c.get(x).getImageView().setColorFilter(R.color.colorNotShipAllowed);

    }
}
