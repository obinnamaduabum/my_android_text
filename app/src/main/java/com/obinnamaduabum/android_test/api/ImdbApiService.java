package com.obinnamaduabum.android_test.api;

import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.obinnamaduabum.android_test.adapter.MovieAdapter;
import com.obinnamaduabum.android_test.model.MovieModel;
import com.obinnamaduabum.android_test.model.Rating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Call;
import okhttp3.Callback;

public class ImdbApiService {

    private static final String TAG = ImdbApiService.class.getSimpleName();

    private static final String apiKey = "7b18536f";
    private String baseUrl;
    private Gson gson;
    private String url;
    // private Retrofit retrofit;

    public ImdbApiService() {
        baseUrl = "http://www.omdbapi.com/?apikey=" + apiKey;

         gson = new GsonBuilder()
                .setLenient()
                .create();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(url)
//                .client(new OkHttpClient())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

    }

    public static String getValue(JSONObject jsonObject, String name) {
        try {
            return  jsonObject.getString(name);
        } catch (JSONException jsonException) {
            return null;
        }
    }


    public MovieModel getMovie(String jsonData) throws JSONException {
        MovieModel movieModel = new MovieModel();
        JSONObject jsonObject = new JSONObject(jsonData);
        String title = getValue(jsonObject, "Title");
        String year = getValue(jsonObject,"Year");
        String rated = getValue(jsonObject,"Rated");
        String released = getValue(jsonObject,"Released");
        String runtime = getValue(jsonObject,"Runtime");
        String genre = getValue(jsonObject,"Genre");
        String director = getValue(jsonObject,"Director");
        String writer = getValue(jsonObject,"Writer");
        String actors = getValue(jsonObject,"Actors");
        String country = getValue(jsonObject,"Country");

        String awards = getValue(jsonObject,"Awards");
        String poster = getValue(jsonObject,"Poster");
        JSONArray ratings = jsonObject.getJSONArray("Ratings");

        List<Rating> ratingList = new ArrayList<>();
        for (int i = 0; i < ratings.length(); i++) {
            JSONObject actor = ratings.getJSONObject(i);
            String source = actor.getString("Source");
            String value = actor.getString("Value");
            ratingList.add(new Rating(source, value));
        }


        String metaScore = getValue(jsonObject,"Metascore");
        String imdbRating = getValue(jsonObject,"imdbRating");
        String imdbVotes = getValue(jsonObject,"imdbVotes");
        String imdbID = getValue(jsonObject,"imdbID");
        String type = getValue(jsonObject,"Type");
        String totalSeasons = getValue(jsonObject,"totalSeasons");
        String jsonResponse = getValue(jsonObject,"Response");


        if(jsonResponse != null) {
            if (jsonResponse.equalsIgnoreCase("True")) {

                movieModel.setTitle(title);
                movieModel.setYear(year);
                movieModel.setRated(rated);
                movieModel.setReleased(released);
                movieModel.setRuntime(runtime);
                movieModel.setGenre(genre);
                movieModel.setDirector(director);
                movieModel.setActors(actors);
                movieModel.setWriter(writer);
                movieModel.setCountry(country);
                movieModel.setAwards(awards);
                movieModel.setPoster(poster);
                movieModel.setMetaScore(metaScore);
                movieModel.setImdbID(imdbID);
                movieModel.setImdbRating(imdbRating);
                movieModel.setImdbVotes(imdbVotes);
                movieModel.setResponse(jsonResponse);
                movieModel.setTotalSeasons(totalSeasons);
                movieModel.setImdbVotes(imdbVotes);
                movieModel.setType(type);
                movieModel.setRatings(ratingList);

//

                Log.e(TAG, "response 33: " + movieModel.getTitle());

            }
        }

        return movieModel;
    }

    public Call search(String movieName, Callback callback) {

        Log.e(TAG, movieName);
        url  = baseUrl + "&t=" + movieName + "&r=json";
        Log.e(TAG, url);


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
}
