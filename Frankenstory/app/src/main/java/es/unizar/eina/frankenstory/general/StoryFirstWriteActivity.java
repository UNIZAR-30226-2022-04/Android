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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.unizar.eina.frankenstory.R;

public class StoryFirstWriteActivity extends AppCompatActivity{


    private TextView mUsername;
    private TextView mStars;
    private TextView mCoins;
    private Button mNotifications;
    private ImageView mIconUser;
    String number_writings;
    String number_chars;
    String isPrivate_game;
    String title;
    String username;
    String password;
    String stars;
    String coins;
    String notifications;
    String iconUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_first_write);

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
        iconUser = intent.getStringExtra("iconUser");
        number_writings = intent.getStringExtra("number_writings");
        number_chars = intent.getStringExtra("number_chars");
        isPrivate_game = intent.getStringExtra("isPrivate_game");
        title = intent.getStringExtra("title");
                System.out.println(username);
                System.out.println(password);
                System.out.println(title);
                System.out.println(number_writings);
                System.out.println(number_chars);
                System.out.println(isPrivate_game);
        // GET VIEWS AND SET DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mNotifications = (Button) findViewById(R.id.notifications);
        mIconUser = (ImageView) findViewById(R.id.iconUser);
        mUsername.setText(username);
        mStars.setText(stars);
        mCoins.setText(coins);
        chooseIconUser(mIconUser, iconUser);

        //Rellenar el titulo
        setContenido();

        // BUTTONS FROM TOP AND BOTTOM
        setNavegavilidad();

    }

    public void setContenido() {
        TextView story_title = (TextView) findViewById(R.id.story_name);
        story_title.setText(String.valueOf(title));
    }

    public void setNavegavilidad(){

        // BUTTON TO SettingsActivity
        ImageButton buttonSettings = (ImageButton)findViewById(R.id.configbutton);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(StoryFirstWriteActivity.this, SettingsActivity.class);
                i.putExtra("username",username);
                i.putExtra("password",password);
                i.putExtra("stars", stars);
                i.putExtra("coins", coins);
                i.putExtra("notifications", notifications);
                i.putExtra("iconUser", iconUser);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON SEND TEXT
        Button send_text = (Button)findViewById(R.id.finish);
        send_text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText content = (EditText) findViewById(R.id.story_content);
                String first_paragraph = String.valueOf(content.getText());

                // CALL ASYNC TASK
                AsyncTaskCreateTale myTask = new AsyncTaskCreateTale(StoryFirstWriteActivity.this);
                myTask.execute(username, password, title, number_writings, number_chars, isPrivate_game, first_paragraph);

                Intent i = new Intent(StoryFirstWriteActivity.this, MainStoryActivity.class);
                i.putExtra("username",username);
                i.putExtra("password",password);
                i.putExtra("stars", stars);
                i.putExtra("coins", coins);
                i.putExtra("notifications", notifications);
                i.putExtra("iconUser", iconUser);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

    }

    // ASYNC TASK ADAPTER
    public void setupAdapter(AsyncTaskCreateTale.Result resultado)
    {
        if (resultado.result==null || resultado.result.equals("error")) {
            Toast.makeText(getApplicationContext(),"ERROR CREANDO RELATO",Toast.LENGTH_SHORT).show();
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
