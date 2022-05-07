package es.unizar.eina.frankenstory.quick;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.List;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;

public class QuickVoteActivity extends AppCompatActivity{

    private TextView mUsername;
    private TextView mCoins;
    private ListView mParagraphs;
    private ImageView mIconUser;
    private TextView mTheme;

    private String code;
    private String mode;
    Integer votedParagraph;
    View selectedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_game_vote);

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // GET VIEWS AND SET DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mParagraphs = (ListView) findViewById(R.id.paragraphs);
        mIconUser = (ImageView) findViewById(R.id.iconUser);
        mTheme = (TextView) findViewById(R.id.twitter_trend);

        updateData();
        votedParagraph = 0;

        Intent i = getIntent();
        code = i.getStringExtra("code");
        mode = i.getStringExtra("mode");

        setNavegavilidad();

        // BACKGROUND ANIMATION
        RelativeLayout relativeLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        if (!mode.equals("twitter")) {
            mTheme.setVisibility(View.GONE);
        }

        // CALL ASYNC TASK RESUME VOTE QUICK GAME
        AsyncTaskResumeQuickVote myTask = new AsyncTaskResumeQuickVote(this);
        myTask.execute(code);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // CALL ASYNC TASK RESUME VOTE QUICK GAME
        AsyncTaskResumeQuickVote myTask = new AsyncTaskResumeQuickVote(this);
        myTask.execute(code);
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
                //ASYNC TASK VOTE PARAGRAPH
                //AsyncVoteQuick myTask = new AsyncVoteQuick(QuickVoteActivity.this);
                //myTask.execute(id, String.valueOf(votedParagraph));

                //GO TO QUICK GAME VOTED
                //Intent i = new Intent(QuickVoteActivity.this, QuickVotedActivity.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                //i.putExtra("code",code);
                //i.putExtra("mode",mode);
                //startActivity(i);

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

    // ASYNC TASK RESUME VOTE QUICK GAME
    public void setupAdapter(AsyncTaskResumeQuickVote.Result resultado)
    {
        if (resultado.result!=null && resultado.result.equals("success")){
            fillParagraphs(resultado.paragraphs);
        }
    }

    //ASYNC TASK ADAPTER VOTE QUICK
    public void setupAdapter(AsyncTaskVoteQuick.Result resultado) {

        if (resultado.result!=null && resultado.result.equals("success")){

            //Intent i = new Intent(QuickVoteActivity.this, QuickVotedActivity.class);
            //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            //startActivity(i);
            //finish();
        }

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
                Log.d("VOTED PARAGRAPH", votedParagraph.toString());
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
