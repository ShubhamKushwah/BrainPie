package com.programmersk.brainpie;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    private Typeface time_burner_type_face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setFullscreen();
        init();
    }

    public void init(){

        time_burner_type_face = Typeface.createFromAsset(getAssets(), "fonts/time_burner_normal.ttf");

        TextView splash_owner_name_text = (TextView) findViewById(R.id.splash_owner_name);
        splash_owner_name_text.setTypeface(time_burner_type_face);

        final ImageView imageView = (ImageView)findViewById(R.id.splash_image);
        final Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        final Animation anim2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

        imageView.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(anim2);
                finish();
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //to hide the action bar and navigation bar
    public void setFullscreen(){
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

}
