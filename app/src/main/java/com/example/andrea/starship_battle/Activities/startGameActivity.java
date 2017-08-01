package com.example.andrea.starship_battle.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    int dim_field_square = 11;

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
       /*Bundle b = getIntent().getBundleExtra("bundle");
        ArrayList<Casella> caselleTableListSX = (ArrayList<Casella>) b.getSerializable("caselleListSX");*/

/*       ArrayList<Casella> caselleTableListSX = tableActivity2.caselleTableList;
        TableLayout rowCompletaSX = (TableLayout) findViewById(R.id.idTab);
        for (int i = 1; i < rowCompletaSX.getChildCount(); i++) {
            TableRow row = (TableRow) findViewById(rowCompletaSX.getChildAt(i).getId());
            for (int j = 0; j < row.getChildCount(); j++) {
                if (row.getChildAt(j) instanceof ImageView) {
                    ((ImageView) row.getChildAt(j)).setImageDrawable(caselleTableListSX.get(i-1).getDrawable());
                    r.resize(row, dim_field_square); //resize delle caselle della scacchiera

                }
            }
        }*/

        //TABLE GAME DX: tablegame con le ship dell'avversario
        TableLayout rowCompletaRX = (TableLayout) findViewById(R.id.idTabB);
        for (int i = 1; i < rowCompletaRX.getChildCount(); i++) {
            TableRow row = (TableRow) findViewById(rowCompletaRX.getChildAt(i).getId());
            for (int j = 0; j < row.getChildCount(); j++) {
                if (row.getChildAt(j) instanceof ImageView) {
                    r.resize(row, dim_field_square); //resize delle caselle della scacchiera

                    Casella c = new Casella((ImageView) row.getChildAt(j), false, false); //Matrice di caselle: ImageView vuote
                    caselleTableListDX.add(c);
                } else {
                    r.resize(row, dim_field_square); //resize delle textView
                }
            }
        }
        rowCompletaRX.setOnTouchListener(this);

        goBack((Button) findViewById(R.id.btnBack));
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            view.setBackgroundColor(Color.CYAN);
            return true;
        }
        return false;
    }


    public void goBack(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(startGameActivity.this, tableActivity2.class);
                startActivity(intent);
            }

        });
    }
}


