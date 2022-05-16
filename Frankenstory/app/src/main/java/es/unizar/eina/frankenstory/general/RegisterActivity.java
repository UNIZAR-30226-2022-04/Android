package es.unizar.eina.frankenstory.general;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.airbnb.lottie.LottieAnimationView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import es.unizar.eina.frankenstory.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUserName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mRepeatPassword;
    private LottieAnimationView mchargeAnimation;
    private Button button;
    private String salt;

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
        mEmail = (EditText) findViewById(R.id.emailRegister);
        mPassword = (EditText) findViewById(R.id.passwordRegister);
        mRepeatPassword = (EditText) findViewById(R.id.secondpasswordRegister);
        mchargeAnimation = (LottieAnimationView) findViewById(R.id.chargeRegister);

        // ON CLICK BUTTON CREAR CUENTA
        button = (Button)findViewById(R.id.createAccountButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // LEER EDIT TEXTS
                String username = mUserName.getText().toString();
                String password = mPassword.getText().toString();
                String email = mEmail.getText().toString();
                String repeatpassword = mRepeatPassword.getText().toString();
                boolean camposLlenos = true;
                if (username.equalsIgnoreCase("")){
                    mUserName.setError("Introduce un nombre de usuario");
                    camposLlenos = false;
                }
                if (username.length()>=30){
                    mUserName.setError("El nombre es demasiado largo");
                    camposLlenos = false;
                }
                if (email.equalsIgnoreCase("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmail.setError("Introduce un email válido");
                    camposLlenos = false;
                }
                if (email.length()>=40){
                    mEmail.setError("El email es demasiado largo");
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
                if (!repeatpassword.equals(password)){
                    mRepeatPassword.setError("La contraseña no es la misma");
                    camposLlenos = false;
                }
                // LLAMAR A LA TAREA ASINCRONA
                if (camposLlenos){
                    // GENERATE SALT
                    salt = getSalt();
                    // HASH
                    //password = getSecurePassword(password, salt);
                    password = getSHA512(password + salt);
                    // SEND
                    AsyncTaskRegister myTask = new AsyncTaskRegister(RegisterActivity.this);
                    myTask.execute(username, password, email, salt);
                    // INFORMAR CARGANDO
                    mchargeAnimation.setVisibility(View.VISIBLE);
                    button.setClickable(false);
                }
            }
        });

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

    private static String getSalt() {
        try {
            // Always use a SecureRandom generator
            SecureRandom sr = new SecureRandom();

            // Create array for salt
            byte[] salt = new byte[16];

            // Get a random salt
            sr.nextBytes(salt);

            // return salt
            return salt.toString();
        } /*catch (NoSuchAlgorithmException e){
            Log.e("ERROR SALT", e.getMessage());
        } catch (NoSuchProviderException e){
            Log.e("ERROR SALT", e.getMessage());
        }*/ catch (Exception e){
            Log.e("ERROR SALT", e.getMessage());
        }
        return "";
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

    public void setupAdapter(AsyncTaskRegister.ResultRegister resultado)
    {
        mchargeAnimation.setVisibility(View.INVISIBLE);
        button.setClickable(true);
        if (resultado.result != null && resultado.result.equals("success")) {
            // RECEIVED SUCCESS -> return LogInActivity where it was called
            finish();
        } else if (resultado.result != null && resultado.reason.equals("email_already_registered")){
            // RECEIVED ERROR EMAIL -> SHOW ERROR
            mEmail.setError("Ya estás registrado con este email");
        } else if (resultado.result != null && resultado.reason.equals("user_already_registered")){
            // RECEIVED ERROR USERNAME -> SHOW ERROR
            mUserName.setError("El nombre no está disponible");
        } else {
            // COULDN'T CONNECT
            mUserName.setError("ERROR EN EL REGISTRO");
        }
    }

}
