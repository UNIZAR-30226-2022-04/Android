package es.unizar.eina.frankenstory.quick;

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

public class AsyncTaskResumeQuickVoted extends AsyncTask<String, Void, AsyncTaskResumeQuickVoted.Result>{

        private QuickVoteActivity mActivity_vote = null;
        private QuickVotedActivity mActivity_voted = null;

    static class Paragraph {
        String body;
        String username;
    }

    static class Result {
        String result;
        List<Paragraph> paragraphs;
        Integer winner;
    }

    public AsyncTaskResumeQuickVoted(QuickVoteActivity activity_vote, QuickVotedActivity activity_voted)
    {
        mActivity_vote = activity_vote;
        mActivity_voted = activity_voted;
    }

    protected AsyncTaskResumeQuickVoted.Result doInBackground(String... params) {
        String username, password;
         if (mActivity_voted == null) {
             username = ((MyApplication) mActivity_vote.getApplication()).getUsername();
             password = ((MyApplication) mActivity_vote.getApplication()).getPassword();
         } else {
             username = ((MyApplication) mActivity_voted.getApplication()).getUsername();
             password = ((MyApplication) mActivity_voted.getApplication()).getPassword();
         }

        String id = params[0];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/quick_game/resume_voted_quick_game").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\"" + username + "\",\"password\":\"" + password +
                     "\",\"id\":\"" + id + "\"}";
            Log.d("ResumeVotedQuick", jsonInputString.toString());
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, Result.class);

        } catch (IOException e) {
            Log.e("AsyncTaskTesumeVoted",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyncTaskTesumeVoted",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(AsyncTaskResumeQuickVoted.Result resultado) {
        if (mActivity_voted == null) mActivity_voted.setupAdapter(resultado);
        else mActivity_vote.setupAdapter(resultado);
    }

}
