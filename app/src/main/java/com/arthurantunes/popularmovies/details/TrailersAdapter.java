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
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arthurantunes.popularmovies.R;
import com.arthurantunes.popularmovies.api.ApiClient;
import com.arthurantunes.popularmovies.model.Movie;
import com.arthurantunes.popularmovies.model.MovieTrailer;
import com.bumptech.glide.Glide;
import java.util.List;

class TrailersAdapter
    extends RecyclerView.Adapter<TrailersAdapter.GalleryViewHolder> {

  private final TrailersGalleryItemClickListener clickListener;
  private final Movie movie;
  private final List<MovieTrailer> movieTrailers;

  TrailersAdapter(TrailersGalleryItemClickListener clickListener, Movie movie) {
    this.clickListener = clickListener;
    this.movie = movie;
    this.movieTrailers = movie.getTrailers();
  }

  @Override
  public TrailersAdapter.GalleryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    View view = inflater.inflate(R.layout.trailers_gallery_item, viewGroup, false);
    return new GalleryViewHolder(view);
  }

  @Override
  public void onBindViewHolder(TrailersAdapter.GalleryViewHolder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return movieTrailers.size();
  }

  interface TrailersGalleryItemClickListener {
    void onTrailerItemClick(int clickedItemIndex);
  }

  class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.trailer_gallery_item_image_view) ImageView movieTrailerPoster;
    @BindView(R.id.trailer_gallery_item_text_view) TextView movieTrailerTitle;

    GalleryViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    void bind(int listIndex) {
      String trailerTitle = itemView.getResources()
          .getString(R.string.movie_details_trailer_text, listIndex);

      movieTrailerTitle.setText(trailerTitle);

      String posterPath = ApiClient.POSTER_IMAGE_BASE_URL
          + ApiClient.POSTER_IMAGE_SIZE + movie.getBackdropPath();

      Glide.with(itemView.getContext())
          .load(posterPath)
          .into(movieTrailerPoster);
    }

    @Override public void onClick(View v) {
      int clickedPosition = getAdapterPosition();
      clickListener.onTrailerItemClick(clickedPosition);
    }
  }
}
