package es.unizar.eina.frankenstory.story;

import java.net.HttpURLConnection;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import es.unizar.eina.frankenstory.MyApplication;

public class AsyncTaskCreateStory extends AsyncTask<String, Void, AsyncTaskCreateStory.Result> {

    private StoryFirstWriteActivity mActivity = null;

    static class Result {
        String result;
    }

    public AsyncTaskCreateStory(StoryFirstWriteActivity activity){mActivity = activity; }

    protected Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        String title = params[0];
        int num_writings = Integer.parseInt(params[1]);
        int num_chars = Integer.parseInt(params[2]);
        boolean isPrivate_game = Boolean.parseBoolean(params[3]);
        String first_paragraph = params[4].replaceAll("\n","\\\\n");
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/create_tale").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            String jsonInputString = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"," +
                    "\"title\":\"" + title + "\"," + "\"maxTurns\":" + num_writings + "," +
                    "\"maxCharacters\":" + num_chars + "," + "\"privacy\":" + isPrivate_game + "," +
                    "\"first_paragraph\":\"" + first_paragraph + "\"}";
            Log.d("CreateStory",jsonInputString.toString());
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, Result.class);

        } catch (IOException e) {
            Log.e("ERROR_CreateStory", e.getMessage());
        }

        return new Result();

    }

    protected void onPostExecute(Result resultado) { mActivity.setupAdapter(resultado); }

}
