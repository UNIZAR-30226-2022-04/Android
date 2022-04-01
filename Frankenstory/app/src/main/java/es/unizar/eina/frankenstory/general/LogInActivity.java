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

import es.unizar.eina.frankenstory.R;

public class LogInActivity extends AppCompatActivity {

    private EditText mUserName;
    private EditText mPassword;
    private LottieAnimationView mchargeAnimation;

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
        Button button = (Button)findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // LEER EDIT TEXTS
                String username = mUserName.getText().toString();
                String password = mPassword.getText().toString();
                boolean camposLlenos = true;
                if (username.equalsIgnoreCase("")){
                    mUserName.setError("Introduce un nombre de usuario");
                    camposLlenos = false;
                }
                if (password.equalsIgnoreCase("")){
                    mPassword.setError("Introduce una contraseña");
                    camposLlenos = false;
                }

                // LLAMAR A LA TAREA ASINCRONA
                if (camposLlenos){
                    AsyncTaskLogIn myTask = new AsyncTaskLogIn(LogInActivity.this);
                    myTask.execute(username, password);
                    // INFORMAR CARGANDO
                    //Toast.makeText(LogInActivity.this, "Preguntando a la web", Toast.LENGTH_LONG).show();
                    mchargeAnimation.setVisibility(View.VISIBLE);
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

    // ASYNC TASK ADAPTER
    public void setupAdapter(boolean loggeadoCorrectamente, String error)
    {
        mchargeAnimation.setVisibility(View.INVISIBLE);
        if (loggeadoCorrectamente) {
            // START
            Intent i = new Intent(LogInActivity.this, MainMenuActivity.class);
            startActivity(i);
        } else {
            // SHOW ERROR
            //Toast.makeText(this, "ERROR:"+error, Toast.LENGTH_LONG).show();
            if (error.equals("user_not_found")){
                mUserName.setError("Usuario no encontrado");
            } else if (error.equals("wrong_password")){
                mPassword.setError("Contraseña incorrecta");
            }
        }
    }

}
