package com.programmersk.brainpie;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TapStartSplash extends AppCompatActivity {

    private Typeface raleway_typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_start_splash);

        init();
        setFullscreen();
    }

    public void init(){

        raleway_typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway_Thin.otf");
        TextView tap_to_start = (TextView) findViewById(R.id.tap_start_text);
        tap_to_start.setTypeface(raleway_typeface);

    }

    //to hide the action bar and navigation bar
    public void setFullscreen(){
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    //when tapped throughout the layout
    public void startGame(View view){
        startActivity(new Intent(TapStartSplash.this, AddTest.class));
        finish();
    }
}
