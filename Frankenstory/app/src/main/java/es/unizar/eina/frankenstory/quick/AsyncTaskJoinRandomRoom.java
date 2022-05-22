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

public class AsyncTaskJoinRandomRoom extends AsyncTask<String, Void, AsyncTaskJoinRandomRoom.Result>{

        private QuickActivity mActivity = null;

    static class Result {
        String result;
        String id;
        String reason;
    }

    public AsyncTaskJoinRandomRoom(QuickActivity activity)
    {
        mActivity = activity;
    }

    protected AsyncTaskJoinRandomRoom.Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL(mActivity.getResources().getString(R.string.url_server)+"/quick_game/join_random_room").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
            Log.d("JoinRandomRoom", jsonInputString.toString());
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, Result.class);

        } catch (IOException e) {
            Log.e("AsyncTaskJoinRandomRoom",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyncTaskJoinRandomRoom",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(AsyncTaskJoinRandomRoom.Result resultado) {
        if (resultado.result !=null && resultado.reason!=null) Log.d("JoinRandomRoomRes:",resultado.result+"("+resultado.reason+")");
        mActivity.setupAdapter(resultado);
    }

}
