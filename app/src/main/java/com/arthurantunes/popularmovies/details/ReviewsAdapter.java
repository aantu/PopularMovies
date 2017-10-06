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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arthurantunes.popularmovies.R;
import com.arthurantunes.popularmovies.model.Movie;
import com.arthurantunes.popularmovies.model.MovieReview;
import java.util.List;

class ReviewsAdapter
    extends RecyclerView.Adapter<ReviewsAdapter.GalleryViewHolder> {

  private final Movie movie;
  private final List<MovieReview> movieReviews;

  ReviewsAdapter(Movie movie) {
    this.movie = movie;
    this.movieReviews = movie.getReviews();
  }

  @Override
  public ReviewsAdapter.GalleryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    View view = inflater.inflate(R.layout.reviews_gallery_item, viewGroup, false);
    return new GalleryViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ReviewsAdapter.GalleryViewHolder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return movieReviews.size();
  }

  class GalleryViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.review_gallery_item_review_author_text_view) TextView movieReviewAuthor;
    @BindView(R.id.review_gallery_item_review_text_view) TextView movieReviewText;

    GalleryViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    void bind(int listIndex) {
      movieReviewAuthor.setText(movie.getReviews().get(listIndex).getAuthor());
      movieReviewText.setText(movie.getReviews().get(listIndex).getContent());
    }
  }
}
