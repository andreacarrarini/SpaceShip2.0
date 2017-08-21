package com.example.andrea.starship_battle.Activities;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.ParcelUuid;
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
import com.example.andrea.starship_battle.model.CasellaPosition;
import com.example.andrea.starship_battle.model.Resizer;

import java.io.InputStream;
import java.io.OutputStream;
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
    StringBuilder messageRecived;

    //TODO: Passalo con il bundle da prima!
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ArrayList<Casella> caselleTableListSX = savedInstanceState.getParcelableArrayList()
        ArrayList<CasellaPosition> casellaPositionArrayListSX = savedInstanceState.getParcelableArrayList("casellePositionListSX");
        avversarioDevice = getIntent().getExtras().getParcelable("avversarioDevice");
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("incomingMessage"));

        setContentView(R.layout.start_game);
        Resizer r = new Resizer(this);


        //TABLE GAME SX: tablegame con le ships inserite dal giocatore
        Bundle b = getIntent().getBundleExtra("bundle");
        //ArrayList<Casella> caselleTableListSX = b.getParcelableArrayList("caselleListSX");

        TableLayout rowCompletaSX = (TableLayout) findViewById(R.id.idTab);
        for (int i = 1; i < rowCompletaSX.getChildCount(); i++) {
            TableRow row = (TableRow) findViewById(rowCompletaSX.getChildAt(i).getId());
            for (int j = 0; j < row.getChildCount(); j++) {
                if (row.getChildAt(j) instanceof ImageView) {
                    if (!casellaPositionArrayListSX.isEmpty()) {
                        ((ImageView) row.getChildAt(j)).setImageDrawable(getShip(casellaPositionArrayListSX, i, j));
                    }
                    r.resize(row, dim_field_square); //resize delle caselle della scacchiera

                }
            }
        }

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

        mBluetoothConnection = new BluetoothConnectionService(StartGameActivity.this);
        Log.d(TAG, "StartGameAct: " + avversarioDevice.getName()); //TEST



        //Confronto delle barche via Bluetooth --> scambio pacchetti
        for (final Casella c : caselleTableListDX){
            c.getImageView().setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String messageToSend = String.valueOf (c.getImageView().getId()) ; //value of ImageView ID
                    Log.d(TAG, "sending Data: " + messageToSend); //TEST
                    startBTConnection(avversarioDevice,MY_UUID_INSECURE);

                    byte[] message = messageToSend.getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(message);

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
            Toast.makeText(StartGameActivity.this, messageRecived, Toast.LENGTH_LONG).show();
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

        mBluetoothConnection.startClient(device,uuid);
    }

    public Drawable getShip(ArrayList<CasellaPosition> casellaPositionArrayList, int row, int column) {
        if (!casellaPositionArrayList.isEmpty()) {
            String shipName = casellaPositionArrayList.get(row*8 + column).getImageName();
            switch (shipName) {
                case "tie_sx":
                    return getResources().getDrawable(R.drawable.tie_sx);
                case "star_destroyer_sx_2":
                    return getResources().getDrawable(R.drawable.star_destroyer_sx_2);

            }

        }
        else return null;
    }
}


