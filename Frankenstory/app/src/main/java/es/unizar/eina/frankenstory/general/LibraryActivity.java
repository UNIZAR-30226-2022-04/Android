package es.unizar.eina.frankenstory.general;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class LibraryActivity extends AppCompatActivity {

    private TextView mUsername;
    private TextView mStars;
    private TextView mCoins;
    private Button mNotifications;
    private ListView mList;
    private ImageView mIconUser;
    private ListView mListQuickGames;
    private ListView mListStoryGames;
    private ImageButton mSeeQuickGame;
    private ImageButton mSeeStoryGame;
    private TextView mNoQuickGames;
    private TextView mNoStoryGames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_library);

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // GET VIEWS AND UPDATE DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mNotifications = (Button) findViewById(R.id.notifications);
        mNotifications.setVisibility(View.INVISIBLE);
        mList = (ListView) findViewById(R.id.statistics);
        mIconUser = (ImageView) findViewById(R.id.iconUser);

        mListQuickGames = (ListView) findViewById(R.id.quick_games);
        mListStoryGames = (ListView) findViewById(R.id.story_games);
        mSeeQuickGame = (ImageButton) findViewById(R.id.read_quick);
        mSeeStoryGame = (ImageButton) findViewById(R.id.read_story);
        mNoQuickGames = (TextView) findViewById(R.id.noQuickGames);
        mNoStoryGames = (TextView) findViewById(R.id.noStoryGames);

        updateData();

        // BUTTONS FROM TOP AND BOTTOM
        setNavegavilidad();

        // BACKGROUND ANIMATION
        RelativeLayout relativeLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        // CALL ASYNC TASK
        AsyncTaskGetStories myTask = new AsyncTaskGetStories(this);
        myTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // CALL ASYNC TASK
        AsyncTaskGetStories myTask = new AsyncTaskGetStories(this);
        myTask.execute();

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
                Intent i = new Intent(LibraryActivity.this, SettingsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO FriendsActivity
        Button buttonFriends = (Button)findViewById(R.id.friends);
        buttonFriends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(LibraryActivity.this, FriendsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO MainMenuActivity
        Button buttonHome = (Button)findViewById(R.id.home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(LibraryActivity.this, MainMenuActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO READ QUICK GAME

        /*mSeeQuickGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(LibraryActivity.this, CreateStoryActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });*/

        // BUTTON TO READ STORY GAME

        /*mSeeStoryGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(LibraryActivity.this, CreateStoryActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });*/

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

    // ASYNC TASK GET STORIES ADAPTER
    public void setupAdapter(AsyncTaskGetStories.Result resultado)
    {
        if (resultado.result!=null && resultado.result.equals("success")){

            List<AsyncTaskGetStories.Story> quickStories = null;
            List<AsyncTaskGetStories.Story> storyStories = null;

            for (int i = 0; i < resultado.stories.size(); i++ ) {
                AsyncTaskGetStories.Story aux = resultado.stories.get(i);
                if (aux.type == "story") { storyStories.add(aux);
                }else { quickStories.add(aux); }
            }

            fillDataQuickGames(quickStories);
            fillDataStoryGames(storyStories);

        }
    }

   private void fillDataQuickGames(List<AsyncTaskGetStories.Story> stories) {
        // instantiate the custom list adapter
        ListLibraryGamesAdapter adapter = new ListLibraryGamesAdapter(this, stories);

        // get the ListView and attach the adapter
        mListQuickGames.setAdapter(adapter);

        // Message no my games
        if (stories.isEmpty()) mNoQuickGames.setVisibility(View.VISIBLE);
        else mNoQuickGames.setVisibility(View.GONE);
    }

    private void fillDataStoryGames(List<AsyncTaskGetStories.Story> stories) {
        // instantiate the custom list adapter
        ListLibraryGamesAdapter adapter = new ListLibraryGamesAdapter(this, stories);

        // get the ListView and attach the adapter
        mListStoryGames.setAdapter(adapter);

        // Message no my games
        if (stories.isEmpty()) mNoStoryGames.setVisibility(View.VISIBLE);
        else mNoStoryGames.setVisibility(View.GONE);
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