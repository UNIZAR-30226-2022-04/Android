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
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;
public class CreateStoryActivity extends AppCompatActivity{

    private TextView mUsername;
    private TextView mStars;
    private TextView mCoins;
    private TextView mTitle;
    private ListView mList;
    private ImageView mIconUser;
    int number_writings;
    int number_chars;
    boolean isPrivate_game;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_create);

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // GET VIEWS
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mTitle = (TextView) findViewById(R.id.story_name);
        mList = (ListView) findViewById(R.id.statistics);
        mIconUser = (ImageView) findViewById(R.id.iconUser);
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        title = "";
        mTitle.setText("");

        number_writings = 1;
        number_chars = 1;
        setContenido();

        isPrivate_game = false;
        Switch private_game = (Switch) findViewById(R.id.private_game);
        private_game.setChecked(false);

    }

    public void setNavegavilidad(){
        //BUTTON PLUS WRITINGS
        Button plusWritings = (Button)findViewById(R.id.plus_writings);
        plusWritings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                number_writings++;
                setContenido();
            }
        });

        //BUTTON MINUS WRITINGS
        Button minusWritings = (Button)findViewById(R.id.minus_writings);
        minusWritings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (number_writings > 1) number_writings--;
                setContenido();
            }
        });

        //BUTTON PLUS LENGTH
        Button plusLength = (Button)findViewById(R.id.plus_lenght);
        plusLength.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                number_chars++;
                setContenido();
            }
        });

        //BUTTON MINUS LENGTH
        Button minusLenght = (Button)findViewById(R.id.minus_length);
        minusLenght.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (number_chars > 1) number_chars--;
                setContenido();
            }
        });

        //SWITCH PRIVATE GAME
        Switch private_game = (Switch) findViewById(R.id.private_game);
        private_game.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isPrivate_game = !isPrivate_game;
            }
        });

        // BUTTON TO SettingsActivity
        ImageButton buttonSettings = (ImageButton)findViewById(R.id.configbutton);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(CreateStoryActivity.this, SettingsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO CreateStoryActivity
        Button startStory = (Button)findViewById(R.id.start_story);
        startStory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                title = mTitle.getText().toString();
                if (title.equalsIgnoreCase("")){
                    mTitle.setError("Introduce un titulo");
                }else  {
                    Intent i = new Intent(CreateStoryActivity.this, StoryFirstWriteActivity.class);
                    i.putExtra("number_writings", String.valueOf(number_writings));
                    i.putExtra("number_chars", String.valueOf(number_chars));
                    i.putExtra("isPrivate_game", String.valueOf(isPrivate_game));
                    i.putExtra("title", title);
                    i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                }
            }
        });

    }

    public void setContenido() {
        TextView writings = (TextView) findViewById(R.id.writings);
        writings.setText(String.valueOf(number_writings));
        TextView length = (TextView) findViewById(R.id.length);
        length.setText(String.valueOf(number_chars));
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
