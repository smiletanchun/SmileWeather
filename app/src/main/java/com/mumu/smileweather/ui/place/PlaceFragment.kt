package com.mumu.smileweather.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mumu.smileweather.databinding.FragmentPlaceBinding

class PlaceFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    private lateinit var fragmentPlaceBinding: FragmentPlaceBinding
    private lateinit var placeAdapter: PlaceAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPlaceBinding = FragmentPlaceBinding.inflate(inflater)
        return fragmentPlaceBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(context)
        placeAdapter = PlaceAdapter(this, viewModel.placeList)
        fragmentPlaceBinding.recyclerView.layoutManager = linearLayoutManager
        fragmentPlaceBinding.recyclerView.adapter = placeAdapter

        fragmentPlaceBinding.searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            } else {
                fragmentPlaceBinding.recyclerView.visibility = View.GONE
                fragmentPlaceBinding.bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                placeAdapter.notifyDataSetChanged()
            }
        }

        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
            val places = result.getOrNull()
            if (places != null) {
                fragmentPlaceBinding.recyclerView.visibility = View.VISIBLE
                fragmentPlaceBinding.bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                placeAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}