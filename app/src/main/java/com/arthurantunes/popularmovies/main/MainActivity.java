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

package com.arthurantunes.popularmovies.main;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arthurantunes.popularmovies.R;
import com.arthurantunes.popularmovies.api.ApiClient;
import com.arthurantunes.popularmovies.api.ApiService;
import com.arthurantunes.popularmovies.api.MoviesApiResponse;
import com.arthurantunes.popularmovies.data.FavoriteMoviesContract;
import com.arthurantunes.popularmovies.details.MovieDetailsActivity;
import com.arthurantunes.popularmovies.model.Movie;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
    implements ApiMoviesAdapter.GalleryItemClickListener,
    LoaderManager.LoaderCallbacks<Cursor>,DataBaseMoviesAdapter.GalleryItemClickListener {

  public static final String INTENT_EXTRA_MOVIE = "MOVIE_FROM_API";
  private static final int MOVIE_FROM_DB_REQUEST_CODE = 100;
  private static final int FAVORITE_MOVIES_LOADER_ID = 0;

  @BindView(R.id.main_image_view_empty_state) ImageView imageViewEmptyState;
  @BindView(R.id.main_text_view_empty_state) TextView textViewEmptyState;
  @BindView(R.id.main_empty_state) ConstraintLayout layoutEmptyState;
  @BindView(R.id.main_recycler_view_gallery) RecyclerView galleryRecyclerView;
  @BindView(R.id.main_loading_indicator) ProgressBar progressBar;
  @BindView(R.id.main_button_retry_connection) Button retryButton;

  private List<Movie> movies;
  private ApiService apiService;
  private DataBaseMoviesAdapter dataBaseMoviesAdapter;
  private Cursor cursorFromDatabase;
  private Boolean menuVisibility;
  private Toast toast;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    dataBaseMoviesAdapter = new DataBaseMoviesAdapter(this);
    galleryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    galleryRecyclerView.addItemDecoration(new GalleryItemDecoration(2, 20, true));
    galleryRecyclerView.setHasFixedSize(true);

    apiService = ApiClient.getClient().create(ApiService.class);

    requestPopularMoviesFromApi();
  }

  @Override
  protected void onResume() {
    super.onResume();
    requestFavoriteMoviesFromDataBase();
  }

  private void requestPopularMoviesFromApi() {
    if (isInternetConnected()) {
      hideGallery();
      hideEmptyState();
      showLoading();
      Call<MoviesApiResponse> call = apiService.getPopularMovies(ApiClient.API_KEY);
      call.enqueue(new Callback<MoviesApiResponse>() {
        @Override public void onResponse(@NonNull Call<MoviesApiResponse> call,
            @NonNull Response<MoviesApiResponse> response) {
          if (response.isSuccessful()) {
            if (response.body().getResults() != null && !response.body().getResults().isEmpty()) {
              movies = response.body().getResults();
              setMoviesToApiMoviesAdapter(movies);
              hideLoading();
              showGallery();
              requestFavoriteMoviesFromDataBase();
            } else {
              hideGallery();
              hideLoading();
              prepareMenuToBeInVisible();
              showGenericErrorEmptyState(getString(R.string.gallery_empty_state_generic_error_text));
            }
          } else {
            hideGallery();
            hideLoading();
            prepareMenuToBeInVisible();
            showGenericErrorEmptyState(getString(R.string.gallery_empty_state_generic_error_text));
          }
        }

        @Override
        public void onFailure(@NonNull Call<MoviesApiResponse> call, @NonNull Throwable t) {
          hideGallery();
          hideLoading();
          prepareMenuToBeInVisible();
          showGenericErrorEmptyState(getString(R.string.gallery_empty_state_generic_error_text));
        }
      });
    } else {
      hideGallery();
      invalidateOptionsMenu();
      showNotConnectionEmptyState();
    }
  }

  private void requestTopRatedMoviesFromApi() {
    if (isInternetConnected()) {
      hideGallery();
      hideEmptyState();
      showLoading();
      Call<MoviesApiResponse> call = apiService.getTopRatedMovies(ApiClient.API_KEY);
      call.enqueue(new Callback<MoviesApiResponse>() {
        @Override public void onResponse(@NonNull Call<MoviesApiResponse> call,
            @NonNull Response<MoviesApiResponse> response) {
          if (response.isSuccessful()) {
            if (response.body().getResults() != null && !response.body().getResults().isEmpty()) {
              movies = response.body().getResults();
              setMoviesToApiMoviesAdapter(movies);
              hideLoading();
              showGallery();
              requestFavoriteMoviesFromDataBase();
            } else {
              hideGallery();
              hideLoading();
              prepareMenuToBeInVisible();
              showGenericErrorEmptyState(getString(R.string.gallery_empty_state_generic_error_text));
            }
          } else {
            hideGallery();
            hideLoading();
            prepareMenuToBeInVisible();
            showGenericErrorEmptyState(getString(R.string.gallery_empty_state_generic_error_text));
          }
        }

        @Override
        public void onFailure(@NonNull Call<MoviesApiResponse> call, @NonNull Throwable t) {
          hideGallery();
          hideLoading();
          prepareMenuToBeInVisible();
          showGenericErrorEmptyState(getString(R.string.gallery_empty_state_generic_error_text));
        }
      });
    } else {
      hideGallery();
      invalidateOptionsMenu();
      showNotConnectionEmptyState();
    }
  }

  private void requestFavoriteMoviesFromDataBase() {
    getSupportLoaderManager().restartLoader(FAVORITE_MOVIES_LOADER_ID, null, this);
  }

  private void setMoviesToApiMoviesAdapter(List<Movie> movieList) {
    List<Movie> movies = movieList;
    for (Movie movie : movies) {
      if (existMovieOnDatabase(movie.getId())) {
        movie.setFavorite(true);
      }
    }
    ApiMoviesAdapter apiMoviesAdapter = new ApiMoviesAdapter(this, movies);
    galleryRecyclerView.setAdapter(apiMoviesAdapter);
  }

  private void setMoviesToDataBaseAdapter() {
    galleryRecyclerView.setAdapter(dataBaseMoviesAdapter);
  }

  private boolean isInternetConnected() {
    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    if (netInfo != null && netInfo.isConnected()) {
      prepareMenuToBeVisible();
      return true;
    } else {
      prepareMenuToBeInVisible();
      return false;
    }
  }

  private void showNotConnectionEmptyState() {
    imageViewEmptyState.setImageResource(R.drawable.ic_connection_off_24dp);
    imageViewEmptyState.setContentDescription(
        getString(R.string.gallery_empty_state_not_connection_text));
    textViewEmptyState.setText(R.string.gallery_empty_state_not_connection_text);
    retryButton.setVisibility(View.VISIBLE);
    layoutEmptyState.setVisibility(View.VISIBLE);
  }

  private void showGenericErrorEmptyState(String errorMessage) {
    imageViewEmptyState.setImageResource(R.drawable.ic_error_24dp);
    imageViewEmptyState.setContentDescription(errorMessage);
    textViewEmptyState.setText(errorMessage);
    retryButton.setVisibility(View.VISIBLE);
    layoutEmptyState.setVisibility(View.VISIBLE);
  }

  private void hideEmptyState() {
    layoutEmptyState.setVisibility(View.INVISIBLE);
  }

  private void showLoading() {
    progressBar.setVisibility(View.VISIBLE);
  }

  private void hideLoading() {
    progressBar.setVisibility(View.INVISIBLE);
  }

  private void showGallery() {
    galleryRecyclerView.setVisibility(View.VISIBLE);
  }

  private void hideGallery() {
    galleryRecyclerView.setVisibility(View.INVISIBLE);
  }

  private void prepareMenuToBeVisible() {
    menuVisibility = true;
  }

  private void prepareMenuToBeInVisible() {
    menuVisibility = false;
  }

  private void showToast(String message) {
    if (toast != null) {
      toast.cancel();
    }
    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
    toast.show();
  }

  private Boolean existMovieOnDatabase(int movieId) {
    if (cursorFromDatabase != null) {
      try {
        while (cursorFromDatabase.moveToNext()) {
          int cursorMovieId = cursorFromDatabase.getInt(cursorFromDatabase.getColumnIndex(
              FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID));
          if (cursorMovieId == movieId) {
            return true;
          }
        }
      } finally {
        cursorFromDatabase.close();
      }
    }
    return false;
  }

  private Movie getMovieFromCursorDatabase(int clickedMovieId) {
    Movie movie = new Movie();
    if (cursorFromDatabase != null) {
      try {
        cursorFromDatabase.moveToFirst();
        do {
          int cursorMovieId = cursorFromDatabase.getInt(cursorFromDatabase.getColumnIndex(
              FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID));
          if (cursorMovieId == clickedMovieId) {
            movie.setId(cursorMovieId);
            movie.setVoteAverage(cursorFromDatabase.getDouble(cursorFromDatabase.getColumnIndex(
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RATING)));
            movie.setTitle(cursorFromDatabase.getString(cursorFromDatabase.getColumnIndex(
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE)));
            movie.setPosterPath(cursorFromDatabase.getString(cursorFromDatabase.getColumnIndex(
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH)));
            movie.setBackdropPath(cursorFromDatabase.getString(cursorFromDatabase.getColumnIndex(
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_BACKDROP_PATH)));
            movie.setOverview(cursorFromDatabase.getString(cursorFromDatabase.getColumnIndex(
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW)));
            movie.setReleaseDate(cursorFromDatabase.getString(cursorFromDatabase.getColumnIndex(
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE)));
            movie.setFavorite(true);
            break;
          }
        } while (cursorFromDatabase.moveToNext());
      } finally {
        cursorFromDatabase.close();
      }
    }
    return movie;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    menu.findItem(R.id.action_filter).setVisible(menuVisibility);
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_sort_by_most_popular:
        if (!item.isChecked()) {
          item.setChecked(true);
          requestPopularMoviesFromApi();
        }
        return true;
      case R.id.action_sort_by_top_rated:
        if (!item.isChecked()) {
          item.setChecked(true);
          requestTopRatedMoviesFromApi();
        }
        return true;
      case R.id.action_sort_by_favorites:
        if (!item.isChecked()) {
          item.setChecked(true);
          setMoviesToDataBaseAdapter();
        }
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onItemFromApiClick(int clickedItemIndex) {
    if (movies != null && !movies.isEmpty()) {
      Movie movie = movies.get(clickedItemIndex);
      movie.setFavorite(existMovieOnDatabase(movie.getId()));
      Intent intent = new Intent(this, MovieDetailsActivity.class);
      intent.putExtra(INTENT_EXTRA_MOVIE, movie);
      startActivity(intent);
    }
  }

  @Override
  public void onItemFromDataBaseClick(int clickedMovieId) {
    Movie movie = getMovieFromCursorDatabase(clickedMovieId);
    Intent intent = new Intent(this, MovieDetailsActivity.class);
    intent.putExtra(INTENT_EXTRA_MOVIE, movie);
    startActivityForResult(intent, MOVIE_FROM_DB_REQUEST_CODE);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new AsyncTaskLoader<Cursor>(this) {
      Cursor cursorFavoriteMovies = null;

      @Override protected void onStartLoading() {
        if (cursorFavoriteMovies != null) {
          deliverResult(cursorFavoriteMovies);
        } else {
          forceLoad();
        }
      }

      @Override public Cursor loadInBackground() {
        try {
          return getContentResolver().query(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
              null, null, null, null);
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
      }

      public void deliverResult(Cursor data) {
        cursorFavoriteMovies = data;
        super.deliverResult(data);
      }
    };
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    cursorFromDatabase = data;
    dataBaseMoviesAdapter.swapCursor(cursorFromDatabase);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    dataBaseMoviesAdapter.swapCursor(null);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == MOVIE_FROM_DB_REQUEST_CODE) {
      requestFavoriteMoviesFromDataBase();
    }
  }

  @OnClick(R.id.main_button_retry_connection)
  public void onViewClicked() {
    if (isInternetConnected()) {
      hideEmptyState();
      invalidateOptionsMenu();
      requestPopularMoviesFromApi();
    } else {
      showToast(getString(R.string.gallery_retry_toast_text));
    }
  }
}
