package es.unizar.eina.frankenstory.quick;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.concurrent.TimeUnit;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;
import es.unizar.eina.frankenstory.story.AsyncTaskCreateStory;
import es.unizar.eina.frankenstory.story.CreateStoryActivity;
import es.unizar.eina.frankenstory.story.StoryActivity;

public class QuickFirstWriteActivity extends AppCompatActivity{


    private TextView mUsername;
    private TextView mCoins;
    private ImageView mIconUser;
    private EditText content;
    private TextView mTheme;
    private TextView mFirstWord;
    private TextView mSecondWord;
    private TextView mThirdWord;
    private Button send_text;
    private TextView mTime;

    private String code;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // GET PARAMETERS
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");

        if (mode.equals("twitter"))
            setContentView(R.layout.activity_quick_game_twitter_first);
        else {
            setContentView(R.layout.activity_quick_game_random_first);
        }

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // BACKGROUND ANIMATION
        RelativeLayout relativeLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //GET PARAMETERS
        Intent i = this.getIntent();
        code = i.getExtras().getString("code");

        // GET VIEWS AND SET DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mIconUser = (ImageView) findViewById(R.id.iconUser);
        content = (EditText) findViewById(R.id.story_content);
        send_text = (Button)findViewById(R.id.sendText);

        if (mode.equals("twitter")) {
            mTheme = (TextView) findViewById(R.id.twitter_trend);
        }else {
            mFirstWord = (TextView) findViewById(R.id.first_Word);
            mSecondWord = (TextView) findViewById(R.id.second_word);
            mThirdWord = (TextView) findViewById(R.id.third_word);
        }

        mTime = (TextView) findViewById(R.id.time);
        mUsername.setText(((MyApplication) this.getApplication()).getUsername());
        mCoins.setText(((MyApplication) this.getApplication()).getCoins());

        chooseIconUser(mIconUser, ((MyApplication) this.getApplication()).getIconUser());

        //SEND TEXT
        setNavegavilidad();

        // CALL ASYNC TASK PLAY QUICK GAME
        AsyncTaskPlayQuickGame myTask = new AsyncTaskPlayQuickGame(this);
        myTask.execute(code);


    }

    public void setNavegavilidad(){

        // BUTTON SEND TEXT
        send_text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String first_paragraph = String.valueOf(content.getText());
                // CALL ASYNC TASK ADD PARAGRAPH
                // AsyncTaskAddParagraph myTask = new AsyncTaskAddParagraph(this);
                // myTask.execute(code,first_paragraph,1,false,punetas);
            }
        });

    }

    // ASYNC TASK ADAPTER ADD PARAGRAPH
    /*public void setupAdapter(AsyncTaskCreateStory.Result resultado)
    {
        if (resultado.result==null || resultado.result.equals("error")) {
            Toast.makeText(getApplicationContext(),"ERROR CREANDO RELATO",Toast.LENGTH_SHORT).show();
        }
    }*/

    // ASYNC TASK ADAPTER PLAY QUICK GAME
    public void setupAdapter(AsyncTaskPlayQuickGame.Result resultado)
    {
        if (resultado.result!=null && !resultado.result.equals("error")) {

            if (mode.equals("twitter")) {
                mTheme.setText(resultado.topic);
            }else {
                mFirstWord.setText(resultado.randomWords.get(0));
                mSecondWord.setText(resultado.randomWords.get(1));
                mThirdWord.setText(resultado.randomWords.get(2));
            }
            new CountDownTimer(resultado.s * 1000L,1000){

                public void onTick (long millisUntilFinished) {

                     mTime.setText(""+String.format("%d min %d sec",
                    TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                }

                public void onFinish() {

                    //GO TO NOT FIRST WRITE

                }

            }.start();

        }
    }

    // Para ocultar Navigation bar y lo de arriba.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //This is used to hide/show 'Status Bar' & 'System Bar'. Swip bar to get it as visible.
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    // SET ICON USER
    @SuppressLint("ResourceType")
    public void chooseIconUser(ImageView imagen, String picture){
        if (picture.equals("0")) imagen.setImageResource(R.raw.icon0);
        else if (picture.equals("1")) imagen.setImageResource(R.raw.icon1);
        else if (picture.equals("2")) imagen.setImageResource(R.raw.icon2);
        else if (picture.equals("3")) imagen.setImageResource(R.raw.icon3);
        else if (picture.equals("4")) imagen.setImageResource(R.raw.icon4);
        else if (picture.equals("5")) imagen.setImageResource(R.raw.icon5);
        else if (picture.equals("6")) imagen.setImageResource(R.raw.icon6);
        else if (picture.equals("7")) imagen.setImageResource(R.raw.icon7);
        else if (picture.equals("8")) imagen.setImageResource(R.raw.icon8);
        else if (picture.equals("9")) imagen.setImageResource(R.raw.icon9);
        imagen.setVisibility(View.VISIBLE);
    }

}
