package com.example.andrea.starship_battle.Activities;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.andrea.starship_battle.Bluetooth.BluetoothConnectionService;
import com.example.andrea.starship_battle.R;
import com.example.andrea.starship_battle.dragNdrop.ShipPosition;
import com.example.andrea.starship_battle.model.CasellaPosition;
import com.example.andrea.starship_battle.model.Constants;
import com.example.andrea.starship_battle.model.Resizer;

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
    ShipPosition position;


    BluetoothConnectionService mBluetoothConnection;
    BluetoothDevice avversarioDevice;
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    String text;


    private final android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    Toast.makeText(getApplicationContext(), "messagetoSend "
                            + writeMessage, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Toast.makeText(getApplicationContext(), "messageResived "
                            + readMessage, Toast.LENGTH_SHORT).show();

                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    if (null != getApplicationContext()) {
                        Toast.makeText(getApplicationContext(), "Connected to "
                                + avversarioDevice.getName(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

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

        //TABLE GAME SX: tablegame con le ships inserite dal giocatore
        Bundle b = getIntent().getBundleExtra("bundle");
        ArrayList<CasellaPosition> casellaPositionArrayListSX = b.getParcelableArrayList("casellePositionListSX");

        TableLayout rowCompletaSX = (TableLayout) findViewById(R.id.idTab);
        for (int i = 1; i < rowCompletaSX.getChildCount(); i++) {
            TableRow row = (TableRow) findViewById(rowCompletaSX.getChildAt(i).getId());
            for (int j = 1; j < row.getChildCount(); j++) {
                if (row.getChildAt(j) instanceof ImageView) {
                    if (!casellaPositionArrayListSX.isEmpty()) {
                        //first row are always labels
                        (row.getChildAt(j)).setBackground(getResources().getDrawable(R.drawable.ic_galactic_space));
                        ((ImageView) row.getChildAt(j)).setImageDrawable(getShip(casellaPositionArrayListSX, i - 1, j - 1));
                    }
                    r.resize(row, dim_field_square); //resize delle caselle della scacchiera

                }
            }
        }

        //TABLE GAME DX: tablegame per la ricerca delle ship dell'avversario
        TableLayout rowCompletaRX = (TableLayout) findViewById(R.id.idTabB);
        rowCompletaRX.setBackground(getResources().getDrawable(R.drawable.sfondotrovadisp));
        for (int i = 1; i < rowCompletaRX.getChildCount(); i++) {
            TableRow row = (TableRow) findViewById(rowCompletaRX.getChildAt(i).getId());
            for (int j = 1; j < row.getChildCount(); j++) {
                if (row.getChildAt(j) instanceof ImageView) {
                    if (!casellaPositionListDX.isEmpty()) {
                        ((ImageView) row.getChildAt(j)).setImageDrawable(getShip(casellaPositionListDX, i - 1, j - 1));
                    }
                    r.resize(row, dim_field_square); //resize delle caselle della scacchiera

                }
            }
        }

        //Message receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("incomingMessage"));

        //Confronto delle barche via Bluetooth --> scambio pacchetti
        Button startConnectionGame = (Button) findViewById(R.id.btnStart);
        startConnectionGame.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                mBluetoothConnection = new BluetoothConnectionService(StartGameActivity.this);
                Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");
                Log.d(TAG, "Trying to pair with " + avversarioDevice.getName());
                mBluetoothConnection.startClient(avversarioDevice, MY_UUID_INSECURE);
            }
        });


        for (int i = 1; i < rowCompletaRX.getChildCount(); i++) {
            TableRow row = (TableRow) findViewById(rowCompletaRX.getChildAt(i).getId());
            for (int j = 0; j < row.getChildCount(); j++) {

                final View c = row.getChildAt(j);
                row.getChildAt(j).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        String messageToSend = String.valueOf(c.getId()); //value of ImageView ID
                        Log.d(TAG, "messaggio inviato MAINACTIVY: " + messageToSend);
                        byte[] bytes = messageToSend.getBytes(Charset.defaultCharset());
                        if (mBluetoothConnection != null) {
                            mBluetoothConnection.write(bytes);
                        } else {
                            Log.d(TAG, "mbt null");
                        }

                        v.setVisibility(View.INVISIBLE);


                    /*TODO: casella.occupata corrispondente o casella.posizione forse serve un altro medoto per il thread parallelo
                    * TODO: se la casella che ho selezionato (dalla lista via bluethoot) è vuota (boolean)
                    * allora prendi la drawable corrispongente e disegnala
                    * TODO: altrimenti colorala di rosso*/
                    }

                });
            }

        }
        goBack((Button) findViewById(R.id.btnBack));
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            text = intent.getStringExtra("message");
            System.out.println("testo: " + text);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            Log.d(TAG, "messaggio ricevuto MAINACTIVY: " + text);

        }
    };


    public void goBack(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartGameActivity.this, TableActivity2.class);
                startActivity(intent);
            }

        });
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