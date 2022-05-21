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

public class AsyncTaskResumeQuickVote extends AsyncTask<String, Void, AsyncTaskResumeQuickVote.Result>{

        private QuickPlayActivity mActivity_play = null;
        private QuickVoteActivity mActivity_vote = null;
        private QuickVotedActivity mActivity_voted = null;

    static class Paragraph {
        String body;
        List<String> words;
    }

    static class Result {
        String result;
        String topic;
        Boolean isLast;
        Integer turn;
        Integer s;
        List<Paragraph> paragraphs;
    }

    public AsyncTaskResumeQuickVote(QuickVoteActivity activity_vote, QuickVotedActivity activity_voted, QuickPlayActivity activity_play)
    {
        mActivity_vote = activity_vote;
        mActivity_voted = activity_voted;
        mActivity_play = activity_play;
    }

    protected AsyncTaskResumeQuickVote.Result doInBackground(String... params) {
        String username, password;
        if (mActivity_vote != null){
            username = ((MyApplication) mActivity_vote.getApplication()).getUsername();
            password = ((MyApplication) mActivity_vote.getApplication()).getPassword();
        } else if (mActivity_voted != null){
            username = ((MyApplication) mActivity_voted.getApplication()).getUsername();
            password = ((MyApplication) mActivity_voted.getApplication()).getPassword();
        } else {
            username = ((MyApplication) mActivity_play.getApplication()).getUsername();
            password = ((MyApplication) mActivity_play.getApplication()).getPassword();
        }

        String id = params[0];
        int turn = Integer.parseInt(params[1]);
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/quick_game/resume_vote_quick_game").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\"" + username + "\",\"password\":\"" + password +
                     "\",\"turn\":" + turn + ",\"id\":\"" + id + "\"}";
            Log.d("ResumeVoteQuick", jsonInputString.toString());
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, Result.class);

        } catch (IOException e) {
            Log.e("AsyncTaskTesumeVote",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyncTaskTesumeVote",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(AsyncTaskResumeQuickVote.Result resultado) {
        if (mActivity_vote != null) mActivity_vote.setupAdapter(resultado);
        else if (mActivity_voted != null) mActivity_voted.setupAdapter(resultado);
        else if (mActivity_play != null) mActivity_play.setupAdapter(resultado);
    }

}
