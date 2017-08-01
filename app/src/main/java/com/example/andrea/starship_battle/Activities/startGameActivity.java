package com.example.andrea.starship_battle.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.andrea.starship_battle.R;
import com.example.andrea.starship_battle.dragNdrop.dragShadowBuilder;
import com.example.andrea.starship_battle.model.Casella;
import com.example.andrea.starship_battle.model.Resizer;

import java.util.ArrayList;

/**
 * Created by utente on 31/07/2017.
 */

public class startGameActivity extends Activity implements View.OnTouchListener{

    int dim_field_square = 12;
    int dim_ship = 4;

    ArrayList<Casella> caselleTableListDX = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_game);
        Resizer r = new Resizer(this);

        TableRow rowLabelsSX = (TableRow) findViewById(R.id.rowLabels0);
        r.resize(rowLabelsSX, dim_field_square);

        TableRow rowLabelDX = (TableRow) findViewById(R.id.rowLabels0B);
        r.resize(rowLabelDX, dim_field_square);


        //TABLE GAME SX: tablegame con le ships inserite dal giocatore
       /* Bundle b = getIntent().getBundleExtra("bundle");
        ArrayList<Casella> caselleTableListSX = (ArrayList<Casella>) b.getSerializable("caselleListSX");*/
        ArrayList<Casella> caselleTableListSX = tableActivity2.caselleTableList;
        TableLayout rowCompletaSX = (TableLayout) findViewById(R.id.idTab);
        for (int i = 1; i < rowCompletaSX.getChildCount(); i++) {
            TableRow row = (TableRow) findViewById(rowCompletaSX.getChildAt(i).getId());
            for (int j = 0; j < row.getChildCount(); j++) {
                if (row.getChildAt(j) instanceof ImageView) {
                    ((ImageView) row.getChildAt(j)).setImageDrawable(caselleTableListSX.get(i-1).getDrawable());
                    r.resize(row, dim_field_square); //resize delle caselle della scacchiera

                }
            }
        }


        //TABLE GAME DX: tablegame con le ship dell'avversario
        TableLayout rowCompletaRX = (TableLayout) findViewById(R.id.idTabB);
        for (int i = 1; i < rowCompletaRX.getChildCount(); i++) {
            TableRow row = (TableRow) findViewById(rowCompletaRX.getChildAt(i).getId());
            for (int j = 0; j < row.getChildCount(); j++) {
                if (row.getChildAt(j) instanceof ImageView) {
                    //InvisibileView:((ImageView) row.getChildAt(j)).setImageDrawable(null);

                    r.resize(row, dim_field_square); //resize delle caselle della scacchiera
                    Casella c = new Casella((ImageView) row.getChildAt(j), false, false); //Matrice di caselle: ImageView vuote
                    caselleTableListDX.add(c);
                } else {
                    r.resize(row, dim_field_square); //resize delle textView
                }
            }
        }

      /* LinearLayout shipLayout = (LinearLayout) findViewById(R.id.ship_deposit);
        resize(shipLayout, dim_ship);

        ImageView x_wing = (ImageView) findViewById(R.id.id_x_wing); //tie
        ImageView rebel_cruiser = (ImageView) findViewById(R.id.id_rebel_cruiser); //star_destroyer
        ImageView millenium = (ImageView) findViewById(R.id.id_millenium); //Deathstar*/

        restartGame((Button) findViewById(R.id.btnAnnulla2));


    }

    @Override
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

    public void restartGame(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(startGameActivity.this, startGameActivity.class);
                startActivity(intent);
                ArrayList<Casella> caselleTableListSX = tableActivity2.caselleTableList;

            }

        });
    }



}


