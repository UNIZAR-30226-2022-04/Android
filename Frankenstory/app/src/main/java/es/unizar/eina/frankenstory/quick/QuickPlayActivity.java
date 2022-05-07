package es.unizar.eina.frankenstory.quick;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;

public class QuickPlayActivity extends AppCompatActivity{


    private TextView mUsername;
    private TextView mCoins;
    private ImageView mIconUser;
    private EditText content;
    private TextView mTheme;
    private TextView mFirstWord;
    private TextView mSecondWord;
    private TextView mThirdWord;
    private Button send_text;
    private TextView mTime;
    private TextView previous_content;

    private Button buttonPunetas;
    private LinearLayout letras_reves;
    private LinearLayout escribe_ciegas;
    private LinearLayout desorden_total;
    private TextView textPunetas;
    private ListView mlistfriendsToSend;

    private String code;
    private String mode;
    private String word1;
    private String word2;
    private String word3;

    static class FriendPuneta {
        String username;
        String puneta;
    }

    static class ParagraphToSend extends Object {
        String id;
        String body;
        Integer turn;
        Boolean isLast;
        List<FriendPuneta> listFriendPuneta;
    }
    ParagraphToSend paragraphToSend;
    private List<AsyncTaskGetRoom.Participants> gameParticipants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paragraphToSend = new ParagraphToSend();
        
        // GET PARAMETERS
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        setContentView(R.layout.activity_quick_game_play);
        paragraphToSend.id = intent.getStringExtra("code");
        gameParticipants = (List<AsyncTaskGetRoom.Participants>) intent.getSerializableExtra("gameParticipants");


        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // BACKGROUND ANIMATION
        RelativeLayout relativeLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //GET PARAMETERS
        Intent i = this.getIntent();
        code = i.getExtras().getString("code");

        // GET VIEWS AND SET DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mIconUser = (ImageView) findViewById(R.id.iconUser);
        content = (EditText) findViewById(R.id.story_content);
        send_text = (Button)findViewById(R.id.sendText);
        mTheme = (TextView) findViewById(R.id.twitter_trend);
        mFirstWord = (TextView) findViewById(R.id.first_Word);
        mSecondWord = (TextView) findViewById(R.id.second_word);
        mThirdWord = (TextView) findViewById(R.id.third_word);
        mTime = (TextView) findViewById(R.id.time);
        previous_content = (TextView) findViewById(R.id.previous_content);
        mlistfriendsToSend = (ListView) findViewById(R.id.listfriendsToSend);

        mUsername.setText(((MyApplication) this.getApplication()).getUsername());
        mCoins.setText(((MyApplication) this.getApplication()).getCoins());
        chooseIconUser(mIconUser, ((MyApplication) this.getApplication()).getIconUser());

        paragraphToSend.turn = 0;

        //SEND TEXT
        setNavegavilidad();

        // CALL ASYNC TASK PLAY QUICK GAME
        AsyncTaskPlayQuickGame myTask = new AsyncTaskPlayQuickGame(this);
        myTask.execute(code);

        // HIDE UNUSED BCS OF MODE
        if (mode.equals("twitter")){
            LinearLayout mWords = (LinearLayout) findViewById(R.id.random_words);
            mWords.setVisibility(View.GONE);
            TextView mTextRandomWords = (TextView) findViewById(R.id.text_random_words);
            mTextRandomWords.setVisibility(View.GONE);
        } else {
            LinearLayout mTwitter = (LinearLayout) findViewById(R.id.twitter);
            mTwitter.setVisibility(View.GONE);
        }

        previous_content.setVisibility(View.GONE);



        // PUÑETAS
        paragraphToSend.listFriendPuneta = new ArrayList<FriendPuneta>();
        buttonPunetas = (Button) findViewById(R.id.buttonPunetas);
        letras_reves = (LinearLayout) findViewById(R.id.letras_reves);
        escribe_ciegas = (LinearLayout) findViewById(R.id.escribe_ciegas);
        desorden_total = (LinearLayout) findViewById(R.id.desorden_total);
        textPunetas = (TextView) findViewById(R.id.textPunetas);
        buttonPunetas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (buttonPunetas.getText().equals("+")){
                    // OPEN
                    buttonPunetas.setText("-");
                    textPunetas.setText("Comprar puñetas");
                    letras_reves.setVisibility(View.VISIBLE);
                    escribe_ciegas.setVisibility(View.VISIBLE);
                    desorden_total.setVisibility(View.VISIBLE);
                } else {
                    // CLOSE
                    closePunetas();
                }
            }
        });
        letras_reves.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textPunetas.setText("¿A quién envías la puñeta?");
                letras_reves.setVisibility(View.GONE);
                escribe_ciegas.setVisibility(View.GONE);
                desorden_total.setVisibility(View.GONE);
                // INFLATE LIST
                mlistfriendsToSend.setVisibility(View.VISIBLE);
                ListPunetasAdapter adapter = new ListPunetasAdapter(QuickPlayActivity.this, gameParticipants, "reves",150);
                mlistfriendsToSend.setAdapter(adapter);
            }
        });
        escribe_ciegas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textPunetas.setText("¿A quién envías la puñeta?");
                letras_reves.setVisibility(View.GONE);
                escribe_ciegas.setVisibility(View.GONE);
                desorden_total.setVisibility(View.GONE);
                // INFLATE LIST
                mlistfriendsToSend.setVisibility(View.VISIBLE);
                ListPunetasAdapter adapter = new ListPunetasAdapter(QuickPlayActivity.this, gameParticipants, "ciego",300);
                mlistfriendsToSend.setAdapter(adapter);
            }
        });
        desorden_total.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textPunetas.setText("¿A quién envías la puñeta?");
                letras_reves.setVisibility(View.GONE);
                escribe_ciegas.setVisibility(View.GONE);
                desorden_total.setVisibility(View.GONE);
                // INFLATE LIST
                mlistfriendsToSend.setVisibility(View.VISIBLE);
                ListPunetasAdapter adapter = new ListPunetasAdapter(QuickPlayActivity.this, gameParticipants, "desorden",500);
                mlistfriendsToSend.setAdapter(adapter);
            }
        });

    }

    public void closePunetas(){
        buttonPunetas.setText("+");
        textPunetas.setText("Comprar puñetas");
        letras_reves.setVisibility(View.GONE);
        escribe_ciegas.setVisibility(View.GONE);
        desorden_total.setVisibility(View.GONE);
        mlistfriendsToSend.setVisibility(View.GONE);
    }

    public void setNavegavilidad(){

        // BUTTON SEND TEXT
        send_text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                paragraphToSend.body = String.valueOf(content.getText());
                paragraphToSend.isLast = true; /*Debuggear*/
                /*PARA DEBUG*/
                //GO TO QUICK GAME VOTE
                Intent i = new Intent(QuickPlayActivity.this, QuickVoteActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                i.putExtra("code",code);
                i.putExtra("mode",mode);
                startActivity(i);

                // CALL ASYNC TASK ADD PARAGRAPH
                // AsyncTaskAddParagraph myTask = new AsyncTaskAddParagraph(this);
                // myTask.execute(paragraphToSend);
            }
        });

    }

    // ASYNC TASK ADAPTER ADD PARAGRAPH
    public void setupAdapter(AsyncTaskAddParagraph.Result resultado)
    {
        if (resultado.result==null || resultado.result.equals("error")) {
            Toast.makeText(getApplicationContext(),"ERROR ENVIANDO APORTACION", Toast.LENGTH_SHORT).show();
        }else {
            paragraphToSend.turn++;

            if (paragraphToSend.turn == 9) send_text.setBackgroundResource(R.drawable.buttom_finish_story);
            if (paragraphToSend.turn == 10) {
                //GO TO QUICK GAME VOTE
                Intent i = new Intent(QuickPlayActivity.this, QuickVoteActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }

            previous_content.setVisibility(View.VISIBLE);

            // CALL ASYNC TASK PLAY QUICK GAME
            AsyncTaskPlayQuickGame myTask = new AsyncTaskPlayQuickGame(this);
            myTask.execute(code);
        }
    }

    // ASYNC TASK ADAPTER PLAY QUICK GAME
    public void setupAdapter(AsyncTaskPlayQuickGame.Result resultado)
    {
        if (resultado.result!=null && !resultado.result.equals("error")) {

            if (mode.equals("twitter")) {
                // Add hashtag if it has not
                String hashtag = "";
                if (resultado.topic.charAt(0)!='#') hashtag +="#";
                mTheme.setText(hashtag + resultado.topic);
            }else {
                mFirstWord.setText(resultado.randomWords.get(0));
                mSecondWord.setText(resultado.randomWords.get(1));
                mThirdWord.setText(resultado.randomWords.get(2));

                word1 = resultado.randomWords.get(0);
                word2 = resultado.randomWords.get(1);
                word3 = resultado.randomWords.get(2);

            }

            if (paragraphToSend.turn > 0) previous_content.setText(resultado.lastParagraph);

            new CountDownTimer(resultado.s * 1000L,1000){

                public void onTick (long millisUntilFinished) {

                     mTime.setText(""+String.format("%d min %d sec",
                    TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                    if (!mode.equals("twitter")) UpdateUsedWords();

                }

                public void onFinish() {

                    //GO TO NOT FIRST WRITE

                }

            }.start();

        }
    }

    //Update colours of random words in editText
    public void UpdateUsedWords() {

        String text = String.valueOf(content.getText());

        if (text.contains(word1) || text.contains(word1.toLowerCase(Locale.ROOT)) ) mFirstWord.setBackgroundColor(getResources().getColor(R.color.verde_parrafo_seleccionado));
        else mFirstWord.setBackgroundColor(getResources().getColor(R.color.verde_letras));

        if (text.contains(word2) || text.contains(word2.toLowerCase(Locale.ROOT))) mSecondWord.setBackgroundColor(getResources().getColor(R.color.verde_parrafo_seleccionado));
        else mSecondWord.setBackgroundColor(getResources().getColor(R.color.verde_letras));

        if (text.contains(word3) || text.contains(word3.toLowerCase(Locale.ROOT))) mThirdWord.setBackgroundColor(getResources().getColor(R.color.verde_parrafo_seleccionado));
        else mThirdWord.setBackgroundColor(getResources().getColor(R.color.verde_letras));

    }

    public void subtractMooncoins(Integer mooncoinsPayed) {
        Integer pocket = Integer.parseInt(((MyApplication) this.getApplication()).getCoins());
        pocket -= mooncoinsPayed;
        ((MyApplication) this.getApplication()).setCoins(pocket.toString());
        mCoins.setText(pocket.toString());
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
