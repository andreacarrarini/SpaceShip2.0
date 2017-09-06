package com.example.andrea.starship_battle.dragNdrop;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import com.example.andrea.starship_battle.R;
import com.example.andrea.starship_battle.model.Casella;
import com.example.andrea.starship_battle.model.CasellaPosition;
import com.example.andrea.starship_battle.model.Fazione;

import java.util.ArrayList;

/**
 * Created by Diletta on 15/07/2017.
 */

public class ShipPosition{

    private Context context;
    public ShipPosition(Context currentContext){
        this.context = currentContext;
    }

    private int numTie=0;
    private int numStarDest=0;
    private int numStarDeath=0;


    public  ArrayList<Casella> setPositionShip(View view, View cella, ArrayList<Casella> caselleTableList, Fazione fazione) {

        ShipFence fence = new ShipFence();
        ViewGroup owner = (ViewGroup) view.getParent();
        //Drop a seconda della grandezza delle navi
        switch (view.getId()) {
            case R.id.id_tie:

                for (int i = 0; i < caselleTableList.size(); i++) {
                    Casella casella = caselleTableList.get(i);

                    if ( (casella.getImageViewId() == cella.getId()) && casellaLibera(casella)) {

                        casella.setOccupata(true); //La casella o contiene una barca
                        generateDrawable(casella, fazione,"tie_sx");

                        //disattiva il drag delle caselle intorno alla ship droppata
                        caselleTableList= fence.setShipFence(view, i, caselleTableList);

                        //disattiva il drag per quelle caselle
                        owner.setOnDragListener(null);

                        //Numero di Volte che si possono inserire le ships

                        numTie += 1;
                        if(numTie>1)
                            owner.removeView(view);

                    }else{
                        view.setVisibility(View.VISIBLE);
                    }
                }
                break;

            case R.id.id_star_dest:

                for (int i = 0; i < caselleTableList.size(); i++) {
                    Casella casella = caselleTableList.get(i);//seleziona la casella

                    if (casella.getImageViewId() == cella.getId() && ((i+1)%8!=0)) {  // ((i+1)%8!=0): evita il bordo DX della tabella;
                        Casella casellaAccanto = caselleTableList.get(i + 1);
                        Casella casellaAccanto1 = caselleTableList.get(i + 2);

                        if (casellaLibera(casella) && casellaLibera(casellaAccanto) && casellaLibera(casellaAccanto1) ) {

                            casella.setOccupata(true);
                            casellaAccanto1.setOccupata(true);
                            generateDrawable(casella, fazione, "star_destroyer_sx_2");
                            casellaAccanto.setOccupata(true);
                            generateDrawable(casellaAccanto, fazione, "star_destroyer_sx_1");

                            //disattiva il drag delle caselle intorno alla ship droppata
                            caselleTableList=fence.setShipFence(view, i, caselleTableList);

                            //disattiva il drag per quelle caselle
                            owner.setOnDragListener(null);

                            //Numero di Volte che si possono inserire le ships
                            numStarDest += 1;
                            if (numStarDest>1)
                                owner.removeView(view);

                        }else{
                            view.setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;

            case R.id.id_death_star:

                for (int i = 0; i < caselleTableList.size(); i++) {
                    Casella casella = caselleTableList.get(i);

                    if (casella.getImageViewId() == cella.getId() && ((i+1)%8!=0)&&(i<55)) { // ((i+1)%8!=0): evita il bordo DX della tabella
                        //(i<55): evita il bordo in basso della tabella
                        Casella casellaAccanto = caselleTableList.get(i + 1);
                        Casella casellaAccanto1 = caselleTableList.get(i + 2);
                        Casella casellaSotto = caselleTableList.get(i + 8);
                        Casella casellaSotto1 = caselleTableList.get(i + 10);
                        Casella casellaSottoAccanto = caselleTableList.get(i + 9);//seleziona la casella

                        if (casellaLibera(casella) && casellaLibera(casellaAccanto) && casellaLibera(casellaSotto) && casellaLibera(casellaSottoAccanto)
                            && casellaLibera(casellaAccanto1) && casellaLibera(casellaSotto1)
                                && caselleTableList.contains(casellaSotto) && caselleTableList.contains(casellaSottoAccanto) ) {

                            casella.setOccupata(true);
                            casellaAccanto1.setOccupata(true);
                            casellaSotto1.setOccupata(true);
                            generateDrawable(casella, fazione, "death_star_sx_3");

                            casellaAccanto.setOccupata(true);
                            generateDrawable(casellaAccanto, fazione, "death_star_sx_1");

                            casellaSotto.setOccupata(true);
                            generateDrawable(casellaSotto, fazione, "death_star_sx_4");

                            casellaSottoAccanto.setOccupata(true);
                            generateDrawable(casellaSottoAccanto, fazione, "death_star_sx_2");

                            //disattiva il drag delle caselle intorno alla ship droppata
                            caselleTableList=fence.setShipFence(view, i, caselleTableList);

                            //disattiva il drag per quelle caselle
                            owner.setOnDragListener(null);

                            //Numero di Volte che si possono inserire le ships
                            numStarDeath += 1;
                            if (numStarDeath>0)
                                owner.removeView(view);

                        }else{
                            view.setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;
        }
        return caselleTableList;
    }

    //Andrea: to fill the ArrayList<CasellaPosition>
    public  ArrayList<CasellaPosition> setPositionShip2(View view, View cella, ArrayList<Casella> caselleTableList, ArrayList<CasellaPosition> casellaPositionArrayList) {

        //Drop a seconda della grandezza delle navi
        switch (view.getId()) {
            case R.id.id_tie:

                for (int i = 0; i < caselleTableList.size(); i++) {
                    Casella casella = caselleTableList.get(i);

                    if ( (casella.getImageViewId() == cella.getId())) {

                        //Andrea: fills another ArrayList in parallel
                        casellaPositionArrayList.get(i).setImageName("tie_sx");

                    }else{
                        view.setVisibility(View.VISIBLE);
                    }
                }
                break;

            case R.id.id_star_dest:

                for (int i = 0; i < caselleTableList.size(); i++) {
                    Casella casella = caselleTableList.get(i);//seleziona la casella

                    if (casella.getImageViewId() == cella.getId() && ((i+1)%8!=0)) {  // ((i+1)%8!=0): evita il bordo DX della tabella;

                        //Andrea: fills another ArrayList in parallel
                        casellaPositionArrayList.get(i).setImageName("star_destroyer_sx_2");
                        casellaPositionArrayList.get(i+1).setImageName("star_destroyer_sx_1");

                    }
                }
                break;

            case R.id.id_death_star:

                for (int i =0; i < caselleTableList.size(); i++) {
                    Casella casella = caselleTableList.get(i);

                    if (casella.getImageViewId() == cella.getId() && ((i+1)%8!=0)&&(i<55)) { // ((i+1)%8!=0): evita il bordo DX della tabella
                        //(i<55): evita il bordo in basso della tabella

                        //Andrea: fills another ArrayList in parallel
                        casellaPositionArrayList.get(i).setImageName("death_star_sx_3");
                        casellaPositionArrayList.get(i+1).setImageName("death_star_sx_1");
                        casellaPositionArrayList.get(i+8).setImageName("death_star_sx_4");
                        casellaPositionArrayList.get(i+9).setImageName("death_star_sx_2");

                    }
                }
                break;
        }
        return casellaPositionArrayList;
    }

    public ArrayList<CasellaPosition> createEnemyBattlefield(ArrayList<CasellaPosition> casellaPositionArrayList) {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                CasellaPosition casellaPosition= new CasellaPosition();
                casellaPosition.setImageName("space");
            }
        }
        return casellaPositionArrayList;
    }

    private boolean casellaLibera(Casella c){
        return !c.getOccupata();
    }

    private void generateDrawable(Casella casella, Fazione fazione, String s){
        ShipDrawer drawer = new ShipDrawer(context);
        Drawable d = drawer.getDrawableFromString(0, s, fazione);
        casella.setDrawable(d);
    }
}
