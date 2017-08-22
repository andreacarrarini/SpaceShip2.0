package com.example.andrea.starship_battle.Activities;
import com.example.andrea.starship_battle.R;
import com.example.andrea.starship_battle.dragNdrop.ShipPosition;
import com.example.andrea.starship_battle.dragNdrop.dragShadowBuilder;
import com.example.andrea.starship_battle.model.Casella;
import com.example.andrea.starship_battle.model.CasellaPosition;
import com.example.andrea.starship_battle.model.Resizer;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.*;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;


public class TableActivity2 extends Activity implements View.OnTouchListener, View.OnDragListener {
    private static final String LOGCAT = null;

    //CONST: Sets the dimension of the field square and the ships
    int dim_field_square = 9;
    int dim_ship = 4;
    static ArrayList<Casella> caselleTableList = new ArrayList<>();
    static ArrayList<CasellaPosition> casellePositionList = new ArrayList<>();
    ShipPosition position;

    BluetoothDevice avversarioDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        position = new ShipPosition(this);
        Resizer r = new Resizer(this);

        avversarioDevice = getIntent().getExtras().getParcelable("avversarioDevice");
//ANDREA

        // Sets the activity title
        TextView place_ships = (TextView) findViewById(R.id.id_place_ships);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Starjedi.ttf");
        place_ships.setTypeface(custom_font);

        // Sets the ship's conteiner
        LinearLayout shipLayout = (LinearLayout) findViewById(R.id.ship_deposit);
        r.resize(shipLayout, dim_ship);

        //Declares the imageview and creates the bitmaps by them
        ImageView tie = (ImageView) findViewById(R.id.id_tie);
        ImageView star_destroyer = (ImageView) findViewById(R.id.id_star_dest);
        ImageView death_star = (ImageView) findViewById(R.id.id_death_star);

//Creo una matrice di ImageView che rappresenta la tabella e setto ogni ImageView onDRAGListener
        TableLayout rowCompleta = (TableLayout) findViewById(R.id.idTab);
        rowCompleta.setBackground(getResources().getDrawable(R.drawable.sfondotrovadisp));
        for (int i = 1; i < rowCompleta.getChildCount(); i++) {
            TableRow row = (TableRow) findViewById(rowCompleta.getChildAt(i).getId());
            for (int j = 0; j < row.getChildCount(); j++) {
                if(row.getChildAt(j) instanceof ImageView) {
                    //InvisibileView:((ImageView) row.getChildAt(j)).setImageDrawable(null);
                    row.getChildAt(j).setOnDragListener(this);

                    r.resize(row, dim_field_square); //resize delle caselle della scacchiera

                    Casella c = new Casella((ImageView) row.getChildAt(j), false, false); //Matrice di caselle: ImageView vuote
                    caselleTableList.add(c);
                    CasellaPosition casellaPosition = new CasellaPosition();
                    casellaPosition.setImageName("space");
                    casellePositionList.add(casellaPosition);

                }else{
                    r.resize(row, dim_field_square); //resize delle textView
                }
            }
        }

//DILETTA

        // ImageView's onTOUCHListener for Drag: se una delle ships viene toccata, inizia il Drag
        tie.setOnTouchListener(this);
        star_destroyer.setOnTouchListener(this);
        death_star.setOnTouchListener(this);

        //Annulla tutta la disposizione delle navi
        restartShiptable((Button) findViewById(R.id.btnAnnulla));
        startGameButton((Button) findViewById(R.id.btnStart));
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

    @Override
    public boolean onDrag(View cella, DragEvent dragevent) {

        int action = dragevent.getAction();
        View view = (View) dragevent.getLocalState();

        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                Log.d(LOGCAT, "Drag started");
                break;

            case DragEvent.ACTION_DROP:
                Log.d(LOGCAT, "Drop started");

                //ShipPosition gestisce il drop in base al tipo di ship
                caselleTableList = position.setPositionShip(view, cella, caselleTableList);
                casellePositionList = position.setPositionShip2(view, cella, caselleTableList, casellePositionList);
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                Log.d(LOGCAT, "Drag ended");

                if (!dropEventNotHandled(dragevent)) {
                    view.setVisibility(View.VISIBLE);
                }
                return true;

            default:
                break;
        }
        return true;
    }

    private boolean dropEventNotHandled(DragEvent dragEvent) {
        return !dragEvent.getResult();
    }

    public void restartShiptable(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableActivity2.this, TableActivity2.class);
                startActivity(intent);
                caselleTableList = new ArrayList<>();
                casellePositionList = new ArrayList<CasellaPosition>();
            }

        });
    }

    public void startGameButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( ((LinearLayout) findViewById(R.id.ship_deposit)).getChildCount() == 0  ) { //non ci sono più navi

                   /* for (Casella c: caselleTableList)
                    System.out.println("casella: "+c.getImageView().getId()+
                            "  occupata: " +c.getOccupata()+
                            "  drawable: " +c.getImageView().getDrawable());*/
                    /*TODO: inviare la lista caselleTableList a startGameActivity*/


                    Bundle extrainBundle = new Bundle();
                    extrainBundle.putParcelableArrayList("casellePositionListSX", casellePositionList);

                    Intent intent = new Intent(TableActivity2.this, StartGameActivity.class);
                    intent.putExtra("bundle", extrainBundle); //Passa la lista alla nuova activity
                    intent.putExtra("avversarioDevice", avversarioDevice); //Sending paired device's info to StartGameActivity
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),R.string.addShips , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}