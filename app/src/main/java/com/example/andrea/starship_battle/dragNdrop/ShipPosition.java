package com.example.andrea.starship_battle.dragNdrop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.example.andrea.starship_battle.R;
import com.example.andrea.starship_battle.model.Casella;

import java.util.ArrayList;

/**
 * Created by utente on 15/07/2017.
 */

public class ShipPosition{

    private Context context;
    public ShipPosition(Context currentContext){
        this.context = currentContext;
    }

    int numTie=0;
    int numStarDest=0;
    int numStarDeath=0;

    public  ArrayList<Casella> setPositionShip(View view, View cella, ArrayList<Casella> caselleTableList) {

        ShipFence fence = new ShipFence();
        ViewGroup owner = (ViewGroup) view.getParent();
        //Drop a seconda della grandezza delle navi
        switch (view.getId()) {
            case R.id.id_tie:

                for (int i = 0; i < caselleTableList.size(); i++) {
                    Casella casella = caselleTableList.get(i);

                    if ( (casella.getImageViewId() == cella.getId()) && casellaLibera(casella)) {
                        casella.setOccupata(true); //La casella o contiene una barca
                        casella.setDrawable(context.getResources().getDrawable(R.drawable.tie_dx));

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

                        if (casellaLibera(casella) && casellaLibera(casellaAccanto)) {
                            casella.setOccupata(true);
                            casella.setDrawable(context.getResources().getDrawable(R.drawable.star_destroyer_sx_2)); //La casella o contiene una barca

                            casellaAccanto.setOccupata(true);
                            casellaAccanto.setDrawable(context.getResources().getDrawable(R.drawable.star_destroyer_sx_1));

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

                for (int i =0; i < caselleTableList.size(); i++) {
                    Casella casella = caselleTableList.get(i);

                    if (casella.getImageViewId() == cella.getId() && ((i+1)%8!=0)&&(i<39)) { // ((i+1)%8!=0): evita il bordo DX della tabella
                        //(i<39): evita il bordo in basso della tabella
                        Casella casellaAccanto = caselleTableList.get(i + 1);
                        Casella casellaSotto = caselleTableList.get(i + 8);
                        Casella casellaSottoAccanto = caselleTableList.get(i + 9); //seleziona la casella

                        if (casellaLibera(casella) && casellaLibera(casellaAccanto) && casellaLibera(casellaSotto) && casellaLibera(casellaSottoAccanto)
                                && caselleTableList.contains(casellaSotto) && caselleTableList.contains(casellaSottoAccanto)) {

                            casella.setOccupata(true);
                            casella.setDrawable(context.getResources().getDrawable(R.drawable.death_star_sx_3)); //La casella o contiene una barca

                            casellaAccanto.setOccupata(true);
                            casellaAccanto.setDrawable(context.getResources().getDrawable(R.drawable.death_star_sx_1));

                            casellaSotto.setOccupata(true);
                            casellaSotto.setDrawable(context.getResources().getDrawable(R.drawable.death_star_sx_4));

                            casellaSottoAccanto.setOccupata(true);
                            casellaSottoAccanto.setDrawable(context.getResources().getDrawable(R.drawable.death_star_sx_2));

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

    private boolean casellaLibera(Casella c){
        if (c.getOccupata()){
           // Toast.makeText(context, "There is another ship here", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
