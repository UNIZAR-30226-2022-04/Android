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

import com.airbnb.lottie.LottieAnimationView;

import es.unizar.eina.frankenstory.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUserName;
    private EditText mPassword;
    private EditText mRepeatPassword;
    private LottieAnimationView mchargeAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_register);

        // MODE NIGHT OFF
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // BACKGROUND ANIMATION
        ConstraintLayout constraintLayout = findViewById(R.id.layoutmain);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        mUserName = (EditText) findViewById(R.id.usernameRegister);
        mPassword = (EditText) findViewById(R.id.passwordRegister);
        mRepeatPassword = (EditText) findViewById(R.id.secondpasswordRegister);
        mchargeAnimation = (LottieAnimationView) findViewById(R.id.chargeRegister);

        // ON CLICK BUTTON CREAR CUENTA
        Button button = (Button)findViewById(R.id.createAccountButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // LEER EDIT TEXTS
                String username = mUserName.getText().toString();
                String password = mPassword.getText().toString();
                String repeatpassword = mRepeatPassword.getText().toString();
                boolean camposLlenos = true;
                if (username.equalsIgnoreCase("")){
                    mUserName.setError("Introduce un nombre de usuario");
                    camposLlenos = false;
                }
                if (password.equalsIgnoreCase("")){
                    mPassword.setError("Introduce una contraseña");
                    camposLlenos = false;
                }
                if (!repeatpassword.equals(password)){
                    mRepeatPassword.setError("La contraseña no es la misma");
                    camposLlenos = false;
                }
                // LLAMAR A LA TAREA ASINCRONA
                if (camposLlenos){
                    AsyncTaskRegister myTask = new AsyncTaskRegister(RegisterActivity.this);
                    myTask.execute(username, password);
                    // INFORMAR CARGANDO
                    mchargeAnimation.setVisibility(View.VISIBLE);
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
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            );
        }
    }

    public void setupAdapter(boolean registradoCorrectamente, String error)
    {
        mchargeAnimation.setVisibility(View.INVISIBLE);
        if (registradoCorrectamente) {
            // It's closed -> LogInActivity where it was called
            finish();
        } else if (error.equals("user_already_registered")){
            // SHOW ERROR
            mUserName.setError("El nombre no está disponible");
        }
    }

}
