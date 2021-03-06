package es.unizar.eina.frankenstory.general;

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

public class AsyncTaskAnswerPetition extends AsyncTask<String, Void, AsyncTaskAnswerPetition.Result> {
    private FriendsActivity mActivity = null;

    static class Result {
        String result;
    }

    public AsyncTaskAnswerPetition(FriendsActivity activity)
    {
        mActivity = activity;
    }

    protected AsyncTaskAnswerPetition.Result doInBackground(String... params) {
        String username = ((MyApplication) mActivity.getApplication()).getUsername();
        String password = ((MyApplication) mActivity.getApplication()).getPassword();
        String targetUser = params[0];
        String answer = params[1];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL(mActivity.getResources().getString(R.string.url_server)+"/general/answer_petition").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\""+username+"\",\"password\":\""+password+"\",\"targetUser\":\""+targetUser+"\",\"answer\":"+answer.equals("true")+"}";
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, AsyncTaskAnswerPetition.Result.class);

        } catch (IOException e) {
            Log.e("AsyncTaskAnswerPetition",e.getMessage());
        } catch (Exception e) {
            Log.e("AsyncTaskAnswerPetition",e.getMessage());
        }
        return new Result();
    }

    protected void onPostExecute(AsyncTaskAnswerPetition.Result resultado) {
        mActivity.refreshPage();
    }
}
