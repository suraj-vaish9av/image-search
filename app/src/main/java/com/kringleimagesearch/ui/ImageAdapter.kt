package com.kringleimagesearch.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kringleimagesearch.data.database.entities.ImageResultModel
import com.kringleimagesearch.databinding.ItemImageBinding

/**
 * a type of PagingDataAdapter to show searched image result with paging
 */
class ImageAdapter : PagingDataAdapter<ImageResultModel, ImageAdapter.ViewHolder>(ImageModelDiffUtil) {

    companion object ImageModelDiffUtil : DiffUtil.ItemCallback<ImageResultModel>() {
        override fun areItemsTheSame(oldItem: ImageResultModel, newItem: ImageResultModel): Boolean {
            return oldItem.imageId==newItem.imageId
        }
        override fun areContentsTheSame(oldItem: ImageResultModel, newItem: ImageResultModel): Boolean {
            return oldItem==newItem
        }
    }

    class ViewHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                return ViewHolder(
                    ItemImageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                )
            }
        }

        fun bindData(imageResultModel: ImageResultModel?){
            binding.imageResultModel=imageResultModel
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

}