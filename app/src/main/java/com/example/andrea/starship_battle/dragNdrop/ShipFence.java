package com.example.andrea.starship_battle.dragNdrop;

import android.view.View;
import android.widget.ImageView;

import com.example.andrea.starship_battle.R;
import com.example.andrea.starship_battle.model.Casella;

import java.util.ArrayList;

/**
 * Created by Diletta on 27/07/2017.
 */

public class ShipFence {

    public ArrayList<Casella> setShipFence (View view, int i, ArrayList<Casella> c){

        //Crea un quadrato intorno alla Shipview in cui non è possibile inserire altre navi

        switch (view.getId()) {
            case R.id.id_tie:

                //CASO SX
                if( (i+1)%8==1){
                    for (int j = (i - 8); j>=0 && j < (i - 6) && j<63; j++)
                        drawFench(j, c);
                    for (int j = (i + 8); j>=0 && j < (i + 10) && j<63; j++)
                        drawFench(j, c);

                    drawFench(i+1, c);
                }

                //CASI CENTRALI
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

                //CASO DX
                if( (i+1)%8==0){
                    for (int j = (i - 9); j>=0 && j < (i - 7)&& j<63; j++)
                        drawFench(j, c);

                    for (int j = (i + 7); j>=0 && j < (i + 9)&& j<63; j++)
                        drawFench(j, c);

                    drawFench(i-1, c);
                }

                break;

            case R.id.id_star_dest:

                //CASO SX
                if( (i+1)%8==1){
                    for (int j = (i - 8); j>=0 && j < (i - 5)&& j<63; j++)
                        drawFench(j, c);
                    for (int j = (i + 8); j>=0 && j < (i + 11)&& j<63; j++)
                        drawFench(j, c);

                    drawFench(i+2, c);
                }


                //CASI CENTRALI
                if( (i+1)%8>=2 && (i+1)%8<=7) {
                    for (int j = (i - 9); j>=0 && j < (i - 5)&& j<63; j++)
                        drawFench(j, c);
                    for (int j = (i + 7); j>=0 && j < (i + 11)&& j<63; j++)
                        drawFench(j, c);

                    drawFench(i - 1, c);
                    drawFench(i + 2, c);
                }

                //CASO DX
                if( (i+1)%8==0){
                    for (int j = (i - 9); j>=0 && j < (i - 8)&& j<63; j++)
                        drawFench(j, c);

                    for (int j = (i + 7); j>=0 && j < (i + 8)&& j<63; j++)
                        drawFench(j, c);

                    drawFench(i-1, c);
                }

                break;




            case R.id.id_death_star:

                //CASO SX
                if( (i+1)%8==1){
                    for (int j = (i - 8); j>=0 && j < (i - 5)&& j<63; j++)
                        drawFench(j, c);
                    for (int j = (i + 16); j>=0 && j < (i + 19) && j<63; j++)
                        drawFench(j, c);


                    drawFench(i + 2, c);
                    drawFench(i + 10, c);

                }


                //CASI CENTRALI
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

                //CASO DX
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
        c.get(x).getImageView().setColorFilter(R.color.colorNotShipAllowed); //setVisibility(View.INVISIBLE);*/

    }
}
