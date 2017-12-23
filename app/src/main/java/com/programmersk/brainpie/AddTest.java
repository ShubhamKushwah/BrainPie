package com.programmersk.brainpie;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class AddTest extends AppCompatActivity {

    private TextView addition_marathon_text;
    private TextView score, score_text;
    private TextView level;
    private TextView answer;
    private TextView time_left_save_me;
    private TextView best_score, best_score_text;
    private TextView coins_text, x_before_coins_text;
    private int best_score_value;

    /* just for house keeping stuffs */
    @SuppressLint("StaticFieldLeak")
    public TextView num1, num2;
    public int n1 = 1, n2 = 1;
    private int i = 1;
    private int score_value = 0;
    private int coins_earned = 0;
    private int level_value = 1;
    public int high, low;
    public Random random;
    private TextView timer_text;
    private int time_value;
    private CountDownTimer timer, save_me_timer;
    private int total_time_elapsed = 0;
    private pl.droidsonroids.gif.GifImageView timer_gif;
    private String answer_value = "";
    private DatabaseHelper db;
    private AlertDialog alert, alert_time_up, alert_quit, alert_save_me;
    private Typeface ubuntu_typeface, raleway_typeface, lato_thin_typeface, varela_typeface;

    //pause dialog views
    private TextView time_up_TUD, level_text_TUD, level_TUD, score_text_TUD, score_TUD, coins_text_TUD, coins_TUD, total_time_taken_TUD, total_time_taken_text_TUD;    //TUD = in Time Up Dialog
    private Button try_again_btn_TUD, quit_btn_TUD;

    //quit dialog views
    private TextView are_you_sure_text_QD, all_progress_lost_text_QD;   //QD = in Quit Dialog
    private Button yes_btn_QD, no_btn_QD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreen();
        setContentView(R.layout.activity_add_test);

        init();
        startTimer();
    }


    public void init() {
        x_before_coins_text = (TextView) findViewById(R.id.before_coins_text_x);
        coins_text = (TextView) findViewById(R.id.coins_text);
        addition_marathon_text = (TextView) findViewById(R.id.addition_marathon_text);
        num1 = (TextView)findViewById(R.id.num1);
        num2 = (TextView)findViewById(R.id.num2);
        answer = (TextView) findViewById(R.id.result_text);
        score = (TextView)findViewById(R.id.score_value);
        score_text = (TextView) findViewById(R.id.score_text);
        best_score = (TextView) findViewById(R.id.best_score_value);
        best_score_text = (TextView) findViewById(R.id.best_score_text);
        timer_text = (TextView)findViewById(R.id.timer_text);
        level = (TextView)findViewById(R.id.level);
        level.setText(String.valueOf(level_value));
        timer_gif = (pl.droidsonroids.gif.GifImageView)findViewById(R.id.timer_gif);
        db = new DatabaseHelper(this);
        ubuntu_typeface = Typeface.createFromAsset(getAssets(), "fonts/Ubuntu_Light.ttf");
        raleway_typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway_Thin.otf");
        lato_thin_typeface = Typeface.createFromAsset(getAssets(), "fonts/Lato_Thin.ttf");
        varela_typeface = Typeface.createFromAsset(getAssets(), "fonts/Varela_Round.ttf");

        addition_marathon_text.setTypeface(raleway_typeface);
        level.setTypeface(ubuntu_typeface);
        score.setTypeface(ubuntu_typeface);
        score_text.setTypeface(ubuntu_typeface);
        best_score.setTypeface(ubuntu_typeface);
        best_score_text.setTypeface(ubuntu_typeface);
        timer_text.setTypeface(ubuntu_typeface);
        num1.setTypeface(varela_typeface);
        num2.setTypeface(varela_typeface);
        answer.setTypeface(lato_thin_typeface);
        x_before_coins_text.setTypeface(ubuntu_typeface);
        coins_text.setTypeface(ubuntu_typeface);

        time_value = 15000;  //15secs

        random = new Random();
        high = 100;
        low = 80;
        score.setText(String.valueOf(score_value));
        best_score.setText(String.valueOf(best_score_value));
        getRandomNumber();
        setValues();

        //to set the best score
        setBestScore();

        //to set the avatar image
        setAvatar();
    }

    public void setBestScore(){
        Cursor res = db.getData();
        while(res.moveToNext()) {
            best_score_value = Integer.valueOf(res.getString(5));
        }
        best_score.setText(String.valueOf(best_score_value));
    }

    //to set the difficulty using 'i' at every five correct answers
    public void setDifficulty(){
        if(i%5 == 0) {
            increaseDifficulty();
            level_value += 1;
            level.setText(String.valueOf(level_value));
        }
    }

    public void increaseDifficulty(){
        high *= 3;    //just random numbers
        low *= 2;     //just random numbers
        coins_earned += 10;
        score_value += 100;
        time_value += 1000;
    }

    public void checkAnswer(){
        int sum = n1 + n2;
        if(answer_value.equals("")){
            answer.setText("");
            Toast.makeText(getApplicationContext(), "Please answer the question !", Toast.LENGTH_SHORT).show();
            return;
        }
        //if correct
        if(answer.getText().toString().equals(String.valueOf(sum))) {
            getRandomNumber();
            setValues();
            answer.setText("");
            i++;    //to flag for increase difficulty
            score_value += 50;
            coins_earned += 5;

            setDifficulty();
            setScoreText();
            setCoinsText();

            //start timer again
            timer.cancel();
            timer_text.setTextColor(Color.parseColor("#26A69A"));
            startTimer();

            //clear the value in answer_value
                answer_value = "";
        }

        //to update best score value of it is beaten
        if(score_value > best_score_value)
            best_score_value = score_value;
        best_score.setText(String.valueOf(best_score_value));
    }

    public void setScoreText(){
        if(score_value >= 0)
            score.setText(String.valueOf(score_value));
        else {
            score_value = 0;
            score.setText(String.valueOf(score_value));
        }
    }

    public void setCoinsText(){
        if(coins_earned >= 0)
            coins_text.setText(String.valueOf(coins_earned));
        else {
            coins_earned = 0;
            coins_text.setText(String.valueOf(coins_earned));
        }
    }

    public void setValues(){
        num1.setText(String.valueOf(n1));
        num2.setText(String.valueOf(n2));
    }

    public void getRandomNumber(){
        n1 = random.nextInt(high - low) + low;
        n2 = random.nextInt(high - low) + low;
        setRandomColors();
    }

    //to hide the action bar and navigation bar
    public void setFullscreen(){
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    public void tryAgainButtonPressed(View view){
        alert_time_up.cancel();
        AddTest.this.recreate();
    }

    public void quitButtonPressedTimeUpDialog(View view){
        finish();
    }

    public void saveMeButtonPressed(View view){
        //if diamonds not enough --> return
        if(!checkDiamonds()){
            return;
        }

        save_me_timer.cancel();
        alert_save_me.cancel();
        getRandomNumber();
        setValues();

        //clear the answer textView
        answer.setText("");

        //start timer again
        timer.cancel();
        timer_text.setTextColor(Color.parseColor("#26A69A"));
        startTimer();

        //clear the value in answer_value
        answer_value = "";
    }

    public boolean checkDiamonds(){
        int diamonds = 0;
        Cursor res = db.getData();
        while (res.moveToNext()){
            diamonds = Integer.valueOf(res.getString(7));
        }
        if(diamonds > 0){
            --diamonds; // deduct a diamond
            db.UpdateDiamonds("1", String.valueOf(diamonds));
            return true;    //successfully deducted
        }else {
            Toast.makeText(getBaseContext(), "You don't have enough diamonds", Toast.LENGTH_SHORT).show();
            //don't have enough diamonds
            return false;
        }
    }

    public void quitButtonPressedPauseDialog(View view){
        showExitMessageDialog();
    }

    //PAUSE MENU
    public void openPauseMenu(View view){
        score_value -= 50;
        coins_earned -= 5;
        timer.cancel();
        showPauseDialog();
    }

    public void showExitMessageDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTest.this);
        builder.setCancelable(false);
        View dialogView = getLayoutInflater().inflate(R.layout.exit_dialog_layout, null);

        //views declared here
        are_you_sure_text_QD = (TextView) dialogView.findViewById(R.id.are_you_sure_QD);
        all_progress_lost_text_QD = (TextView) dialogView.findViewById(R.id.all_progress_lost_text_QD);
        yes_btn_QD = (Button) dialogView.findViewById(R.id.yes_btn_TUD);
        no_btn_QD = (Button) dialogView.findViewById(R.id.no_btn_QD);

        are_you_sure_text_QD.setTypeface(raleway_typeface);
        all_progress_lost_text_QD.setTypeface(ubuntu_typeface);
        yes_btn_QD.setTypeface(ubuntu_typeface);;
        no_btn_QD.setTypeface(ubuntu_typeface);
        //end here

        builder.setView(dialogView);
        alert_quit = builder.create();
        alert_quit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert_quit.show();
    }


    public void startTimer(){
        // +1000 just for house keeping stuffs
        timer = new CountDownTimer(time_value + 1000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                timer_text.setText(String.valueOf(millisUntilFinished/1000));
                perTickUpdate();
                //to change color to red if less time remains
                if(millisUntilFinished < 4000){
                    timer_text.setTextColor(Color.parseColor("#F44336"));
                }
            }

            @Override
            public void onFinish() {
                showSaveMeDialog();
            }
        }.start();
    }

    //this method will update values every second
    public void perTickUpdate(){
        total_time_elapsed++;
        score_value ++;
        setScoreText();
        //to update best score vallue of it is beaten
        if(score_value > best_score_value)
            best_score_value = score_value;
        best_score.setText(String.valueOf(best_score_value));
    }

    public void startSaveMeTimer(){
        //runs for 5 secs
        save_me_timer = new CountDownTimer(4000 + 1000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                time_left_save_me.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                alert_save_me.cancel();
                showTimeUpDialog();
            }
        }.start();
    }

    public void numberButtonsListener(View view){
        Button btn = (Button) view;
        if(answer_value.length() < maxLengthAllowed())
            answer_value += btn.getText().toString();
        updateScreen(false);

        //check for answer
        if(answer.length() == maxLengthAllowed())
            checkAnswer();
    }

    public int maxLengthAllowed(){
        return String.valueOf(n1+n2).length();
    }

    public void updateScreen(Boolean backPressed){
        //backpressed is used to check if 'updateScreen()' function is called from backspace key or not.
        if(answer_value.equals(""))
            answer.setText("");
        if(answer.length() < maxLengthAllowed() || backPressed) {
            answer.setText(answer_value);
        }
    }

    public void backspacePressed(View view){
        if (!answer_value.equals("")) {
            answer_value = answer_value.substring(0, answer_value.length()-1);
            updateScreen(true);     //true will allow backspace to erase the text even if it has reached the limit
        }
    }

    public void setRandomColors(){
        int color_flag = random.nextInt(10);
        switch (color_flag){
            case 0:
                num1.setTextColor(Color.parseColor("#26A69A"));
                num2.setTextColor(Color.parseColor("#26A69A"));
                break;
            case 1:
                num1.setTextColor(Color.parseColor("#00BCD4"));
                num2.setTextColor(Color.parseColor("#00BCD4"));
                break;
            case 2:
                num1.setTextColor(Color.parseColor("#1DE9B6"));
                num2.setTextColor(Color.parseColor("#1DE9B6"));
                break;
            case 3:
                num1.setTextColor(Color.parseColor("#E57373"));
                num2.setTextColor(Color.parseColor("#E57373"));
                break;
            case 4:
                num1.setTextColor(Color.parseColor("#9C27B0"));
                num2.setTextColor(Color.parseColor("#9C27B0"));
                break;
            case 5:
                num1.setTextColor(Color.parseColor("#E91E63"));
                num2.setTextColor(Color.parseColor("#E91E63"));
                break;
            case 6:
                num1.setTextColor(Color.parseColor("#4CAF50"));
                num2.setTextColor(Color.parseColor("#4CAF50"));
                break;
            case 7:
                num1.setTextColor(Color.parseColor("#CDDC39"));
                num2.setTextColor(Color.parseColor("#CDDC39"));
                break;
            case 8:
                num1.setTextColor(Color.parseColor("#00E676"));
                num2.setTextColor(Color.parseColor("#00E676"));
                break;
            case 9:
                num1.setTextColor(Color.parseColor("#FF9800"));
                num2.setTextColor(Color.parseColor("#FF9800"));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showExitMessageDialog();
    }

    public void setAvatar(){
        ImageView avatar = (ImageView) findViewById(R.id.avatar);
        Cursor res = db.getData();
        while(res.moveToNext()){
            if(res.getString(3).equals("101")){
                avatar.setImageResource(R.drawable.avatar_boy);
            }
            else if(res.getString(3).equals("102")){
                avatar.setImageResource(R.drawable.avatar_girl);
            }
            else if(res.getString(3).equals("103")){
                avatar.setImageResource(R.drawable.avatar_man);
            }
            else if(res.getString(3).equals("104")){
                avatar.setImageResource(R.drawable.avatar_woman);
            }
        }
    }

    public void showPauseDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTest.this);
        builder.setCancelable(false);
        View dialogView = getLayoutInflater().inflate(R.layout.pause_dialog_layout, null);

        //set views here
        TextView pause_text = (TextView) dialogView.findViewById(R.id.pause_text);
        TextView store_text = (TextView) dialogView.findViewById(R.id.store_text_pause_dialog);
        Button resume_btn = (Button) dialogView.findViewById(R.id.resume_btn);

        resume_btn.setTypeface(ubuntu_typeface);
        store_text.setTypeface(raleway_typeface);
        pause_text.setTypeface(ubuntu_typeface);
        //end views

        builder.setView(dialogView);
        alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();
    }

    public void resumeButtonPressed(View view){
        alert.cancel();

        setScoreText();
        setCoinsText();

        getRandomNumber();
        setValues();
        startTimer();
    }

    public void openStore(View view){
        startActivity(new Intent(AddTest.this, Store.class));
    }

    public void yes_btn_pressed(View view){
        timer.cancel();
        alert_quit.cancel();
        finish();
    }

    public void no_btn_pressed(View view){
        alert_quit.cancel();
    }

    public void updateBestScoreInDB(){
        db.UpdateBestScore("1", String.valueOf(best_score_value));
    }

    public void updateCoinsInDB(){
        Cursor res = db.getData();
        while ((res.moveToNext())){
            coins_earned += Integer.valueOf(res.getString(6));
        }
        db.UpdateCoins("1", String.valueOf(coins_earned));
    }

    public void showSaveMeDialog(){

        if (alert_quit != null) {
            alert_quit.cancel();   //if quit quit dialog is not being shown don't attempt to close it
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(AddTest.this);
        builder.setCancelable(false);
        View dialogView = getLayoutInflater().inflate(R.layout.saveme_dialog_layout, null);

        //views declared here
        time_left_save_me = (TextView) dialogView.findViewById(R.id.time_left_save_me);
        time_left_save_me.setTypeface(ubuntu_typeface);
        startSaveMeTimer();
        //end here

        builder.setView(dialogView);
        alert_save_me = builder.create();
        alert_save_me.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert_save_me.show();
    }

    public void showTimeUpDialog(){

        if (alert_quit != null) {
            alert_quit.cancel();   //if quit quit dialog is not being shown don't attempt to close it
        }

        timer_gif.setVisibility(View.GONE);
        timer_text.setTextColor(Color.parseColor("#F44336"));
        timer_text.setText("X");

        AlertDialog.Builder builder = new AlertDialog.Builder(AddTest.this);
        builder.setCancelable(false);
        View dialogView = getLayoutInflater().inflate(R.layout.timeup_dialog_layout, null);

        //views declared here
        time_up_TUD = (TextView) dialogView.findViewById(R.id.time_up_text_TUD);
        level_text_TUD = (TextView) dialogView.findViewById(R.id.level_text_TUD);
        level_TUD = (TextView) dialogView.findViewById(R.id.level_TUD);
        score_text_TUD = (TextView) dialogView.findViewById(R.id.score_text_TUD);
        score_TUD = (TextView) dialogView.findViewById(R.id.score_TUD);
        coins_text_TUD = (TextView) dialogView.findViewById(R.id.coins_text_TUD);
        coins_TUD = (TextView) dialogView.findViewById(R.id.coins_TUD);
        total_time_taken_text_TUD = (TextView) dialogView.findViewById(R.id.total_time_text_TUD);
        total_time_taken_TUD = (TextView) dialogView.findViewById(R.id.total_time_TUD);
        try_again_btn_TUD = (Button) dialogView.findViewById(R.id.try_again_btn_TUD);
        quit_btn_TUD = (Button) dialogView.findViewById(R.id.quit_btn_TUD);

        level_TUD.setText(String.valueOf(level_value));
        score_TUD.setText(String.valueOf(score_value));
        coins_TUD.setText(String.valueOf(coins_earned));
        total_time_taken_TUD.setText(String.valueOf(total_time_elapsed));


        time_up_TUD.setTypeface(ubuntu_typeface);
        level_text_TUD.setTypeface(ubuntu_typeface);
        level_TUD.setTypeface(ubuntu_typeface);;
        score_text_TUD.setTypeface(ubuntu_typeface);
        score_TUD.setTypeface(ubuntu_typeface);
        coins_text_TUD.setTypeface(ubuntu_typeface);
        coins_TUD.setTypeface(ubuntu_typeface);
        total_time_taken_text_TUD.setTypeface(ubuntu_typeface);
        total_time_taken_TUD.setTypeface(ubuntu_typeface);
        try_again_btn_TUD.setTypeface(ubuntu_typeface);
        quit_btn_TUD.setTypeface(ubuntu_typeface);
        //end here

        updateBestScoreInDB();
        updateCoinsInDB();

        builder.setView(dialogView);
        alert_time_up = builder.create();
        alert_time_up.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert_time_up.show();

    }

    public void skipsButtonListener(View view){
        //random numbers
        getRandomNumber();
        setValues();
        answer.setText("");

        //setting values
        setDifficulty();
        setScoreText();
        setCoinsText();

        //start timer again
        timer.cancel();
        timer_text.setTextColor(Color.parseColor("#26A69A"));
        startTimer();

        //clear the value in answer_value
        answer_value = "";

        //to update best score value of it is beaten
        if(score_value > best_score_value)
            best_score_value = score_value;
        best_score.setText(String.valueOf(best_score_value));
    }

}