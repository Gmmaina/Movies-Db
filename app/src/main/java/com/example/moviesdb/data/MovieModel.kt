package com.example.moviesdb.data

data class MovieModel(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)