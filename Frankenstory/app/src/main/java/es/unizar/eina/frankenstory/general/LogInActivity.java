package es.unizar.eina.frankenstory.general;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.unizar.eina.frankenstory.R;

public class LogInActivity extends AppCompatActivity {

    private EditText mUserName;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_log_in);

        mUserName = (EditText) findViewById(R.id.usernameLogIn);
        mPassword = (EditText) findViewById(R.id.passwordLogIn);

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
                    Toast.makeText(LogInActivity.this, "Preguntando a la web", Toast.LENGTH_LONG).show();
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

    // ASYNC TASK ADAPTER
    public void setupAdapter()
    {
        Intent i = new Intent(LogInActivity.this, MainMenuActivity.class);
        startActivity(i);
    }

}
