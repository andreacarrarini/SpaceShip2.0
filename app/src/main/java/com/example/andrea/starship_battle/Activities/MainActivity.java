package com.example.andrea.starship_battle.Activities;

import android.Manifest;
import android.app.*;
import android.bluetooth.*;
import android.content.*;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.*;

import com.example.andrea.starship_battle.R;

import java.util.ArrayList;


public class MainActivity extends Activity {
    public Button buttonProfilo;
    public Button buttonBluetooth;
    public Button buttonIniziaPartita;
    public TextView textViewSpaceButtle;
    public BluetoothAdapter myBluetoothAdapter;
    private ProgressDialog scanningDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonProfilo = (Button) findViewById(R.id.btn_profilo);
        buttonBluetooth = (Button) findViewById(R.id.btn_bluethoot);
        buttonIniziaPartita = (Button) findViewById(R.id.btn_iniziapartita);
        textViewSpaceButtle = (TextView) findViewById(R.id.txt_spacebuttle);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        cambiaFontTextView(textViewSpaceButtle);
        cambiaFontButton(buttonProfilo);
        muoviBottone(buttonProfilo);
        cambiaFontButton(buttonBluetooth);
        muoviBottone(buttonBluetooth);
        cambiaFontButton(buttonIniziaPartita);
        muoviBottone(buttonIniziaPartita);


        scanningDialog = new ProgressDialog(this);
        scanningDialog.setMessage(getResources().getString(R.string.scanning));
        scanningDialog.setCanceledOnTouchOutside(false);
        scanningDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                myBluetoothAdapter.cancelDiscovery();
            }
        });

        //The player chooses his team
        selectFazione(buttonProfilo);
        //The player activates the device's bluetooth
        attivaBluethoot();

        intentTrovaDispositivi();
    }

    public void selectFazione(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChooseFazione.class);
                startActivity(intent);
            }

        });
    }

//--------------------------------------------------------------------------------------------------

    public void cambiaFontButton(Button button) {
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Shadded South Personal Use.ttf");
        button.setTypeface(face);
    }

    public void cambiaFontTextView(TextView textView) {
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Starjedi.ttf");
        textView.setTypeface(face);
    }

//--------------------------------------------------------------------------------------------------

    public void muoviBottone(Button button) {
        //sets the TranslateAnimation position(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)
        final Animation animation = new TranslateAnimation(0, 0, 200, 0);
        // imposta l'Animazione per 2,5 sec
        animation.setDuration(2000);
        //to stop the button in the new position
        animation.setFillAfter(true);
        button.startAnimation(animation);
    }

//--------------------------------------------------------------------------------------------------

    public void attivaBluethoot() {

        //If the device does not support bluetooth
        if (myBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), R.string.no_Bluetooth_Default, Toast.LENGTH_SHORT).show();
        }
        if (myBluetoothAdapter.isEnabled()) {
            buttonBluetooth.setText(R.string.bluetooth_attivo);
        }
        //turning on/off bluetooth
        buttonBluetooth.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {

                                                   if (!myBluetoothAdapter.isEnabled()) {
                                                       Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                                       //calling startActivityForResult turning on the bleuetooth
                                                       startActivityForResult(intent, 10);
                                                       buttonBluetooth.setText(R.string.activated_bluetooth);
                                                       Toast.makeText(getApplicationContext(), R.string.activated_bluetooth, Toast.LENGTH_SHORT).show();

                                                       //Enabling discovery on this device
                                                       Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                                                       discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                                                       startActivity(discoverableIntent);

                                                   } else {
                                                       myBluetoothAdapter.disable();
                                                       buttonBluetooth.setText(R.string.disabled_bluetooth);
                                                       Toast.makeText(getApplicationContext(), R.string.disabled_bluetooth, Toast.LENGTH_SHORT).show();
                                                   }
                                               }}
        );
    }

//--------------------------------------------------------------------------------------------------

    private BluetoothAdapter bAdapter;
    public ArrayList<BluetoothDevice> dispositiviList;

    private void intentTrovaDispositivi() {
        buttonIniziaPartita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bAdapter = BluetoothAdapter.getDefaultAdapter();

                //showing all the permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                //discovering other devices
                bAdapter.startDiscovery();

                if (!bAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(), R.string.please, Toast.LENGTH_LONG).show();
                } else {

                    IntentFilter filter = new IntentFilter();
                    filter.addAction(BluetoothDevice.ACTION_FOUND);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

                    registerReceiver(broadcastReceiver, filter);
                }

            }
        });
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //For each discovery creates a new ArrayList
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                scanningDialog.show();
                dispositiviList = new ArrayList<>();
            }

            //Adding the devices which has been found
            else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!dispositiviList.contains(device)) {
                    dispositiviList.add(device);
                }
            }

            //Ending the scanning and showing the new Activity
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                scanningDialog.dismiss();
                Intent intent1 = new Intent(MainActivity.this, DispositiviTrovatiActivity.class);
                intent1.putParcelableArrayListExtra("dispositiviDisponibili", dispositiviList);
                                finish();
                startActivity(intent1);
            }
        }
    };

    //ending the BroadcastReceiver
    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }


}
