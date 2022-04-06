package es.unizar.eina.frankenstory.general;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import es.unizar.eina.frankenstory.R;

public class SettingsActivity extends AppCompatActivity {
    private TextView mUsername;
    private TextView mStars;
    private TextView mCoins;
    private Button mNotifications;
    private Button mChangePassw;
    private Button mCloseSession;
    private EditText mNewPassw;
    private EditText mNewPassw2;
    String username;
    String password;
    String stars;
    String coins;
    String notifications;
    String newPassw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_settings);

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
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        stars = intent.getStringExtra("stars");
        coins = intent.getStringExtra("coins");
        notifications = intent.getStringExtra("notifications");

        // GET VIEWS AND SET DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mNotifications = (Button) findViewById(R.id.notifications);
        mChangePassw = (Button) findViewById(R.id.change_pass);
        mCloseSession = (Button) findViewById(R.id.close_session);
        mUsername.setText(username);
        mStars.setText(stars);
        mCoins.setText(coins);
        mNewPassw = (EditText) findViewById(R.id.passwordRegister);
        mNewPassw2 = (EditText) findViewById(R.id.secondpasswordRegister);
        if (Integer.parseInt(notifications)>0){
            mNotifications.setText(notifications);
        } else mNotifications.setVisibility(View.INVISIBLE);

        // BUTTONS FROM TOP AND BOTTOM
        setNavegavilidad();

        // ON CLICK BUTTON "CAMBIAR CONTRASEÑA"
        mChangePassw.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // LEER EDIT TEXTS
                newPassw = mNewPassw.getText().toString();
                String newPassw2 = mNewPassw2.getText().toString();
                boolean camposLlenos = true;
                if (newPassw.equalsIgnoreCase("")){
                    mNewPassw.setError("Introduce una contraseña");
                    camposLlenos = false;
                }
                if (newPassw.length()>=30){
                    mNewPassw.setError("La contraseña es demasiado larga");
                    camposLlenos = false;
                }
                if (!newPassw.equals(newPassw2)){
                    mNewPassw2.setError("Las contraseñas deben ser iguales");
                    camposLlenos = false;
                }

                // LLAMAR A LA TAREA ASINCRONA
                if (camposLlenos){
                    AsyncTaskChangePassw myTask = new AsyncTaskChangePassw(SettingsActivity.this);
                    myTask.execute(username, password, newPassw);
                    mChangePassw.setClickable(false);
                }
            }
        });

        // ON CLICK BUTTON "CERRAR SESIÓN"
        mCloseSession.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // ENVIAR A ACTIVITY LOGIN
                Intent i = new Intent(SettingsActivity.this, LogInActivity.class);
                startActivity(i);
            }
        });

    }

    public void setNavegavilidad(){
        // BUTTON TO MainMenuActivity
        Button buttonHome = (Button)findViewById(R.id.home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, MainMenuActivity.class);
                i.putExtra("username",username);
                i.putExtra("password",password);
                i.putExtra("stars", stars);
                i.putExtra("coins", coins);
                i.putExtra("notifications", notifications);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        // BUTTON TO FriendsActivity
        Button buttonFriends = (Button)findViewById(R.id.friends);
        buttonFriends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, FriendsActivity.class);
                i.putExtra("username",username);
                i.putExtra("password",password);
                i.putExtra("stars", stars);
                i.putExtra("coins", coins);
                i.putExtra("notifications", notifications);
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
    public void setupAdapter(AsyncTaskChangePassw.Result resultado)
    {
        mChangePassw.setClickable(true);
        if (resultado.result!=null && !resultado.result.equals("success")) {
            mChangePassw.setError("ERROR CAMBIANDO CONTRASEÑA");
        }
    }

}
