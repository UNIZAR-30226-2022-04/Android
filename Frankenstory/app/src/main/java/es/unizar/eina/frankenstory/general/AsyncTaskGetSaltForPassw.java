package es.unizar.eina.frankenstory.general;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncTaskGetSaltForPassw extends AsyncTask<String, Void, AsyncTaskGetSaltForPassw.Result> {

    private SettingsActivity mActivity = null;

    static class Result {
        String salt;
        String result;
        String reason;
    }

    public AsyncTaskGetSaltForPassw(SettingsActivity activity)
    {
        mActivity = activity;
    }

    protected Result doInBackground(String... params) {
        String username = params[0];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/general/get_salt").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\""+username+"\"}";
            Log.d("GETSATL ", jsonInputString.toString());
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, Result.class);

        } catch (IOException e) {
            Log.e("ERROR_AsyncTaskSalt",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(Result resultado) {
        mActivity.setupAdapter(resultado);
    }
}
