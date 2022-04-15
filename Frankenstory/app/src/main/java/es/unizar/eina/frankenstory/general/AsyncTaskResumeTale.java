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

public class AsyncTaskResumeTale extends AsyncTask<String, Void, AsyncTaskResumeTale.Result>{

        private StoryNotFirstWriteActivity mActivity = null;

    static class Story {
        String body;
        int orden;
    }
    static class Result {
        String result;
        String title;
        List<Story> paragraphs;
        int maxCharacters;
    }

    public AsyncTaskResumeTale(StoryNotFirstWriteActivity activity)
    {
        mActivity = activity;
    }

    protected AsyncTaskResumeTale.Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        int id = Integer.parseInt(params[0]);
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/resume_tale").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\""+username+"\",\"password\":\""+password+"\",\"id\":\""+id+"\"}";
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, AsyncTaskResumeTale.Result.class);

        } catch (IOException e) {
            Log.e("AsyncTaskResumeTale",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyncTaskResumeTale",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(AsyncTaskResumeTale.Result resultado) { mActivity.setupAdapter(resultado); }

}
