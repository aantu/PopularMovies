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

public class MovieReview implements Parcelable {

  public static final Parcelable.Creator<MovieReview> CREATOR =
      new Parcelable.Creator<MovieReview>() {
        @Override public MovieReview createFromParcel(Parcel source) {
          return new MovieReview(source);
        }

        @Override public MovieReview[] newArray(int size) {
          return new MovieReview[size];
        }
      };
  private String author;
  private String content;

  private MovieReview(Parcel in) {
    this.author = in.readString();
    this.content = in.readString();
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.author);
    dest.writeString(this.content);
  }
}
