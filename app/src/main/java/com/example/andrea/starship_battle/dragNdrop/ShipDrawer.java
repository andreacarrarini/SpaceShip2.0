package com.example.andrea.starship_battle.dragNdrop;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.example.andrea.starship_battle.R;
import com.example.andrea.starship_battle.model.Casella;
import com.example.andrea.starship_battle.model.Fazione;

/**
 * Created by utente on 06/09/2017.
 */

public class ShipDrawer {

    private Context context;
    public ShipDrawer (Context currentContext){
        this.context = currentContext;
    }


    public Drawable getDrawableFromString (int tipoGiocatore, String s, Fazione fazione) {
        Drawable drawable = null;
        switch (fazione) {
            case Sith:

                switch (tipoGiocatore) {
                    case 0:
                        switch (s) {
                            case "tie_sx":
                                drawable = context.getResources().getDrawable(R.drawable.tie_sx);
                                break;
                            case "star_destroyer_sx_2":
                                drawable = context.getResources().getDrawable(R.drawable.star_destroyer_sx_2);
                                break;
                            case "star_destroyer_sx_1":
                                drawable = context.getResources().getDrawable(R.drawable.star_destroyer_sx_1);
                                break;
                            case "death_star_sx_3":
                                drawable = context.getResources().getDrawable(R.drawable.death_star_sx_3);
                                break;
                            case "death_star_sx_1":
                                drawable = context.getResources().getDrawable(R.drawable.death_star_sx_1);
                                break;
                            case "death_star_sx_4":
                                drawable = context.getResources().getDrawable(R.drawable.death_star_sx_4);
                                break;
                            case "death_star_sx_2":
                                drawable = context.getResources().getDrawable(R.drawable.death_star_sx_2);
                                break;
                            case "space":
                                drawable = context.getResources().getDrawable(R.drawable.ic_galactic_space);
                                break;
                        }
                        break;

                    case 1:
                        switch (s) {
                            case "tie_sx":
                                drawable = context.getResources().getDrawable(R.drawable.x_wing_dx);
                                break;
                            case "star_destroyer_sx_2":
                                drawable = context.getResources().getDrawable(R.drawable.rebel_cruiser_dx_2);
                                break;
                            case "star_destroyer_sx_1":
                                drawable = context.getResources().getDrawable(R.drawable.rebel_cruiser_dx_1);
                                break;
                            case "death_star_sx_3":
                                drawable = context.getResources().getDrawable(R.drawable.millenium_falcon_dx_3);
                                break;
                            case "death_star_sx_1":
                                drawable = context.getResources().getDrawable(R.drawable.millenium_falcon_dx_1);
                                break;
                            case "death_star_sx_4":
                                drawable = context.getResources().getDrawable(R.drawable.millenium_falcon_dx_4);
                                break;
                            case "death_star_sx_2":
                                drawable = context.getResources().getDrawable(R.drawable.millenium_falcon_dx_2);
                                break;
                            case "space":
                                drawable = context.getResources().getDrawable(R.drawable.ic_galactic_space);
                                break;
                        }
                }
                return drawable;

            case Jedi:
                switch (tipoGiocatore) {
                    case 1:

                        switch (s) {
                            case "tie_sx":
                                drawable = context.getResources().getDrawable(R.drawable.tie_dx);
                                break;
                            case "star_destroyer_sx_2":
                                drawable = context.getResources().getDrawable(R.drawable.star_destroyer_dx_2);
                                break;
                            case "star_destroyer_sx_1":
                                drawable = context.getResources().getDrawable(R.drawable.star_destroyer_dx_1);
                                break;
                            case "death_star_sx_3":
                                drawable = context.getResources().getDrawable(R.drawable.death_star_dx_3);
                                break;
                            case "death_star_sx_1":
                                drawable = context.getResources().getDrawable(R.drawable.death_star_dx_1);
                                break;
                            case "death_star_sx_4":
                                drawable = context.getResources().getDrawable(R.drawable.death_star_dx_4);
                                break;
                            case "death_star_sx_2":
                                drawable = context.getResources().getDrawable(R.drawable.death_star_dx_2);
                                break;
                            case "space":
                                drawable = context.getResources().getDrawable(R.drawable.ic_galactic_space);
                                break;
                        }
                        break;
                    case 0:
                        switch (s) {
                            case "tie_sx":
                                drawable = context.getResources().getDrawable(R.drawable.x_wing_sx);
                                break;
                            case "star_destroyer_sx_2":
                                drawable = context.getResources().getDrawable(R.drawable.rebel_cruiser_sx_2);
                                break;
                            case "star_destroyer_sx_1":
                                drawable = context.getResources().getDrawable(R.drawable.rebel_cruiser_sx_1);
                                break;
                            case "death_star_sx_3":
                                drawable = context.getResources().getDrawable(R.drawable.millenium_falcon_sx_3);
                                break;
                            case "death_star_sx_1":
                                drawable = context.getResources().getDrawable(R.drawable.millenium_falcon_sx_1);
                                break;
                            case "death_star_sx_4":
                                drawable = context.getResources().getDrawable(R.drawable.millenium_falcon_sx_4);
                                break;
                            case "death_star_sx_2":
                                drawable = context.getResources().getDrawable(R.drawable.millenium_falcon_sx_2);
                                break;
                            case "space":
                                drawable = context.getResources().getDrawable(R.drawable.ic_galactic_space);
                                break;
                        }
                }
                break;
        }
        return drawable;
    }

    public void generateDrawable(ImageView view, Fazione fazione, int tipoGiocatore, String s ){
        Drawable d = getDrawableFromString(tipoGiocatore, s, fazione);
        view.setImageDrawable(d);
    }
}
