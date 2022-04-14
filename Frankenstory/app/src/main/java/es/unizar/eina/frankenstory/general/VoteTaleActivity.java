package es.unizar.eina.frankenstory.general;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import es.unizar.eina.frankenstory.R;


public class VoteTaleActivity extends AppCompatActivity{

    private TextView mUsername;
    private TextView mCoins;
    private ListView mParagraphs;
    private ImageView mIconUser;
    private TextView mCreator;
    private TextView mTitle;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_vote);

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // GET VIEWS
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mParagraphs = (ListView) findViewById(R.id.paragraphs);
        mIconUser = (ImageView) findViewById(R.id.iconUser);
        mCreator = (TextView) findViewById(R.id.creator);
        mTitle = (TextView) findViewById(R.id.title);

        Intent i = getIntent();
        id = i.getStringExtra("id");

        setNavegavilidad();

        // BACKGROUND ANIMATION
        RelativeLayout relativeLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        // CALL ASYNC TASK
        AsyncTaskGetParagraphs myTask = new AsyncTaskGetParagraphs(this);
        myTask.execute(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // CALL ASYNC TASK
        AsyncTaskGetParagraphs myTask = new AsyncTaskGetParagraphs(VoteTaleActivity.this);
        myTask.execute(id);
    }

    public void setNavegavilidad(){
        // BUTTON VOTE
        ImageButton buttonSettings = (ImageButton)findViewById(R.id.vote);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //ASYNC TASK VOTE PARAGRAPH
                Intent i = new Intent(VoteTaleActivity.this, StoryActivity.class);
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
    public void setupAdapter(AsyncTaskGetParagraphs.Result resultado)
    {
        if (resultado.result!=null && resultado.result.equals("success")){
            fillDataTale(resultado);
        }
    }

    private void fillDataTale(AsyncTaskGetParagraphs.Result resultado) {
        // instantiate the custom list adapter
        ListParagraphsAdapter adapter = new ListParagraphsAdapter(this, resultado.paragraphs);

        // get the ListView and attach the adapter
        mParagraphs.setAdapter(adapter);

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
