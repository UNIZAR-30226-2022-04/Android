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
import android.widget.ImageButton;
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
import java.util.concurrent.TimeUnit;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;

public class QuickVotedActivity extends AppCompatActivity{

    private TextView mUsername;
    private TextView mCoins;
    private TextView mStars;
    private ListView mParagraphs;
    private ImageView mIconUser;

    private String code;
    private String mode;
    private Integer turn;
    public Integer winner;
    private Boolean isLast;
    private Boolean alreadyStartedTimer;
    private TextView mTime;
    Integer votedParagraph;
    private List<AsyncTaskGetRoom.Participants> gameParticipants;

    private Timer myTimer;
    private CountDownTimer addParagraphCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_game_voted);

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // GET VIEWS AND SET DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mParagraphs = (ListView) findViewById(R.id.voted);
        mIconUser = (ImageView) findViewById(R.id.iconUser);
        mTime = (TextView) findViewById(R.id.time);

        updateData();
        votedParagraph = 0;
        alreadyStartedTimer = false;

        Intent i = getIntent();
        code = i.getStringExtra("code");
        mode = i.getStringExtra("mode");
        isLast = i.getBooleanExtra("isLast", false);
        turn = Integer.parseInt(i.getStringExtra("turn"));
        gameParticipants = (List<AsyncTaskGetRoom.Participants>) i.getSerializableExtra("gameParticipants");

        // BACKGROUND ANIMATION
        RelativeLayout relativeLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        // CALL ASYNC TASK RESUME VOTED QUICK GAME
        AsyncTaskResumeQuickVoted myTask = new AsyncTaskResumeQuickVoted(null,this);
        myTask.execute(code, String.valueOf(turn));

        ImageButton buttonSettings = (ImageButton)findViewById(R.id.configbutton);
        buttonSettings.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myTimer != null) myTimer.cancel();
        addParagraphCountDown.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // UPDATE DATA
    public void updateData(){
        mUsername.setText(((MyApplication) this.getApplication()).getUsername());
        mCoins.setText(((MyApplication) this.getApplication()).getCoins());
        mStars.setText(((MyApplication) this.getApplication()).getStars());
        chooseIconUser(mIconUser, ((MyApplication) this.getApplication()).getIconUser());
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

    // ASYNC TASK ADAPTER RESUME QUICK VOTED
    public void setupAdapter(AsyncTaskResumeQuickVoted.Result resultado) {

        if (resultado.result!=null && resultado.result.equals("success")){
            TextView mUsername = (TextView) findViewById(R.id.user_story);
            mUsername.setText("Historia de "+ resultado.paragraphs.get(0).username);
            winner = resultado.winner;
            fillParagraphs(resultado.paragraphs);

            // WAIT UNTIL TIME AND SET TIMER
            addParagraphCountDown = new CountDownTimer(resultado.s * 1000L,1000){
                @Override
                public void onTick(long millisUntilFinished) {
                    mTime.setText(""+String.format("%d min %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));}
                public void onFinish() {
                    if (isLast) {
                        //GO TO QUICK POINTS
                        Intent i = new Intent(QuickVotedActivity.this, QuickPointsActivity.class);
                        i.putExtra("code",code);
                        startActivity(i);
                        finish();
                    } else {  //TO QUICK VOTE
                        askIfWaiting();
                    }
                }
            }.start();
        }
    }

    // ASYNC TASK ADAPTER RESUME QUICK VOTE
    public void setupAdapter(AsyncTaskResumeQuickVote.Result resultado) {

        if (resultado.result!=null && resultado.result.equals("success")){
            if (myTimer != null) myTimer.cancel();
            addParagraphCountDown.cancel();
            //GO TO QUICK GAME VOTE
            Intent i = new Intent(QuickVotedActivity.this, QuickVoteActivity.class);
            i.putExtra("code",code);
            i.putExtra("mode",mode);
            Integer newTurn = turn+1;
            i.putExtra("turn",newTurn.toString());
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
        AsyncTaskResumeQuickVote myTask = new AsyncTaskResumeQuickVote(null, QuickVotedActivity.this, null);
        myTask.execute(code, String.valueOf(turn+1));
    }

    private void fillParagraphs(List<AsyncTaskResumeQuickVoted.Paragraph> paragraphs) {
        ListQuickVotedParagraphsAdapter adapter = new ListQuickVotedParagraphsAdapter(this, paragraphs);
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
