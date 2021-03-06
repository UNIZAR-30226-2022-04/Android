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
import es.unizar.eina.frankenstory.R;

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
        Integer s;
        Integer winner;
    }

    public AsyncTaskResumeQuickVoted(QuickVoteActivity activity_vote, QuickVotedActivity activity_voted)
    {
        mActivity_vote = activity_vote;
        mActivity_voted = activity_voted;
    }

    protected AsyncTaskResumeQuickVoted.Result doInBackground(String... params) {
        String username, password, url = "";
         if (mActivity_voted == null) {
             username = ((MyApplication) mActivity_vote.getApplication()).getUsername();
             password = ((MyApplication) mActivity_vote.getApplication()).getPassword();
             url = mActivity_vote.getResources().getString(R.string.url_server);
         } else {
             username = ((MyApplication) mActivity_voted.getApplication()).getUsername();
             password = ((MyApplication) mActivity_voted.getApplication()).getPassword();
             url = mActivity_voted.getResources().getString(R.string.url_server);
         }

        String id = params[0];
        int turn = Integer.parseInt(params[1]);
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL(url+"/quick_game/resume_voted_quick_game").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\"" + username + "\",\"password\":\"" + password +
                     "\",\"turn\":" + turn + ",\"id\":\"" + id + "\"}";
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
        if (mActivity_voted == null) mActivity_vote.setupAdapter(resultado);
        else mActivity_voted.setupAdapter(resultado);
    }

}
