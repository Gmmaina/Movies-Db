package com.example.moviesdb.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesdb.adapter.MoviesAdapter
import com.example.moviesdb.data.MovieModel
import com.example.moviesdb.data.Result
import com.example.moviesdb.databinding.FragmentUpcomingMoviesBinding
import com.example.moviesdb.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingMoviesFragment : Fragment() {
    private lateinit var binding: FragmentUpcomingMoviesBinding
    private val apiKey = "c89168cb22d9a8b1982f7cb49aabe573"

    private var currentPage = 1
    private var totalPages = 1
    private val moviesList = mutableListOf<Result>()
    private var isLoading = false
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpcomingMoviesBinding.inflate(inflater, container, false)

        setUpRecyclerView()
        fetchUpcomingMovies(currentPage)

        return binding.root
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        moviesAdapter = MoviesAdapter(moviesList)
        binding.recyclerView.adapter = moviesAdapter

        binding.recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && currentPage < totalPages && lastVisibleItem >= totalItemCount - 5) {
                    fetchUpcomingMovies(++currentPage)
                }
            }
        })
    }

    private fun fetchUpcomingMovies(page: Int) {
        isLoading = true
        binding.loading.visibility = View.VISIBLE
        RetrofitInstance.api.getUpcomingMovies(apiKey, page)
            .enqueue(object : Callback<MovieModel> {
                override fun onResponse(call: Call<MovieModel>, response: Response<MovieModel>) {
                    binding.loading.visibility = View.GONE
                    isLoading = false

                    if (response.isSuccessful) {
                        val movies = response.body()
                        totalPages = response.body()?.total_pages ?: 1

                        val newMovies = movies?.results ?: emptyList()
                        moviesList.addAll(newMovies)
                        moviesAdapter.notifyItemRangeChanged(moviesList.size - newMovies.size, newMovies.size)


                    } else {
                        binding.failedTv.text = "Failed to fetch movies"
                        binding.failedTv.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<MovieModel>, t: Throwable) {
                    binding.loading.visibility = View.GONE
                    isLoading = false
                    binding.failedTv.text = "Error fetching movies: ${t.message}"
                    binding.failedTv.visibility = View.VISIBLE
                }
            })
    }


}