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
import java.util.List;

public class AsyncTaskMainMenu extends AsyncTask<String, Void, AsyncTaskMainMenu.Result> {

    private MainMenuActivity mActivity = null;

    static class Statistic {
        String username;
        Integer stars;
    }
    static class Result {
        String result;
        Integer picture;
        Integer stars;
        Integer coins;
        List<Statistic> bestFour;
        Integer notifications;
    }

    public AsyncTaskMainMenu(MainMenuActivity activity)
    {
        mActivity = activity;
    }

    protected Result doInBackground(String... params) {
        String username = params[0];
        String password = params[1];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/home").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\""+username+"\",\"password\":\""+password+"\"}";
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, Result.class);

        } catch (IOException e) {
            Log.e("ERROR_AsyncTaskMainMenu",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(Result resultado) {
        mActivity.setupAdapter(resultado);
    }
}
