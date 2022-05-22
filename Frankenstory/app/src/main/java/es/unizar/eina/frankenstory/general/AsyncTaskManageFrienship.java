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
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        String targetUser = params[0];
        String type = params[1];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL(mActivity.getResources().getString(R.string.url_server)+"/general/manage_friendship").openConnection();
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
        return new Result();
    }

    protected void onPostExecute(AsyncTaskManageFrienship.Result resultado) {
        mActivity.refreshPage();
    }
}
