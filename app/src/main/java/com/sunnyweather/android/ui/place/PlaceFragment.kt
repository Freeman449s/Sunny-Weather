package com.sunnyweather.android.ui.place

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunnyweather.android.R
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.ToastUtils
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

                    // 搜索框回调函数
                    binding.searchPlaceEdit.addTextChangedListener {
                        val originalLength = it.toString().length
                        val content = it.toString().trim()
                        if (content.length != originalLength) {
                            // 去除可能存在的换行符
                            // 会再次回调到TextChangedListener()
                            binding.searchPlaceEdit.setText(content)
                        }
                        if (content.isNotEmpty()) {
                            viewModel.searchPlaces(
                                content,
                                SunnyWeatherApplication.getToken()
                            )
                        } else {
                            binding.recyclerView.visibility = View.GONE
                            binding.bgImageView.visibility = View.VISIBLE
                            viewModel.placeList.clear()
                            // Adapter的placeList尽管是私有的，但是和ViewModel中的placeList是同一个
                            adapter.notifyDataSetChanged()
                        }
                    }

                    // viewModel.placeList的观察函数
                    viewModel.placeLiveData.observe(this@PlaceFragment) {
                        val placeList = it.getOrNull()
                        // UI层的数据操作
                        if (placeList != null) {
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.bgImageView.visibility = View.GONE
                            viewModel.placeList.clear()
                            viewModel.placeList.addAll(placeList)
                            adapter.notifyDataSetChanged()
                        } else {
                            ToastUtils.makeToast("没有查询到任何地点")
                            it.exceptionOrNull()?.printStackTrace()
                        }
                    }
                }
                else -> {}
            }
        }
    }
}