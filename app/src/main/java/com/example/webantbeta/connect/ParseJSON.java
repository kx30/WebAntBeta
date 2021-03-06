package com.example.webantbeta.connect;

import android.util.Log;

import com.example.webantbeta.content.Content;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.webantbeta.content.Content.countOfPages;

public class ParseJSON {
    private String resultJson = "";

    private static final String TAG = "ParseJSON: ";


    public String connect(String connectUrl) {
        try {
            URL url = new URL(connectUrl);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) { buffer.append(line); }
            resultJson = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJson;
    }

    public void ParseJson(ArrayList<Content> mContent, String strJson) {
        Log.d(TAG, strJson);
        JSONObject dataJsonObj = null;

        try {

            dataJsonObj = new JSONObject(strJson);
            JSONArray data = dataJsonObj.getJSONArray("data");

            int page =  dataJsonObj.getInt("countOfPages");
            countOfPages=page;
            Log.d(TAG, "ParseJson: countOfPages "+page);

            for (int i = 0; i < data.length(); i++) {

                JSONObject images = data.getJSONObject(i);
                JSONObject image = images.getJSONObject("image");

                mContent.add(new Content(
                        images.getString("name"),
                        image.getString("contentUrl"),
                        images.getString("description"),
                        images.getString("new"),
                        images.getString("popular")
                ));
                Log.d(TAG, "\ronPostExecute: "
                        + "Name: " + mContent.get(i).getName()
                        + ", Url: " + mContent.get(i).getUrl()
                        + ", Description " + mContent.get(i).getDescription()
                        + ", new " + mContent.get(i).getTypeNew()
                        + ", popular " + mContent.get(i).getTypePopular()
                );

            }
        } catch (JSONException e) { e.printStackTrace(); }
    }
}
