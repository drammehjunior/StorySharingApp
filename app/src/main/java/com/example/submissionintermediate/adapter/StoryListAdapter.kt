package com.example.submissionintermediate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionintermediate.R
import com.example.submissionintermediate.databinding.StoryRowBinding
import com.example.submissionintermediate.pojo.ListStoryItem

class StoryListAdapter :
    PagingDataAdapter<ListStoryItem, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {


    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: StoryListAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
            Glide.with(holder.itemView.context)
                .load(data.photoUrl)
                .into(holder.picture)
            holder.itemView.setOnClickListener{ onItemClickCallback.onItemClicked(getItem(position)!!)}

        }
    }

    class MyViewHolder(private val binding: StoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            binding.txtUsername.text = data.name
        }
        var picture: ImageView = itemView.findViewById(R.id.img_user_picture)

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }
}