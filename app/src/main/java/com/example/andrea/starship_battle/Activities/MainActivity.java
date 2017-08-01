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

import com.example.andrea.starship_battle.Bluetooth.TrovaDispositiviList;
import com.example.andrea.starship_battle.R;

import java.util.ArrayList;


public class MainActivity extends Activity {
    public Button buttonProfilo;
    public Button buttonBluethoot;
    public Button buttonIniziaPartita;
    public TextView textViewSpaceButtle;
    public BluetoothAdapter myBluetoothAdapter;
    public Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inizializzo i bottoni
        buttonProfilo = (Button) findViewById(R.id.btn_profilo);
        buttonBluethoot = (Button) findViewById(R.id.btn_bluethoot);
        buttonIniziaPartita = (Button) findViewById(R.id.btn_iniziapartita);
        //inizializzo le textView
        textViewSpaceButtle = (TextView) findViewById(R.id.txt_spacebuttle);
        //inizializzo il bluethoot
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        button = (Button) findViewById(R.id.button);


        cambiaFontTextView(textViewSpaceButtle);
        cambiaFontButton(buttonProfilo);
        muoviBottone(buttonProfilo);
        cambiaFontButton(buttonBluethoot);
        muoviBottone(buttonBluethoot);
        cambiaFontButton(buttonIniziaPartita);
        muoviBottone(buttonIniziaPartita);

        attivaBluethoot();

        intentATrovaDispositivi();
        playGame(button);

    }

    //--------------------------------------------------------------------------------------------------
   /* public void playGame(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, tableActivity.class);
                startActivity(intent);
            }

        });
    }*/

    public void playGame(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, tableActivity2.class);
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
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Blanche de la Fontaine.ttf");
        textView.setTypeface(face);
    }

//--------------------------------------------------------------------------------------------------

    public void muoviBottone(Button button) {
        //imposta la posizione TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)
        final Animation animation = new TranslateAnimation(0, 0, 200, 0);
        // imposta l'Animazione per 2,5 sec
        animation.setDuration(3000);
        //per fermare il bottone nella nuova posizione
        animation.setFillAfter(true);
        button.startAnimation(animation);
    }

//--------------------------------------------------------------------------------------------------

    public void attivaBluethoot() {

        //mostra toast se il dispositivo non sopporta il bluetooth
        if (myBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), R.string.no_Bluethoot_Default, Toast.LENGTH_SHORT).show();
        }
        if (myBluetoothAdapter.isEnabled()) {
            buttonBluethoot.setText(R.string.bluethoot_attivo);
        }
        //accensione e spegnimento del bluethoot
        buttonBluethoot.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {

                                                   if (!myBluetoothAdapter.isEnabled()) {
                                                       Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                                       //chiamo la startActivityForResult per acendere il bluethoot attraverso
                                                       //le impostazioni del sistema senza uscire dall'app
                                                       startActivityForResult(intent, 10);
                                                       buttonBluethoot.setText(R.string.activated_bluethoot);
                                                       Toast.makeText(getApplicationContext(), R.string.activated_bluethoot, Toast.LENGTH_SHORT).show();
                                                       //attivo la visibilita del bluethoot sul mio dispositivo
                                                       Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                                                       discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                                                       startActivity(discoverableIntent);

                                                   } else {
                                                       myBluetoothAdapter.disable();
                                                       buttonBluethoot.setText(R.string.disabled_bluethoot);
                                                       Toast.makeText(getApplicationContext(), R.string.disabled_bluethoot, Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                           }
        );
    }

//--------------------------------------------------------------------------------------------------

    private BluetoothAdapter bAdapter;
    public ArrayList<BluetoothDevice> dispositiviList;
  /*  private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;*/

    private void intentATrovaDispositivi() {
        buttonIniziaPartita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bAdapter = BluetoothAdapter.getDefaultAdapter();

                //inizio la scansione per i nuovi dispositivi
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }

                bAdapter.startDiscovery();

                if (!bAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(), R.string.please, Toast.LENGTH_LONG).show();
                } else {
                    //ottengo i dispositivi con cui ho già accettato di condividere il bluethoot pairedDevices
                   /* Set<BluetoothDevice> pairedDevices = bAdapter.getBondedDevices();
                    dispositiviList.addAll(pairedDevices);*/

                    IntentFilter filter = new IntentFilter();
                    filter.addAction(BluetoothDevice.ACTION_FOUND);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

                    registerReceiver(BrodcastReceiver, filter);

                }

            }
        });
    }


    private final BroadcastReceiver BrodcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //Una volta partita la scansione svuoto l'arraylist e faccio partire l'indicatore di avanzamento
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Toast.makeText(getApplicationContext(), R.string.scanning, Toast.LENGTH_SHORT).show();
                dispositiviList = new ArrayList<>();
            }

            //ogni volta che trova un dispositivo lo aggiunge all'arraylist di dispositivi
            else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!dispositiviList.contains(device)) {
                    dispositiviList.add(device);
                }

            }

            //Finita la scansione chiudo l'indicatore avanzamento, e passo alla ListActivity i dispositivi trovati
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Intent intent1 = new Intent(MainActivity.this, TrovaDispositiviList.class);
                intent1.putParcelableArrayListExtra("dispositiviDisponibili", dispositiviList);
                startActivity(intent1);
            }
        }
    };


    //viene tolta la registrazione del brodcast receiver
    @Override
    public void onDestroy() {
        unregisterReceiver(BrodcastReceiver);
        super.onDestroy();
    }

}
