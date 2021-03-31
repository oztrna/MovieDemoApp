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
import com.oztrna.moviedemoapp.adapter.CreditAdapter
import com.oztrna.moviedemoapp.databinding.FragmentPersonDetailBinding
import com.oztrna.moviedemoapp.util.downloadFromUrl
import com.oztrna.moviedemoapp.util.placeholderProgressBar
import com.oztrna.moviedemoapp.viewmodel.PersonDetailViewModel
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import kotlinx.android.synthetic.main.fragment_person_detail.*

class PersonDetailFragment : Fragment() {

    private val IMAGE_PATH = "https://image.tmdb.org/t/p/w780"

    private lateinit var dataBinding: FragmentPersonDetailBinding
    private lateinit var viewModel: PersonDetailViewModel

    private var adapter = CreditAdapter(arrayListOf())

    private var actorId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_person_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            actorId = PersonDetailFragmentArgs.fromBundle(it).actorId
        }

        viewModel = ViewModelProviders.of(this).get(PersonDetailViewModel::class.java)
        viewModel.refreshData(actorId)

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.detail.observe(viewLifecycleOwner, Observer {
            dataBinding.actorDetail = it

            context?.let {context ->
                actor_detail_image.downloadFromUrl(IMAGE_PATH + it.profile_path, placeholderProgressBar(context))
            }
        })

        viewModel.credits.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter = CreditAdapter(it)
                actor_credits_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                actor_credits_list.adapter = adapter
            }
        })
    }
}