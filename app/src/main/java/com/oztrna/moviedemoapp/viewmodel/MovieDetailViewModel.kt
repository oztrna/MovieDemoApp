package com.oztrna.moviedemoapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oztrna.moviedemoapp.model.Actors
import com.oztrna.moviedemoapp.model.Cast
import com.oztrna.moviedemoapp.model.MovieDetail
import com.oztrna.moviedemoapp.model.MovieVideo
import com.oztrna.moviedemoapp.service.MovieAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MovieDetailViewModel: ViewModel() {

    private val movieApiService = MovieAPIService()
    private val disposable = CompositeDisposable()
    var actors = MutableLiveData<List<Cast>>()
    var detail = MutableLiveData<MovieDetail>()
    var videos = MutableLiveData<MovieVideo>()

    var actorsLoading = MutableLiveData<Boolean>()
    var movieDetailLoading = MutableLiveData<Boolean>()

    fun refreshData(movieId: Int) {
        getDataFromAPI(movieId)
    }

    private fun getDataFromAPI(movieId: Int) {
        actorsLoading.value = true
        movieDetailLoading.value = true

        disposable.add(
            movieApiService.getActors(movieId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Actors>() {
                    override fun onSuccess(t: Actors) {
                        actors.value = t.cast
                        actorsLoading.value = false
                    }

                    override fun onError(e: Throwable) {
                        actorsLoading.value = false
                    }
                })
        )

        disposable.add(
            movieApiService.getMovieDetail(movieId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<MovieDetail>() {
                    override fun onSuccess(t: MovieDetail) {
                        detail.value = t
                        movieDetailLoading.value = false
                    }

                    override fun onError(e: Throwable) {
                        movieDetailLoading.value = false
                    }
                })
        )

        disposable.add(
            movieApiService.getMovieVideos(movieId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<MovieVideo>() {
                    override fun onSuccess(t: MovieVideo) {
                        videos.value = t
                    }

                    override fun onError(e: Throwable) {
                    }
                })
        )
    }

    override fun onCleared() {
        disposable.clear()
    }

}