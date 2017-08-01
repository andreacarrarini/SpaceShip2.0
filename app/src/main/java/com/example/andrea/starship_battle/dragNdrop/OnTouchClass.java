package com.example.andrea.starship_battle.dragNdrop;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;

/**
 * Created by utente on 31/07/2017.
 */

public class OnTouchClass implements View.OnTouchListener{

    public boolean onTouch(View view, MotionEvent motionEvent) {
        /*Con lo stesso metodo Touch eseguo le due azioni: si differenzia in base a:
        -ACTION_DOWN = l'utente ha appena toccato qualcosa (le ships che verranno draggate)
        -ACTION_UP = l'utente ha staccato il dito dallo schermo ( inizia il drop della ship)
        */
        Boolean check=true;

        if (view instanceof ImageView) {  //DRAG on ImageView
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                View.DragShadowBuilder shadowBuilder = new dragShadowBuilder(view);
                //Se vogliamo mantenere l'immagine in movimwento: View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(null, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                check = true;
            } else {
                view.setVisibility(View.VISIBLE); //Non fa sparire la view se si sbaglia a cliccare
                check = false;

            }
        }else  if (view instanceof TableRow) {  //DROP on Table
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                check = true;
            }
        }
        return check;
    }
}
