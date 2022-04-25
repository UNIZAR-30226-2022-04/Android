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

public class AsyncTaskGetStories extends AsyncTask<String, Void, AsyncTaskGetStories.Result> {
    private LibraryActivity mActivity = null;

    static class Story {
        Integer id;
        String title;
        String date;
        String type;
    }
    static class Result {
        String result;
        List<Story> stories;
    }

    public AsyncTaskGetStories(LibraryActivity activity)
    {
        mActivity = activity;
    }

    protected AsyncTaskGetStories.Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/general/get_stories").openConnection();
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
            return gson.fromJson(reader, AsyncTaskGetStories.Result.class);

        } catch (IOException e) {
            Log.e("AsyncTaskGetStories",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyncTaskGetStories",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(AsyncTaskGetStories.Result resultado) { mActivity.setupAdapter(resultado); }
}
