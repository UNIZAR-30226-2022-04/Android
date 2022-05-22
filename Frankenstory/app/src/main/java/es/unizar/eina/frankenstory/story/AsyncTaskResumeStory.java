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

public class AsyncTaskResumeStory extends AsyncTask<String, Void, AsyncTaskResumeStory.Result>{

        private StoryNotFirstWriteActivity mActivity = null;

    static class Paragraph {
        String text;
        String username;
        Integer turn_number;
    }
    static class Result {
        String result;
        String title;
        List<Paragraph> paragraphs;
        Integer maxCharacters;
    }

    public AsyncTaskResumeStory(StoryNotFirstWriteActivity activity)
    {
        mActivity = activity;
    }

    protected AsyncTaskResumeStory.Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        int id = Integer.parseInt(params[0]);
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL(mActivity.getResources().getString(R.string.url_server)+"/tale_mode/resume_tale").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\""+username+"\",\"password\":\""+password+"\",\"id\":"+id+"}";
            Log.d("ResumeStory",jsonInputString.toString());
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, AsyncTaskResumeStory.Result.class);

        } catch (IOException e) {
            Log.e("AsyncTaskResumeStory",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyncTaskResumeStory",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(AsyncTaskResumeStory.Result resultado) { mActivity.setupAdapter(resultado); }

}
