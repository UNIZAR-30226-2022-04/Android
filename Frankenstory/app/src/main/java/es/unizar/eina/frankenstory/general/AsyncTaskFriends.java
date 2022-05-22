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

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;

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
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL(mActivity.getResources().getString(R.string.url_server)+"/general/friends").openConnection();
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
