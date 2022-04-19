package es.unizar.eina.frankenstory.general;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;

public class FriendsActivity extends AppCompatActivity {

    private TextView mUsername;
    private TextView mStars;
    private TextView mCoins;
    private Button mNotifications;
    private ImageView mIconUser;
    private ListView mListFriends;
    private ListView mListNotifications;
    private EditText mSearchFriend;
    private ImageButton mSearchFriendButton;
    private LinearLayout mrowsearchedfriend;
    private TextView mSeachedFound;
    private ImageButton mAddFriend;
    private TextView mNoFriends;
    private TextView mNoPetitions;
    String searchedName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_friends);

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // BACKGROUND ANIMATION
        RelativeLayout relativeLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        // GET VIEWS AND SET DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mNotifications = (Button) findViewById(R.id.notifications);
        mIconUser = (ImageView) findViewById(R.id.iconUser);
        mListFriends = (ListView) findViewById(R.id.your_friends);
        mListNotifications = (ListView) findViewById(R.id.petitions);
        mSearchFriend = (EditText) findViewById(R.id.usernameSearch);
        mSearchFriendButton = (ImageButton) findViewById(R.id.searchfriend);
        mSeachedFound = (TextView) findViewById(R.id.friendNameSearched);
        mAddFriend = (ImageButton) findViewById(R.id.addfriend);
        mNoFriends = (TextView) findViewById(R.id.noFriends);
        mNoPetitions = (TextView) findViewById(R.id.noPetitions);
        updateData();

        // BUTTON SEARCH
        mrowsearchedfriend = (LinearLayout) findViewById(R.id.rowsearchedfriend);
        mrowsearchedfriend.setVisibility(View.GONE);
        mSearchFriendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchedName = mSearchFriend.getText().toString();
                String username = ((MyApplication) FriendsActivity.this.getApplication()).getUsername();
                if (!searchedName.equals(username) && !searchedName.equals("")){ // IF its not me
                    AsyncTaskSearchFriends myTaskSearch = new AsyncTaskSearchFriends(FriendsActivity.this);
                    myTaskSearch.execute(searchedName);
                    mSearchFriendButton.setClickable(false);
                }
            }
        });

        // ADD FRIEND SEARCHED
        mAddFriend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AsyncTaskManageFrienship myTaskSearch = new AsyncTaskManageFrienship(FriendsActivity.this);
                myTaskSearch.execute(searchedName, "add");
                mrowsearchedfriend.setVisibility(View.GONE);
            }
        });

        // BUTTONS FROM TOP AND BOTTOM
        setNavegavilidad();

        // CALL ASYNC TASK FRIENDS
        refreshPage();
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

    public void refreshPage(){
        AsyncTaskFriends myTask = new AsyncTaskFriends(this);
        myTask.execute();
    }

    public void setNavegavilidad(){

        // BUTTON TO MainMenuActivity
        Button buttonHome = (Button)findViewById(R.id.home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(FriendsActivity.this, MainMenuActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO SettingsActivity
        ImageButton buttonSettings = (ImageButton)findViewById(R.id.configbutton);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(FriendsActivity.this, SettingsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO LibraryActivity
        Button Buttomlibrary = (Button) findViewById(R.id.your_stories);
        Buttomlibrary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(FriendsActivity.this, LibraryActivity.class);
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

    // ASYNC TASK FRIENDS ADAPTER
    public void setupAdapter(AsyncTaskFriends.Result resultado)
    {
        if (resultado.result!=null && resultado.result.equals("success")){
            // NOTIFICATIONS NUMBER
            if (resultado.notifications.size() > 0){
                String notifications = Integer.valueOf(resultado.notifications.size()).toString();
                ((MyApplication) this.getApplication()).setNotifications(notifications);
                mNotifications.setText(notifications);
                mNotifications.setVisibility(View.VISIBLE);
            } else mNotifications.setVisibility(View.INVISIBLE);
            // FRIENDS
            fillDataFriends(resultado);
            // PETITIONS
            fillDataPetitions(resultado);

        }
    }

    private void fillDataFriends(AsyncTaskFriends.Result resultado) {
        // instantiate the custom list adapter
        ListFriendAdapter adapter = new ListFriendAdapter(this, resultado.friends);

        // get the ListView and attach the adapter
        mListFriends.setAdapter(adapter);

        // Message no friends
        if (resultado.friends.isEmpty()) mNoFriends.setVisibility(View.VISIBLE);
        else mNoFriends.setVisibility(View.GONE);
    }

    private void fillDataPetitions(AsyncTaskFriends.Result resultado) {
        // instantiate the custom list adapter
        ListPetitionsAdapter adapter = new ListPetitionsAdapter(this, resultado.notifications);

        // get the ListView and attach the adapter
        mListNotifications.setAdapter(adapter);

        // Message no petitions
        if (resultado.notifications.isEmpty()) mNoPetitions.setVisibility(View.VISIBLE);
        else mNoPetitions.setVisibility(View.GONE);
    }

    // ASYNC TASK FRIENDS ADAPTER
    public void setupAdapterSearch(AsyncTaskSearchFriends.Result resultado)
    {
        mSearchFriendButton.setClickable(true);
        if (resultado.result!=null && resultado.result.equals("success")){
            // NOTIFICATIONS NUMBER
            if (resultado.isFound && !resultado.isFriend){
                mSeachedFound.setText(searchedName);
                mrowsearchedfriend.setVisibility(View.VISIBLE);
            }

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
