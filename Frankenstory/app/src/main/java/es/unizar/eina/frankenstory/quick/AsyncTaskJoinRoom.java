package es.unizar.eina.frankenstory.quick;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.story.StoryNotFirstWriteActivity;

public class AsyncTaskJoinRoom extends AsyncTask<String, Void, AsyncTaskJoinRoom.Result>{

        private QuickActivity mActivity = null;

    static class Result {
        String result;
        String reason;
    }

    public AsyncTaskJoinRoom(QuickActivity activity)
    {
        mActivity = activity;
    }

    protected AsyncTaskJoinRoom.Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        String id = params[0];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/quick_game/join_room").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"," +
                     "\",\"id\":\"" + id + "}";
            Log.d("JoinRoom", jsonInputString.toString());
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, Result.class);

        } catch (IOException e) {
            Log.e("AsyncTaskJoinRoom",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyncTaskJoinRoom",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(AsyncTaskJoinRoom.Result resultado) { mActivity.setupAdapter(resultado); }

}
