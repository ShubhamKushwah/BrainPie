package com.programmersk.brainpie;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MathsTest extends AppCompatActivity {

    private Typeface raleway_typeface;
    private TextView choose_maarathon_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_test);

        init();
        setFullscreen();
    }

    public void init(){
        raleway_typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway_Thin.otf");
        choose_maarathon_text = (TextView) findViewById(R.id.choose_marathon_text);
        choose_maarathon_text.setTypeface(raleway_typeface);
    }

    public void openAddTest(View view){
        startActivity(new Intent(MathsTest.this, TapStartSplash.class));
    }

    public void openSubtractionTest(View view){
        showMessageDialog();
    }

    public void openProductTest(View view){
        showMessageDialog();
    }

    public void openDivisionTest(View view){
        showMessageDialog();
    }

    public void openAllMathTest(View view){
        showMessageDialog();
    }

    //to hide the action bar and navigation bar
    public void setFullscreen(){
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    public void showMessageDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MathsTest.this);
        builder.setTitle("Sorry !").setMessage("This game is not available yet !\nStart Addition Marathon Instead !")
                .setCancelable(true)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        startActivity(new Intent(getApplicationContext(), TapStartSplash.class));
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }

    public void backArrowKeyPressed(View view){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
