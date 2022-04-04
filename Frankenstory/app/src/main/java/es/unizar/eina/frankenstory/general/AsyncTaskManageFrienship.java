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

public class AsyncTaskManageFrienship extends AsyncTask<String, Void, AsyncTaskManageFrienship.Result> {
    private FriendsActivity mActivity = null;

    static class Result {
        String result;
    }

    public AsyncTaskManageFrienship(FriendsActivity activity)
    {
        mActivity = activity;
    }

    protected AsyncTaskManageFrienship.Result doInBackground(String... params) {
        String username = params[0];
        String password = params[1];
        String targetUser = params[2];
        String type = params[3];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/manage_friendship").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\""+username+"\",\"password\":\""+password+"\",\"targetUser\":\""+targetUser+"\",\"type\":\""+type+"\"}";
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, AsyncTaskManageFrienship.Result.class);

        } catch (IOException e) {
            Log.e("AsyncTaskManageFriend",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyncTaskManageFriend",e.getMessage());
        }
        Result erroneo = new Result();
        erroneo.result = "error";
        return erroneo;
    }

    protected void onPostExecute(AsyncTaskManageFrienship.Result resultado) {
        mActivity.refreshPage();
    }
}
