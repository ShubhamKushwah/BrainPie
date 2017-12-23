package com.programmersk.brainpie;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Store extends AppCompatActivity {

    private Typeface raleway_typeface;
    private TextView store_main_text;
    private DatabaseHelper db;
    private int coins_value = 0, diamonds_value = 0, skips_value = 0;
    private AlertDialog alert;
    private TextView coins_text, diamonds_text, skips_text;
    private int diamonds_to_be_debited = 0, coins_to_be_credited = 0, coins_to_be_debited = 0, skips_to_be_credited = 0;
    private EditText coupon_edit_text;
    private boolean coins_called = false, skips_called = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        init();
        setFullscreen();
    }

    public void init(){
        db = new DatabaseHelper(this);

        raleway_typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway_Thin.otf");
        store_main_text = (TextView) findViewById(R.id.store_text);
        store_main_text.setTypeface(raleway_typeface);

        coins_text = (TextView) findViewById(R.id.coins_text_store);
        diamonds_text = (TextView) findViewById(R.id.diamonds_text_store);
        skips_text = (TextView) findViewById(R.id.skips_text_store);
        coupon_edit_text = (EditText) findViewById(R.id.coupon_edit_text);

        setCoinsAndDiamonds();
        setSkips();
    }

    public void setCoinsAndDiamonds(){
        Cursor res = db.getData();
        while(res.moveToNext()){
            coins_value = Integer.valueOf(res.getString(6));
            diamonds_value = Integer.valueOf(res.getString(7));
        }

        setTextSizes();

        coins_text.setText(String.valueOf(coins_value));
        diamonds_text.setText(String.valueOf(diamonds_value));
    }

    public void setSkips(){

        Cursor res = db.getData();
        while(res.moveToNext()){
            skips_value = Integer.valueOf(res.getString(8));
        }

        skips_text.setText(String.valueOf(skips_value));
    }

    public void setTextSizes(){
        if(coins_value >= 100000)
            coins_text.setTextSize(25);
        else if(coins_value >= 10000)
            coins_text.setTextSize(35);
        else
            coins_text.setTextSize(40);

        if(diamonds_value >= 100000)
            diamonds_text.setTextSize(25);
        else if(diamonds_value >= 10000)
            diamonds_text.setTextSize(35);
        else
            diamonds_text.setTextSize(40);
    }

    public void backArrowKeyPressed(View view){
        //save data and go back to previous activity
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    //to hide the action bar and navigation bar
    public void setFullscreen(){
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    public void showConfirmDialog(int key){
        if(key == 0){
            //called from buy coins
            coins_called = true;
            skips_called = false;
        }else if(key == 1){
            //called from buy skips
            coins_called = false;
            skips_called = true;
        }

        if(coins_called)
            if(diamonds_value - diamonds_to_be_debited < 0) {
                final Toast toast = Toast.makeText(getBaseContext(), "You don't have enough Diamonds!", Toast.LENGTH_SHORT);
                toast.show();
                //to make its length shorter
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 1500);
                return;
            }

        if(skips_called)
            if(coins_value - coins_to_be_debited < 0) {
                final Toast toast = Toast.makeText(getBaseContext(), "You don't have enough Coins!", Toast.LENGTH_SHORT);
                toast.show();
                //to make its length shorter
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 1500);
                return;
            }
        System.out.println("-->");
        System.out.println("-->");
        System.out.println("-->");
        System.out.println("                                         yep yada yada" + skips_called);
        System.out.println("-->");
        System.out.println("-->");
        System.out.println("-->");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        View dialogView = getLayoutInflater().inflate(R.layout.store_confirmation_dialog_layout, null);

        builder.setView(dialogView);
        alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();
    }

    public void yesButtonPressed(View view){

        if(skips_called){
            debitCoins(coins_to_be_debited);
            creditSkips(skips_to_be_credited);
            alert.cancel();

            setCoinsAndDiamonds();
            setSkips();
            return;
        }

        debitDiamonds(diamonds_to_be_debited);
        creditCoins(coins_to_be_credited);
        alert.cancel();

        setCoinsAndDiamonds();
    }

    public void noButtonPressed(View view){
        alert.cancel();
    }

    public void debitDiamonds(int howMuch){

        Cursor res = db.getData();
        while (res.moveToNext()){
            howMuch = Integer.valueOf(res.getString(7)) - howMuch;
        }
        db.UpdateDiamonds("1", String.valueOf(howMuch));
    }

    public void debitCoins(int howMuch){

        Cursor res = db.getData();
        while (res.moveToNext()){
            howMuch = Integer.valueOf(res.getString(6)) - howMuch;
        }
        db.UpdateCoins("1", String.valueOf(howMuch));
    }

    public void creditCoins(int howMuch){
        Cursor res = db.getData();
        while (res.moveToNext()){
            howMuch += Integer.valueOf(res.getString(6));
        }

        if(howMuch > 999999) {
            howMuch = 999999;
        }

        db.UpdateCoins("1", String.valueOf(howMuch));
    }

    public void creditDiamonds(int howMuch){
        Cursor res = db.getData();
        while (res.moveToNext()){
            howMuch += Integer.valueOf(res.getString(7));
        }

        if(howMuch > 999999)
            howMuch = 999999;

        db.UpdateDiamonds("1", String.valueOf(howMuch));
    }

    public void checkCouponCode(View view){
        if(coupon_edit_text.getText().toString().equals(""))
            return;
        else if(coupon_edit_text.getText().toString().equals("000001"))
            creditDiamonds(10);
        else if(coupon_edit_text.getText().toString().equals("212121"))
            creditDiamonds(50);
        else if(coupon_edit_text.getText().toString().equals("455445"))
            creditDiamonds(100);
        else if(coupon_edit_text.getText().toString().equals("999999"))
            creditDiamonds(999999);
        else
            Toast.makeText(getBaseContext(), "Sorry, Wrong Code !", Toast.LENGTH_SHORT).show();

        setCoinsAndDiamonds();
    }

    public void creditSkips(int howMuch){
        Cursor res = db.getData();
        while (res.moveToNext()){
            howMuch += Integer.valueOf(res.getString(8));
        }

        if(howMuch > 9999)
            howMuch = 9999;

        db.UpdateSkips("1", String.valueOf(howMuch));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void buyCoinsButton0Pressed(View view){
        diamonds_to_be_debited = 1;
        coins_to_be_credited = 1000;
        showConfirmDialog(0);
    }
    public void buyCoinsButton1Pressed(View view){
        diamonds_to_be_debited = 5;
        coins_to_be_credited = 6000;
        showConfirmDialog(0);
    }
    public void buyCoinsButton2Pressed(View view){
        diamonds_to_be_debited = 10;
        coins_to_be_credited = 15000;
        showConfirmDialog(0);
    }
    public void buyCoinsButton3Pressed(View view){
        diamonds_to_be_debited = 50;
        coins_to_be_credited = 80000;
        showConfirmDialog(0);
    }

    public void buySkipsButton0Pressed(View view){
        coins_to_be_debited = 200;
        skips_to_be_credited = 1;

        showConfirmDialog(1);
    }
    public void buySkipsButton1Pressed(View view){
        coins_to_be_debited = 1000;
        skips_to_be_credited = 6;

        showConfirmDialog(1);
    }
    public void buySkipsButton2Pressed(View view){
        coins_to_be_debited = 2000;
        skips_to_be_credited = 15;

        showConfirmDialog(1);
    }
    public void buySkipsButton3Pressed(View view){
        coins_to_be_debited = 3000;
        skips_to_be_credited = 20;

        showConfirmDialog(1);
    }

}
