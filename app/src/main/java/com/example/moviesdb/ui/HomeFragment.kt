package com.example.moviesdb.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesdb.R
import com.example.moviesdb.adapter.MoviesAdapter
import com.example.moviesdb.data.MovieModel
import com.example.moviesdb.databinding.FragmentHomeBinding
import com.example.moviesdb.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val apiKey = "c89168cb22d9a8b1982f7cb49aabe573"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        fetchPopularMovies()

        return binding.root
    }

    private fun fetchPopularMovies() {
        RetrofitInstance.api.getPopularMovies(apiKey)
            .enqueue(object : Callback<MovieModel> {
                override fun onResponse(call: Call<MovieModel>, response: Response<MovieModel>) {
                    if (response.isSuccessful) {
                        val movies = response.body()?.results ?: emptyList()
                        binding.recyclerView.adapter = MoviesAdapter(movies)
                    } else {
                        binding.failedTv.text = "Failed to fetch movies"
                        binding.failedTv.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<MovieModel>, t: Throwable) {
                    binding.failedTv.text = "Error fetching movies: ${t.message}"
                    binding.failedTv.visibility = View.VISIBLE
                }
            })
    }


}