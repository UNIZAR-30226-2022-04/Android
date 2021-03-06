package es.unizar.eina.frankenstory.story;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;
import es.unizar.eina.frankenstory.general.FriendsActivity;
import es.unizar.eina.frankenstory.general.LibraryActivity;
import es.unizar.eina.frankenstory.general.MainMenuActivity;
import es.unizar.eina.frankenstory.general.SettingsActivity;

public class StoryActivity extends AppCompatActivity {

    private TextView mUsername;
    private TextView mStars;
    private TextView mCoins;
    private Button mNotifications;
    private ListView mList;
    private ImageView mIconUser;
    private ListView mListMyGames;
    private ListView mListFriendsGames;
    private ListView mPublicGames;
    private ListView mVotingGames;
    private ImageButton mJoinGame;
    private ImageButton mVoteGame;
    private ImageView mButtonCreateStory;
    private TextView mNoMyGames;
    private TextView mNoFriendsGames;
    private TextView mNoPublicGames;
    private TextView mNoVoteGames;

    private Integer num_OwnTales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // GET VIEWS AND UPDATE DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mNotifications = (Button) findViewById(R.id.notifications);
        mList = (ListView) findViewById(R.id.statistics);
        mIconUser = (ImageView) findViewById(R.id.iconUser);
        mListMyGames = (ListView) findViewById(R.id.my_stories);
        mListFriendsGames = (ListView) findViewById(R.id.friends_games);
        mPublicGames = (ListView) findViewById(R.id.public_games);
        mVotingGames = (ListView) findViewById(R.id.VoteGames);
        mJoinGame = (ImageButton) findViewById(R.id.joinGame);
        mVoteGame = (ImageButton) findViewById(R.id.vote);
        mButtonCreateStory = (ImageView)findViewById(R.id.create_story);
        mNoMyGames = (TextView) findViewById(R.id.noMyGames);
        mNoFriendsGames = (TextView) findViewById(R.id.noFriendsGames);
        mNoPublicGames = (TextView) findViewById(R.id.noPublicGames);
        mNoVoteGames = (TextView) findViewById(R.id.noVoteGames);
        updateData();

        // BUTTONS FROM TOP AND BOTTOM
        setNavegavilidad();

        // BACKGROUND ANIMATION
        RelativeLayout relativeLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        /*
        // BUTTON ANIMATION
        AnimationDrawable animationButton = (AnimationDrawable) mButtonCreateStory.getBackground();
        animationButton.setEnterFadeDuration(2000);
        animationButton.setExitFadeDuration(2000);
        animationButton.start();*/
        mButtonCreateStory.setClipToOutline(true);

        // CALL ASYNC TASK
        AsyncTaskStories myTask = new AsyncTaskStories(this);
        myTask.execute();

        // OPEN LISTS
        /*
        TextView mispartidas = (TextView) findViewById(R.id.mispartidas);
        mispartidas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LinearLayout content = (LinearLayout) findViewById(R.id.mispartidasContent);
                if (content.getVisibility()==View.VISIBLE) {
                    content.setVisibility(View.GONE);
                    mispartidas.setText(getText(R.string.my_games_closed));
                }
                else {
                    content.setVisibility(View.VISIBLE);
                    mispartidas.setText(getText(R.string.my_games_opened));
                }
            }
        });

        TextView partidasamigos = (TextView) findViewById(R.id.partidasamigos);
        partidasamigos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LinearLayout content = (LinearLayout) findViewById(R.id.partidasamigosContent);
                if (content.getVisibility()==View.VISIBLE) {
                    content.setVisibility(View.GONE);
                    partidasamigos.setText(getText(R.string.friend_games_closed));
                }
                else {
                    content.setVisibility(View.VISIBLE);
                    partidasamigos.setText(getText(R.string.friend_games_opened));
                }
            }
        });

        TextView partidaspublicas = (TextView) findViewById(R.id.partidaspublicas);
        partidaspublicas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LinearLayout content = (LinearLayout) findViewById(R.id.partidaspublicasContent);
                if (content.getVisibility()==View.VISIBLE) {
                    content.setVisibility(View.GONE);
                    partidaspublicas.setText(getText(R.string.public_games_closed));
                }
                else {
                    content.setVisibility(View.VISIBLE);
                    partidaspublicas.setText(getText(R.string.public_games_opened));
                }
            }
        });

        TextView partidasvotar = (TextView) findViewById(R.id.partidasvotar);
        partidasvotar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LinearLayout content = (LinearLayout) findViewById(R.id.partidasvotarContent);
                if (content.getVisibility()==View.VISIBLE) {
                    content.setVisibility(View.GONE);
                    partidasvotar.setText(getText(R.string.no_voted_closed));
                }
                else {
                    content.setVisibility(View.VISIBLE);
                    partidasvotar.setText(getText(R.string.no_voted_opened));
                }
            }
        });*/
        LinearLayout botonesOpcion1 = (LinearLayout) findViewById(R.id.botonesOpcion1);
        LinearLayout botonesOpcion2 = (LinearLayout) findViewById(R.id.botonesOpcion2);
        LinearLayout ventanaMisPartidas = (LinearLayout) findViewById(R.id.mispartidasContent);
        LinearLayout ventanaPartidasAmigos = (LinearLayout) findViewById(R.id.partidasamigosContent);
        LinearLayout ventanaPartidasPublicas = (LinearLayout) findViewById(R.id.partidaspublicasContent);
        LinearLayout ventanaPartidasVotacion = (LinearLayout) findViewById(R.id.partidasvotarContent);
        // Opci??n 1
        Button buttonMisPartidas = (Button) findViewById(R.id.buttonMisPartidas);
        Button buttonPartidasAmigos = (Button) findViewById(R.id.buttonPartidasAmigos);
        Button buttonPartidasPublicas = (Button) findViewById(R.id.buttonPartidasPublicas);
        Button buttonPartidasEnVotacion = (Button) findViewById(R.id.buttonPartidasEnVotacion);
        buttonMisPartidas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ventanaMisPartidas.setVisibility(View.VISIBLE);
                ventanaPartidasAmigos.setVisibility(View.GONE);
                ventanaPartidasPublicas.setVisibility(View.GONE);
                ventanaPartidasVotacion.setVisibility(View.GONE);
            }
        });
        buttonPartidasAmigos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ventanaMisPartidas.setVisibility(View.GONE);
                ventanaPartidasAmigos.setVisibility(View.VISIBLE);
                ventanaPartidasPublicas.setVisibility(View.GONE);
                ventanaPartidasVotacion.setVisibility(View.GONE);
            }
        });
        buttonPartidasPublicas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ventanaMisPartidas.setVisibility(View.GONE);
                ventanaPartidasAmigos.setVisibility(View.GONE);
                ventanaPartidasPublicas.setVisibility(View.VISIBLE);
                ventanaPartidasVotacion.setVisibility(View.GONE);
                botonesOpcion1.setVisibility(View.GONE);
                botonesOpcion2.setVisibility(View.VISIBLE);
            }
        });
        buttonPartidasEnVotacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ventanaMisPartidas.setVisibility(View.GONE);
                ventanaPartidasAmigos.setVisibility(View.GONE);
                ventanaPartidasPublicas.setVisibility(View.GONE);
                ventanaPartidasVotacion.setVisibility(View.VISIBLE);
                botonesOpcion1.setVisibility(View.GONE);
                botonesOpcion2.setVisibility(View.VISIBLE);
            }
        });
        // Opci??n 2
        Button buttonMisPartidas2 = (Button) findViewById(R.id.buttonMisPartidas2);
        Button buttonPartidasAmigos2 = (Button) findViewById(R.id.buttonPartidasAmigos2);
        Button buttonPartidasPublicas2 = (Button) findViewById(R.id.buttonPartidasPublicas2);
        Button buttonPartidasEnVotacion2 = (Button) findViewById(R.id.buttonPartidasEnVotacion2);
        buttonMisPartidas2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ventanaMisPartidas.setVisibility(View.VISIBLE);
                ventanaPartidasAmigos.setVisibility(View.GONE);
                ventanaPartidasPublicas.setVisibility(View.GONE);
                ventanaPartidasVotacion.setVisibility(View.GONE);
                botonesOpcion1.setVisibility(View.VISIBLE);
                botonesOpcion2.setVisibility(View.GONE);
            }
        });
        buttonPartidasAmigos2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ventanaMisPartidas.setVisibility(View.GONE);
                ventanaPartidasAmigos.setVisibility(View.VISIBLE);
                ventanaPartidasPublicas.setVisibility(View.GONE);
                ventanaPartidasVotacion.setVisibility(View.GONE);
                botonesOpcion1.setVisibility(View.VISIBLE);
                botonesOpcion2.setVisibility(View.GONE);
            }
        });
        buttonPartidasPublicas2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ventanaMisPartidas.setVisibility(View.GONE);
                ventanaPartidasAmigos.setVisibility(View.GONE);
                ventanaPartidasPublicas.setVisibility(View.VISIBLE);
                ventanaPartidasVotacion.setVisibility(View.GONE);
            }
        });
        buttonPartidasEnVotacion2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ventanaMisPartidas.setVisibility(View.GONE);
                ventanaPartidasAmigos.setVisibility(View.GONE);
                ventanaPartidasPublicas.setVisibility(View.GONE);
                ventanaPartidasVotacion.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // CALL ASYNC TASK
        AsyncTaskStories myTask = new AsyncTaskStories(this);
        myTask.execute();
        updateData();

        // IMAGEN ALEATORIA
        int min = 0;
        int max = 3;
        int random = new Random().nextInt((max - min) + 1) + min;
        switch (random){
            case 1:
                mButtonCreateStory.setImageResource(R.drawable.caperucita_roja);
                break;
            case 2:
                mButtonCreateStory.setImageResource(R.drawable.los_tres_cerditos);
                break;
            case 3:
                mButtonCreateStory.setImageResource(R.drawable.alicia_en_el_pais_maravillas);
                break;
            default:
                mButtonCreateStory.setImageResource(R.drawable.caperucita_roja);
        }
    }

    // UPDATE DATA
    public void updateData(){
        mUsername.setText(((MyApplication) this.getApplication()).getUsername());
        mStars.setText(((MyApplication) this.getApplication()).getStars());
        mCoins.setText(((MyApplication) this.getApplication()).getCoins());
        chooseIconUser(mIconUser, ((MyApplication) this.getApplication()).getIconUser());
        if (Integer.parseInt(((MyApplication) this.getApplication()).getNotifications())>0){
            mNotifications.setText(((MyApplication) this.getApplication()).getNotifications());
        } else mNotifications.setVisibility(View.INVISIBLE);
    }

    public void setNavegavilidad(){
        // BUTTON TO SettingsActivity
        ImageButton buttonSettings = (ImageButton)findViewById(R.id.configbutton);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(StoryActivity.this, SettingsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO FriendsActivity
        Button buttonFriends = (Button)findViewById(R.id.friends);
        buttonFriends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(StoryActivity.this, FriendsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO MainMenuActivity
        Button buttonHome = (Button)findViewById(R.id.home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(StoryActivity.this, MainMenuActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO LibraryActivity
        Button Buttomlibrary = (Button) findViewById(R.id.your_stories);
        Buttomlibrary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(StoryActivity.this, LibraryActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO CreateStoryActivity

        mButtonCreateStory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (num_OwnTales < 3) {
                    Intent i = new Intent(StoryActivity.this, CreateStoryActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                } else {
                    Toast.makeText(StoryActivity.this,"Has alcanzado el limite de historias (3)", Toast.LENGTH_LONG).show();
                }
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

    // ASYNC TASK STORIES ADAPTER
    public void setupAdapter(AsyncTaskStories.Result resultado)
    {
        if (resultado.result!=null && resultado.result.equals("success")){
            num_OwnTales = resultado.myTales.size();
            fillDataMyGames(resultado.myTales);
            fillDataFriendGames(resultado.friendTales);
            fillDataPublicGames(resultado.publicTales);
            fillDataVoteGames(resultado.talesForVote);
        }
    }

   private void fillDataMyGames(List<AsyncTaskStories.Story> stories) {
        // instantiate the custom list adapter
        ListMyStoriesAdapter adapter = new ListMyStoriesAdapter(this, stories);

        // get the ListView and attach the adapter
        mListMyGames.setAdapter(adapter);

        // Message no my games
        if (stories.isEmpty()) mNoMyGames.setVisibility(View.VISIBLE);
        else mNoMyGames.setVisibility(View.GONE);
    }

    private void fillDataFriendGames(List<AsyncTaskStories.Story> stories) {
        // instantiate the custom list adapter
        ListOtherStoriesAdapter adapter = new ListOtherStoriesAdapter(this, stories);

        // get the ListView and attach the adapter
        mListFriendsGames.setAdapter(adapter);

        // Message no friends games
        if (stories.isEmpty()) mNoFriendsGames.setVisibility(View.VISIBLE);
        else mNoFriendsGames.setVisibility(View.GONE);
    }

    private void fillDataPublicGames(List<AsyncTaskStories.Story> stories) {
        // instantiate the custom list adapter
        ListOtherStoriesAdapter adapter = new ListOtherStoriesAdapter(this, stories);

        // get the ListView and attach the adapter
        mPublicGames.setAdapter(adapter);

        // Message no public games
        if (stories.isEmpty()) mNoPublicGames.setVisibility(View.VISIBLE);
        else mNoPublicGames.setVisibility(View.GONE);
    }

    private void fillDataVoteGames(List<AsyncTaskStories.Story> stories) {
        // instantiate the custom list adapter
        ListVoteStoriesAdapter adapter = new ListVoteStoriesAdapter(this, stories);

        // get the ListView and attach the adapter
        mVotingGames.setAdapter(adapter);

        // Message no vote games
        if (stories.isEmpty()) mNoVoteGames.setVisibility(View.VISIBLE);
        else mNoVoteGames.setVisibility(View.GONE);
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