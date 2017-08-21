package com.example.andrea.starship_battle.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.View;
import android.widget.*;

import com.example.andrea.starship_battle.Activity_2.TableActivity2_1;
import com.example.andrea.starship_battle.Bluetooth.AdapterTrovaDispositiviClass;
import com.example.andrea.starship_battle.R;

import java.util.ArrayList;


public class DispositiviTrovatiActivity extends Activity {

    public TextView textViewBluethootTrovati;
    public ListView listViewBluethootTrovati;
    private ArrayList<BluetoothDevice> dispositiviList=new ArrayList<>();

    public AdapterTrovaDispositiviClass adapter;
    BluetoothDevice avversarioDevice;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trovadispositivilist);

        Button btnBack = (Button) findViewById(R.id.btnIndietro);
        goBack(btnBack);
        Button btnNext = (Button) findViewById(R.id.btnAvanti);
        goNext(btnNext);

        textViewBluethootTrovati = (TextView) findViewById(R.id.txt_bluethootTrovati);
        listViewBluethootTrovati = (ListView) findViewById(R.id.listView_bluethoot_trovati);

        //Presi i valori dei dispositivi attivi, li mostro nella listView
        adapter = new AdapterTrovaDispositiviClass(this);
        dispositiviList = getIntent().getExtras().getParcelableArrayList("dispositiviDisponibili");


        if(dispositiviList==null){ //TODO: NON FUNZIONA
            builder = new AlertDialog.Builder(getApplicationContext(), android.R.style.TextAppearance_Theme); //Theme_Material_Dialog_Alert
            builder.setTitle(R.string.noSelectableDevices)
                    .setMessage(R.string.errorMessage)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(DispositiviTrovatiActivity.this, MainActivity.class);
                            Bundle b = new Bundle();
                            b.putBoolean("new_window", true); //sets new window
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else {
            adapter.setData(dispositiviList);
            listViewBluethootTrovati.setAdapter(adapter);
            listViewBluethootTrovati.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    avversarioDevice = dispositiviList.get(position);
                    try {
                        //String a = createBond(avversarioDevice);
                        Toast.makeText(getApplicationContext(), avversarioDevice.getName(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(DispositiviTrovatiActivity.this, TableActivity2.class);
                        intent.putExtra("avversarioDevice", avversarioDevice);
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        cambiaFontTextView2(textViewBluethootTrovati);
    }

//--------------------------------------------------------------------------------------------------

    public void cambiaFontTextView2(TextView textView) {
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Blanche de la Fontaine.ttf");
        textView.setTypeface(face);
    }

//--------------------------------------------------------------------------------------------------

    /*accoppiamento device
    public String createBond(BluetoothDevice btDevice) throws Exception{

            Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
            Method createBondMethod = class1.getMethod("createBond");
            Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        //TEST
            return "OK";
    }*/

    public void goBack(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DispositiviTrovatiActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });
    }

    public void goNext(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DispositiviTrovatiActivity.this, TableActivity2_1.class);
                startActivity(intent);
            }

        });
    }

/*
    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                tmp = adapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }

                if (socket != null) {
                    // A connection was accepted. Perform work associated with
                    // the connection in a separate thread.
                    manageMyConnectedSocket(socket);
                    mmServerSocket.close();
                    break;
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }*/

}