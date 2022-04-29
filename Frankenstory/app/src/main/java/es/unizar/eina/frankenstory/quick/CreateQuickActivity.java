package es.unizar.eina.frankenstory.quick;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;
import es.unizar.eina.frankenstory.story.StoryFirstWriteActivity;

public class CreateQuickActivity extends AppCompatActivity{

    private TextView mUsername;
    private TextView mStars;
    private TextView mCoins;
    private TextView mTitle;
    private ListView mList;
    private ImageView mIconUser;
    private TextView mTime;
    public static Handler handlerTofinish;
    private Button buttonPublic;
    private Button butttonPrivate;

    int time;
    boolean isPrivate_game;
    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_game_create);

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // GET VIEWS
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mTitle = (TextView) findViewById(R.id.story_name);
        mList = (ListView) findViewById(R.id.statistics);
        mIconUser = (ImageView) findViewById(R.id.iconUser);
        buttonPublic = (Button)findViewById(R.id.buttonPublic);
        butttonPrivate = (Button)findViewById(R.id.buttonPrivate);
        mTime = (TextView) findViewById(R.id.time);

        mUsername.setText(((MyApplication) this.getApplication()).getUsername());
        mStars.setText(((MyApplication) this.getApplication()).getStars());
        mCoins.setText(((MyApplication) this.getApplication()).getCoins());
        chooseIconUser(mIconUser, ((MyApplication) this.getApplication()).getIconUser());

        // BUTTONS FROM TOP AND BOTTOM
        setNavegavilidad();

        // BACKGROUND ANIMATION
        ConstraintLayout constraintLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        // INITIALIZE VARIABLES
        time = 30;
        mode = "random";
        isPrivate_game = false;
        buttonPublic.setBackgroundColor(getResources().getColor(R.color.verde_publico_seleccionado));
        butttonPrivate.setBackgroundColor(getResources().getColor(R.color.verde_letras));
        setContenido();

        //BUTTON PLUS TIME
        Button plusTime = (Button)findViewById(R.id.plus_time);
        plusTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (time < 300) time++;
                setContenido();
            }
        });

        //BUTTON MINUS TIME
        Button minusTime = (Button)findViewById(R.id.minus_time);
        minusTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (time > 5) time--;
                setContenido();
            }
        });

        //BUTTON SET MODE RANDOM
        LottieAnimationView random = (LottieAnimationView)findViewById(R.id.random_mode);
        random.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mode = "random";
            }
        });

        //BUTTON SET MODE TWITTER
        LottieAnimationView twitter = (LottieAnimationView)findViewById(R.id.twitter_mode);
        twitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mode = "twitter";
            }
        });

        //SWITCH PRIVATE GAME
        buttonPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPrivate_game = false;
                buttonPublic.setBackgroundColor(getResources().getColor(R.color.verde_publico_seleccionado));
                butttonPrivate.setBackgroundColor(getResources().getColor(R.color.verde_letras));
            }
        });

        butttonPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPrivate_game = true;
                butttonPrivate.setBackgroundColor(getResources().getColor(R.color.verde_publico_seleccionado));
                buttonPublic.setBackgroundColor(getResources().getColor(R.color.verde_letras));
            }
        });

        // FINISHER
        handlerTofinish = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what) {
                    case 0:
                        finish();
                        break;
                }
            }
        };
    }

    public void setNavegavilidad(){
        // BUTTON TO SettingsActivity
        ImageButton buttonSettings = (ImageButton)findViewById(R.id.configbutton);
        buttonSettings.setVisibility(View.GONE);

        // BUTTON TO QuickGameRoom
        Button createRoom = (Button)findViewById(R.id.create_room);
        createRoom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // CALL ASYNC TASK CREATE ROOM
                AsyncTaskCreateRoom myTask = new AsyncTaskCreateRoom(CreateQuickActivity.this);
                myTask.execute(String.valueOf(time), String.valueOf(isPrivate_game), mode);

            }
        });
    }

    // ASYNC TASK ADAPTER CREATE ROOM
    public void setupAdapter(AsyncTaskCreateRoom.Result resultado)
    {
        if (resultado.result==null || resultado.result.equals("error")) {
            Toast.makeText(getApplicationContext(),"ERROR CREANDO LA SALA",Toast.LENGTH_SHORT).show();
        } else {

            Intent i = new Intent(CreateQuickActivity.this, QuickGameRoom.class);
            i.putExtra("code", String.valueOf(resultado.id));
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);

            CreateQuickActivity.handlerTofinish.sendEmptyMessage(0);
            finish();

        }
    }

    public void setContenido() {
        mTime.setText(String.valueOf(time)+" seg");
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
