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

public class MovieTrailer implements Parcelable {

  public static final Parcelable.Creator<MovieTrailer> CREATOR = new Parcelable.Creator<MovieTrailer>() {
    @Override public MovieTrailer createFromParcel(Parcel source) {
      return new MovieTrailer(source);
    }

    @Override public MovieTrailer[] newArray(int size) {
      return new MovieTrailer[size];
    }
  };
  @SerializedName("name")
  private String trailerName;
  @SerializedName("key")
  private String trailerKey;
  @SerializedName("site")
  private String trailerSite;

  private MovieTrailer(Parcel in) {
    this.trailerName = in.readString();
    this.trailerKey = in.readString();
    this.trailerSite = in.readString();
  }

  public String getTrailerName() {
    return trailerName;
  }

  public void setTrailerName(String trailerName) {
    this.trailerName = trailerName;
  }

  public String getTrailerKey() {
    return trailerKey;
  }

  public void setTrailerKey(String trailerKey) {
    this.trailerKey = trailerKey;
  }

  public String getTrailerSite() {
    return trailerSite;
  }

  public void setTrailerSite(String trailerSite) {
    this.trailerSite = trailerSite;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.trailerName);
    dest.writeString(this.trailerKey);
    dest.writeString(this.trailerSite);
  }
}
