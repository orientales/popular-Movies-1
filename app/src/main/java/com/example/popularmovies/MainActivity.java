package com.example.popularmovies;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.adapter.MovieAdapter;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final int NUM_OF_COLUMNS = 2;
    private final String popular = "popular";
    private final String topRated = "top_rated";
    RecyclerView mRecyclerView;
    MovieAdapter mAdapter;
    Movie[] mMovieList;
    ProgressBar mProgressBar;
    String api = "popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.movies_rv);

        if (isOnline()) {
            fetchMovies(api);
        } else {
            Toast.makeText(this, "No Internet Connection. App Needs Internet", Toast.LENGTH_SHORT).show();
        }

    }

    public void fetchMovies(String api) {
        URL apiLink = NetworkUtils.buildUrl(api);
        new FetchMovie().execute(apiLink);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void sortedMovies(String sortType) {
        if (isOnline()) {
            URL sortURL = NetworkUtils.buildUrl(sortType);
            new FetchMovie().execute(sortURL);
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                sortedMovies(popular);
                return true;
            case R.id.top_rated:
                sortedMovies(topRated);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    // the following method checks for internet connection by pinging
    // copied from https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public class FetchMovie extends AsyncTask<URL, Void, String> {
        private final String TAG = "FetchMovieAsyncTask";

        private int mNoOfMovies;

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String movieResults = null;
            try {
                movieResults = NetworkUtils.getResponseFromUrl(url);
            } catch (IOException e) {
                Log.d(TAG, "doInBackground: " + e);
            }
            return movieResults;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.equals("")) {
                mProgressBar.setVisibility(View.INVISIBLE);
                try {
                    JSONObject moviesJSON = new JSONObject(s);
                    JSONArray moviesArray = moviesJSON.getJSONArray("results");

                    mNoOfMovies = moviesArray.length();
                    moviesArray.getString(1);
                    mMovieList = new Movie[20];
                    for (int i = 0; i < mNoOfMovies; i++) {
                        Movie movieDetails = new Movie();
                        String details = moviesArray.get(i).toString();
                        JSONObject parsedDetails = new JSONObject(details);
                        movieDetails.setMoviePoster("http://image.tmdb.org/t/p/w185/"
                                + parsedDetails.getString("poster_path"));
                        movieDetails.setMovieTitle(parsedDetails.getString("title"));
                        movieDetails.setReleaseDate(parsedDetails.getString("release_date"));
                        movieDetails.setSynopsis(parsedDetails.getString("overview"));
                        movieDetails.setVoteAverage(parsedDetails.getString("vote_average"));
                        mMovieList[i] = movieDetails;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter = new MovieAdapter(getApplicationContext(), mMovieList);
                mRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, NUM_OF_COLUMNS));
                mRecyclerView.setAdapter(mAdapter);
                Toast.makeText(getApplicationContext(), "Loaded", Toast.LENGTH_SHORT).show();
            }
        }
    }
}