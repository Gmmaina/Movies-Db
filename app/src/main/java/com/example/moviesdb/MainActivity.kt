package com.example.moviesdb

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesdb.adapter.MoviesAdapter
import com.example.moviesdb.data.MovieModel
import com.example.moviesdb.databinding.ActivityMainBinding
import com.example.moviesdb.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val apiKey = "c89168cb22d9a8b1982f7cb49aabe573"
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        fetchPopularMovies()
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
