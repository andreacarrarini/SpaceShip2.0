package com.example.andrea.starship_battle.model;

import com.example.andrea.starship_battle.R;

/**
 * Created by utente on 03/08/2017.
 */

public enum Designs {

    //all sx

    tie("tie"),
    star_destroyer_1("star_d_1"),
    star_destroyer_2("star_d_2"),
    death_star_1("death_1"),
    death_star_2("death_2"),
    death_star_3("death_3"),
    death_star_4("death_4"),
    no_design("null");


    private final String textRepresentation;

    private Designs (String textRepresentation) {
        this.textRepresentation = textRepresentation;

    }




}
