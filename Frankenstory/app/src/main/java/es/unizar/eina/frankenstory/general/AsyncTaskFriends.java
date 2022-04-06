package es.unizar.eina.frankenstory.general;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class AsyncTaskFriends extends AsyncTask<String, Void, AsyncTaskFriends.Result> {
    private FriendsActivity mActivity = null;

    static class Friend {
        String username;
        String hola;
    }
    static class Result {
        String result;
        List<String> friends;
        List<Friend> notifications;
    }

    public AsyncTaskFriends(FriendsActivity activity)
    {
        mActivity = activity;
    }

    protected AsyncTaskFriends.Result doInBackground(String... params) {
        String username = params[0];
        String password = params[1];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/friends").openConnection();
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
            return gson.fromJson(reader, AsyncTaskFriends.Result.class);

        } catch (IOException e) {
            Log.e("ERROR_AsyncTaskFriends",e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR_AsyncTaskFriends",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(AsyncTaskFriends.Result resultado) {
        mActivity.setupAdapter(resultado);
    }
}
