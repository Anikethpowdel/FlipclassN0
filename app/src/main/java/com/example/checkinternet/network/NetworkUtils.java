package com.example.checkinternet.network;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    public static void fetchData(String url, OnDataFetchedListener listener) {
        new FetchDataTask(listener).execute(url);
    }

    private static class FetchDataTask extends AsyncTask<String, Void, String> {
        private OnDataFetchedListener listener;

        public FetchDataTask(OnDataFetchedListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();
                }
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            if (listener != null) {
                listener.onDataFetched(result);
            }
        }
    }

    public interface OnDataFetchedListener {
        void onDataFetched(String data);
    }
}
