package es.unizar.eina.frankenstory.general;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;

import es.unizar.eina.frankenstory.R;

public class FriendsActivity extends AppCompatActivity {

    private TextView mUsername;
    private TextView mStars;
    private TextView mCoins;
    private Button mNotifications;
    private ListView mListFriends;
    private ListView mListNotifications;
    private EditText mSearchFriend;
    private ImageButton mSearchFriendButton;
    private LinearLayout mrowsearchedfriend;
    private TextView mSeachedFound;
    private ImageButton mAddFriend;
    String username;
    String password;
    String stars;
    String coins;
    String notifications;
    String searchedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_friends);

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // BACKGROUND ANIMATION
        ConstraintLayout constraintLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        // GET PARAMETERS
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        stars = intent.getStringExtra("stars");
        coins = intent.getStringExtra("coins");
        notifications = intent.getStringExtra("notifications");

        // GET VIEWS AND SET DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mNotifications = (Button) findViewById(R.id.notifications);
        mListFriends = (ListView) findViewById(R.id.your_friends);
        mListNotifications = (ListView) findViewById(R.id.petitions);
        mSearchFriend = (EditText) findViewById(R.id.usernameSearch);
        mSearchFriendButton = (ImageButton) findViewById(R.id.searchfriend);
        mSeachedFound = (TextView) findViewById(R.id.friendNameSearched);
        mAddFriend = (ImageButton) findViewById(R.id.addfriend);
        mUsername.setText(username);
        mStars.setText(stars);
        mCoins.setText(coins);
        if (Integer.parseInt(notifications)>0){
            mNotifications.setText(notifications);
        } else mNotifications.setVisibility(View.INVISIBLE);

        // BUTTON SEARCH
        mrowsearchedfriend = (LinearLayout) findViewById(R.id.rowsearchedfriend);
        mrowsearchedfriend.setVisibility(View.GONE);
        mSearchFriendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchedName = mSearchFriend.getText().toString();
                if (!searchedName.equals(username)){ // IF its not me
                    AsyncTaskSearchFriends myTaskSearch = new AsyncTaskSearchFriends(FriendsActivity.this);
                    myTaskSearch.execute(username, password, searchedName);
                    mSearchFriendButton.setClickable(false);
                }
            }
        });

        // ADD FRIEND SEARCHED
        mAddFriend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AsyncTaskManageFrienship myTaskSearch = new AsyncTaskManageFrienship(FriendsActivity.this);
                myTaskSearch.execute(username, password, searchedName, "add");
                mrowsearchedfriend.setVisibility(View.GONE);
            }
        });

        // BUTTONS FROM TOP AND BOTTOM
        setNavegavilidad();

        // CALL ASYNC TASK FRIENDS
        refreshPage();
    }

    public void refreshPage(){
        AsyncTaskFriends myTask = new AsyncTaskFriends(this);
        myTask.execute(username, password);
    }

    public void setNavegavilidad(){
        // BUTTON TO SettingsActivity
        ImageButton buttonSettings = (ImageButton)findViewById(R.id.configbutton);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(FriendsActivity.this, SettingsActivity.class);
                i.putExtra("username",username);
                i.putExtra("password",password);
                i.putExtra("stars", stars);
                i.putExtra("coins", coins);
                i.putExtra("notifications", notifications);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO MainMenuActivity
        Button buttonHome = (Button)findViewById(R.id.home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(FriendsActivity.this, MainMenuActivity.class);
                i.putExtra("username",username);
                i.putExtra("password",password);
                i.putExtra("stars", stars);
                i.putExtra("coins", coins);
                i.putExtra("notifications", notifications);
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
        if (resultado.result.equals("success")){
            // NOTIFICATIONS NUMBER
            if (resultado.notifications.size() > 0){
                notifications = Integer.valueOf(resultado.notifications.size()).toString();
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
    }

    private void fillDataPetitions(AsyncTaskFriends.Result resultado) {
        /*
        // CREANDO CURSOR CON LOS RESULTADOS
        String[] columns = new String[] { "_id", "friendName"};
        MatrixCursor matrixCursor= new MatrixCursor(columns);

        Integer i=0;
        for(AsyncTaskFriends.Friend p : resultado.notifications){
            matrixCursor.addRow(new Object[]{i,p.username});
            i++;
        }

        // PARSEANDO CURSOR A LISTVIEW
        String[] from = new String[] { "friendName"};
        int[] to = new int[] { R.id.friendName};

        SimpleCursorAdapter photos =
                new SimpleCursorAdapter(this, R.layout.row_friend_petitions, matrixCursor, from, to);
        mListNotifications.setAdapter(photos);*/
        // instantiate the custom list adapter
        ListPetitionsAdapter adapter = new ListPetitionsAdapter(this, resultado.notifications);

        // get the ListView and attach the adapter
        mListNotifications.setAdapter(adapter);
    }

    // ASYNC TASK FRIENDS ADAPTER
    public void setupAdapterSearch(AsyncTaskSearchFriends.Result resultado)
    {
        mSearchFriendButton.setClickable(true);
        if (resultado.result.equals("success")){
            // NOTIFICATIONS NUMBER
            if (resultado.isFound && !resultado.isFriend){
                mSeachedFound.setText(searchedName);
                mrowsearchedfriend.setVisibility(View.VISIBLE);
            }

        }
    }
}
