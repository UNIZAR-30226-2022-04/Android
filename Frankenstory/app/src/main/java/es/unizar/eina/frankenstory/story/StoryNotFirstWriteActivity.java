package es.unizar.eina.frankenstory.story;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;
import es.unizar.eina.frankenstory.general.FriendsActivity;
import es.unizar.eina.frankenstory.general.MainMenuActivity;
import es.unizar.eina.frankenstory.general.SettingsActivity;


public class StoryNotFirstWriteActivity extends AppCompatActivity{


    private TextView mUsername;
    private TextView mStars;
    private TextView mCoins;
    private ImageView mIconUser;
    private EditText content;
    private TextView mcharactersToUse;
    private Button send_text;
    private Button end_tale;

    private String number_chars;
    private String title;
    private String body;
    private String id;
    private String previous_content;
    private boolean myStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_not_first_write);

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
        title = intent.getStringExtra("title");

        // GET VIEWS AND SET DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        content = (EditText) findViewById(R.id.story_content);
        mIconUser = (ImageView) findViewById(R.id.iconUser);
        send_text = (Button)findViewById(R.id.sendText);
        mcharactersToUse = (TextView) findViewById(R.id.charactersToUse);
        end_tale = (Button)findViewById(R.id.finishStory);
        mUsername.setText(((MyApplication) this.getApplication()).getUsername());
        mStars.setText(((MyApplication) this.getApplication()).getStars());
        mCoins.setText(((MyApplication) this.getApplication()).getCoins());
        chooseIconUser(mIconUser, ((MyApplication) this.getApplication()).getIconUser());

        //FILL CONTENT
        setContenido();

         // SET MAX CHAR
        mcharactersToUse.setText(number_chars.toString()+" caracteres");
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Integer chars = Integer.parseInt(number_chars)-content.length();
                mcharactersToUse.setText(chars.toString()+" caracteres");
                if (chars < 0) {
                    mcharactersToUse.setTextColor(getResources().getColor(R.color.rojo));
                    send_text.setEnabled(false);
                    end_tale.setEnabled(false);
                }
                else {
                    mcharactersToUse.setTextColor(getResources().getColor(R.color.white));
                    send_text.setEnabled(true);
                    end_tale.setEnabled(true);
                }
            }
        });

        // BUTTONS FROM TOP AND BOTTOM
        setNavegavilidad();
    }

    public void setContenido() {

        //Rellenar variables number_chars y number_writings
        number_chars = "120";

        //ASYNC TASK RESUME_TALE
        //AsyncTaskResumeStory myTask = new AsyncTaskResumeStory(this);
        //myTask.execute(id);

        TextView story_title = (TextView) findViewById(R.id.story_name);
        story_title.setText(String.valueOf(title));

        //TextView previous_content = (TextView) findViewById(R.id.previous_content);
        //previous_content.setText(String.valueOf(body));
    }

    public void setNavegavilidad(){

        // BUTTON TO SettingsActivity
        ImageButton buttonSettings = (ImageButton)findViewById(R.id.configbutton);
        buttonSettings.setVisibility(View.GONE);

        // BUTTON SEND TEXT
        send_text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String new_paragraph = String.valueOf(content.getText());

                // CALL ASYNC TASK ADD PARAGRAPH
                AsyncTaskAddParagraph myTask = new AsyncTaskAddParagraph(StoryNotFirstWriteActivity.this);
                myTask.execute(id, new_paragraph, String.valueOf(false));

                Intent i = new Intent(StoryNotFirstWriteActivity.this, StoryActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                finish();
            }
        });

        // BUTTON FINISH TALE
        if (myStory) {
            end_tale.setVisibility(View.VISIBLE);
            end_tale.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    String new_paragraph = String.valueOf(content.getText());

                    // CALL ASYNC TASK ADD PARAGRAPH
                    AsyncTaskAddParagraph myTask = new AsyncTaskAddParagraph(StoryNotFirstWriteActivity.this);
                    myTask.execute(id, new_paragraph, String.valueOf(true));

                    Intent i = new Intent(StoryNotFirstWriteActivity.this, StoryActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                    finish();
                }
            });
        }else{
            end_tale.setVisibility(View.GONE);
        }
    }

    // ASYNC TASK ADAPTER ADD PARAGRAPH
    public void setupAdapter(AsyncTaskAddParagraph.Result resultado)
    {
        if (resultado.result==null || resultado.result.equals("error")) {
            Toast.makeText(getApplicationContext(),"ERROR AÑADIENDO PÁRRAFO", Toast.LENGTH_SHORT).show();
        }
    }

    // ASYNC TASK ADAPTER RESUME TALE
    public void setupAdapter(AsyncTaskResumeStory.Result resultado)
    {
        if (resultado.result==null || resultado.result.equals("error")) {
            Toast.makeText(getApplicationContext(),"ERROR AÑADIENDO PÁRRAFO",Toast.LENGTH_SHORT).show();
        } else {
            previous_content = setPreviousContent(resultado.paragraphs);
        }
    }

    public String setPreviousContent (List<AsyncTaskResumeStory.Story> paragraphs)
    {
        String aux = "";
        int i = 0;
        while ( i < paragraphs.size()) {
            for (AsyncTaskResumeStory.Story paragraph : paragraphs) {
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
