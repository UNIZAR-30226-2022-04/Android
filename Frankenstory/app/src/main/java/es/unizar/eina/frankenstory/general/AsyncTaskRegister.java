package es.unizar.eina.frankenstory.general;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import es.unizar.eina.frankenstory.R;

public class AsyncTaskRegister extends AsyncTask<String, Void, AsyncTaskRegister.ResultRegister> {

    private RegisterActivity mActivity = null;

    static class ResultRegister {
        String result;
        String reason;
    }

    public AsyncTaskRegister(RegisterActivity activity)
    {
        mActivity = activity;
    }

    protected ResultRegister doInBackground(String... params) {
        String username = params[0];
        String password = params[1];
        String email = params[2];
        String salt = params[3];
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL(mActivity.getResources().getString(R.string.url_server)+"/general/register").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"username\":\""+username+"\",\"password\":\""+password+"\",\"email\":\""+email+"\",\"salt\":\""+salt+"\"}";
            Log.d("REGISTER", jsonInputString.toString());
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            }

            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            Gson gson = new Gson();
            return gson.fromJson(reader, ResultRegister.class);

        } catch (IOException e) {
            Log.e("ERROR_AsyncTaskRegister",e.getMessage());
        }
        return new ResultRegister();
    }

    protected void onPostExecute(ResultRegister resultado) {
        mActivity.setupAdapter(resultado);
    }

}
