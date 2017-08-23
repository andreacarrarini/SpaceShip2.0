package com.example.andrea.starship_battle.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.*;

import com.example.andrea.starship_battle.Bluetooth.AdapterTrovaDispositiviClass;
import com.example.andrea.starship_battle.R;

import java.lang.reflect.Method;
import java.util.ArrayList;


public class DispositiviTrovatiActivity extends Activity {

    public TextView textViewBluethootTrovati;
    public ListView listViewBluethootTrovati;
    private ArrayList<BluetoothDevice> dispositiviList = new ArrayList<>();

    public AdapterTrovaDispositiviClass adapter;
    BluetoothDevice avversarioDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trovadispositivilist);

        Button btnBack = (Button) findViewById(R.id.btnIndietro);
        goBack(btnBack);

        textViewBluethootTrovati = (TextView) findViewById(R.id.txt_bluethootTrovati);
        listViewBluethootTrovati = (ListView) findViewById(R.id.listView_bluethoot_trovati);

        //Presi i valori dei dispositivi attivi, li mostro nella listView
        adapter = new AdapterTrovaDispositiviClass(this);
        dispositiviList = getIntent().getExtras().getParcelableArrayList("dispositiviDisponibili");


        if (dispositiviList == null) { //TODO: NON FUNZIONA

            AlertDialog.Builder builder = new AlertDialog.Builder(DispositiviTrovatiActivity.this); //, android.R.style.Theme_Material_Dialog
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
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            adapter.setData(dispositiviList);
            listViewBluethootTrovati.setAdapter(adapter);
            listViewBluethootTrovati.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    avversarioDevice = dispositiviList.get(position);
                    try {
                        String deviceName = createBond(avversarioDevice);
                        Toast.makeText(getApplicationContext(), deviceName, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(DispositiviTrovatiActivity.this, TableActivity2.class);
                        intent.putExtra("avversarioDevice", avversarioDevice);
                        finish();
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

    //accoppiamento device
    public String createBond(BluetoothDevice btDevice) throws Exception{
        btDevice.createBond();
            /*Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
            Method createBondMethod = class1.getMethod("createBond");
            Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);*/
        //TEST
        if(btDevice.getBondState()==BluetoothDevice.BOND_BONDED)
            return btDevice.getName()+ ": bonded "+ String.valueOf(btDevice.getBondState()); //TODO: string value
        return "error";
    }

    public void goBack(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DispositiviTrovatiActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });
    }


}