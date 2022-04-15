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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;

public class StoryNotFirstWriteActivity extends AppCompatActivity{


    private TextView mUsername;
    private TextView mStars;
    private TextView mCoins;
    private Button mNotifications;
    private ImageView mIconUser;
    private EditText mParagraph;

    String title;
    String body;
    String id;
    String previous_content;
    boolean myStory;

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
        myStory = intent.getBooleanExtra("myStory",false);
        id = intent.getStringExtra("id");

        // GET VIEWS AND SET DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mNotifications = (Button) findViewById(R.id.notifications);
        mIconUser = (ImageView) findViewById(R.id.iconUser);
        mParagraph = (EditText) findViewById(R.id.story_content);
        mUsername.setText(((MyApplication) this.getApplication()).getUsername());
        mStars.setText(((MyApplication) this.getApplication()).getStars());
        mCoins.setText(((MyApplication) this.getApplication()).getCoins());
        chooseIconUser(mIconUser, ((MyApplication) this.getApplication()).getIconUser());

        //Rellenar el titulo y el anterior párrafo
        setContenido();

        // BUTTONS FROM TOP AND BOTTOM
        setNavegavilidad();

    }

    public void setContenido() {

        //ASYNC TASK RESUME_TALE
        AsyncTaskResumeTale myTask = new AsyncTaskResumeTale(this);
        myTask.execute(id);

        TextView story_title = (TextView) findViewById(R.id.story_name);
        story_title.setText(String.valueOf(title));

        TextView previous_content = (TextView) findViewById(R.id.previous_content);
        previous_content.setText(String.valueOf(body));
    }

    public void setNavegavilidad(){

        // BUTTON TO SettingsActivity
        ImageButton buttonSettings = (ImageButton)findViewById(R.id.configbutton);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(StoryNotFirstWriteActivity.this, SettingsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON SEND TEXT
        Button send_text = (Button)findViewById(R.id.sendText);
        send_text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText content = (EditText) findViewById(R.id.story_content);
                String new_paragraph = String.valueOf(content.getText());

                // CALL ASYNC TASK ADD PARAGRAPH
                AsyncTaskAddParagraph myTask = new AsyncTaskAddParagraph(StoryNotFirstWriteActivity.this);
                myTask.execute(id, new_paragraph, String.valueOf(false));

                Intent i = new Intent(StoryNotFirstWriteActivity.this, StoryActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON FINISH TALE
        Button end_tale = (Button)findViewById(R.id.finishStory);
        send_text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText content = (EditText) findViewById(R.id.story_content);
                String new_paragraph = String.valueOf(content.getText());

                // CALL ASYNC TASK ADD PARAGRAPH
                AsyncTaskAddParagraph myTask = new AsyncTaskAddParagraph(StoryNotFirstWriteActivity.this);
                myTask.execute(id, new_paragraph, String.valueOf(true));

                Intent i = new Intent(StoryNotFirstWriteActivity.this, StoryActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

    }

    // ASYNC TASK ADAPTER ADD PARAGRAPH
    public void setupAdapter(AsyncTaskAddParagraph.Result resultado)
    {
        if (resultado.result==null || resultado.result.equals("error")) {
            Toast.makeText(getApplicationContext(),"ERROR AÑADIENDO PÁRRAFO",Toast.LENGTH_SHORT).show();
        }
    }

    // ASYNC TASK ADAPTER RESUME TALE
    public void setupAdapter(AsyncTaskResumeTale.Result resultado)
    {
        if (resultado.result==null || resultado.result.equals("error")) {
            Toast.makeText(getApplicationContext(),"ERROR AÑADIENDO PÁRRAFO",Toast.LENGTH_SHORT).show();
        } else {
            previous_content = setPreviousContent(resultado.paragraphs);
        }
    }

    public String setPreviousContent (List<AsyncTaskResumeTale.Story> paragraphs)
    {
        String aux = "";
        int i = 0;
        while ( i < paragraphs.size()) {
            for (AsyncTaskResumeTale.Story paragraph : paragraphs) {
                if (paragraph.orden == i) {
                    aux += "\n" + paragraph.body;
                    i++;
                }
            }
        }
        return aux;
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
