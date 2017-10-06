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

package com.arthurantunes.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class FavoriteMoviesDbHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "FavoritesMovies.db";
  private static final int DATABASE_VERSION = 1;

  FavoriteMoviesDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    final String SQL_CREATE_FAVORITE_MOVIES_TABLE = "CREATE TABLE " +
        FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME + " (" +
        FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY," +
        FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE + " TEXT NOT NULL," +
        FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
        FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RATING+ " DOUBLE NOT NULL," +
        FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE+" TEXT NOT NULL," +
        FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
        FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL );";

    db.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME);
    onCreate(db);
  }
}

