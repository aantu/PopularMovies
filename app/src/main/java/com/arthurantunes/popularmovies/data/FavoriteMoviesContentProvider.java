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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FavoriteMoviesContentProvider extends ContentProvider {

  private static final int FAVORITE_MOVIES = 100;
  private static final int FAVORITE_MOVIE_WITH_ID = 101;
  private static final UriMatcher uriMatcher = buildUriMatcher();
  private FavoriteMoviesDbHelper favoriteMoviesDbHelper;

  private static UriMatcher buildUriMatcher() {
    UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY,
        FavoriteMoviesContract.PATH_FAVORITE_MOVIES, FAVORITE_MOVIES);

    uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY,
        FavoriteMoviesContract.PATH_FAVORITE_MOVIES + "/#", FAVORITE_MOVIE_WITH_ID);

    return uriMatcher;
  }

  @Override
  public boolean onCreate() {
    this.favoriteMoviesDbHelper = new FavoriteMoviesDbHelper(getContext());
    return true;
  }

  @Nullable @Override
  public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
      @Nullable String[] selectionArgs, @Nullable String sortOrder) {
    final SQLiteDatabase db = favoriteMoviesDbHelper.getReadableDatabase();
    Cursor returnCursor;
    int match = uriMatcher.match(uri);

    switch (match) {
      case FAVORITE_MOVIES:
        returnCursor =  db.query(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder);
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
    return returnCursor;
  }

  @Nullable @Override
  public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
    final SQLiteDatabase db = favoriteMoviesDbHelper.getWritableDatabase();
    Uri returnUri;
    int match = uriMatcher.match(uri);

    switch (match) {
      case FAVORITE_MOVIES:
        long id = db.insert(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME, null, values);
        if ( id > 0 ) {
          returnUri = ContentUris.withAppendedId(
              FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, id);
        } else {
          throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return returnUri;
  }

  @Override
  public int delete(@NonNull Uri uri, @Nullable String selection,
      @Nullable String[] selectionArgs) {
    final SQLiteDatabase db = favoriteMoviesDbHelper.getWritableDatabase();
    int movieDeleted;
    int match = uriMatcher.match(uri);

    switch (match) {
      case FAVORITE_MOVIE_WITH_ID:
        String id = uri.getPathSegments().get(1);
        movieDeleted = db.delete(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
            FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID.concat("=?"),
            new String[]{id});
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    if (movieDeleted != 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return movieDeleted;
  }

  @Override
  public int
  update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
      @Nullable String[] selectionArgs) {
    return 0;
  }

  @Nullable @Override
  public String getType(@NonNull Uri uri) {
    return null;
  }

}
