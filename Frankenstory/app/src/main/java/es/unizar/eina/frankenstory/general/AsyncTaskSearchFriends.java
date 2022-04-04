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

public class AsyncTaskSearchFriends extends AsyncTask<String, Void, AsyncTaskSearchFriends.Result> {
    private FriendsActivity mActivity = null;

    static class Result {
        String result;
        String reason;
        Boolean isFound;
        Integer picture;
        Boolean isFriend;
    }

    public AsyncTaskSearchFriends(FriendsActivity activity)
    {
        mActivity = activity;
    }

    protected AsyncTaskSearchFriends.Result doInBackground(String... params) {
        String username = params[0];
        String password = params[1];
        String searchedName = params[2];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/search_friends").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\""+username+"\",\"password\":\""+password+"\",\"searchedName\":\""+searchedName+"\"}";
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, AsyncTaskSearchFriends.Result.class);

        } catch (IOException e) {
            Log.e("AsyncTaskSearchFriends",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyncTaskSearchFriends",e.getMessage());
        }
        Result erroneo = new Result();
        erroneo.result = "error";
        return erroneo;
    }

    protected void onPostExecute(AsyncTaskSearchFriends.Result resultado) {
        mActivity.setupAdapterSearch(resultado);
    }
}
