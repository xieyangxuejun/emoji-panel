package com.foretree.support.emoji.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.foretree.support.emoji.R


/**
 * 简单图片
 * Created by silen on 16/08/2018
 */
class EmojiTextVH(
        parent: ViewGroup
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.emoji_item_text, parent, false))