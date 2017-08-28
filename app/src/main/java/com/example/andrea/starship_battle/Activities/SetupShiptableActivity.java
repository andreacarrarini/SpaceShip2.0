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
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.io.IOException;
import java.util.ArrayList;


public class SetupShiptableActivity extends Activity implements View.OnTouchListener, View.OnDragListener {
    private static final String LOGCAT = "SetupShiptableActivity";

    //CONST: Sets the dimension of the field square and the ships
    int dim_field_square = 11;
    int dim_ship = 4;
    static ArrayList<Casella> caselleTableList = new ArrayList<>();
    static ArrayList<CasellaPosition> casellePositionList = new ArrayList<>();
    ShipPosition position = null;
    BluetoothDevice avversarioDevice;
    MediaPlayer mediaPlayer = new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        position = new ShipPosition(this);
        Resizer r = new Resizer(this);

      /*TODO  mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.d(LOGCAT, "File audio prepared");
                mediaPlayer.start();
                return;
            }
        });

        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse("android.resource://com.example.andrea.starship_battle/" + R.raw.star_wars_cantina_song));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //prepares the file audio asynchrously
        mediaPlayer.prepareAsync();*/

        avversarioDevice = getIntent().getExtras().getParcelable("avversarioDevice");

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

        //Creo una lista di ImageView che rappresenta la tabella e setto ogni ImageView onDRAGListener
        TableLayout rowCompleta = (TableLayout) findViewById(R.id.idTab);
        rowCompleta.setBackground(getResources().getDrawable(R.drawable.sfondotrovadisp));
        for (int i = 1; i < rowCompleta.getChildCount(); i++) {
            TableRow row = (TableRow) findViewById(rowCompleta.getChildAt(i).getId());
            for (int j = 1; j < row.getChildCount(); j++) {
                if(row.getChildAt(j) instanceof ImageView) {
                    row.getChildAt(j).setOnDragListener(this);

                    r.resize(row, dim_field_square); //resize delle caselle della scacchiera

                    Casella c = new Casella((ImageView) row.getChildAt(j), false, false); //Lista di caselle: ImageView vuote
                    caselleTableList.add(c);
                    CasellaPosition casellaPosition = new CasellaPosition();
                    casellaPosition.setImageName("space");
                    casellePositionList.add(casellaPosition);

                }else{
                    r.resize(row, dim_field_square); //resize delle textView
                }
            }
        }

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
                //TODO:Se vogliamo mantenere l'immagine in movimwento: View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
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

                caselleTableList = new ArrayList<>();
                casellePositionList = new ArrayList<>();
                Intent intent = new Intent(SetupShiptableActivity.this, SetupShiptableActivity.class);
                intent.putExtra("avversarioDevice", avversarioDevice); //Sending paired device's info to StartGameActivity

                startActivity(intent);
            }

        });
    }

    public void startGameButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( ((LinearLayout) findViewById(R.id.ship_deposit)).getChildCount() == 0  ) { //non ci sono più navi

                    Bundle extrainBundle = new Bundle();
                    extrainBundle.putParcelableArrayList("casellePositionListSX", casellePositionList);

                    Intent intent = new Intent(SetupShiptableActivity.this, StartGameActivity.class);
                    intent.putExtra("bundle", extrainBundle); //Passa la lista alla nuova activity
                    intent.putExtra("avversarioDevice", avversarioDevice); //Sending paired device's info to StartGameActivity
                    finish();
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),R.string.addShips , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}