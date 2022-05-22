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
import es.unizar.eina.frankenstory.story.StoryNotFirstWriteActivity;

public class AsyncTaskWatchStory extends AsyncTask<String, Void, AsyncTaskWatchStory.Result>{

    private LibraryStoryActivity mActivity = null;

    static class Result {
        String result;
        String body;
    }

    public AsyncTaskWatchStory(LibraryStoryActivity activity)
    {
        mActivity = activity;
    }

    protected AsyncTaskWatchStory.Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        String id = params[0];
        String type = params[1];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL(mActivity.getResources().getString(R.string.url_server)+"/general/watch_story").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\""+username+"\",\"password\":\""+password+"\",\"id\":"+id+",\"type\":\""+type+"\"}";
            Log.d("WatchStory",jsonInputString.toString());
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, AsyncTaskWatchStory.Result.class);

        } catch (IOException e) {
            Log.e("AsyncTaskWatchStory",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyncTaskWatchStory",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(AsyncTaskWatchStory.Result resultado) { mActivity.setupAdapter(resultado); }

}
