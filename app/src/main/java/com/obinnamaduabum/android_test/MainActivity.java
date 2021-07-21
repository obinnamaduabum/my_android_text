package com.obinnamaduabum.android_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.obinnamaduabum.android_test.adapter.MovieAdapter;
import com.obinnamaduabum.android_test.api.ImdbApiService;
import com.obinnamaduabum.android_test.database.MovieDatabaseHandler;
import com.obinnamaduabum.android_test.database.RatingDatabaseHandler;
import com.obinnamaduabum.android_test.model.MovieModel;
import com.obinnamaduabum.android_test.model.Rating;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MovieDatabaseHandler movieDatabaseHandler;
    private RatingDatabaseHandler ratingDatabaseHandler;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private EditText editText;
    private Button searchButton;
    private String inputMovieName;
    private MovieAdapter movieAdapter;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.movie_list);

        editText = findViewById(R.id.search_movie_name);
        searchButton = findViewById(R.id.search_btn);
        progressBar = findViewById(R.id.movie_progress_bar);
        textView = findViewById(R.id.empty_movie_list);

        movieDatabaseHandler = new MovieDatabaseHandler(getApplicationContext());
        movieDatabaseHandler.onCreate(movieDatabaseHandler.getDatabase());

        ratingDatabaseHandler = new RatingDatabaseHandler(getApplicationContext());
        ratingDatabaseHandler.onCreate(ratingDatabaseHandler.getDatabase());

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    inputMovieName = String.valueOf(s);
                } else {
                    inputMovieName = "";
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputMovieName != null) {
                    if(!inputMovieName.isEmpty()) {
                        doSearch(inputMovieName);
                    } else {
                        showToast();
                    }
                } else {
                    showToast();
                }
            }
        });

        setUpRecyclerView();
    }

    private void showToast() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Search cannot be empty",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    private void doSearch(String query) {
        showProgressViewView();
        ImdbApiService imdbApiService = new ImdbApiService();
        imdbApiService.search(query, new Callback() {
           @Override
           public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
               if (response.isSuccessful()) {
                   String responseStr = response.body().string();
                   try {
                       MovieModel movieModel = imdbApiService.getMovie(responseStr);

                       MovieModel foundByMovieModelTitle = movieDatabaseHandler.getByMovieTitle(movieModel.getTitle());
                       if(foundByMovieModelTitle == null) {
                           long id = movieDatabaseHandler.insert(movieModel);
                           if (id > 0) {
                               ratingDatabaseHandler.bulkInsert(movieModel.ratings, id);
                           }

                           List<Rating> ratingList = ratingDatabaseHandler.getAllByMovieId(id);
                           MovieModel foundMovieModel = movieDatabaseHandler.getByMovieId(id);
                           if (ratingList != null) {
                               if (ratingList.size() > 0) {
                                   foundMovieModel.setRatings(ratingList);
                               }
                           }
                           movieAdapter.movieList.add(foundMovieModel);

                           int len = movieAdapter.movieList.size();
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   movieAdapter.notifyItemInserted(len + 1);
                                   showRecyclerView(movieAdapter.movieList.size());
                               }
                           });

                       }

                       // notifyAll();
                      //movieAdapter.addedNewData(movieModel);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               } else {
                   // Request not successful
                   showRecyclerView(movieAdapter.movieList.size());
               }
           }

           @Override
           public void onFailure(@NotNull Call call, @NotNull IOException e) {
               showRecyclerView(movieAdapter.movieList.size());
           }

        });



    }

    private void showRecyclerView(int listSize) {
        progressBar.setVisibility(View.GONE);

        if(listSize > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void showProgressViewView() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void setUpRecyclerView() {
        List<MovieModel> modelList = movieDatabaseHandler.getAll();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        movieAdapter = new MovieAdapter(modelList);
        int count = modelList.size();
        Log.d(TAG, "found list of user groups: "+ count);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(movieAdapter);
        showRecyclerView(movieAdapter.movieList.size());
    }
}