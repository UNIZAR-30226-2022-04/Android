package es.unizar.eina.frankenstory.quick;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import es.unizar.eina.frankenstory.MyApplication;

public class AsyncTaskAddParagraph extends AsyncTask<QuickPlayActivity.ParagraphToSend, Void, AsyncTaskAddParagraph.Result>{

        private QuickCreateActivity mActivity = null;

    static class Punetas {
        String puneta;
        String username;
    }

    static class Result {
        String result;
    }

    public AsyncTaskAddParagraph(QuickCreateActivity activity)
    {
        mActivity = activity;
    }

    protected AsyncTaskAddParagraph.Result doInBackground(QuickPlayActivity.ParagraphToSend... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        String punetas = "[";
        for (QuickPlayActivity.FriendPuneta f : params[0].listFriendPuneta){
            punetas+= "{\"puneta\":\""+f.puneta+"\",\"username\":\""+f.username+"\"},";
        }
        if (punetas.charAt(punetas.length()-1) == ',') {
            punetas = punetas.substring(0,punetas.length()-1) + "]";
        } else {
            punetas += "]";
        }

        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("https://mooncode-frankenstory-dev.herokuapp.com/api/quick_game/add_quick_game_paragraph").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"," +
                    "\"id\":\"" + params[0].id + "\"," + "\"body\":" + params[0].body + "\"," +
                    "\"turn\":\"" + params[0].turn + "\"," + "\"isLast\":" + params[0].isLast.toString() + ",\"punetas\":" + punetas + "}";
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
