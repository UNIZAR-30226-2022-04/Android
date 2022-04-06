package es.unizar.eina.frankenstory.general;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncTaskChangePassw extends AsyncTask<String, Void, AsyncTaskChangePassw.Result> {

    private SettingsActivity mActivity = null;

    static class Result {
        String result;
        String reason;
    }

    public AsyncTaskChangePassw(SettingsActivity activity)
    {
        mActivity = activity;
    }

    protected Result doInBackground(String... params) {
        String username = params[0];
        String password = params[1];
        String newPassw = params[2];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/change_password").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\""+username+"\",\"password\":\""+password+"\""+newPassw+"\"}";
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, Result.class);

        } catch (IOException e) {
            Log.e("ERROR_AsyncTaskLogin",e.getMessage());
        }

        return new Result();
    }

    protected void onPostExecute(Result resultado) {
        if (resultado.result != null) {
            mActivity.setupAdapter(resultado.result.equals("success"), resultado.reason);
        }else {
           mActivity.setupAdapter(false, "error");
        }
    }
}
