package com.oztrna.moviedemoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.oztrna.moviedemoapp.R
import com.oztrna.moviedemoapp.adapter.CastAdapter
import com.oztrna.moviedemoapp.adapter.VideosAdapter
import com.oztrna.moviedemoapp.databinding.FragmentMovieDetailBinding
import com.oztrna.moviedemoapp.util.downloadFromUrl
import com.oztrna.moviedemoapp.util.placeholderProgressBar
import com.oztrna.moviedemoapp.viewmodel.MovieDetailViewModel
import kotlinx.android.synthetic.main.fragment_movie_detail.*

class MovieDetailFragment : Fragment() {

    private val IMAGE_PATH = "https://image.tmdb.org/t/p/w780"

    private var movieId = 0
    private var adapter = CastAdapter(arrayListOf())
    private var videosAdapter = VideosAdapter(arrayListOf())

    private lateinit var dataBinding: FragmentMovieDetailBinding
    private lateinit var viewModel : MovieDetailViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            movieId = MovieDetailFragmentArgs.fromBundle(it).movieId
        }

        viewModel = ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)
        viewModel.refreshData(movieId)

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.actors.observe(viewLifecycleOwner, Observer {
            adapter = CastAdapter(it)
            cast_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            cast_list.adapter = adapter
        })

        viewModel.detail.observe(viewLifecycleOwner, Observer {
            dataBinding.movieDetail = it

            context?.let {context ->
                detail_image.downloadFromUrl(IMAGE_PATH + it.backdrop_path, placeholderProgressBar(context))
            }

        })

        viewModel.movieDetailLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                movie_detail_container.visibility = View.GONE
                movie_detail_loading.visibility = View.VISIBLE
            } else {
                movie_detail_container.visibility = View.VISIBLE
                movie_detail_loading.visibility = View.GONE
            }
        })

        viewModel.actorsLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                cast_list.visibility = View.GONE
            } else {
                cast_list.visibility = View.VISIBLE
            }
        })

        viewModel.videos.observe(viewLifecycleOwner, Observer {
            videosAdapter = VideosAdapter(it.results)
            actor_videos_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            actor_videos_list.adapter = videosAdapter
        })
    }

}