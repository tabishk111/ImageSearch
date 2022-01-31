package com.example.sunbase_task.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.example.sunbase_task.R
import com.example.sunbase_task.data.UnsplashPhoto
import com.example.sunbase_task.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class GalleryFragment: Fragment(R.layout.fragment_gallery),
    UnsplashPhotoAdapter.OnItemClickListener {
    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)

        (activity as AppCompatActivity).supportActionBar?.title = "Images"

        val adapter = UnsplashPhotoAdapter(this)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        //loadData()




    }

    private fun loadData() {
        val adapter = UnsplashPhotoAdapter(this)
        lifecycleScope.launch {
            viewModel.getModelNews().collectLatest { pageData->
                adapter.submitData(pageData)

            }
        }


    }


    override fun onItemClick(photo: UnsplashPhoto) {
        val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}