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

import es.unizar.eina.frankenstory.MyApplication;

public class AsyncTaskChangeIcon extends AsyncTask<String, Void, AsyncTaskChangeIcon.Result> {

    private SettingsActivity mActivity = null;

    static class Result {
        String result;
    }

    public AsyncTaskChangeIcon(SettingsActivity activity)
    {
        mActivity = activity;
    }

    protected Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        String newPicture = params[0];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/change_picture").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\""+username+"\",\"password\":\""+password+"\",\"newPicture\":"+newPicture+"}";
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, Result.class);

        } catch (IOException e) {
            Log.e("ERROR_AsyncTaskChangeIc",e.getMessage());
        }

        return new Result();
    }

    protected void onPostExecute(Result resultado) {
        mActivity.setupAdapter(resultado);
    }
}
