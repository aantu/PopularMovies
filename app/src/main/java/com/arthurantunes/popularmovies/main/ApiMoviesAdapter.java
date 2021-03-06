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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arthurantunes.popularmovies.R;
import com.arthurantunes.popularmovies.api.ApiClient;
import com.arthurantunes.popularmovies.model.Movie;
import com.bumptech.glide.Glide;
import java.util.List;

class ApiMoviesAdapter extends
    RecyclerView.Adapter<ApiMoviesAdapter.GalleryViewHolder> {

  private final GalleryItemClickListener clickListener;
  private final List<Movie> movies;

  ApiMoviesAdapter(GalleryItemClickListener clickListener, List<Movie> movies) {
    this.clickListener = clickListener;
    this.movies = movies;
  }

  @Override
  public ApiMoviesAdapter.GalleryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    View view = inflater.inflate(R.layout.gallery_item, viewGroup, false);
    return new GalleryViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ApiMoviesAdapter.GalleryViewHolder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return movies.size();
  }

  interface GalleryItemClickListener {
    void onItemFromApiClick(int clickedItemIndex);
  }

  class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.movie_gallery_item_image_view) ImageView moviePoster;

    GalleryViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    void bind(int listIndex) {
      String posterPath = ApiClient.POSTER_IMAGE_BASE_URL
          + ApiClient.POSTER_IMAGE_SIZE + movies.get(listIndex).getPosterPath();

      Glide.with(itemView.getContext()).load(posterPath).into(moviePoster);
    }

    @Override public void onClick(View v) {
      int clickedPosition = getAdapterPosition();
      clickListener.onItemFromApiClick(clickedPosition);
    }
  }
}
