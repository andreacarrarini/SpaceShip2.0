package com.example.andrea.starship_battle.model;

/**
 * Created by utente on 06/09/2017.
 */

public enum Fazione {
    Sith ("sith"),
    Jedi ("jedi");


    private String value;

    Fazione(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }

    public Fazione getFazione(String s) {
        return Fazione.valueOf(s);
    }

}
