package com.example.oopee10.util;

import android.content.Context;
import android.util.Log;

import com.example.oopee10.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JSONUtility {
    private static final String TAG = "JSONUtility";
    private final Context context;

    public JSONUtility(Context context) {
        this.context = context;
    }

    public List<Movie> loadMovies() throws JSONException, IOException {
        List<Movie> movies = new ArrayList<>();
        String jsonString = readJSONFromAsset();
        
        if (jsonString == null) {
            throw new IOException("Unable to read movie data file");
        }

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject movieJson = jsonArray.getJSONObject(i);
                try {
                    Movie movie = parseMovieFromJSON(movieJson);
                    if (movie != null) {
                        movies.add(movie);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing movie data: " + e.getMessage());
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Invalid JSON format: " + e.getMessage());
            throw new JSONException("Invalid movie data format");
        }

        return movies;
    }

    private String readJSONFromAsset() {
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open("movies.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            json = stringBuilder.toString();
            inputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to read JSON file: " + e.getMessage());
            return null;
        }
        return json;
    }

    private Movie parseMovieFromJSON(JSONObject jsonObject) {
        try {
            String title = jsonObject.isNull("title") ? null : jsonObject.getString("title");
            
            // Year processing
            Integer year = null;
            if (!jsonObject.isNull("year")) {
                try {
                    double yearValue = jsonObject.getDouble("year");
                    // Accept only integer years
                    if (yearValue == Math.floor(yearValue)) {
                        year = (int) yearValue;
                    }
                } catch (JSONException e) {
                    // If year is in string format, try to parse number
                    String yearStr = jsonObject.getString("year");
                    try {
                        year = Integer.parseInt(yearStr);
                    } catch (NumberFormatException ignored) {
                        // Unparseable year format, keep as null
                    }
                }
            }
            
            String genre = jsonObject.isNull("genre") ? null : jsonObject.getString("genre");
            String poster = jsonObject.isNull("poster") ? null : jsonObject.getString("poster");

            return new Movie(title, year, genre, poster);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing movie object: " + e.getMessage());
            return null;
        }
    }
}