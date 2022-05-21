package es.unizar.eina.frankenstory.quick;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;

public class QuickVoteActivity extends AppCompatActivity{

    private TextView mUsername;
    private TextView mCoins;
    private ListView mParagraphs;
    private ImageView mIconUser;
    private TextView mTheme;
    private TextView mTittleTheme;
    private Button mVote;
    private Boolean isLast;

    private String code;
    private String mode;
    private Integer turn;
    Integer votedParagraph;
    View selectedView;
    private List<AsyncTaskGetRoom.Participants> gameParticipants;

    private Boolean alreadyStartedTimer;

    private Timer myTimer;
    private CountDownTimer addParagraphCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_game_vote);

        alreadyStartedTimer = false;
        isLast = false;

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // GET VIEWS AND SET DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mParagraphs = (ListView) findViewById(R.id.paragraphs);
        mIconUser = (ImageView) findViewById(R.id.iconUser);
        mTheme = (TextView) findViewById(R.id.twitter_trend);
        mTittleTheme = (TextView) findViewById(R.id.title_theme);
        mVote = (Button) findViewById(R.id.vote);

        updateData();
        votedParagraph = 0;

        Intent i = getIntent();
        code = i.getStringExtra("code");
        mode = i.getStringExtra("mode");
        turn = Integer.parseInt(i.getStringExtra("turn"));
        gameParticipants = (List<AsyncTaskGetRoom.Participants>) i.getSerializableExtra("gameParticipants");
        setNavegavilidad();

        // BACKGROUND ANIMATION
        RelativeLayout relativeLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        if (!mode.equals("twitter")) {
            mTheme.setVisibility(View.GONE);
            mTittleTheme.setVisibility(View.GONE);
        }

        // CALL ASYNC TASK RESUME VOTE QUICK GAME
        AsyncTaskResumeQuickVote myTask = new AsyncTaskResumeQuickVote(QuickVoteActivity.this, null);
        myTask.execute(code, String.valueOf(turn));

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // UPDATE DATA
    public void updateData(){
        mUsername.setText(((MyApplication) this.getApplication()).getUsername());
        mCoins.setText(((MyApplication) this.getApplication()).getCoins());
        chooseIconUser(mIconUser, ((MyApplication) this.getApplication()).getIconUser());
    }

    public void setNavegavilidad(){
        Button ButtonVote = (Button)findViewById(R.id.vote);

        // BUTTON VOTE
        ButtonVote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addParagraphCountDown.cancel();
                //ASYNC TASK VOTE PARAGRAPH
                AsyncTaskVoteQuick myTask = new AsyncTaskVoteQuick(QuickVoteActivity.this);
                myTask.execute(code, String.valueOf(votedParagraph));

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

    // ASYNC TASK ADAPTER RESUME QUICK VOTE
    public void setupAdapter(AsyncTaskResumeQuickVote.Result resultado)
    {

        if (resultado.result!=null && resultado.result.equals("success")){
            mTheme.setText(resultado.topic);
            turn = resultado.turn;
            isLast = resultado.isLast;
            fillParagraphs(resultado.paragraphs);

            // WAIT UNTIL TIME AND SET TIMER
            addParagraphCountDown = new CountDownTimer(resultado.s * 1000L,1000){
                @Override
                public void onTick(long millisUntilFinished) {}
                public void onFinish() {
                    //ASYNC TASK VOTE PARAGRAPH
                    AsyncTaskVoteQuick myTask = new AsyncTaskVoteQuick(QuickVoteActivity.this);
                    myTask.execute(code, String.valueOf(votedParagraph));
                }
            }.start();
        }
    }

    //ASYNC TASK ADAPTER VOTE QUICK
    public void setupAdapter(AsyncTaskVoteQuick.Result resultado) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        System.out.println(resultado.result);
        if (resultado.result==null || resultado.result.equals("error")) {
            Toast.makeText(getApplicationContext(), "ERROR ENVIANDO VOTACION", Toast.LENGTH_SHORT).show();
        } else {
            mVote.setClickable(false);
            mVote.setBackground(getResources().getDrawable(R.drawable.button_grey));
            TextView waiting = (TextView) findViewById(R.id.waitingPlayers);
            waiting.setVisibility(View.VISIBLE);

            AsyncTaskResumeQuickVoted myTask = new AsyncTaskResumeQuickVoted(QuickVoteActivity.this, null);
            myTask.execute(code, String.valueOf(turn+1));
        }
    }

    // ASYNC TASK ADAPTER RESUME QUICK VOTED
    public void setupAdapter(AsyncTaskResumeQuickVoted.Result resultado) {

        if (resultado.result!=null && resultado.result.equals("success")) {
            if (myTimer != null) myTimer.cancel();
            addParagraphCountDown.cancel();
            //GO TO QUICK GAME VOTED
            Intent i = new Intent(QuickVoteActivity.this, QuickVotedActivity.class);
            i.putExtra("code",code);
            i.putExtra("mode",mode);
            i.putExtra("turn",turn);
            i.putExtra("isLast", isLast);
            i.putExtra("gameParticipants", (Serializable) gameParticipants);
            startActivity(i);
            finish();

        } else if (resultado.result!=null && resultado.result.equals("waiting_players")) {
            if (!alreadyStartedTimer) {
                alreadyStartedTimer = true;
                myTimer = new Timer();
                TimerTask doThis;
                doThis = new TimerTask() {
                    @Override
                    public void run() {
                        askIfWaiting();
                    }
                };
                myTimer.scheduleAtFixedRate(doThis, 0, 3000);
            }
        }
    }

    public void askIfWaiting (){
        AsyncTaskResumeQuickVoted myTask = new AsyncTaskResumeQuickVoted(QuickVoteActivity.this, null);
        myTask.execute(code, String.valueOf(turn+1));
    }

    private void fillParagraphs(List<AsyncTaskResumeQuickVote.Paragraph> paragraphs) {

        ListQuickVoteParagraphsAdapter adapter = new ListQuickVoteParagraphsAdapter(this, paragraphs);
        mParagraphs.setAdapter(adapter);

        mParagraphs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // INVISIBLE LAST HEART
                ImageView heart = (ImageView) selectedView.findViewById(R.id.imageVote);
                heart.setVisibility(View.GONE);
                TextView textViewItem = (TextView) selectedView.findViewById(R.id.body);
                textViewItem.setBackgroundColor(getResources().getColor(R.color.verde_parrafo));
                // VISIBLE NEW HEART
                selectedView = view;
                textViewItem = (TextView) selectedView.findViewById(R.id.body);
                textViewItem.setBackgroundColor(getResources().getColor(R.color.verde_parrafo_seleccionado));
                heart = (ImageView) view.findViewById(R.id.imageVote);
                heart.setVisibility(View.VISIBLE);
                votedParagraph = position;
            }
        });
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
