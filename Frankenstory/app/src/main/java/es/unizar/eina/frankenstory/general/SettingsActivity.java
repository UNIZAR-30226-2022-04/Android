package es.unizar.eina.frankenstory.general;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import es.unizar.eina.frankenstory.R;

public class SettingsActivity extends AppCompatActivity {
    private TextView mUsername;
    private TextView mStars;
    private TextView mCoins;
    private Button mNotifications;
    private ImageView mIconUser;
    private Button mChangePassw;
    private Button mCloseSession;
    private Button mDeleteAccount;
    private EditText mNewPassw;
    private EditText mNewPassw2;
    private ImageButton mIcon0; private ImageButton mIcon1;
    private ImageButton mIcon2; private ImageButton mIcon3;
    private ImageButton mIcon4; private ImageButton mIcon5;
    private ImageButton mIcon6; private ImageButton mIcon7;
    private ImageButton mIcon8; private ImageButton mIcon9;
    private Button mchangeIcon;
    String username;
    String password;
    String stars;
    String coins;
    String notifications;
    String iconUser;
    String newPassw;
    Integer iconSelected;

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
        iconUser = intent.getStringExtra("iconUser");
        iconSelected = Integer.parseInt(iconUser);

        // GET VIEWS AND SET DATA
        mUsername = (TextView) findViewById(R.id.usernameTop);
        mStars = (TextView) findViewById(R.id.starsTop);
        mCoins = (TextView) findViewById(R.id.coinsTop);
        mNotifications = (Button) findViewById(R.id.notifications);
        mIconUser = (ImageView) findViewById(R.id.iconUser);
        mChangePassw = (Button) findViewById(R.id.change_pass);
        mCloseSession = (Button) findViewById(R.id.close_session);
        mDeleteAccount = (Button) findViewById(R.id.eliminate_acc);
        mIcon0 = (ImageButton) findViewById(R.id.icon0); mIcon1 = (ImageButton) findViewById(R.id.icon1);
        mIcon2 = (ImageButton) findViewById(R.id.icon2); mIcon3 = (ImageButton) findViewById(R.id.icon3);
        mIcon4 = (ImageButton) findViewById(R.id.icon4); mIcon5 = (ImageButton) findViewById(R.id.icon5);
        mIcon6 = (ImageButton) findViewById(R.id.icon6); mIcon7 = (ImageButton) findViewById(R.id.icon7);
        mIcon8 = (ImageButton) findViewById(R.id.icon8); mIcon9 = (ImageButton) findViewById(R.id.icon9);
        mchangeIcon = (Button) findViewById(R.id.change_icon);
        mUsername.setText(username);
        mStars.setText(stars);
        mCoins.setText(coins);
        chooseIconUser(mIconUser, iconUser);
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
                if (newPassw.equalsIgnoreCase("")) {
                    mNewPassw.setError("Introduce una contraseña");
                    camposLlenos = false;
                }
                if (newPassw.length() >= 30) {
                    mNewPassw.setError("La contraseña es demasiado larga");
                    camposLlenos = false;
                }
                if (!newPassw.equals(newPassw2)) {
                    mNewPassw2.setError("Las contraseñas deben ser iguales");
                    camposLlenos = false;
                }

                // LLAMAR A LA TAREA ASINCRONA
                if (camposLlenos) {
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
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); //TO DELETE THE OTHER ACTIVITIES
                startActivity(i);
            }
        });

        // ON CLICK BUTTON "ELIMINAR CUENTA"
        mDeleteAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // LLAMAR A LA TAREA ASINCRONA
                AsyncTaskDeleterUser myTask = new AsyncTaskDeleterUser(SettingsActivity.this);
                myTask.execute(username, password);
                mDeleteAccount.setClickable(false);
            }
        });

        // ON CLICK BUTTONS IMAGES
        onClickImages();

        // ON CLICK BUTTON "CAMBIAR ICONO"
        mchangeIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // LLAMAR A LA TAREA ASINCRONA
                AsyncTaskChangeIcon myTask = new AsyncTaskChangeIcon(SettingsActivity.this);
                myTask.execute(username, password, iconSelected.toString());
                mchangeIcon.setClickable(false);
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
                i.putExtra("iconUser", iconUser);
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
                i.putExtra("iconUser", iconUser);
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

    // ASYNC TASK ADAPTER CHANGE PASSWORD
    public void setupAdapter(AsyncTaskChangePassw.Result resultado)
    {
        mChangePassw.setClickable(true);
        if (resultado.result!=null && !resultado.result.equals("success")) {
            mChangePassw.setError("ERROR CAMBIANDO CONTRASEÑA");
        }
    }

    // ASYNC TASK ADAPTER DELETE ACCOUNT
    public void setupAdapter(AsyncTaskDeleterUser.Result resultado)
    {
        mDeleteAccount.setClickable(true);
        if (resultado.result!=null && !resultado.result.equals("success")) {
            Toast.makeText(getApplicationContext(),"ERROR ELIMINANDO CUENTA",Toast.LENGTH_SHORT).show();
        } else {
             Intent i = new Intent(SettingsActivity.this, LogInActivity.class);
             i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); //TO DELETE THE OTHER ACTIVITIES
             startActivity(i);
        }

    }

    // ASYNC TASK ADAPTER CHANGE ICON
    public void setupAdapter(AsyncTaskChangeIcon.Result resultado)
    {
        mchangeIcon.setClickable(true);
        if (resultado.result!=null && !resultado.result.equals("success")) {
            mchangeIcon.setError("ERROR CAMBIANDO EL ICONO");
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

    // ON CLICK BUTTONS IMAGES
    public void onClickImages(){
        mIcon0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setIconTransparent(iconSelected);
                iconSelected = 0;
                mIcon0.setBackgroundColor(getResources().getColor(R.color.verde_top));
            }
        });
        mIcon1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setIconTransparent(iconSelected);
                iconSelected = 1;
                mIcon1.setBackgroundColor(getResources().getColor(R.color.verde_top));
            }
        });
        mIcon2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setIconTransparent(iconSelected);
                iconSelected = 2;
                mIcon2.setBackgroundColor(getResources().getColor(R.color.verde_top));
            }
        });
        mIcon3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setIconTransparent(iconSelected);
                iconSelected = 3;
                mIcon3.setBackgroundColor(getResources().getColor(R.color.verde_top));
            }
        });
        mIcon4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setIconTransparent(iconSelected);
                iconSelected = 4;
                mIcon4.setBackgroundColor(getResources().getColor(R.color.verde_top));
            }
        });
        mIcon5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setIconTransparent(iconSelected);
                iconSelected = 5;
                mIcon5.setBackgroundColor(getResources().getColor(R.color.verde_top));
            }
        });
        mIcon6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setIconTransparent(iconSelected);
                iconSelected = 6;
                mIcon6.setBackgroundColor(getResources().getColor(R.color.verde_top));
            }
        });
        mIcon7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setIconTransparent(iconSelected);
                iconSelected = 7;
                mIcon7.setBackgroundColor(getResources().getColor(R.color.verde_top));
            }
        });
        mIcon8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setIconTransparent(iconSelected);
                iconSelected = 8;
                mIcon8.setBackgroundColor(getResources().getColor(R.color.verde_top));
            }
        });
        mIcon9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setIconTransparent(iconSelected);
                iconSelected = 9;
                mIcon9.setBackgroundColor(getResources().getColor(R.color.verde_top));
            }
        });
    }

    // Sets color transparend
    public void setIconTransparent(Integer picture){
        if (picture==0) mIcon0.setBackgroundColor(Color.TRANSPARENT);
        else if (picture==1) mIcon1.setBackgroundColor(Color.TRANSPARENT);
        else if (picture==2) mIcon2.setBackgroundColor(Color.TRANSPARENT);
        else if (picture==3) mIcon3.setBackgroundColor(Color.TRANSPARENT);
        else if (picture==4) mIcon4.setBackgroundColor(Color.TRANSPARENT);
        else if (picture==5) mIcon5.setBackgroundColor(Color.TRANSPARENT);
        else if (picture==6) mIcon6.setBackgroundColor(Color.TRANSPARENT);
        else if (picture==7) mIcon7.setBackgroundColor(Color.TRANSPARENT);
        else if (picture==8) mIcon8.setBackgroundColor(Color.TRANSPARENT);
        else if (picture==9) mIcon9.setBackgroundColor(Color.TRANSPARENT);
    }

}
