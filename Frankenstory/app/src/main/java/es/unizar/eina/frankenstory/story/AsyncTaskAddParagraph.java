package es.unizar.eina.frankenstory.story;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import es.unizar.eina.frankenstory.MyApplication;

public class AsyncTaskAddParagraph extends AsyncTask<String, Void, AsyncTaskAddParagraph.Result>{

        private StoryNotFirstWriteActivity mActivity = null;

    static class Result {
        String result;
    }

    public AsyncTaskAddParagraph(StoryNotFirstWriteActivity activity)
    {
        mActivity = activity;
    }

    protected AsyncTaskAddParagraph.Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        int id = Integer.parseInt(params[0]);
        String body = params[1];
        boolean isLast = Boolean.parseBoolean(params[2]);
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/add_tale_paragraph").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"," +
                    "\"id\":\"" + id + "\"," + "\"body\":" + body + "," + "\"isLast\":" + isLast + "\"}";
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, AsyncTaskAddParagraph.Result.class);

        } catch (IOException e) {
            Log.e("AsyncTaskAddParagraph",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyncTaskAddParagraph",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(AsyncTaskAddParagraph.Result resultado) { mActivity.setupAdapter(resultado); }

}
