package com.oztrna.moviedemoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.oztrna.moviedemoapp.R
import com.oztrna.moviedemoapp.model.Cast
import com.oztrna.moviedemoapp.model.CastX
import com.oztrna.moviedemoapp.model.Result
import com.oztrna.moviedemoapp.util.downloadFromUrl
import com.oztrna.moviedemoapp.util.placeholderProgressBar
import com.oztrna.moviedemoapp.view.MovieDetailFragmentDirections
import com.oztrna.moviedemoapp.view.PersonDetailFragmentDirections
import kotlinx.android.synthetic.main.row_cast.view.*
import kotlinx.android.synthetic.main.row_credits.view.*

class CreditAdapter(private val list: List<CastX>): RecyclerView.Adapter<CreditAdapter.ViewHolder>() {

    private val IMAGE_PATH = "https://image.tmdb.org/t/p/w185"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_credits, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.credit_image.downloadFromUrl(IMAGE_PATH + list[position].backdrop_path, placeholderProgressBar(holder.itemView.context))
        holder.itemView.credit_name.text = list[position].title

        holder.itemView.setOnClickListener {
            val action = PersonDetailFragmentDirections.actionPersonDetailFragmentToMovieDetailFragment(list[position].id)
            Navigation.findNavController(it).navigate(action)
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)
}