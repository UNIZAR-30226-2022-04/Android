package es.unizar.eina.frankenstory.general;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;

public class StoryActivity extends AppCompatActivity {

    private TextView mUsername;
    private TextView mStars;
    private TextView mCoins;
    private Button mNotifications;
    private ListView mList;
    private ImageView mIconUser;
    private ListView mListMyGames;
    private ListView mListFriendsGames;
    private ListView mPublicGames;
    private ListView mVotingGames;
    private ImageButton mJoinGame;
    private ImageButton mVoteGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // GET DATA FROM MAINACTIVITY
        Intent intent = getIntent();

        // GET VIEWS AND UPDATE DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mNotifications = (Button) findViewById(R.id.notifications);
        mNotifications.setVisibility(View.INVISIBLE);
        mList = (ListView) findViewById(R.id.statistics);
        mIconUser = (ImageView) findViewById(R.id.iconUser);
        mListMyGames = (ListView) findViewById(R.id.my_stories);
        mListFriendsGames = (ListView) findViewById(R.id.friends_games);
        mPublicGames = (ListView) findViewById(R.id.public_games);
        mVotingGames = (ListView) findViewById(R.id.VoteGames);
        mJoinGame = (ImageButton) findViewById(R.id.joinGame);
        mVoteGame = (ImageButton) findViewById(R.id.vote);
        updateData();

        // BUTTONS FROM TOP AND BOTTOM
        setNavegavilidad();

        // BACKGROUND ANIMATION
        ConstraintLayout constraintLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        // CALL ASYNC TASK
        //AsyncTaskStory myTask = new AsyncTaskStory(this);
        //myTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // CALL ASYNC TASK
        //AsyncTaskStory myTask = new AsyncTaskStory(this);
        //myTask.execute();
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
                Intent i = new Intent(StoryActivity.this, SettingsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO FriendsActivity
        Button buttonFriends = (Button)findViewById(R.id.friends);
        buttonFriends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(StoryActivity.this, FriendsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO MainMenuActivity
        Button buttonHome = (Button)findViewById(R.id.home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(StoryActivity.this, MainMenuActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO CreateStoryActivity
        Button createStory = (Button)findViewById(R.id.create_story);
        createStory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(StoryActivity.this, CreateStoryActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
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

    // ASYNC TASK STORIES ADAPTER
    public void setupAdapter(AsyncTaskStories.Result resultado)
    {
        if (resultado.result!=null && resultado.result.equals("success")){
            fillDataMyGames(resultado);
            fillDataFriendGames(resultado);
            fillDataPublicGames(resultado);
            fillDataVoteGames(resultado);
        }
    }

   private void fillDataMyGames(AsyncTaskStories.Result resultado) {
        // instantiate the custom list adapter
        ListStoriesAdapter adapter = new ListStoriesAdapter(this, resultado.stories);

        // get the ListView and attach the adapter
        mListMyGames.setAdapter(adapter);
    }

    private void fillDataFriendGames(AsyncTaskStories.Result resultado) {
        // instantiate the custom list adapter
        ListStoriesAdapter adapter = new ListStoriesAdapter(this, resultado.stories);

        // get the ListView and attach the adapter
        mListFriendsGames.setAdapter(adapter);
    }

    private void fillDataPublicGames(AsyncTaskStories.Result resultado) {
        // instantiate the custom list adapter
        ListStoriesAdapter adapter = new ListStoriesAdapter(this, resultado.stories);

        // get the ListView and attach the adapter
        mPublicGames.setAdapter(adapter);
    }

    private void fillDataVoteGames(AsyncTaskStories.Result resultado) {
        // instantiate the custom list adapter
        ListVoteStoriesAdapter adapter = new ListVoteStoriesAdapter(this, resultado.stories);

        // get the ListView and attach the adapter
        mVotingGames.setAdapter(adapter);
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