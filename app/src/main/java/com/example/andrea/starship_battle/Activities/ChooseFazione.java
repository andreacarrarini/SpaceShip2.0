package com.example.andrea.starship_battle.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrea.starship_battle.R;
import com.example.andrea.starship_battle.model.Fazione;

/**
 * Created by Diletta on 06/09/2017.
 */

public class ChooseFazione extends Activity {

    ImageView Sith;
    ImageView Jedi;
    public static Fazione fazione;
    private static final String LOGCAT = "ChooseFazione";

    //The player chooses his team between Jedi and Sith
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_fazione);

        TextView place_ships = (TextView) findViewById(R.id.chooseFazione);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Starjedi.ttf");
        place_ships.setTypeface(custom_font);

        TextView jedi_text = (TextView) findViewById(R.id.jedi_text);
        jedi_text.setTypeface(custom_font);

        TextView sith_text = (TextView) findViewById(R.id.sith_text);
        sith_text.setTypeface(custom_font);


        Sith = (ImageView) findViewById(R.id.DeathStar);
        Sith.setImageDrawable(getResources().getDrawable(R.drawable.death_star_dx));
        Sith.setVisibility(View.VISIBLE);
        Sith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fazione = Fazione.Sith;
                Log.d(LOGCAT, "fazione= "+ fazione.toString()+ "-");
                sendFazione(fazione);

            }
        });

        Jedi = (ImageView) findViewById(R.id.FalconImg);
        Jedi.setImageDrawable(getResources().getDrawable(R.drawable.millenium_falcon_sx));
        Jedi.setVisibility(View.VISIBLE);
        Jedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fazione = Fazione.Jedi;
                Log.d(LOGCAT, "fazione = "+ fazione.toString()+ "-");
                sendFazione(fazione);
            }
        });

    }
    private void sendFazione (Fazione fazione){

        Intent intent = new Intent(ChooseFazione.this, MainActivity.class);
        switch (fazione) {
            case Sith:
                intent.putExtra("fazioneScelta", fazione.toString());
                break;
            case Jedi:
                intent.putExtra("fazioneScelta", fazione.toString());
                break;
        }

        finish();
        startActivity(intent);

    }
}
