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

import com.example.andrea.starship_battle.Bluetooth.AdapterTrovaDispositiviClass;
import com.example.andrea.starship_battle.R;

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
        cambiaFontButton(btnBack);
        goBack(btnBack);

        textViewBluethootTrovati = (TextView) findViewById(R.id.txt_bluethootTrovati);
        listViewBluethootTrovati = (ListView) findViewById(R.id.listView_bluethoot_trovati);

        //Shows all the found devices in the listview
        adapter = new AdapterTrovaDispositiviClass(this);
        dispositiviList = getIntent().getExtras().getParcelableArrayList("dispositiviDisponibili");

        //If there are no players around, shows a dialog
        if (dispositiviList.size()==0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);// TODO: android.R.style.Theme_Material_Dialog
            builder.setTitle(R.string.noSelectableDevices);
            builder.setMessage(R.string.errorMessage);
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(DispositiviTrovatiActivity.this, MainActivity.class);
                    Bundle b = new Bundle();
                    b.putBoolean("new_window", true); //sets new window
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
        else {
            adapter.setData(dispositiviList);
            listViewBluethootTrovati.setAdapter(adapter);
            listViewBluethootTrovati.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    avversarioDevice = dispositiviList.get(position);
                    try {
                        avversarioDevice.createBond();
                        String bondedText = getResources().getString(R.string.bonded) + " " + avversarioDevice.getName();
                        Toast.makeText(getApplicationContext(),  bondedText , Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(DispositiviTrovatiActivity.this, SetupShiptableActivity.class);
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
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Starjedi.ttf");
        textView.setTypeface(face);
    }

    public void cambiaFontButton(Button button) {
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Shadded South Personal Use.ttf");
        button.setTypeface(face);
    }

//--------------------------------------------------------------------------------------------------

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