package com.example.andrea.starship_battle.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.example.andrea.starship_battle.Bluetooth.BluetoothConnectionService;
import com.example.andrea.starship_battle.R;
import com.example.andrea.starship_battle.dragNdrop.ShipPosition;
import com.example.andrea.starship_battle.model.CasellaPosition;
import com.example.andrea.starship_battle.model.Resizer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;


/**
 * Created by Diletta on 31/07/2017.
 */

public class StartGameActivity extends Activity {

    boolean turno;

    private static final String TAG = "StartGameActivity";
    int dim_field_square = 11;
    ArrayList<CasellaPosition> casellaPositionListDX = new ArrayList<>();
    ArrayList<CasellaPosition> casellaPositionListSX;
    RelativeLayout layout;
    ShipPosition position;
    TableLayout rowCompletaRX;
    TableLayout rowCompletaSX;
    int  imageTouchedId;

    BluetoothConnectionService mBluetoothConnection;
    BluetoothDevice avversarioDevice;
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    MediaPlayer shipFiringMediaPlayer = new MediaPlayer();
    AlertDialog alertDialogFAIL;

    static int tie_count = 2;
    static int stardest_count = 4;
    static int stardeath_count = 4;

    final int avversario = 1;
    final int giocatore = 0;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.start_game);
        Resizer r = new Resizer(this);
        position = new ShipPosition(this);

        casellaPositionListDX = position.createEnemyBattlefield(casellaPositionListDX);

        avversarioDevice = getIntent().getExtras().getParcelable("avversarioDevice");
        if (avversarioDevice.getBondState() == BluetoothDevice.BOND_BONDED)
            Log.i(TAG, "devices are bonded");

        AlertDialog.Builder builder = new AlertDialog.Builder(StartGameActivity.this);// TODO: android.R.style.Theme_Material_Dialog
        builder.setTitle(R.string.loser);
        builder.setMessage(R.string.restartAll);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(StartGameActivity.this, MainActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("new_window", true); //sets new window
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogFAIL = builder.create();


        /*AUDIO-------------------------------------------------------------------------------------

        shipFiringMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.d(TAG, "File audio prepared");
                return;
            }
        });

        try {
            shipFiringMediaPlayer.setDataSource(getApplicationContext(), Uri.parse("android.resource://com.example.andrea.starship_battle/" + R.raw.tie_fighter_fire2));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //prepares the file audio asynchrously
        shipFiringMediaPlayer.prepareAsync();*/

        //------------------------------------------------------------------------------------------

        //TABLE GAME SX: tablegame con le ships inserite dal giocatore
        Bundle b = getIntent().getBundleExtra("bundle");
        casellaPositionListSX = b.getParcelableArrayList("casellePositionListSX");

        rowCompletaSX = (TableLayout) findViewById(R.id.idTab);
        for (int i = 1; i < rowCompletaSX.getChildCount(); i++) {
            TableRow row = (TableRow) findViewById(rowCompletaSX.getChildAt(i).getId());
            for (int j = 1; j < row.getChildCount(); j++) {
                if (row.getChildAt(j) instanceof ImageView) {
                    if (!casellaPositionListSX.isEmpty()) {
                        //first row are always labels
                        (row.getChildAt(j)).setBackground(getResources().getDrawable(R.drawable.ic_galactic_space));
                        ((ImageView) row.getChildAt(j)).setImageDrawable(getShip(casellaPositionListSX, i - 1, j - 1));
                    }
                    r.resize(row, dim_field_square); //resize delle caselle della scacchiera

                }
            }
        }


        //TABLE GAME DX: tablegame per la ricerca delle ship dell'avversario
        rowCompletaRX = (TableLayout) findViewById(R.id.idTabB);
        rowCompletaRX.setBackground(getResources().getDrawable(R.drawable.sfondotrovadisp));
        for (int i = 1; i < rowCompletaRX.getChildCount(); i++) {
            final TableRow row = (TableRow) findViewById(rowCompletaRX.getChildAt(i).getId());
            for (int j = 1; j < row.getChildCount(); j++) {
                if (row.getChildAt(j) instanceof ImageView) {

                    final int  imageID = (i-1) * 8 + (j-1); // Numero della casella toccata

                    if (!casellaPositionListDX.isEmpty()) {
                        ((ImageView) row.getChildAt(j)).setImageDrawable(getShip(casellaPositionListDX, i - 1, j - 1));
                    }
                    ( row.getChildAt(j)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageTouchedId = v.getId();

                            /*plays the file audio
                            shipFiringMediaPlayer.start();*/

                            String messageToSend = String.valueOf(imageID); //value of ImageView ID
                            sendMessage(messageToSend);

                            /*seeks the file audio to 0 msec
                            shipFiringMediaPlayer.seekTo(0);*/
                        }
                    });
                    r.resize(row, dim_field_square); //resize delle caselle della scacchiera

                }
            }
        }

        //Message receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("incomingMessage"));

        //Sincronizzazione dispositivi sui thread di connessione BT
        Button startBTconnession = (Button) findViewById(R.id.btnStart);
        startBTconnession.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                mBluetoothConnection = new BluetoothConnectionService(StartGameActivity.this);
                Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");
                Log.d(TAG, "Trying to pair with " + avversarioDevice.getName());
                mBluetoothConnection.startClient(avversarioDevice, MY_UUID_INSECURE);

                turno = true;
            }
        });
        goBack((Button) findViewById(R.id.btnBack));
    }

    //scambio pacchetti via BT
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String text = intent.getStringExtra("message");
            Log.d(TAG, "messaggio ricevuto MAINACTIVY: " + text);

            if(text == null) {
                setDrawValue("space");
            }else if(isInteger(text)){
                int imageID = Integer.parseInt(text);

                setTurno (true);

                CasellaPosition casellaSelected = casellaPositionListSX.get(imageID);
                casellaSelected.setAffondata(true);


                TableRow row = (TableRow) findViewById(rowCompletaSX.getChildAt( (imageID)/8 +1 ).getId());
                ImageView image = (ImageView) row.getChildAt( (imageID+1)%8 );
                if (casellaSelected.getImageName().equals("space"))
                    image.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher)); //TODO: cambia per "acqua"
                else
                    image.setImageDrawable(getResources().getDrawable(R .drawable.icon)); //TODO: cambia per "colpito"
                sendMessage(casellaSelected.getImageName());

            }else if(!isInteger(text)){
                if(text.equals("perso")){
                    alertDialogFAIL.show();
                }
                Log.d(TAG, "messaggio ricevuto MAINACTIVY2: " + text);
                setDrawValue(text);

                setTurno(false);
            }
        }
    };


    private void setDrawValue(String s){
        Drawable d = getDrawableFromString(avversario,s);

        for (int i = 1; i < rowCompletaRX.getChildCount(); i++) {
            TableRow row = (TableRow) findViewById(rowCompletaRX.getChildAt(i).getId());
            for (int j = 1; j < row.getChildCount(); j++) {
                if (row.getChildAt(j) instanceof ImageView && row.getChildAt(j).getId()==imageTouchedId) {
                    if (s.equals("space"))
                        (row.getChildAt(j)).setVisibility(View.INVISIBLE);
                        //TODO: suono flop
                    else {
                        ((ImageView) row.getChildAt(j)).setImageDrawable(d);
                        //TODO: suono affondata
                    }
                    break;
                }
            }
        }
        //contatore per le navi dell'avversario trovate
        shipFoundCounter(s);
    }

    @Override
    protected void onStop() {
        //to avoid memory leak
        shipFiringMediaPlayer.release();
        shipFiringMediaPlayer = null;
        super.onStop();
    }

    public void goBack(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartGameActivity.this, SetupShiptableActivity.class);
                intent.putExtra("avversarioDevice", avversarioDevice);
                startActivity(intent);
            }

        });
    }

    private void sendMessage(String messageToSend) {
        Log.d(TAG, "messaggio inviato MAINACTIVY: " + messageToSend);
        byte[] bytes = messageToSend.getBytes(Charset.defaultCharset());
        if (mBluetoothConnection != null) {
            mBluetoothConnection.write(bytes);
        }
    }

    public static boolean isInteger(String s ){
        try{
            Integer.parseInt(s);
        }catch (NumberFormatException e){
            return false;
        }catch (NullPointerException e1){
            return false;
        }
        return true;
    }

    public Drawable getShip(ArrayList<CasellaPosition> casellaPositionArrayList, int row, int column) {

        Drawable drawable = null;
        if (!casellaPositionArrayList.isEmpty()) {
            String shipName = casellaPositionArrayList.get(row * 8 + column).getImageName();
            drawable = getDrawableFromString(giocatore,shipName);
        }
        return drawable;
    }

    private Drawable getDrawableFromString (int tipoGiocatore, String s){
        Drawable drawable = null;
        switch (tipoGiocatore) {
            case giocatore:

                switch (s) {
                    case "tie_sx":
                        drawable = getResources().getDrawable(R.drawable.tie_sx);
                        break;
                    case "star_destroyer_sx_2":
                        drawable = getResources().getDrawable(R.drawable.star_destroyer_sx_2);
                        break;
                    case "star_destroyer_sx_1":
                        drawable = getResources().getDrawable(R.drawable.star_destroyer_sx_1);
                        break;
                    case "death_star_sx_3":
                        drawable = getResources().getDrawable(R.drawable.death_star_sx_3);
                        break;
                    case "death_star_sx_1":
                        drawable = getResources().getDrawable(R.drawable.death_star_sx_1);
                        break;
                    case "death_star_sx_4":
                        drawable = getResources().getDrawable(R.drawable.death_star_sx_4);
                        break;
                    case "death_star_sx_2":
                        drawable = getResources().getDrawable(R.drawable.death_star_sx_2);
                        break;
                    case "space":
                        drawable = getResources().getDrawable(R.drawable.ic_galactic_space);
                        break;
                }
                break;
            case avversario:

                switch (s) {
                    case "tie_sx":
                        drawable = getResources().getDrawable(R.drawable.x_wing_sx);
                        break;
                    case "star_destroyer_sx_2":
                        drawable = getResources().getDrawable(R.drawable.rebel_cruiser_sx_2);
                        break;
                    case "star_destroyer_sx_1":
                        drawable = getResources().getDrawable(R.drawable.rebel_cruiser_sx_1);
                        break;
                    case "death_star_sx_3":
                        drawable = getResources().getDrawable(R.drawable.millenium_falcon_sx_3);
                        break;
                    case "death_star_sx_1":
                        drawable = getResources().getDrawable(R.drawable.millenium_falcon_sx_1);
                        break;
                    case "death_star_sx_4":
                        drawable = getResources().getDrawable(R.drawable.millenium_falcon_sx_4);
                        break;
                    case "death_star_sx_2":
                        drawable = getResources().getDrawable(R.drawable.millenium_falcon_sx_2);
                        break;
                    case "space":
                        drawable = getResources().getDrawable(R.drawable.ic_galactic_space);
                        break;
                }
        }
        return drawable;
    }

    private void shipFoundCounter(String s){

        if (s.contains("tie"))
            tie_count = tie_count - 1;
        if (s.contains("star_destroyer"))
            stardest_count = stardest_count - 1;
        if (s.contains("death_star"))
            stardeath_count = stardeath_count - 1;
        if (tie_count==0 && stardeath_count==0 && stardest_count==0){
            //TODO: suono hai vinto
            sendMessage("perso");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);// TODO: android.R.style.Theme_Material_Dialog
            builder.setTitle(R.string.winner);
            builder.setMessage(R.string.restartAll);
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(StartGameActivity.this, MainActivity.class);
                    Bundle b = new Bundle();
                    b.putBoolean("new_window", true); //sets new window
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
    private void setTurno (boolean b){
        turno = b;

        layout = (RelativeLayout) findViewById(R.id.starGameSfondo);
        if(!turno) {
            layout.setBackgroundColor(Color.RED);
        }else
            layout.setBackgroundColor(Color.BLACK);
    }
}