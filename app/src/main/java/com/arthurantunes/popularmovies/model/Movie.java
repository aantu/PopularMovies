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

package com.arthurantunes.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Movie implements Parcelable {

  public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
    @Override
    public Movie createFromParcel(Parcel source) {
      return new Movie(source);
    }

    @Override
    public Movie[] newArray(int size) {
      return new Movie[size];
    }
  };
  private Integer id;
  @SerializedName("vote_average")
  private Double voteAverage;
  @SerializedName("title")
  private String title;
  @SerializedName("poster_path")
  private String posterPath;
  @SerializedName("original_title")
  private String originalTitle;
  @SerializedName("backdrop_path")
  private String backdropPath;
  @SerializedName("overview")
  private String overview;
  @SerializedName("release_date")
  private String releaseDate;
  private List<MovieTrailer> trailers;
  private List<MovieReview> reviews;
  private Boolean isFavorite = false;

  public Movie() {}

  private Movie(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.voteAverage = (Double) in.readValue(Double.class.getClassLoader());
    this.title = in.readString();
    this.posterPath = in.readString();
    this.originalTitle = in.readString();
    this.backdropPath = in.readString();
    this.overview = in.readString();
    this.releaseDate = in.readString();
    this.isFavorite = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.trailers = in.createTypedArrayList(MovieTrailer.CREATOR);
    this.reviews = in.createTypedArrayList(MovieReview.CREATOR);
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Double getVoteAverage() {
    return voteAverage;
  }

  public void setVoteAverage(Double voteAverage) {
    this.voteAverage = voteAverage;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPosterPath() {
    return posterPath;
  }

  public void setPosterPath(String posterPath) {
    this.posterPath = posterPath;
  }

  public String getOriginalTitle() {
    return originalTitle;
  }

  public void setOriginalTitle(String originalTitle) {
    this.originalTitle = originalTitle;
  }

  public String getBackdropPath() {
    return backdropPath;
  }

  public void setBackdropPath(String backdropPath) {
    this.backdropPath = backdropPath;
  }

  public String getOverview() {
    return overview;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  public List<MovieTrailer> getTrailers() {
    return trailers;
  }

  public void setTrailers(List<MovieTrailer> trailers) {
    this.trailers = trailers;
  }

  public List<MovieReview> getReviews() {
    return reviews;
  }

  public void setReviews(List<MovieReview> reviews) {
    this.reviews = reviews;
  }

  public Boolean isFavorite() {
    return isFavorite;
  }

  public void setFavorite(Boolean favorite) {
    isFavorite = favorite;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeValue(this.voteAverage);
    dest.writeString(this.title);
    dest.writeString(this.posterPath);
    dest.writeString(this.originalTitle);
    dest.writeString(this.backdropPath);
    dest.writeString(this.overview);
    dest.writeString(this.releaseDate);
    dest.writeValue(this.isFavorite);
    dest.writeTypedList(this.trailers);
    dest.writeTypedList(this.reviews);
  }
}
