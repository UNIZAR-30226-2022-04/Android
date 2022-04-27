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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.List;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;
import es.unizar.eina.frankenstory.general.AsyncTaskGetStories;
import es.unizar.eina.frankenstory.general.FriendsActivity;
import es.unizar.eina.frankenstory.general.LibraryActivity;
import es.unizar.eina.frankenstory.general.MainMenuActivity;
import es.unizar.eina.frankenstory.general.SettingsActivity;

public class QuickGameRoom extends AppCompatActivity {

    private TextView mUsername;
    private TextView mStars;
    private TextView mCoins;
    private ImageView mIconUser;

    private TextView mParticipants;
    private TextView mCode;
    private Button mStartGame;
    private TextView mTwitter_mode;
    private TextView mRandom_mode;
    private ListView mListParticipants;

    private String code;
    private int gameState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_game_room);

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //GET PARAMETERS
        Intent i = this.getIntent();
        code = i.getExtras().getString("code");

        // GET VIEWS AND UPDATE DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);

        mParticipants = (TextView) findViewById(R.id.number_participants);
        mIconUser = (ImageView) findViewById(R.id.iconUser);
        mCode = (TextView) findViewById(R.id.code);
        mStartGame = (Button) findViewById(R.id.start_game);
        mTwitter_mode = (TextView) findViewById(R.id.twitter_mode);
        mRandom_mode = (TextView) findViewById(R.id.random_mode);
        mListParticipants = (ListView) findViewById(R.id.participants);

        mCode.setText("CODIGO DE SALA: " + code);

        updateData();

        // BUTTONS FROM TOP AND BOTTOM
        setNavegavilidad();

        // BACKGROUND ANIMATION
        RelativeLayout relativeLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        // CALL ASYNC TASK GET ROOM
        AsyncTaskGetRoom myTask = new AsyncTaskGetRoom(this);
        myTask.execute(code);

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
    }

    public void setNavegavilidad(){
        // BUTTON TO SettingsActivity
        ImageButton buttonSettings = (ImageButton)findViewById(R.id.configbutton);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(QuickGameRoom.this, SettingsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO START GAME
        /*
        mCreateRoom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // CALL ASYNC TASK PLAY QUICK GAME
                //AsyncTaskStories myTask = new AsyncTaskStories(this);
                //myTask.execute(code);
            }
        });
        */

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

    // ASYNC TASK GET ROOM ADAPTER
    public void setupAdapter(AsyncTaskGetRoom.Result resultado)
    {
        if (resultado.result!=null && resultado.result.equals("success")){

            mParticipants.setText(resultado.participants.size() + "/10 Participantes");
            gameState = resultado.hasStarted;

            if (resultado.mode.equals("random")) {
                mTwitter_mode.setVisibility(View.GONE);
                mRandom_mode.setVisibility(View.VISIBLE);

            } else if (resultado.mode.equals("twitter")){
                mTwitter_mode.setVisibility(View.VISIBLE);
                mRandom_mode.setVisibility(View.GONE);
            }

            fillDataParticipants(resultado.participants);

        }
    }

   private void fillDataParticipants(List<AsyncTaskGetRoom.Participants> participants) {
        // instantiate the custom list adapter
        ListParticipantsAdapter adapter = new ListParticipantsAdapter(this, participants);

        // get the ListView and attach the adapter
        mListParticipants.setAdapter(adapter);

    }


    // ASYNC TASK PLAY QUICK GAME ADAPTER
    /*
    public void setupAdapter(AsyncTaskJoinRandomRoom.Result resultado)
    {
        if (resultado.result!=null && resultado.result.equals("success")){

            String id = resultado.result id;

            Intent i = new Intent(QuickActivity.this, QuickRoom.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            i.putExtra("id", id);
            startActivity(i);
        }
    }
    */

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