package es.unizar.eina.frankenstory.quick;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import es.unizar.eina.frankenstory.MyApplication;
import es.unizar.eina.frankenstory.R;

public class AsyncTaskPlayQuickGame extends AsyncTask<String, Void, AsyncTaskPlayQuickGame.Result>{

    private QuickPlayActivity mActivity = null;

    static class Result {
        String result;
        int s;
        String topic;
        List<String> randomWords;
        String lastParagraph;
        boolean isLast;
        String puneta;
        Integer turn;
    }

    public AsyncTaskPlayQuickGame(QuickPlayActivity activity) { mActivity = activity; }

    protected AsyncTaskPlayQuickGame.Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        String id = params[0];
        String turn = params[1];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL(mActivity.getResources().getString(R.string.url_server)+"/quick_game/play_quick_game").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\"" + username + "\",\"password\":\"" + password +
                     "\",\"id\":\"" + id + "\",\"turn\":"+turn+"}";
            Log.d("PlayQuick", jsonInputString.toString());
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
        if (resultado != null)
        mActivity.setupAdapter(resultado);
        else Toast.makeText(mActivity, "PlayResponse null", Toast.LENGTH_SHORT).show();
    }
}
