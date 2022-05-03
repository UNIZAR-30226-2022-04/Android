package es.unizar.eina.frankenstory.quick;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import es.unizar.eina.frankenstory.MyApplication;

public class AsyncTaskAddParagraph extends AsyncTask<String, Void, AsyncTaskAddParagraph.Result>{

        private CreateQuickActivity mActivity = null;

    static class Result {
        String result;
    }

    public AsyncTaskAddParagraph(CreateQuickActivity activity)
    {
        mActivity = activity;
    }

    protected AsyncTaskAddParagraph.Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        String id = params[0];
        String body = params[1];
        String turn = params[2];
        boolean isLast = Boolean.parseBoolean(params[3]);
        List<String> punetas = Collections.singletonList(params[4]);
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/quick_game/add_quick_game_paragraph").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"," +
                    "\"id\":" + id + "," + "\"body\":" + body + "," +
                    "\"turn\":\"" + turn + "," + "isLast:" + isLast + "" + punetas + "\"}";
            Log.d("AddParagraph", jsonInputString.toString());
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, Result.class);

        } catch (IOException e) {
            Log.e("AsyscTaskAddParagraph",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyscTaskAddParagraph",e.getMessage());
        }
        return new Result();
    }

    //protected void onPostExecute(AsyncTaskAddParagraph.Result resultado) { mActivity.setupAdapter(resultado); }

}
