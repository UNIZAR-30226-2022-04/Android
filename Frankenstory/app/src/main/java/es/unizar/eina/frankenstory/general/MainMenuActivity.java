package es.unizar.eina.frankenstory.general;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.List;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;
import es.unizar.eina.frankenstory.quick.QuickActivity;
import es.unizar.eina.frankenstory.story.StoryActivity;

public class MainMenuActivity extends AppCompatActivity {

    private TextView mUsername;
    private TextView mStars;
    private TextView mCoins;
    private Button mNotifications;
    private ListView mList;
    private ImageView mIconUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_main_menu);

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // GET VIEWS
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mNotifications = (Button) findViewById(R.id.notifications);
        mNotifications.setVisibility(View.INVISIBLE);
        mList = (ListView) findViewById(R.id.statistics);
        mIconUser = (ImageView) findViewById(R.id.iconUser);

        // BUTTONS FROM TOP AND BOTTOM
        setNavegavilidad();

        // BACKGROUND ANIMATION
        RelativeLayout relativeLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        // CALL ASYNC TASK
        AsyncTaskMainMenu myTask = new AsyncTaskMainMenu(this);
        myTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // CALL ASYNC TASK
        AsyncTaskMainMenu myTask = new AsyncTaskMainMenu(this);
        myTask.execute();
    }

    public void setNavegavilidad(){
        // BUTTON TO SettingsActivity
        ImageButton buttonSettings = (ImageButton)findViewById(R.id.configbutton);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, SettingsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO FriendsActivity
        Button buttonFriends = (Button) findViewById(R.id.friends);
        buttonFriends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, FriendsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO StoryActivity
        LottieAnimationView lottie = (LottieAnimationView) findViewById(R.id.buttonStory_mode);
        lottie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, StoryActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO LibraryActivity
        Button Buttomlibrary = (Button) findViewById(R.id.your_stories);
        Buttomlibrary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, LibraryActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO QuickActivity
        LottieAnimationView lottie_quick = (LottieAnimationView) findViewById(R.id.buttonQuick_game);
        lottie_quick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, QuickActivity.class);
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
        if (resultado.result != null && resultado.result.equals("success")){
            // Saved to send to other activities
            ((MyApplication) this.getApplication()).setStars(resultado.stars.toString());
            ((MyApplication) this.getApplication()).setCoins(resultado.coins.toString());
            ((MyApplication) this.getApplication()).setNotifications(resultado.notifications.toString());
            ((MyApplication) this.getApplication()).setIconUser(resultado.picture.toString());
            // TOP INFORMATION
            mUsername.setText(((MyApplication) this.getApplication()).getUsername());
            mCoins.setText(resultado.coins.toString());
            mStars.setText(resultado.stars.toString());
            chooseIconUser(mIconUser, resultado.picture.toString());
            // NOTIFICATIONS
            if (resultado.notifications > 0){
                mNotifications.setText(resultado.notifications.toString());
                mNotifications.setVisibility(View.VISIBLE);
            } else mNotifications.setVisibility(View.INVISIBLE);
            // STATISTICS
            fillData(resultado.bestFour);
        } else {
            // IF USER/PASSW IS NOT OKAY COME BACK TO LOGIN
            finish();
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
