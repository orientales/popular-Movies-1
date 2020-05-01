package com.example.popularmovies;


import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailsActivity";
    Movie details;
    ImageView mMoviePoster;
    TextView mTitle, mReleaseDate, mVoteAvg, mSynopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mMoviePoster = findViewById(R.id.poster_imageview);
        mTitle = findViewById(R.id.title_text);
        mReleaseDate = findViewById(R.id.release_date_text);
        mVoteAvg = findViewById(R.id.vote_avg_text);
        mSynopsis = findViewById(R.id.synopsis_text);

        if(getIntent().hasExtra("details")){
            details = getIntent().getParcelableExtra("details");
            Log.d(TAG, "onCreate: " + details);

            Picasso.get().load(details.getMoviePoster()).into(mMoviePoster);
            mTitle.setText(details.getMovieTitle());
            mReleaseDate.setText(details.getReleaseDate());
            mVoteAvg.setText(details.getVoteAverage());
            mSynopsis.setText(details.getSynopsis());
        } else {
            Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show();
        }
    }
}

