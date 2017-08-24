package com.example.andrea.starship_battle.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.andrea.starship_battle.R;

/**
 * Created by utente on 03/08/2017.
 */

public enum Designs {

    //all sx

    tie("tie_sx"),
    star_destroyer_1("star_destroyer_sx_1"),
    star_destroyer_2("star_destroyer_sx_2"),
    death_star_1("death_star_sx_1"),
    death_star_2("death_star_sx_2"),
    death_star_3("death_star_sx_3"),
    death_star_4("death_star_sx_4"),
    no_design( "space");


    private final String textRepresentation;

    private Designs(String textRepresentation) {
        this.textRepresentation = textRepresentation;

    }


}





