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
import es.unizar.eina.frankenstory.R;

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
        String id = params[0];
        String body = params[1].replaceAll("\n","\\\\n");
        String isLast = params[2];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL(mActivity.getResources().getString(R.string.url_server)+"/tale_mode/add_tale_paragraph").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"," +
                    "\"id\":" + id + "," + "\"body\":\"" + body + "\"," + "\"isLast\":" + isLast + "}";
            Log.d("AddPargraph", jsonInputString.toString());
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, Result.class);

        } catch (IOException e) {
            Log.e("AsyncTaskAddParagraph",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyncTaskAddParagraph",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(AsyncTaskAddParagraph.Result resultado) { mActivity.setupAdapter(resultado); }

}
