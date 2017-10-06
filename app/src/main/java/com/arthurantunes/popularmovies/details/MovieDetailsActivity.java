/*
 * Copyright (c) 2017 Arthur Antunes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arthurantunes.popularmovies.details;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arthurantunes.popularmovies.R;
import com.arthurantunes.popularmovies.api.ApiClient;
import com.arthurantunes.popularmovies.api.ApiService;
import com.arthurantunes.popularmovies.api.MovieReviewsApiResponse;
import com.arthurantunes.popularmovies.api.MovieTrailersApiResponse;
import com.arthurantunes.popularmovies.data.FavoriteMoviesContract;
import com.arthurantunes.popularmovies.main.MainActivity;
import com.arthurantunes.popularmovies.model.Movie;
import com.bumptech.glide.Glide;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity
    implements TrailersAdapter.TrailersGalleryItemClickListener {

  private static final String YOUTUBE_PLAY_VIDEOS_URI = "http://www.youtube.com/watch?v=";

  @BindView(R.id.movie_detail_coordinator_layout) CoordinatorLayout movieDetailCoordinatorLayout;
  @BindView(R.id.movie_detail_poster) ImageView moviePoster;
  @BindView(R.id.movie_detail_title) TextView movieTitle;
  @BindView(R.id.movie_detail_release_date) TextView movieReleaseDate;
  @BindView(R.id.movie_detail_rating) TextView movieRating;
  @BindView(R.id.movie_detail_overview) TextView movieOverview;
  @BindView(R.id.movie_detail_favorite_fab) FloatingActionButton movieFavoriteFab;
  @BindView(R.id.movie_detail_trailers_recycler_view) RecyclerView movieTrailersRecyclerView;
  @BindView(R.id.movie_detail_trailers_recycler_view_empty_state) TextView movieTrailersEmptyState;
  @BindView(R.id.movie_detail_reviews_recycler_view) RecyclerView movieReviewsRecyclerView;
  @BindView(R.id.movie_detail_reviews_recycler_view_empty_state) TextView movieReviewsEmptyState;

  private Movie movie;
  private ApiService apiService;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_details);
    ButterKnife.bind(this);

    apiService = ApiClient.getClient().create(ApiService.class);

    getMovieDataFromIntent();
    setupMovieDataToViews();
    requestMovieTrailersFromApi();
    requestMovieReviewsFromApi();
  }

  private void getMovieDataFromIntent() {
    if (getIntent().hasExtra(MainActivity.INTENT_EXTRA_MOVIE)) {
      movie = getIntent().getParcelableExtra(MainActivity.INTENT_EXTRA_MOVIE);
    }
  }

  private void setupMovieDataToViews() {
    if (movie != null) {
      String posterPath =
          ApiClient.POSTER_IMAGE_BASE_URL + ApiClient.POSTER_IMAGE_SIZE + movie.getPosterPath();
      Glide.with(this).load(posterPath).into(moviePoster);
      movieTitle.setText(movie.getTitle());
      movieReleaseDate.setText(movie.getReleaseDate());
      movieRating.setText(String.valueOf(movie.getVoteAverage()));
      movieOverview.setText(movie.getOverview());
      setUpFavoriteFab();
    }
  }

  private void requestMovieTrailersFromApi() {
    Call<MovieTrailersApiResponse> call =
        apiService.getMovieTrailers(movie.getId(), ApiClient.API_KEY);
    call.enqueue(new Callback<MovieTrailersApiResponse>() {
      @Override public void onResponse(@NonNull Call<MovieTrailersApiResponse> call,
          @NonNull Response<MovieTrailersApiResponse> response) {
        if (response.isSuccessful()) {
          if (response.body().getResults() != null && !response.body().getResults().isEmpty()) {
            movie.setTrailers(response.body().getResults());
            setTrailersToTrailersGallery(movie);
          } else {
            showTrailersGalleryEmptyState();
          }
        } else {
          showTrailersGalleryEmptyState();
        }
      }

      @Override
      public void onFailure(@NonNull Call<MovieTrailersApiResponse> call, @NonNull Throwable t) {
        showTrailersGalleryEmptyState();
      }
    });
  }

  private void requestMovieReviewsFromApi() {
    Call<MovieReviewsApiResponse> call =
        apiService.getMovieReviews(movie.getId(), ApiClient.API_KEY);
    call.enqueue(new Callback<MovieReviewsApiResponse>() {
      @Override public void onResponse(@NonNull Call<MovieReviewsApiResponse> call,
          @NonNull Response<MovieReviewsApiResponse> response) {
        if (response.isSuccessful()) {
          if (response.body().getResults() != null && !response.body().getResults().isEmpty()) {
            movie.setReviews(response.body().getResults());
            setReviewsToReviewsGallery(movie);
          } else {
            showReviewsGalleryEmptyState();
          }
        } else {
          showReviewsGalleryEmptyState();
        }
      }

      @Override
      public void onFailure(@NonNull Call<MovieReviewsApiResponse> call, @NonNull Throwable t) {
        showReviewsGalleryEmptyState();
      }
    });
  }

  private void setTrailersToTrailersGallery(Movie movie) {
    LinearLayoutManager layoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    movieTrailersRecyclerView.setLayoutManager(layoutManager);
    movieTrailersRecyclerView.addItemDecoration(new TrailersGalleryItemDecoration(10));
    movieTrailersRecyclerView.setHasFixedSize(true);
    movieTrailersRecyclerView.setNestedScrollingEnabled(false);

    TrailersAdapter trailersAdapter =
        new TrailersAdapter(this, movie);
    movieTrailersRecyclerView.setAdapter(trailersAdapter);
  }

  private void setReviewsToReviewsGallery(Movie movie) {
    LinearLayoutManager layoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    movieReviewsRecyclerView.setLayoutManager(layoutManager);
    movieReviewsRecyclerView.setHasFixedSize(true);
    movieReviewsRecyclerView.setNestedScrollingEnabled(false);

    ReviewsAdapter reviewsAdapter = new ReviewsAdapter(movie);
    movieReviewsRecyclerView.setAdapter(reviewsAdapter);
  }

  private void setUpFavoriteFab() {
    if (movie.isFavorite()) {
      movieFavoriteFab.setImageResource(R.drawable.ic_favorite_24dp);
    } else {
      movieFavoriteFab.setImageResource(R.drawable.ic_favorite_border_24dp);
    }
  }

  private void insertMovieIntoDataBase() {
    ContentValues contentValues = new ContentValues();
    contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID, movie.getId());
    contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE, movie.getTitle());
    contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
    contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RATING, movie.getVoteAverage());
    contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
    contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
    contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());

    Uri uri = getContentResolver().insert(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, contentValues);

    if(uri != null) {
      movie.setFavorite(true);
      setUpFavoriteFab();
      showSnackBarMessage(getString(R.string.movie_details_movie_saved_into_favorites_message));
    }
  }

  private void deleteMovieFromDatabase() {
    String stringId = Integer.toString(movie.getId());
    Uri uri = FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI;
    uri = uri.buildUpon().appendPath(stringId).build();

    int deletedMovie = getContentResolver().delete(uri, null, null);

    if (deletedMovie > 0) {
      movie.setFavorite(false);
      setUpFavoriteFab();
      showSnackBarMessage(getString(R.string.movie_details_movie_deleted_from_favorites_message));
    }
  }

  private void showTrailersGalleryEmptyState() {
    movieTrailersRecyclerView.setVisibility(View.GONE);
    movieTrailersEmptyState.setVisibility(View.VISIBLE);
  }

  private void showReviewsGalleryEmptyState() {
    movieReviewsRecyclerView.setVisibility(View.GONE);
    movieReviewsEmptyState.setVisibility(View.VISIBLE);
  }

  private void showSnackBarMessage(String message) {
    Snackbar.make(movieDetailCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void onTrailerItemClick(int clickedItemIndex) {
    if (movie != null && !movie.getTrailers().get(clickedItemIndex).getTrailerKey().isEmpty()) {
      String trailerId = movie.getTrailers().get(clickedItemIndex).getTrailerKey();
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_PLAY_VIDEOS_URI + trailerId));
      startActivity(intent);
    }
  }

  @OnClick(R.id.movie_detail_favorite_fab)
  public void onViewClicked() {
    if (movie.isFavorite()) {
      deleteMovieFromDatabase();
    } else {
      insertMovieIntoDataBase();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
