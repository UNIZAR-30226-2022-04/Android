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
import es.unizar.eina.frankenstory.R;

public class AsyncTaskLeaveRoom extends AsyncTask<String, Void, AsyncTaskLeaveRoom.Result>{

    private QuickRoomActivity mActivity = null;

    static class Result {
        String result;
    }

    public AsyncTaskLeaveRoom(QuickRoomActivity activity)
    {
        mActivity = activity;
    }

    protected AsyncTaskLeaveRoom.Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();

        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL(mActivity.getResources().getString(R.string.url_server)+"/quick_game/leave_room").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"" + "}";
            Log.d("LeaveRoomJSON: ", jsonInputString.toString());
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, Result.class);

        } catch (IOException e) {
            Log.e("LeaveRoom",e.getMessage());
        } catch (Exception e) {
            Log.e("LeaveRoom",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(AsyncTaskLeaveRoom.Result resultado) {
        Log.d("LeaveRoom: ", resultado.result); }
}
