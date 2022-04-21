package es.unizar.eina.frankenstory.story;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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
    private ListView mListPreviousContent;

    private String number_chars;
    private String body;
    private String id;
    private boolean myStory;
    private boolean isLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_not_first_write);

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // BACKGROUND ANIMATION
        RelativeLayout relativeLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        // GET PARAMETERS
        Intent intent = getIntent();
        myStory = intent.getBooleanExtra("myStory",false);
        id = intent.getStringExtra("id");
        isLast = intent.getBooleanExtra("isLast",false);

        // GET VIEWS AND SET DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        content = (EditText) findViewById(R.id.story_content);
        mIconUser = (ImageView) findViewById(R.id.iconUser);
        send_text = (Button)findViewById(R.id.sendText);
        mcharactersToUse = (TextView) findViewById(R.id.charactersToUse);
        end_tale = (Button)findViewById(R.id.finishStory);
        mListPreviousContent = (ListView) findViewById(R.id.paragraphs);
        mUsername.setText(((MyApplication) this.getApplication()).getUsername());
        mStars.setText(((MyApplication) this.getApplication()).getStars());
        mCoins.setText(((MyApplication) this.getApplication()).getCoins());
        chooseIconUser(mIconUser, ((MyApplication) this.getApplication()).getIconUser());
        send_text.setBackground(getResources().getDrawable(R.drawable.button_grey));
        send_text.setEnabled(false);
        end_tale.setBackground(getResources().getDrawable(R.drawable.button_grey));
        end_tale.setEnabled(false);

        //ASYNC TASK RESUME_TALE
        AsyncTaskResumeStory myTask = new AsyncTaskResumeStory(this);
        myTask.execute(id);

        // BUTTONS FROM TOP AND BOTTOM
        setNavegavilidad();
    }

    public void updateCharacters () {
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
                    send_text.setBackground(getResources().getDrawable(R.drawable.button_grey));
                    end_tale.setEnabled(false);
                    end_tale.setBackground(getResources().getDrawable(R.drawable.button_grey));
                } else if (chars == Integer.parseInt(number_chars)){
                    send_text.setEnabled(false);
                    send_text.setBackground(getResources().getDrawable(R.drawable.button_grey));
                    end_tale.setEnabled(false);
                    end_tale.setBackground(getResources().getDrawable(R.drawable.button_grey));
                }
                else {
                    mcharactersToUse.setTextColor(getResources().getColor(R.color.white));
                    send_text.setEnabled(true);
                    send_text.setBackground(getResources().getDrawable(R.drawable.button_green));
                    end_tale.setEnabled(true);
                    end_tale.setBackground(getResources().getDrawable(R.drawable.buttom_finish_story));
                }
            }
        });
    }

    public void setNavegavilidad(){

        // BUTTON TO SettingsActivity
        ImageButton buttonSettings = (ImageButton)findViewById(R.id.configbutton);
        buttonSettings.setVisibility(View.GONE);

        if (!isLast){
            // BUTTON SEND TEXT
            send_text.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    String new_paragraph = String.valueOf(content.getText());
                    // CALL ASYNC TASK ADD PARAGRAPH
                    AsyncTaskAddParagraph myTask = new AsyncTaskAddParagraph(StoryNotFirstWriteActivity.this);
                    myTask.execute(id, new_paragraph, String.valueOf(false));
                }
            });
        } else {
            send_text.setVisibility(View.GONE);
        }

        // BUTTON FINISH TALE
        if (isLast || myStory) {
            end_tale.setVisibility(View.VISIBLE);
            end_tale.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    String new_paragraph = String.valueOf(content.getText());
                    // CALL ASYNC TASK ADD PARAGRAPH
                    AsyncTaskAddParagraph myTask = new AsyncTaskAddParagraph(StoryNotFirstWriteActivity.this);
                    myTask.execute(id, new_paragraph, String.valueOf(true));
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
        } else {
            Intent i = new Intent(StoryNotFirstWriteActivity.this, StoryActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
            finish();
        }
    }

    // ASYNC TASK ADAPTER RESUME TALE
    public void setupAdapter(AsyncTaskResumeStory.Result resultado)
    {

        if (resultado.result==null || resultado.result.equals("error")) {
            Toast.makeText(getApplicationContext(),"ERROR LEYENDO HISTORIA",Toast.LENGTH_SHORT).show();
            finish();
        } else {
            TextView story_title = (TextView) findViewById(R.id.story_name);
            story_title.setText(String.valueOf(resultado.title));

            fillDataParagraphs(resultado.paragraphs);

            // Comprobación no escribir 2 veces seguidas
            if (resultado.paragraphs.get(resultado.paragraphs.size()-1).username.equals(mUsername.getText())) {
                if (!myStory){
                    mcharactersToUse.setVisibility(View.GONE);
                    content.setVisibility(View.GONE);
                    TextView messageCantWrite = (TextView) findViewById(R.id.messageCantWrite);
                    messageCantWrite.setVisibility(View.VISIBLE);
                } else {
                    number_chars = resultado.maxCharacters.toString();
                    updateCharacters();
                }
                send_text.setVisibility(View.GONE);
            } else {
                // update characters
                number_chars = resultado.maxCharacters.toString();
                updateCharacters();
            }
        }
    }

    public void fillDataParagraphs(List<AsyncTaskResumeStory.Paragraph> paragraphs){

        ListPreviousContentAdapter adapter = new ListPreviousContentAdapter(this, paragraphs);

        mListPreviousContent.setAdapter(adapter);

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
