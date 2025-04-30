package com.example.moviesdb.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesdb.data.Result
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.example.moviesdb.databinding.MovieItemBinding

class MoviesAdapter(private val movies: List<Result>): RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int
    ) {
        val movie = movies[position]
        holder.binding.titleTv.text = movie.title
        holder.binding.descTv.text = movie.overview
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
            .into(holder.binding.movieImg)
    }

    override fun getItemCount(): Int = movies.size

}