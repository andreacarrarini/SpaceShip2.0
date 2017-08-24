package com.example.andrea.starship_battle.Activities;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Diletta on 31/07/2017.
 */

public class StartGameActivity extends Activity {

    private static final String TAG = "StartGameActivity";
    int dim_field_square = 11;
    ArrayList<CasellaPosition> casellaPositionListDX = new ArrayList<>();
    ArrayList<CasellaPosition> casellaPositionListSX;
    ShipPosition position;

    String draw;

    BluetoothConnectionService mBluetoothConnection;
    BluetoothDevice avversarioDevice;
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    String text;

    MediaPlayer shipFiringMediaPlayer = new MediaPlayer();

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
            Log.i(TAG, "bonded");


        //AUDIO-------------------------------------------------------------------------------------

        shipFiringMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.d(TAG, "File audio prepared");
                mediaPlayer.start();
                return;
            }
        });

        try {
            shipFiringMediaPlayer.setDataSource(getApplicationContext(), Uri.parse("android.resource://com.example.andrea.starship_battle/" + R.raw.tie_fighter_fire2));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //prepares the file audio asynchrously
        shipFiringMediaPlayer.prepareAsync();

        //------------------------------------------------------------------------------------------

        //TABLE GAME SX: tablegame con le ships inserite dal giocatore
        Bundle b = getIntent().getBundleExtra("bundle");
        casellaPositionListSX = b.getParcelableArrayList("casellePositionListSX");

        TableLayout rowCompletaSX = (TableLayout) findViewById(R.id.idTab);
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
        TableLayout rowCompletaRX = (TableLayout) findViewById(R.id.idTabB);
        rowCompletaRX.setBackground(getResources().getDrawable(R.drawable.sfondotrovadisp));
        for (int i = 1; i < rowCompletaRX.getChildCount(); i++) {
            final TableRow row = (TableRow) findViewById(rowCompletaRX.getChildAt(i).getId());
            for (int j = 1; j < row.getChildCount(); j++) {
                if (row.getChildAt(j) instanceof ImageView) {
                    final int  imageID = (i-1) * 8 + (j-1); // Numero della casella toccata

                    if (!casellaPositionListDX.isEmpty()) {
                        ((ImageView) row.getChildAt(j)).setImageDrawable(getShip(casellaPositionListDX, i - 1, j - 1));
                    }
                    ((ImageView) row.getChildAt(j)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                      /*TODO: casella.occupata corrispondente o casella.posizione forse serve un altro medoto per il thread parallelo
                    * TODO: se la casella che ho selezionato (dalla lista via bluethoot) è vuota (boolean)
                    * allora prendi la drawable corrispongente e disegnala
                    * TODO: altrimenti colorala di rosso*/

                            //plays the file audio
                            shipFiringMediaPlayer.start();

                            String messageToSend = String.valueOf(imageID); //value of ImageView ID
                            sendMessage(messageToSend);

                            Log.d(TAG, "colore image view: " + draw);
                             //v.setBackground(position.setDesign(StartGameActivity.this , draw));
                            v.setVisibility(View.INVISIBLE);

                            //seeks the file audio to 0 msec
                            shipFiringMediaPlayer.seekTo(0);
                        }
                    });
                    r.resize(row, dim_field_square); //resize delle caselle della scacchiera
                }
            }
        }


        //Message receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("incomingMessage"));
        //Confronto delle barche via Bluetooth --> scambio pacchetti
        Button startBTconnession= (Button) findViewById(R.id.btnStart);
        startBTconnession.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                mBluetoothConnection = BluetoothConnectionService.getInstance();

                Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");
                Log.d(TAG, "Trying to pair with " + avversarioDevice.getName());
                mBluetoothConnection.startClient(avversarioDevice, MY_UUID_INSECURE);
                //TODO: un timer di 4-5sec per la syncr dei thread
                Toast.makeText(StartGameActivity.this, "LET'S GO!", Toast.LENGTH_SHORT).show();
            }
        });


        goBack((Button) findViewById(R.id.btnBack));
    }


    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            text = intent.getStringExtra("message");
            System.out.println("testo: " + text);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            Log.d(TAG, "messaggio ricevuto MAINACTIVY: " + text);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

            if(isInteger(text)){
                int imageID = Integer.parseInt(text);

                CasellaPosition casellaSelected = casellaPositionListSX.get(imageID);
                if (!casellaSelected.getImageName().equals("space")) {
                    casellaSelected.setAffondata(true);
                    sendMessage(casellaSelected.getImageName());
                    //TODO: suono affondata
                }
            }else {

                Log.d(TAG, "messaggio ricevuto MAINACTIVY2: " + text);
                draw = text;
                //setDrawValue (text);
                //TODO: suono flop
            }

        }
    };

    @Override
    protected void onStop() {
        //to avoid memory leak
        shipFiringMediaPlayer.release();
        shipFiringMediaPlayer = null;
        super.onStop();
    }


    // starting chat service method
    public void goBack(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartGameActivity.this, TableActivity2.class);
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
            e.printStackTrace();
            return false;
        }catch (NullPointerException e1){
            e1.printStackTrace();
            return false;
        }
        return true;
    }

    public Drawable getShip(ArrayList<CasellaPosition> casellaPositionArrayList, int row, int column) {

        Drawable drawable = null;
        if (!casellaPositionArrayList.isEmpty()) {
            String shipName = casellaPositionArrayList.get(row * 8 + column).getImageName();
            switch (shipName) {
                case "tie_sx":
                    drawable = getResources().getDrawable(R.drawable.tie_sx);
                    return drawable;
                case "star_destroyer_sx_2":
                    drawable = getResources().getDrawable(R.drawable.star_destroyer_sx_2);
                    return drawable;
                case "star_destroyer_sx_1":
                    drawable = getResources().getDrawable(R.drawable.star_destroyer_sx_1);
                    return drawable;
                case "death_star_sx_3":
                    drawable = getResources().getDrawable(R.drawable.death_star_sx_3);
                    return drawable;
                case "death_star_sx_1":
                    drawable = getResources().getDrawable(R.drawable.death_star_sx_1);
                    return drawable;
                case "death_star_sx_4":
                    drawable = getResources().getDrawable(R.drawable.death_star_sx_4);
                    return drawable;
                case "death_star_sx_2":
                    drawable = getResources().getDrawable(R.drawable.death_star_sx_2);
                    return drawable;
                case "space":
                    drawable = getResources().getDrawable(R.drawable.ic_galactic_space);
                    return drawable;
            }
        }
        return drawable;
    }
}