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

public class AsyncTaskCreateRoom extends AsyncTask<String, Void, AsyncTaskCreateRoom.Result>{

        private CreateQuickActivity mActivity = null;

    static class Result {
        String result;
        String id;
    }

    public AsyncTaskCreateRoom(CreateQuickActivity activity)
    {
        mActivity = activity;
    }

    protected AsyncTaskCreateRoom.Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        int time = Integer.parseInt(params[0]);
        boolean isPrivate = Boolean.parseBoolean(params[1]);
        String mode = params[2];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/quick_game/create_room").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"," +
                    "\"time\":" + time + "," + "\"isPrivate\":" + isPrivate + "," +
                    "\"mode\":\"" + mode + "\"}";
            Log.d("CreateRoom", jsonInputString.toString());
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, Result.class);

        } catch (IOException e) {
            Log.e("AsyscTaskCreateRoom",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyscTaskCreateRoom",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(AsyncTaskCreateRoom.Result resultado) { mActivity.setupAdapter(resultado); }

}
