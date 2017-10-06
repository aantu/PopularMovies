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

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class TrailersGalleryItemDecoration extends RecyclerView.ItemDecoration {

  private final int spanCount;
  private final int spacing;

  TrailersGalleryItemDecoration(int spacing) {
    this.spanCount = 1;
    this.spacing = spacing;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    int position = parent.getChildAdapterPosition(view);
    int column = position % spanCount;

    outRect.left = spacing - column * spacing / spanCount;
    outRect.right = (column + 1) * spacing / spanCount;
  }
}
