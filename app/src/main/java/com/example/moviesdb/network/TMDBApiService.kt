package com.example.moviesdb.network

import com.example.moviesdb.data.MovieModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApiService {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
    ): Call<MovieModel>
}