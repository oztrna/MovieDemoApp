package com.oztrna.moviedemoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.oztrna.moviedemoapp.R
import com.oztrna.moviedemoapp.model.Result
import com.oztrna.moviedemoapp.util.downloadFromUrl
import com.oztrna.moviedemoapp.util.placeholderProgressBar
import com.oztrna.moviedemoapp.view.FeedFragmentDirections
import kotlinx.android.synthetic.main.row_feed.view.*

class FeedAdapter(private val list: ArrayList<Result>): RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    private val IMAGE_PATH = "https://image.tmdb.org/t/p/w185"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_feed, parent, false)
        return FeedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.itemView.movie_name.text = list[position].title
        holder.itemView.movie_description.text = list[position].overview

        holder.itemView.movie_image.downloadFromUrl(IMAGE_PATH + list[position].poster_path, placeholderProgressBar(holder.itemView.context))

        holder.itemView.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToMovieDetailFragment(list[position].id)
            Navigation.findNavController(it).navigate(action)
        }
    }

    fun updateList(updatedList: List<Result>) {
        list.clear()
        list.addAll(updatedList)
        notifyDataSetChanged()
    }

    fun addPaginationList(updatedList: List<Result>) {
        list.addAll(updatedList)
        notifyDataSetChanged()
    }


    class FeedViewHolder(view: View): RecyclerView.ViewHolder(view) { }

}