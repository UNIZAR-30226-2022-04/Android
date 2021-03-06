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
import es.unizar.eina.frankenstory.R;

public class AsyncTaskGetParagraphs extends AsyncTask<String, Void, AsyncTaskGetParagraphs.Result>{

    private VoteStoryActivity mActivity = null;

   static class Paragraph {
        String text;
        String username;
        int turn_number;
    }
    static class Result {
        String result;
        String title;
        List<Paragraph> paragraphs;
    }

    public AsyncTaskGetParagraphs(VoteStoryActivity activity) { mActivity = activity; }

    protected Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        int id = Integer.parseInt(params[0]);
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL(mActivity.getResources().getString(R.string.url_server)+"/tale_mode/get_paragraphs").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\""+username+"\",\"password\":\""+password+"\"," + "\"id\":" + id + "}";
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, Result.class);

        } catch (IOException e) {
            Log.e("ERROR_AsyncGetParagraph",e.getMessage());
        }

        return new Result();
    }

    protected void onPostExecute(Result resultado) {
        mActivity.setupAdapter(resultado);
    }
}
