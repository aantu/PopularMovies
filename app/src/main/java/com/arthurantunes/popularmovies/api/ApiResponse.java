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

package com.arthurantunes.popularmovies.api;

import com.arthurantunes.popularmovies.model.Movie;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse {

  @SerializedName("page")
  private Integer page;
  @SerializedName("total_results")
  private Integer totalResults;
  @SerializedName("total_pages")
  private Integer totalPages;
  @SerializedName("results")
  private List<Movie> results;

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Integer getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(Integer totalResults) {
    this.totalResults = totalResults;
  }

  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public List<Movie> getResults() {
    return results;
  }

  public void setResults(List<Movie> results) {
    this.results = results;
  }
}
