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

public class AsyncTaskPlayQuickGame extends AsyncTask<String, Void, AsyncTaskPlayQuickGame.Result>{

        private QuickPlayActivity mActivity = null;

    static class Result {
        String result;
        String reason;
        int s;
        String topic;
        List<String> randomWords;
        String lastParagraph;
        boolean isLast;
        String puneta;
    }

    public AsyncTaskPlayQuickGame(QuickPlayActivity activity) { mActivity = activity; }

    protected AsyncTaskPlayQuickGame.Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        String id = params[0];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/quick_game/play_quick_game").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\"" + username + "\",\"password\":\"" + password +
                     "\",\"id\":\"" + id + "\"}";
            Log.d("GetRoom", jsonInputString.toString());
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, Result.class);

        } catch (IOException e) {
            Log.e("AsyncTaskPlayQuickGame",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyncTaskPlayQuickGame",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(AsyncTaskPlayQuickGame.Result resultado) {
        if (resultado.result !=null && resultado.reason!=null) Log.d("AsyncPlayQuickGameRes:",resultado.result+"("+resultado.reason+")");
        mActivity.setupAdapter(resultado); }
}
