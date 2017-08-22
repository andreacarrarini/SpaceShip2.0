package com.example.andrea.starship_battle.Activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import com.example.andrea.starship_battle.model.Casella;
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
    ArrayList<Casella> caselleTableListDX = new ArrayList<>();
    BluetoothDevice avversarioDevice;

    BluetoothConnectionService mBluetoothConnection;
    StringBuffer mOutStringBuffer;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
        setContentView(R.layout.start_game);
        Resizer r = new Resizer(this);


        avversarioDevice = getIntent().getExtras().getParcelable("avversarioDevice");
        if (avversarioDevice.getBondState() == BluetoothDevice.BOND_BONDED)
            Log.i(TAG, "bonded");
        mBluetoothConnection  = new BluetoothConnectionService(StartGameActivity.this);

        //ArrayList<Casella> caselleTableListSX = savedInstanceState.getParcelableArrayList()
        /*TABLE GAME SX: tablegame con le ships inserite dal giocatore
        Bundle b = getIntent().getBundleExtra("bundle");
        ArrayList<Casella> caselleTableListSX = b.getParcelableArrayList("caselleListSX");

        TableLayout rowCompletaSX = (TableLayout) findViewById(R.id.idTab);
        for (int i = 1; i < rowCompletaSX.getChildCount(); i++) {
            TableRow row = (TableRow) findViewById(rowCompletaSX.getChildAt(i).getId());
            for (int j = 0; j < row.getChildCount(); j++) {
                if (row.getChildAt(j) instanceof ImageView) {
                    if (caselleTableListSX != null) {
                        ((ImageView) row.getChildAt(j)).setImageDrawable(caselleTableListSX.get(i-1).getDrawable());
                    }
                    r.resize(row, dim_field_square); //resize delle caselle della scacchiera

                }
            }
        }*/

        //TABLE GAME DX: tablegame con le ship dell'avversario
        TableLayout rowCompletaRX = (TableLayout) findViewById(R.id.idTabB);
        rowCompletaRX.setBackground(getResources().getDrawable(R.drawable.sfondotrovadisp));
        for (int i = 1; i < rowCompletaRX.getChildCount(); i++) {
            TableRow row = (TableRow) findViewById(rowCompletaRX.getChildAt(i).getId());
            for (int j = 0; j < row.getChildCount(); j++) {
                if (row.getChildAt(j) instanceof ImageView) {
                    r.resize(row, dim_field_square); //resize delle caselle della scacchiera

                    Casella c = new Casella((ImageView) row.getChildAt(j), false, false);//Matrice di caselle: ImageView vuote
                    caselleTableListDX.add(c);
                } else {
                    r.resize(row, dim_field_square); //resize delle textView
                }
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("incomingMessage"));


        //Confronto delle barche via Bluetooth --> scambio pacchetti
        for (final Casella c : caselleTableListDX) {
            c.getImageView().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    startBTConnection(avversarioDevice,MY_UUID_INSECURE);

                    String messageToSend = String.valueOf(c.getImageView().getId()); //value of ImageView ID
                    Log.d(TAG, "messaggio inviato MAINACTIVY: "+ messageToSend);
                    byte[] bytes = messageToSend.getBytes(Charset.defaultCharset());
                    if(mBluetoothConnection != null) {
                        mBluetoothConnection.write(bytes);
                    }else{
                        Log.d(TAG,"mbt null");
                    }

                    v.setVisibility(View.INVISIBLE);



                    /*TODO: casella.occupata corrispondente o casella.posizione forse serve un altro medoto per il thread parallelo
                    * TODO: se la casella che ho selezionato (dalla lista via bluethoot) è vuota (boolean)
                    * allora prendi la drawable corrispongente e disegnala
                    * TODO: altrimenti colorala di rosso*/
                }

            });
        }



        goBack((Button) findViewById(R.id.btnBack));
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            text= intent.getStringExtra("message");
            System.out.println("testo: "+ text);
            Log.d(TAG, "messaggio ricevuto MAINACTIVY: "+ text);

        }
    };


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

    public void startBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");

        Log.d(TAG, "Trying to pair with " + device.getName());
        //device.createBond(); //API>= 19
        mBluetoothConnection.startClient(device,uuid);

        //mBluetoothConnection = new BluetoothConnectionService(StartGameActivity.this);

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
