package es.unizar.eina.frankenstory.quick;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import es.unizar.eina.frankenstory.MyApplication;

public class AsyncTaskGetRoom extends AsyncTask<String, Void, AsyncTaskGetRoom.Result>{

        private QuickRoomActivity mActivity = null;

    static class Participants implements Serializable {
        String username;
        int picture;
        int stars;
    }

    static class Result {
        String result;
        String mode;
        String reason;
        List<Participants> participants;
        int hasStarted;
    }

    public AsyncTaskGetRoom(QuickRoomActivity activity)
    {
        mActivity = activity;
    }

    protected AsyncTaskGetRoom.Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        String id = params[0];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/quick_game/get_room").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\"" + username + "\",\"password\":\"" + password +
                     "\",\"id\":\"" + id + "\"}";
            Log.d("GetRoom", jsonInputString.toString());
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, Result.class);

        } catch (IOException e) {
            Log.e("AsyncTaskGetRoom",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyncTaskGetRoom",e.getMessage());
        }
        return new Result();
    }
//636w0
    protected void onPostExecute(AsyncTaskGetRoom.Result resultado) { mActivity.setupAdapter(resultado); }

}
