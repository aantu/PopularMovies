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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.arthurantunes.popularmovies.BuildConfig;
import com.arthurantunes.popularmovies.R;
import com.arthurantunes.popularmovies.api.ApiClient;
import com.arthurantunes.popularmovies.api.ApiResponse;
import com.arthurantunes.popularmovies.api.ApiService;
import com.arthurantunes.popularmovies.details.MovieDetailsActivity;
import com.arthurantunes.popularmovies.model.Movie;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
    implements GalleryAdapter.GalleryItemClickListener, View.OnClickListener {

  public static final String INTENT_EXTRA_MOVIE = "MOVIE";
  private static final String TAG = MainActivity.class.getSimpleName();
  private List<Movie> movies;
  private RecyclerView galleryRecyclerView;
  private ApiService apiService;
  private ProgressBar progressBar;
  private ConstraintLayout layoutEmptyState;
  private ImageView imageViewEmptyState;
  private TextView textViewEmptyState;
  private Boolean menuVisibility;
  private Toast toast;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button buttonRetryConnection = (Button) findViewById(R.id.main_button_retry_connection);
    buttonRetryConnection.setOnClickListener(this);

    progressBar = (ProgressBar) findViewById(R.id.main_loading_indicator);
    layoutEmptyState = (ConstraintLayout) findViewById(R.id.main_empty_state);
    imageViewEmptyState = (ImageView) findViewById(R.id.main_image_view_empty_state);
    textViewEmptyState = (TextView) findViewById(R.id.main_text_view_empty_state);

    galleryRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view_gallery);
    galleryRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
    galleryRecyclerView.addItemDecoration(new GalleryItemDecoration(2, 20, true));
    galleryRecyclerView.setHasFixedSize(true);

    apiService = ApiClient.getClient().create(ApiService.class);

    requestPopularMoviesFromApi();
  }

  private void requestPopularMoviesFromApi() {
    if (isInternetConnected()) {
      hideGallery();
      showLoading();
      Call<ApiResponse> call = apiService.getPopularMovies(ApiClient.API_KEY);
      call.enqueue(new Callback<ApiResponse>() {
        @Override public void onResponse(@NonNull Call<ApiResponse> call,
            @NonNull Response<ApiResponse> response) {
          if (response.isSuccessful()) {
            if (response.body().getResults() != null && !response.body().getResults().isEmpty()) {
              movies = response.body().getResults();
              setMoviesToGalleryAdapter(movies);
              hideLoading();
              showGallery();
            } else {
              hideGallery();
              hideLoading();
              prepareMenuToBeInVisible();
              showGenericErrorEmptyState();
            }
          } else {
            hideGallery();
            hideLoading();
            prepareMenuToBeInVisible();
            showGenericErrorEmptyState();
          }
        }

        @Override public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
          Log.e(TAG, t.toString());
          hideGallery();
          hideLoading();
          prepareMenuToBeInVisible();
          showGenericErrorEmptyState();
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
      showLoading();
      Call<ApiResponse> call = apiService.getTopRatedMovies(ApiClient.API_KEY);
      call.enqueue(new Callback<ApiResponse>() {
        @Override public void onResponse(@NonNull Call<ApiResponse> call,
            @NonNull Response<ApiResponse> response) {
          if (response.isSuccessful()) {
            if (response.body().getResults() != null && !response.body().getResults().isEmpty()) {
              movies = response.body().getResults();
              setMoviesToGalleryAdapter(movies);
              hideLoading();
              showGallery();
            } else {
              hideGallery();
              hideLoading();
              prepareMenuToBeInVisible();
              showGenericErrorEmptyState();
            }
          } else {
            hideGallery();
            hideLoading();
            prepareMenuToBeInVisible();
            showGenericErrorEmptyState();
          }
        }

        @Override public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
          Log.e(TAG, t.toString());
          hideGallery();
          hideLoading();
          prepareMenuToBeInVisible();
          showGenericErrorEmptyState();
        }
      });
    } else {
      hideGallery();
      invalidateOptionsMenu();
      showNotConnectionEmptyState();
    }
  }

  private void setMoviesToGalleryAdapter(List<Movie> movieList) {
    GalleryAdapter galleryAdapter = new GalleryAdapter(this, movieList);
    galleryRecyclerView.setAdapter(galleryAdapter);
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
    imageViewEmptyState.setContentDescription(getString(R.string.gallery_empty_state_not_connection_text));
    textViewEmptyState.setText(R.string.gallery_empty_state_not_connection_text);
    layoutEmptyState.setVisibility(View.VISIBLE);
    layoutEmptyState.setVisibility(View.VISIBLE);
  }

  private void showGenericErrorEmptyState() {
    imageViewEmptyState.setImageResource(R.drawable.ic_error_24dp);
    imageViewEmptyState.setContentDescription(getString(R.string.gallery_empty_state_generic_error_text));
    textViewEmptyState.setText(R.string.gallery_empty_state_generic_error_text);
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

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    menu.findItem(R.id.action_filter).setVisible(menuVisibility);
    return  super.onPrepareOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
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
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override public void onGalleryItemClick(int clickedItemIndex) {
    if (movies != null && !movies.isEmpty()) {
      Intent intent = new Intent(this, MovieDetailsActivity.class);
      intent.putExtra(INTENT_EXTRA_MOVIE, movies.get(clickedItemIndex));
      startActivity(intent);
    }
  }

  @Override public void onClick(View v) {
    if (isInternetConnected()) {
      hideEmptyState();
      invalidateOptionsMenu();
      requestPopularMoviesFromApi();
    } else {
      showToast(getString(R.string.gallery_retry_toast_text));
    }
  }
}
