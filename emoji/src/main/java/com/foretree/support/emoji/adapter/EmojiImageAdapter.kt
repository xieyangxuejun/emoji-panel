package com.foretree.support.emoji.adapter

import android.graphics.BitmapFactory
import android.support.v7.util.DiffUtil
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import com.foretree.support.emoji.EmojiManager
import com.foretree.support.emoji.R


/**
 * 单图适配器
 * Created by silen on 16/08/2018
 */
class EmojiImageAdapter() : BaseListAdapter<String, EmojiImageVH>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: EmojiImageVH, position: Int, item: String?) {
        ((holder.itemView as ViewGroup).getChildAt(0) as ImageView).run {
            if ("delete" == getItem(position)) {
                setImageResource(R.drawable.delete)
            } else {
                setImageDrawable(EmojiManager.getInstance().getEmojiDrawable(resources, item))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiImageVH = EmojiImageVH(parent)

    companion object {
        @JvmStatic
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String?, newItem: String?): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: String?, newItem: String?): Boolean = oldItem == newItem
        }
    }
}