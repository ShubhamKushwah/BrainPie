package com.programmersk.brainpie;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private Typeface ubuntu_typeface, raleway_typeface, lato_thin_typeface;
    private AlertDialog alert;
    private DatabaseHelper db;
    private TextView user_welcome_text, store_text;
    private EditText edit_name, edit_age;
    private int profile_pic = 101;    //profile image id for saving into database
    private ImageButton avatar;
    private GifImageView img;

    //settings dialog views
    private TextView settings_profile_text, settings_main_text, music_vol_text, sfx_vol_text;
    private TextView enter_name;
    private TextView enter_age;
    private TextView choose_avatar;
    private TextView coins_text, diamonds_text;
    private Button save_btn;
    private Button cancel_btn;
    private ImageView avatar1, avatar2, avatar3, avatar4;
    private View white_line;
    private SeekBar music_seekbar, sfx_seekbar;

    public MainActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init(){
        diamonds_text = (TextView) findViewById(R.id.diamonds_text);
        coins_text = (TextView) findViewById(R.id.coins_text);
        ubuntu_typeface = Typeface.createFromAsset(getAssets(), "fonts/Ubuntu_Light.ttf");
        raleway_typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway_Thin.otf");
        lato_thin_typeface = Typeface.createFromAsset(getAssets(), "fonts/Lato_Thin.ttf");
        user_welcome_text = (TextView) findViewById(R.id.user_welcome_text);
        user_welcome_text.setTypeface(raleway_typeface);
        store_text = (TextView) findViewById(R.id.store_text);
        avatar = (ImageButton) findViewById(R.id.user_avatar);
        img = (GifImageView) findViewById(R.id.dialog_background_gif);
        img.setVisibility(View.VISIBLE);
        store_text.setTypeface(raleway_typeface);

        db = new DatabaseHelper(this);

        setFullscreen();
        insertNullData();
        showUserDialog();
        setWelcomeText();
        setAvatar();
        setCoinsText();
        setDiamondsText();
    }

    public void insertNullData(){
        db.InsertData("1", "Player", "0", "101", "true", "0", "50", "5", "5");   //initially set show_dialog to TRUE
    }

    //to start maths activity
    public void openMathsTest(View view){
        startActivity(new Intent(getApplicationContext(), MathsTest.class));
        finish();
    }

    //to start visual activity
    public void openVisualTest(View view){
        startActivity(new Intent(getApplicationContext(), VisualTest.class));
    }

    //to hide the action bar and navigation bar
    public void setFullscreen(){
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    public void showUserDialog(){
        //if show_dialog is set to false then -> return.
        Cursor res = db.getData();
        while(res.moveToNext()){
            if(res.getString(4).equals("false")) {
                //remove the background gif
                img.setVisibility(View.GONE);
                return;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        View dialogView = getLayoutInflater().inflate(R.layout.user_dialog_layout, null);

        //views & declarations etc. here
        TextView main_text_top = (TextView) dialogView.findViewById(R.id.tell_us_sth_text_view);
        TextView enter_name = (TextView) dialogView.findViewById(R.id.enter_your_name_text_view);
        TextView enter_age = (TextView) dialogView.findViewById(R.id.age_text_view);
        TextView choose_avatar = (TextView) dialogView.findViewById(R.id.choose_avatar_text_view);
        edit_name = (EditText) dialogView.findViewById(R.id.name);
        edit_age = (EditText) dialogView.findViewById(R.id.age);
        Button cont_btn = (Button) dialogView.findViewById(R.id.continue_btn);
        Button exit_btn = (Button) dialogView.findViewById(R.id.exit_btn);

        main_text_top.setTypeface(ubuntu_typeface);
        enter_name.setTypeface(lato_thin_typeface);
        enter_age.setTypeface(lato_thin_typeface);
        choose_avatar.setTypeface(lato_thin_typeface);
        edit_name.setTypeface(ubuntu_typeface);
        edit_age.setTypeface(ubuntu_typeface);
        cont_btn.setTypeface(ubuntu_typeface);
        exit_btn.setTypeface(ubuntu_typeface);

        builder.setView(dialogView);
        alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();
    }

    //this listener updates user data to overwrite null data
    public void continueButtonListener(View view){
        //insert data and close dialog and don't show that dialog until DATA is RESET
        String name;
        if(!edit_name.getText().toString().equals(""))
            name = edit_name.getText().toString();
        else {
            Toast.makeText(getBaseContext(), "Enter your name please !", Toast.LENGTH_SHORT).show();
            return;
        }

        String age;
        if(!edit_age.getText().toString().equals(""))
            age = edit_age.getText().toString();
        else {
            Toast.makeText(getBaseContext(), "Please provide us your age !", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = db.UpdateData("1", name, age, String.valueOf(profile_pic), "false");  //if data is inserted then set show_dialog to FALSE
        if(isUpdated) {
            Toast.makeText(getBaseContext(), "You are Good to go !", Toast.LENGTH_LONG).show();
        }

        alert.cancel();
        setWelcomeText();
        setAvatar();

        //remove the background gif
        img.setVisibility(View.GONE);
    }

    public void setWelcomeText(){
        Cursor res = db.getData();
        while(res.moveToNext()){
            user_welcome_text.setText(res.getString(1));
        }
    }

    public void setCoinsText(){
        Cursor res = db.getData();
        while(res.moveToNext()){
            coins_text.setText(res.getString(6));
        }
    }

    public void setDiamondsText(){
        Cursor res = db.getData();
        while(res.moveToNext()){
            diamonds_text.setText(res.getString(7));
        }
    }

    public void exitButtonListener(View view){
        finish();
    }

    public void showAvatarDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true);
        View dialogView = getLayoutInflater().inflate(R.layout.avatar_dialog_layout, null);

        TextView select_avatar_text = (TextView) dialogView.findViewById(R.id.change_avatar_text);
        Button save_button = (Button) dialogView.findViewById(R.id.save_button_avatar);
        Button cancel_button = (Button) dialogView.findViewById(R.id.cancel_button_avatar);

        save_button.setTypeface(ubuntu_typeface);
        cancel_button.setTypeface(ubuntu_typeface);
        select_avatar_text.setTypeface(ubuntu_typeface);

        builder.setView(dialogView);
        alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();
    }

    public void selectAvatar(View view){
        if(view.getId() == R.id.avatar1) {
            profile_pic = 101;
        }
        else if(view.getId() == R.id.avatar2) {
            profile_pic = 102;
        }
        else if(view.getId() == R.id.avatar3) {
            profile_pic = 103;
        }
        else if(view.getId() == R.id.avatar4) {
            profile_pic = 104;
        }
    }

    public void setAvatar(){
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

    public void openStore(View view){
        startActivity(new Intent(MainActivity.this, Store.class));
    }

    public void saveButtonListener_Avatar(View view){

        boolean isUpdated = db.UpdateAvatar("1", String.valueOf(profile_pic));  //if data is inserted then set show_dialog to FALSE
        if(isUpdated) {
            Toast.makeText(getBaseContext(), "You are Good to go !", Toast.LENGTH_LONG).show();
        }

        alert.cancel();
        setAvatar();
    }

    public void cancelButtonListener_Avatar(View view){
        alert.cancel();
    }

    //Settings Dialog
    public void showSettingsDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true);
        View dialogView = getLayoutInflater().inflate(R.layout.settings_dialog_layout, null);

        //views & declarations etc. here
        music_vol_text = (TextView) dialogView.findViewById(R.id.music_vol_text);
        sfx_vol_text = (TextView) dialogView.findViewById(R.id.sfx_vol_text);
        settings_main_text = (TextView) dialogView.findViewById(R.id.settings_main_text);
        settings_profile_text = (TextView) dialogView.findViewById(R.id.settings_profile_text);
        enter_name = (TextView) dialogView.findViewById(R.id.enter_your_name_text_view);
        enter_age = (TextView) dialogView.findViewById(R.id.age_text_view);
        choose_avatar = (TextView) dialogView.findViewById(R.id.choose_avatar_text_view);
        edit_name = (EditText) dialogView.findViewById(R.id.name);
        edit_age = (EditText) dialogView.findViewById(R.id.age);
        save_btn = (Button) dialogView.findViewById(R.id.save_button_settings);
        cancel_btn = (Button) dialogView.findViewById(R.id.cancel_button_settings);
        avatar1 = (ImageView) dialogView.findViewById(R.id.avatar1);
        avatar2 = (ImageView) dialogView.findViewById(R.id.avatar2);
        avatar3 = (ImageView) dialogView.findViewById(R.id.avatar3);
        avatar4 = (ImageView) dialogView.findViewById(R.id.avatar4);
        white_line = (View) dialogView.findViewById(R.id.white_line);
        music_seekbar = (SeekBar) dialogView.findViewById(R.id.music_seekbar);
        sfx_seekbar = (SeekBar) dialogView.findViewById(R.id.sfx_seekbar);

        settings_profile_text.setTypeface(ubuntu_typeface);
        settings_main_text.setTypeface(ubuntu_typeface);
        enter_name.setTypeface(lato_thin_typeface);
        enter_age.setTypeface(lato_thin_typeface);
        choose_avatar.setTypeface(lato_thin_typeface);
        edit_name.setTypeface(ubuntu_typeface);
        edit_age.setTypeface(ubuntu_typeface);
        save_btn.setTypeface(ubuntu_typeface);
        cancel_btn.setTypeface(ubuntu_typeface);
        music_vol_text.setTypeface(ubuntu_typeface);
        sfx_vol_text.setTypeface(ubuntu_typeface);

        builder.setView(dialogView);
        alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();

        //seekbar listeners
        MusicSeekBarChangeListener();
        SfxSeekBarChangeListener();
    }

    public void saveButtonListener_Settings(View view){
        String name;
        if(!edit_name.getText().toString().equals(""))
            name = edit_name.getText().toString();
        else {
            Toast.makeText(getBaseContext(), "Enter your name please !", Toast.LENGTH_SHORT).show();
            return;
        }

        String age;
        if(!edit_age.getText().toString().equals(""))
            age = edit_age.getText().toString();
        else {
            Toast.makeText(getBaseContext(), "Please provide us your age !", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = db.UpdateData("1", name, age, String.valueOf(profile_pic), "false");  //if data is inserted then set show_dialog to FALSE
        if(isUpdated) {
            Toast.makeText(getBaseContext(), "You are Good to go !", Toast.LENGTH_LONG).show();
        }

        alert.cancel();
        setWelcomeText();
        setAvatar();
    }

    public void cancelButtonListener_Settings(View view){
        alert.cancel();
    }

    public void showEnterNewDataViews(View view){
        settings_profile_text.setVisibility(View.VISIBLE);
        enter_name.setVisibility(View.VISIBLE);
        enter_age.setVisibility(View.VISIBLE);
        choose_avatar.setVisibility(View.VISIBLE);
        edit_name.setVisibility(View.VISIBLE);
        edit_age.setVisibility(View.VISIBLE);
        save_btn.setVisibility(View.VISIBLE);
        cancel_btn.setVisibility(View.VISIBLE);
        avatar1.setVisibility(View.VISIBLE);
        avatar2.setVisibility(View.VISIBLE);
        avatar3.setVisibility(View.VISIBLE);
        avatar4.setVisibility(View.VISIBLE);
        white_line.setVisibility(View.VISIBLE);
    }

    public void MusicSeekBarChangeListener(){
        music_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //save data to database
            }
        });
    }

    public void SfxSeekBarChangeListener(){
        sfx_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //save data to database
            }
        });
    }

}
