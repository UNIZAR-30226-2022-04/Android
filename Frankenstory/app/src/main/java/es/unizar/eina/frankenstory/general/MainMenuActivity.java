package es.unizar.eina.frankenstory.general;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;

import es.unizar.eina.frankenstory.R;

public class MainMenuActivity extends AppCompatActivity {

    private TextView mUsername;
    private TextView mStars;
    private TextView mCoins;
    private Button mNotifications;
    private ListView mList;
    String username;
    String password;
    String stars;
    String coins;
    String notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_main_menu);

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // GET USERNAME AND PASSWORD FROM LOGINACTIVITY
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        // GET VIEWS
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mNotifications = (Button) findViewById(R.id.notifications);
        mNotifications.setVisibility(View.INVISIBLE);
        mList = (ListView) findViewById(R.id.statistics);

        // BUTTONS FROM TOP AND BOTTOM
        setNavegavilidad();

        // BACKGROUND ANIMATION
        ConstraintLayout constraintLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        // CALL ASYNC TASK
        AsyncTaskMainMenu myTask = new AsyncTaskMainMenu(this);
        myTask.execute(username, password);
    }

    public void setNavegavilidad(){
        // BUTTON TO SettingsActivity
        ImageButton buttonSettings = (ImageButton)findViewById(R.id.configbutton);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, SettingsActivity.class);
                i.putExtra("username",username);
                i.putExtra("password",password);
                i.putExtra("stars", stars);
                i.putExtra("coins", coins);
                i.putExtra("notifications", notifications);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO FriendsActivity
        Button buttonFriends = (Button)findViewById(R.id.friends);
        buttonFriends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, FriendsActivity.class);
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

    // ASYNC TASK ADAPTER
    public void setupAdapter(AsyncTaskMainMenu.Result resultado)
    {
        // Saved to send to other activities
        stars = resultado.stars.toString();
        coins = resultado.coins.toString();
        notifications = resultado.notifications.toString();
        if (resultado.result != null && resultado.result.equals("success")){
            // TOP INFORMATION
            mUsername.setText(username);
            mCoins.setText(resultado.coins.toString());
            mStars.setText(resultado.stars.toString());
            // NOTIFICATIONS
            if (resultado.notifications > 0){
                mNotifications.setText(resultado.notifications.toString());
                mNotifications.setVisibility(View.VISIBLE);
            }
            // STATISTICS
            fillData(resultado.bestFour);
        } else {
            // IF USER/PASSW IS NOT OKAY COME BACK TO LOGIN
            finish();
        }
    }

    private void fillData(List<AsyncTaskMainMenu.Statistic> list) {
        // CREANDO CURSOR CON LOS RESULTADOS
        String[] columns = new String[] { "_id", "position", "friendName", "friendStars"};
        MatrixCursor matrixCursor= new MatrixCursor(columns);

        Integer i=1;
        for(AsyncTaskMainMenu.Statistic p : list){
            matrixCursor.addRow(new Object[]{i-1,i.toString()+"º",p.username,"x "+p.stars.toString()});
            i++;
        }

        // PARSEANDO CURSOR A LISTVIEW
        String[] from = new String[] { "position", "friendName", "friendStars" };
        int[] to = new int[] { R.id.position, R.id.friendName, R.id.friendStars };

        SimpleCursorAdapter photos =
                new SimpleCursorAdapter(this, R.layout.row_main_menu, matrixCursor, from, to);
        mList.setAdapter(photos);
    }

}
