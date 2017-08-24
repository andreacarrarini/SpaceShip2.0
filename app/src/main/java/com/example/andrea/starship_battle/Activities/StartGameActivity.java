package com.example.andrea.starship_battle.Activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
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

//import com.example.andrea.starship_battle.Bluetooth.BluetoothConnectionService;
import com.example.andrea.starship_battle.Bluetooth.BluetoothConnectionService;
import com.example.andrea.starship_battle.R;
import com.example.andrea.starship_battle.dragNdrop.ShipPosition;
import com.example.andrea.starship_battle.model.Casella;
import com.example.andrea.starship_battle.model.CasellaPosition;
import com.example.andrea.starship_battle.model.Constants;
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
    ShipPosition position;
    BluetoothConnectionService mBluetoothConnection;
    StringBuffer mOutStringBuffer;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice avversarioDevice;
    MediaPlayer shipFiringMediaPlayer = new MediaPlayer();

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

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
        //ArrayList<Casella> caselleTableListSX = savedInstanceState.getParcelableArrayList()
        //ArrayList<CasellaPosition> casellaPositionArrayListSX = savedInstanceState.getParcelableArrayList("casellePositionListSX");
        //avversarioDevice = getIntent().getExtras().getParcelable("avversarioDevice");
        //LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("incomingMessage"));

        setContentView(R.layout.start_game);
        Resizer r = new Resizer(this);
        position = new ShipPosition(this);

        casellaPositionListDX = position.createEnemyBattlefield(casellaPositionListDX);

        avversarioDevice = getIntent().getExtras().getParcelable("avversarioDevice");
        if (avversarioDevice.getBondState() == BluetoothDevice.BOND_BONDED)
            Log.i(TAG, "bonded");
        mBluetoothConnection = new BluetoothConnectionService(StartGameActivity.this);

        //AUDIO-------------------------------------------------------------------------------------

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
        shipFiringMediaPlayer.prepareAsync();

        //------------------------------------------------------------------------------------------


        //TABLE GAME SX: tablegame con le ships inserite dal giocatore
        Bundle b = getIntent().getBundleExtra("bundle");
        ArrayList<CasellaPosition> casellaPositionArrayListSX = b.getParcelableArrayList("casellePositionListSX");
        //ArrayList<Casella> caselleTableListSX = b.getParcelableArrayList("caselleListSX");

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
        //rowCompletaRX.setBackground(getResources().getDrawable(R.drawable.sfondotrovadisp));
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

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("incomingMessage"));


        //Confronto delle barche via Bluetooth --> scambio pacchetti
        /*for (final Casella c : casellaPositionListDX){*/
        for (int i = 1; i < rowCompletaRX.getChildCount(); i++) {
            TableRow row = (TableRow) findViewById(rowCompletaRX.getChildAt(i).getId());
            for (int j = 0; j < row.getChildCount(); j++) {

                final View c = row.getChildAt(j);
                row.getChildAt(j).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        startBTConnection(avversarioDevice, MY_UUID_INSECURE);

                        String messageToSend = String.valueOf(c.getId()); //value of ImageView ID
                        Log.d(TAG, "messaggio inviato MAINACTIVY: " + messageToSend);
                        byte[] bytes = messageToSend.getBytes(Charset.defaultCharset());
                        if (mBluetoothConnection != null) {
                            mBluetoothConnection.write(bytes);
                        } else {
                            Log.d(TAG, "mbt null");
                        }
                        //plays the file audio
                        shipFiringMediaPlayer.start();
                        v.setVisibility(View.INVISIBLE);
                        //seeks the file audio to 0 msec
                        shipFiringMediaPlayer.seekTo(0);

                    /*TODO: casella.occupata corrispondente o casella.posizione forse serve un altro medoto per il thread parallelo
                    * TODO: se la casella che ho selezionato (dalla lista via bluethoot) è vuota (boolean)
                    * allora prendi la drawable corrispongente e disegnala
                    * TODO: altrimenti colorala di rosso*/
                    }

                });
            }
            goBack((Button) findViewById(R.id.btnBack));
        }
        goBack((Button) findViewById(R.id.btnBack));
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            text = intent.getStringExtra("message");
            System.out.println("testo: " + text);
            Log.d(TAG, "messaggio ricevuto MAINACTIVY: " + text);

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

    public void startBTConnection(BluetoothDevice device, UUID uuid) {
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");

        Log.d(TAG, "Trying to pair with " + device.getName());
        //device.createBond(); //API>= 19
        mBluetoothConnection.startClient(device, uuid);

        //mBluetoothConnection = new BluetoothConnectionService(StartGameActivity.this);

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































    /*

    nell'onClick:
        String messageToSend = String.valueOf(c.getImageView().getId()); //value of ImageView ID

                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    // Get the BluetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(avversarioDevice.getAddress());
                    Log.e(TAG, "calling  -onStart: sending " + messageToSend);
                    onStart(messageToSend, device);

                    v.setVisibility(View.INVISIBLE);




    public void onStart(String message,BluetoothDevice device) {


        if (mChatService == null) {
            Log.d(TAG, "onStart attivo");
            // Initialize the BluetoothChatService to perform bluetooth connections
            mChatService = new BluetoothChatService(StartGameActivity.this, mHandler);  //TODO: forseprima

            sendMessage(message, device);

            // Initialize the buffer for outgoing messages
            mOutStringBuffer = new StringBuffer("");

        }else if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
        return drawable;
    }
}



    private void sendMessage(String message, BluetoothDevice device) {
        mChatService.connect(device, false);

        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Log.e(TAG, "non connesso:STATE_CONNECTED");
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
            Log.e(TAG, "sendMessage "+ message);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }*/
