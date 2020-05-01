package com.example.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    //Title, release date, movie poster, vote average, and plot synopsis.
    private String mMovieTitle;
    private String mReleaseDate;
    private String mMoviePoster;
    private String mVoteAverage;
    private String mSynopsis;

    public Movie() {

    }

    public String getMovieTitle() {
        return mMovieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        mMovieTitle = movieTitle;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getMoviePoster() {
        return mMoviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        mMoviePoster = moviePoster;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public void setSynopsis(String synopsis) {
        mSynopsis = synopsis;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "mMovieTitle='" + mMovieTitle + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mMoviePoster='" + mMoviePoster + '\'' +
                ", mVoteAverage='" + mVoteAverage + '\'' +
                ", mSynopsis='" + mSynopsis + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMovieTitle);
        dest.writeString(mSynopsis);
        dest.writeString(mMoviePoster);
        dest.writeString(mReleaseDate);
        dest.writeString(mVoteAverage);
    }

    public Movie(Parcel parcel) {
        mMovieTitle = parcel.readString();
        mSynopsis = parcel.readString();
        mMoviePoster = parcel.readString();
        mReleaseDate = parcel.readString();
        mVoteAverage = parcel.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        public Movie createFromParcel(Parcel src) {
            return new Movie(src);
        }
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
