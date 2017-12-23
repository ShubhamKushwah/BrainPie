package com.programmersk.brainpie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class VisualTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_test);

        setFullscreen();
    }

    //to hide the action bar and navigation bar
    public void setFullscreen(){
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(view.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }
}