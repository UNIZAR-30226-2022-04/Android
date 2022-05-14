package es.unizar.eina.frankenstory.quick;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
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

import java.io.Serializable;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;
import es.unizar.eina.frankenstory.general.FriendsActivity;
import es.unizar.eina.frankenstory.general.LibraryActivity;
import es.unizar.eina.frankenstory.general.MainMenuActivity;
import es.unizar.eina.frankenstory.general.SettingsActivity;

public class QuickActivity extends AppCompatActivity {

    private TextView mUsername;
    private TextView mStars;
    private TextView mCoins;
    private Button mNotifications;
    private ImageView mIconUser;

    private EditText mCode;
    private Button mCreateRoom;
    private Button mJoinRoom;
    private Button mRandomGame;

    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_game);

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // GET VIEWS AND UPDATE DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mNotifications = (Button) findViewById(R.id.notifications);
        mIconUser = (ImageView) findViewById(R.id.iconUser);

        mCode = (EditText) findViewById(R.id.code);
        mCreateRoom = (Button) findViewById(R.id.quick_game);
        mJoinRoom = (Button) findViewById(R.id.join_room);
        mRandomGame = (Button) findViewById(R.id.random_game);

        updateData();

        // BUTTONS FROM TOP AND BOTTOM
        setNavegavilidad();

        // BACKGROUND ANIMATION
        RelativeLayout relativeLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    // UPDATE DATA
    public void updateData(){
        mUsername.setText(((MyApplication) this.getApplication()).getUsername());
        mStars.setText(((MyApplication) this.getApplication()).getStars());
        mCoins.setText(((MyApplication) this.getApplication()).getCoins());
        chooseIconUser(mIconUser, ((MyApplication) this.getApplication()).getIconUser());
        if (Integer.parseInt(((MyApplication) this.getApplication()).getNotifications())>0){
            mNotifications.setText(((MyApplication) this.getApplication()).getNotifications());
        } else mNotifications.setVisibility(View.INVISIBLE);
    }

    public void setNavegavilidad(){
        // BUTTON TO SettingsActivity
        ImageButton buttonSettings = (ImageButton)findViewById(R.id.configbutton);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(QuickActivity.this, SettingsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO FriendsActivity
        Button buttonFriends = (Button)findViewById(R.id.friends);
        buttonFriends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(QuickActivity.this, FriendsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO MainMenuActivity
        Button buttonHome = (Button)findViewById(R.id.home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(QuickActivity.this, MainMenuActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO LibraryActivity
        Button Buttomlibrary = (Button) findViewById(R.id.your_stories);
        Buttomlibrary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(QuickActivity.this, LibraryActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO CREATE ROOM
        mCreateRoom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //GO TO CREATE ROOM ACTIVITY
                Intent i = new Intent(QuickActivity.this, QuickCreateActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO JOIN ROOM
        mJoinRoom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // IF IT HAS NO HASTAG PUT IT
                code = mCode.getText().toString();
                if (code.charAt(0)!='#') code = "#" + code;
                // CALL ASYNC TASK JOIN ROOM
                AsyncTaskJoinRoom myTask = new AsyncTaskJoinRoom(QuickActivity.this);
                myTask.execute(code);
            }
        });

        // BUTTON TO RANDOM ROOM
        mRandomGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // CALL ASYNC TASK JOIN RANDOM ROOM
                AsyncTaskJoinRandomRoom myTask = new AsyncTaskJoinRandomRoom(QuickActivity.this);
                myTask.execute();
            }
        });

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

    // ASYNC TASK JOIN ROOM ADAPTER
    public void setupAdapter(AsyncTaskJoinRoom.Result resultado)
    {
        if ((resultado.result!=null && resultado.result.equals("success"))){

            Intent i = new Intent(QuickActivity.this, QuickRoomActivity.class);
            i.putExtra("code",code);
            startActivity(i);
        } else if ((resultado.reason!=null && resultado.reason.equals("game_started"))) {
            // SI LA PARTIDA ESTA EMPEZADA
            Toast.makeText(QuickActivity.this, resultado.reason, Toast.LENGTH_LONG).show();
            Intent i = new Intent(QuickActivity.this, QuickRoomActivity.class);
            i.putExtra("code",code);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(QuickActivity.this, resultado.reason, Toast.LENGTH_LONG).show();
        }
    }

    // ASYNC TASK JOIN RANDOM ROOM ADAPTER
    public void setupAdapter(AsyncTaskJoinRandomRoom.Result resultado)
    {
        if (resultado.result!=null && resultado.result.equals("success")){
            String id = resultado.id;
            Intent i = new Intent(QuickActivity.this, QuickRoomActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            i.putExtra("code", id);
            startActivity(i);
        } else {
            Toast.makeText(QuickActivity.this, resultado.reason, Toast.LENGTH_LONG).show();
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