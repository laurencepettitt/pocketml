package com.example.pocketml.ui.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketml.DImagesQuery
import com.example.pocketml.databinding.ListItemDatasetManagerOverviewBinding

class OverviewAdapter(val dImageClickListener: DImageClickListener) :
    ListAdapter<DImagesQuery.DImage, OverviewAdapter.ViewHolder>(
        DatasetDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dImage = getItem(position)
        holder.bind(dImage, dImageClickListener)
    }

    class ViewHolder private constructor(val binding: ListItemDatasetManagerOverviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dImage: DImagesQuery.DImage, clickListener: DImageClickListener) {
            binding.dImage = dImage
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemDatasetManagerOverviewBinding
                    .inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class DatasetDiffCallback : DiffUtil.ItemCallback<DImagesQuery.DImage>() {
    override fun areItemsTheSame(
        oldItem: DImagesQuery.DImage,
        newItem: DImagesQuery.DImage
    ): Boolean {
        return oldItem.id == newItem.id && oldItem.version == newItem.version
    }

    override fun areContentsTheSame(
        oldItem: DImagesQuery.DImage, newItem: DImagesQuery.DImage
    ): Boolean {
        return oldItem == newItem
    }
}

class DImageClickListener(val clickListener: (DImagesQuery.DImage) -> Unit) {
    operator fun invoke(dImage: DImagesQuery.DImage): Unit = clickListener(dImage)
}

