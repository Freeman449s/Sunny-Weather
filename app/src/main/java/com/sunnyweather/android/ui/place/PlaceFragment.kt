package com.sunnyweather.android.ui.place

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunnyweather.android.R
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.databinding.FragmentSearchPlaceBinding

class PlaceFragment : Fragment() {

    lateinit var binding: FragmentSearchPlaceBinding
    private val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(ActivityObserver())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    inner class ActivityObserver : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when (event) {
                Lifecycle.Event.ON_CREATE -> { // 替代onActivityCreated
                    val layoutManager = LinearLayoutManager(activity)
                    binding.recyclerView.layoutManager = layoutManager
                    val adapter = PlaceAdapter(this@PlaceFragment, viewModel.placeList)
                    binding.recyclerView.adapter = adapter
                    binding.searchPlaceEdit.addTextChangedListener {
                        val content = it.toString()
                        if (content.isNotEmpty()) {
                            viewModel.searchPlaces(
                                content,
                                SunnyWeatherApplication.getToken()
                            ) // TODO CHECKPOINT
                        }
                    }
                }
                else -> {}
            }
        }
    }
}