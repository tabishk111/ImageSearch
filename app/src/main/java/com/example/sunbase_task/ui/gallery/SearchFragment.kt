package com.example.sunbase_task.ui.gallery

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.example.sunbase_task.R
import com.example.sunbase_task.data.UnsplashPhoto
import com.example.sunbase_task.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@ExperimentalPagingApi
@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.fragment_search),
    UnsplashPhotoAdapter.OnItemClickListener {
    private val viewModel by viewModels<GalleryViewModel>()
    private var _binding: FragmentSearchBinding? =null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        val adapter = UnsplashPhotoAdapter(this)

        (activity as AppCompatActivity).supportActionBar?.title = "Search Images"

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter
        }

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if(editable.toString().isNotEmpty())
                        binding.recyclerView.scrollToPosition(0)
                        viewModel.searchPhotos(editable.toString())

                }
            }
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }


    }



    override fun onItemClick(photo: UnsplashPhoto) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}