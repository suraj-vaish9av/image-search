package com.kringleimagesearch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.kringleimagesearch.R
import com.kringleimagesearch.databinding.ActivityImageSearchBinding
import com.kringleimagesearch.utils.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ImageSearchActivity : AppCompatActivity() {

    private val imageSearchViewModel by lazy { ViewModelProvider(this).get(ImageSearchViewModel::class.java) }
    private val binding by lazy { ActivityImageSearchBinding.inflate(layoutInflater) }
    private val imageAdapter by lazy { ImageAdapter() }

    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()

        binding.setupImageAdapter()

        binding.setListeners()

    }
    private fun init(){
        setupToolbar(getString(R.string.activity_title_search_images),false)

        binding.imageSearchViewModel = imageSearchViewModel
        binding.lifecycleOwner = this
    }

    /**
     * setting some listeners here
     */
    @ExperimentalPagingApi
    fun ActivityImageSearchBinding.setListeners(){
        imgSearch.onClick {
            fetchImages()
        }

        rvImages.hideKeyboardOnScrollDown(this@ImageSearchActivity,binding.root)

        autoSearchImage.onSearch {
            fetchImages()
        }
    }

    /**
     * this will initiate the API call to search the images
     */
    @ExperimentalPagingApi
    private fun fetchImages() {
        lifecycleScope.launch {
            val query = binding.autoSearchImage.text.toString().trim()
            if (query.isEmpty()) {
                showToast(R.string.empty_query)
                return@launch
            }
            imageSearchViewModel.fetchImages(query).distinctUntilChanged().collectLatest {
                imageAdapter.submitData(it)
            }
        }
    }

    /**
     * set the adapter and layout changes according to the state of the data
     */
    private fun ActivityImageSearchBinding.setupImageAdapter(){
        imageAdapter.run {
            rvImages.adapter = withLoadStateFooter(PagingLoadStateAdapter(this))

            lifecycleScope.launch {
                binding.swipeRefresh.setOnRefreshListener { refresh() }

                loadStateFlow.collectLatest {
                    binding.swipeRefresh.isRefreshing = it.refresh is LoadState.Loading

                    if (it.append is LoadState.NotLoading && it.append.endOfPaginationReached) {
                        binding.partialNoRecord.root.isVisible = itemCount < 1
                    }
                    else{
                        binding.partialNoRecord.root.isVisible = false
                    }
                }

                loadStateFlow.distinctUntilChangedBy { it.refresh }
                    .filter { it.refresh is LoadState.NotLoading }.collect {
                        binding.rvImages.scrollToPosition(0)
                    }
            }
        }
    }
}