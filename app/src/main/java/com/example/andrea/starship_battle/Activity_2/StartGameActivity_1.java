package com.example.andrea.starship_battle.Activity_2;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.andrea.starship_battle.Activities.TableActivity2;
import com.example.andrea.starship_battle.Bluetooth.BluetoothConnectionService;
import com.example.andrea.starship_battle.Bluetooth.BluetoothConnectionService2;
import com.example.andrea.starship_battle.R;
import com.example.andrea.starship_battle.model.Casella;
import com.example.andrea.starship_battle.model.Constants;
import com.example.andrea.starship_battle.model.Resizer;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Diletta on 31/07/2017.
 */

public class StartGameActivity_1 extends Activity {

    private static final String TAG = "StartGameActivity";
    int dim_field_square = 11;
    ArrayList<Casella> caselleTableListDX = new ArrayList<>();
    BluetoothDevice avversarioDevice;
    BluetoothConnectionService mBluetoothConnection;
    StringBuilder messageRecived;




    private BluetoothConnectionService2 mChatService;
    private StringBuffer mOutStringBuffer = new StringBuffer("");
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");


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
                    //  mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Toast.makeText(getApplicationContext(), "messageResived "
                            + readMessage, Toast.LENGTH_SHORT).show();

                    // mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    //   mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
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


        //Confronto delle barche via Bluetooth --> scambio pacchetti
        for (final Casella c : caselleTableListDX) {
            c.getImageView().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //BTCONNSERVICE1: startBTConnection(avversarioDevice,MY_UUID_INSECURE);

                    // Attempt to connect to the device
                    mChatService = new BluetoothConnectionService2(getApplicationContext(), mHandler);
                    mChatService.connect(avversarioDevice, false); //ensecure connection in socket

                    String messageToSend = String.valueOf(c.getImageView().getId()); //value of ImageView ID
                    Log.d(TAG, "sending Data: " + messageToSend); //TEST
                    sendMessage(messageToSend);



                    //byte[] message = messageToSend.getBytes(Charset.defaultCharset());
                    //mBluetoothConnection.write(message);

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
            String text = intent.getStringExtra("message");
            messageRecived.append(text).append("\n");
            Toast.makeText(StartGameActivity_1.this, messageRecived, Toast.LENGTH_LONG).show();
        }
    };

    // starting chat service method
    public void goBack(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartGameActivity_1.this, TableActivity2.class);
                startActivity(intent);
            }

        });
    }

    public void startBTConnection(BluetoothDevice device, UUID uuid) {
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");

        mBluetoothConnection.startClient(device, uuid);
    }


    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothConnectionService2.STATE_CONNECTED) {/*
            Toast.makeText(StartGameActivity.this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "sending data from StartGame " + message);
            return;
        }*/

            // Check that there's actually something to send
            if (message.length() > 0) {
                // Get the message bytes and tell the BluetoothChatService to write
                byte[] send = message.getBytes();
                mChatService.write(send);

                // Reset out string buffer to zero and clear the edit text field
                mOutStringBuffer.setLength(0);

            }
        }
    }




}

