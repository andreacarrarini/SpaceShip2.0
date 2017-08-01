package com.example.andrea.starship_battle.Bluetooth;

import android.app.Activity;
import android.bluetooth.*;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.View;
import android.widget.*;

import com.example.andrea.starship_battle.Bluetooth.AdapterClass;
import com.example.andrea.starship_battle.R;

import java.lang.reflect.Method;
import java.util.ArrayList;


public class TrovaDispositiviList extends Activity {

    public TextView textViewBluethootTrovati;
    public ListView listViewBluethootTrovati;
    private ArrayList<BluetoothDevice> dispositiviList;

    public AdapterClass adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trovadispositivilist);


        textViewBluethootTrovati = (TextView) findViewById(R.id.txt_bluethootTrovati);
        listViewBluethootTrovati = (ListView) findViewById(R.id.listView_bluethoot_trovati);

        //Presi i valori dei dispositivi attivi, li mostro nella listView
        adapter = new AdapterClass(this);
        dispositiviList = getIntent().getExtras().getParcelableArrayList("dispositiviDisponibili");
        adapter.setData(dispositiviList);
        listViewBluethootTrovati.setAdapter(adapter);
        listViewBluethootTrovati.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice avversario = dispositiviList.get(position);
                try {
                    String a = createBond(avversario);
                    //TEST
                    Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });




        cambiaFontTextView2(textViewBluethootTrovati);

    }

//--------------------------------------------------------------------------------------------------

    public void cambiaFontTextView2(TextView textView) {
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Blanche de la Fontaine.ttf");
        textView.setTypeface(face);
    }

//--------------------------------------------------------------------------------------------------

    //accoppiamento device
    public String createBond(BluetoothDevice btDevice) throws Exception{

            Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
            Method createBondMethod = class1.getMethod("createBond");
            Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        //TEST
            return "ok";
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