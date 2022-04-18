package es.unizar.eina.frankenstory.story;

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

public class AsyncTaskStories extends AsyncTask<String, Void, AsyncTaskStories.Result> {
    private StoryActivity mActivity = null;

    static class Story {
        Integer story_id;
        String title;
        String creator;
        Integer max_turns;
        Integer turn;
    }
    static class Result {
        String result;
        String reason;
        List<Story> myTales;
        List<Story> friendTales;
        List<Story> publicTales;
        List<Story> talesForVote;
    }

    public AsyncTaskStories(StoryActivity activity)
    {
        mActivity = activity;
    }

    protected AsyncTaskStories.Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/get_tales").openConnection();
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
            return gson.fromJson(reader, AsyncTaskStories.Result.class);

        } catch (IOException e) {
            Log.e("AsyncTaskStories",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyncTaskStories",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(AsyncTaskStories.Result resultado) { mActivity.setupAdapter(resultado); }
}
