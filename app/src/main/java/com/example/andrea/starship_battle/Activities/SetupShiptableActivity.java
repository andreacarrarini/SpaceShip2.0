package com.example.andrea.starship_battle.Activities;
import com.example.andrea.starship_battle.R;
import com.example.andrea.starship_battle.dragNdrop.ShipPosition;
import com.example.andrea.starship_battle.dragNdrop.DragShadowBuilder;
import com.example.andrea.starship_battle.model.Casella;
import com.example.andrea.starship_battle.model.CasellaPosition;
import com.example.andrea.starship_battle.model.Fazione;
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
    static ArrayList<Casella> caselleTableList;
    static ArrayList<CasellaPosition> casellePositionList;
    ShipPosition position = null;
    BluetoothDevice avversarioDevice;
    MediaPlayer mediaPlayer = new MediaPlayer();
    Button btnAnnulla;
    Button btnStartGame;
    public static Fazione fazione;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        caselleTableList = new ArrayList<Casella>();
        casellePositionList = new ArrayList<CasellaPosition>();

        //If the player has not choose his team, it becomes Sith by default
        if (ChooseFazione.fazione == null)
            fazione = Fazione.Sith;
        else
            fazione = ChooseFazione.fazione;

        btnAnnulla = (Button) findViewById(R.id.btnAnnulla);
        btnStartGame = (Button) findViewById(R.id.btnStart);
        position = new ShipPosition(this);
        Resizer r = new Resizer(this);

          mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
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
        mediaPlayer.prepareAsync();

        //Enemy's device datas
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
        switch (fazione) {
            case Sith:
                break;
            case Jedi:
                tie.setImageDrawable(getResources().getDrawable(R.drawable.x_wing_sx));
                star_destroyer.setImageDrawable(getResources().getDrawable(R.drawable.rebel_cruiser_sx));
                death_star.setImageDrawable(getResources().getDrawable(R.drawable.millenium_falcon_sx));
                break;
        }

        //The playing field is created with a List of ImageViews
        TableLayout rowCompleta = (TableLayout) findViewById(R.id.idTab);
        rowCompleta.setBackground(getResources().getDrawable(R.drawable.sfondotrovadisp));
        for (int i = 1; i < rowCompleta.getChildCount(); i++) {
            TableRow row = (TableRow) findViewById(rowCompleta.getChildAt(i).getId());
            for (int j = 1; j < row.getChildCount(); j++) {
                if(row.getChildAt(j) instanceof ImageView) {

                    //Every ImageView has his own onDragListener
                    row.getChildAt(j).setOnDragListener(this);

                    r.resize(row, dim_field_square); //resize of chessboard boxes

                    //All the Caselle are empty at first
                    Casella c = new Casella((ImageView) row.getChildAt(j), false);
                    caselleTableList.add(c);
                    CasellaPosition casellaPosition = new CasellaPosition();
                    casellaPosition.setImageName("space");
                    casellePositionList.add(casellaPosition);

                }else{
                    r.resize(row, dim_field_square); //resize of textView
                }
            }
        }

        // ImageView's onTouchListener for Drag
        tie.setOnTouchListener(this);
        star_destroyer.setOnTouchListener(this);
        death_star.setOnTouchListener(this);

        //Cancels all ships disposition
        restartShiptable(btnAnnulla);
        cambiaFontButton(btnAnnulla);

        startGameButton(btnStartGame);
        cambiaFontButton(btnStartGame);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        /*Catching two actions:
        -ACTION_DOWN = the player has touched the ship's view and starts dragging it;
        -ACTION_UP = the player has released the view, dropping it.
        */
        Boolean check=true;

        if (view instanceof ImageView) {  //DRAG on ImageView
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(null, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                check = true;
            } else {
                view.setVisibility(View.VISIBLE);
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
                Log.d(LOGCAT, "Drag started_"+fazione.toString());
                break;

            case DragEvent.ACTION_DROP:
                Log.d(LOGCAT, "Drop started");

                //ShipPosition handles the dropped view
                caselleTableList = position.setPositionShip(view, cella, caselleTableList, fazione);
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
                intent.putExtra("fazioneScelta", fazione.toString());
                finish();

                startActivity(intent);
            }

        });
    }

    public void releaseMediaPlayer(MediaPlayer mediaPlayer) {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        return;
    }

    public void startGameButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //The player has to place all the ships
                if( ((LinearLayout) findViewById(R.id.ship_deposit)).getChildCount() == 0  ) {

                    Bundle extrainBundle = new Bundle();
                    extrainBundle.putParcelableArrayList("casellePositionListSX", casellePositionList);

                    Intent intent = new Intent(SetupShiptableActivity.this, StartGameActivity.class);
                    intent.putExtra("bundle", extrainBundle); //Starting the new Activity
                    intent.putExtra("avversarioDevice", avversarioDevice); //Sending paired device's info to StartGameActivity
                    intent.putExtra("fazioneScelta", fazione.toString());
                    releaseMediaPlayer(mediaPlayer);
                    finish();
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),R.string.addShips , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cambiaFontButton(Button button) {
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Shadded South Personal Use.ttf");
        button.setTypeface(face);
    }

/*    @Override
    public void onPause() {
        releaseMediaPlayer(mediaPlayer);
        super.onPause();
    }*/

    @Override
    public void onDestroy() {
        //to avoid memory leak
        mediaPlayer.release();
        mediaPlayer= null;
        super.onDestroy();
    }

}