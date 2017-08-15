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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.arthurantunes.popularmovies.R;
import com.arthurantunes.popularmovies.api.ApiClient;
import com.arthurantunes.popularmovies.main.MainActivity;
import com.arthurantunes.popularmovies.model.Movie;
import com.bumptech.glide.Glide;

public class MovieDetailsActivity extends AppCompatActivity {

  private Movie movie;
  private ImageView moviePoster;
  private TextView movieTitle;
  private TextView movieReleaseDate;
  private TextView movieRating;
  private TextView movieOverview;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_details);

    moviePoster = (ImageView) findViewById(R.id.movie_detail_poster);
    movieTitle = (TextView) findViewById(R.id.movie_detail_title);
    movieReleaseDate = (TextView) findViewById(R.id.movie_detail_release_date);
    movieRating = (TextView) findViewById(R.id.movie_detail_rating);
    movieOverview = (TextView) findViewById(R.id.movie_detail_overview);

    getMovieDataFromIntent();
    setupMovieDataToViews();

  }

  private void getMovieDataFromIntent() {
    if (getIntent().hasExtra(MainActivity.INTENT_EXTRA_MOVIE)) {
      movie = getIntent().getParcelableExtra(MainActivity.INTENT_EXTRA_MOVIE);
    }
  }

  private void setupMovieDataToViews() {
    if (movie != null) {
      String posterPath = ApiClient.POSTER_IMAGE_BASE_URL + ApiClient.POSTER_IMAGE_SIZE + movie.getPosterPath();
      Glide.with(this).load(posterPath).into(moviePoster);
      movieTitle.setText(movie.getTitle());
      movieReleaseDate.setText(movie.getReleaseDate());
      movieRating.setText(String.valueOf(movie.getVoteAverage()));
      movieOverview.setText(movie.getOverview());
    }
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
