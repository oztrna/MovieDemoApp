package com.oztrna.moviedemoapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oztrna.moviedemoapp.R
import com.oztrna.moviedemoapp.adapter.FeedAdapter
import com.oztrna.moviedemoapp.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.fragment_feed.*


class FeedFragment : Fragment() {

    private lateinit var viewModel : FeedViewModel
    private val movieAdapter = FeedAdapter(arrayListOf())

    private var page = 1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        viewModel.refreshData(page)

        feed_list.layoutManager = LinearLayoutManager(context)
        feed_list.adapter = movieAdapter

        observeLiveData()

        checkRecyclerViewPagination()

        btn_search?.setOnClickListener {
            val query = feed_search.text.toString()

            if (query.isEmpty()) {
                page = 1
                viewModel.refreshData(page)
            } else {
                viewModel.refreshData(page, query)
            }
        }
    }

    private fun observeLiveData() {
        viewModel.movies.observe(viewLifecycleOwner, Observer {movies ->
            movies?.let {
                if (isLoading) {
                    movieAdapter.addPaginationList(it)
                    isLoading = false
                } else {
                    movieAdapter.updateList(it)
                }
            }
        })

        viewModel.movieError.observe(viewLifecycleOwner, Observer {error ->
            error?.let {
                if (it) {
                    feed_error_text.visibility = View.VISIBLE
                } else {
                    feed_error_text.visibility = View.GONE
                }
            }
        })

        viewModel.movieLoading.observe(viewLifecycleOwner, Observer {loading ->

            loading?.let {
                if (it) {
                    feed_loading.visibility = View.VISIBLE
                    feed_list.visibility = View.GONE
                    feed_error_text.visibility = View.GONE
                } else {
                    feed_loading.visibility = View.GONE
                    feed_list.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun checkRecyclerViewPagination() {

        feed_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    isLoading = true
                    page++
                    viewModel.refreshData(page)
                }
            }
        })
    }
}