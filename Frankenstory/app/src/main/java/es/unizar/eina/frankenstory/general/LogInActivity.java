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
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;

public class LogInActivity extends AppCompatActivity {

    private EditText mUserName;
    private EditText mPassword;
    private LottieAnimationView mchargeAnimation;
    private Button button;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_log_in);

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // BACKGROUND ANIMATION
        ConstraintLayout constraintLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        mUserName = (EditText) findViewById(R.id.usernameLogIn);
        mPassword = (EditText) findViewById(R.id.passwordLogIn);
        mchargeAnimation = (LottieAnimationView) findViewById(R.id.charge);

        // ON CLICK BUTTON "INICIAR SESIÓN"
        button = (Button)findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // LEER EDIT TEXTS
                username = mUserName.getText().toString();
                password = mPassword.getText().toString();
                boolean camposLlenos = true;
                if (username.equalsIgnoreCase("")){
                    mUserName.setError("Introduce un nombre de usuario");
                    camposLlenos = false;
                }
                if (username.length()>=30){
                    mUserName.setError("El nombre es demasiado largo");
                    camposLlenos = false;
                }
                if (password.equalsIgnoreCase("")){
                    mPassword.setError("Introduce una contraseña");
                    camposLlenos = false;
                }
                if (password.length()>=30){
                    mPassword.setError("La contraseña es demasiado larga");
                    camposLlenos = false;
                }

                // LLAMAR A LA TAREA ASINCRONA
                if (camposLlenos){
                    // SEND GET SALT
                    AsyncTaskGetSalt myTask = new AsyncTaskGetSalt(LogInActivity.this);
                    myTask.execute(username);
                    // INFORMAR CARGANDO
                    //Toast.makeText(LogInActivity.this, "Preguntando a la web", Toast.LENGTH_LONG).show();
                    mchargeAnimation.setVisibility(View.VISIBLE);
                    button.setClickable(false);
                }
            }
        });

        // ON CLICK BUTTON "CREAR CUENTA"
        Button buttonCreate = (Button)findViewById(R.id.createAccountLogIn);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // CREAR CUENTA
                Intent i = new Intent(LogInActivity.this, RegisterActivity.class);
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

    // ASYNC TASK ADAPTER LOGIN
    public void setupAdapter(AsyncTaskLogIn.Result resultado)
    {
        mchargeAnimation.setVisibility(View.INVISIBLE);
        button.setClickable(true);
        if (resultado.result != null && resultado.result.equals("success")) {
            // RECEIVED SUCCESS -> START
            Intent i = new Intent(LogInActivity.this, MainMenuActivity.class);
            // Set global variables username and password
            ((MyApplication) this.getApplication()).setUsername(username);
            ((MyApplication) this.getApplication()).setPassword(password);
            startActivity(i);
            finish();
        } else if (resultado.result != null && resultado.reason.equals("user_not_found")) {
            // RECEIVED ERROR USERNAME -> SHOW ERROR
            mUserName.setError("Usuario no encontrado");
        } else if (resultado.result != null && resultado.reason.equals("wrong_password")) {
            // RECEIVED ERROR PASSWD -> SHOW ERROR
            mPassword.setError("Contraseña incorrecta");
        } else {
            // COULDN'T CONNECT
            mUserName.setError("ERROR EN EL LOGIN");
        }
    }

    public static String getSHA512(String input){

        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(input.getBytes("utf8"));
            toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toReturn;
    }

    // ASYNC TASK ADAPTER GET SALT
    public void setupAdapter(AsyncTaskGetSalt.Result resultado)
    {
        if (resultado.result != null && resultado.result.equals("success")) {
            // GET HASH
            String salt = resultado.salt;
            password = getSHA512(password + salt);
            // SEND LOGIN
            AsyncTaskLogIn myTask = new AsyncTaskLogIn(LogInActivity.this);
            myTask.execute(username, password);
        } else {
            // COULDN'T CONNECT
            mUserName.setError("ERROR EN EL SALT");
            mchargeAnimation.setVisibility(View.VISIBLE);
            button.setClickable(false);
        }
    }
}
