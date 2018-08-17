package com.foretree.support.emoji.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.foretree.support.emoji.getMetricsWidth


/**
 * 简单图片
 * Created by silen on 16/08/2018
 */
class EmojiImageVH(parent: ViewGroup) : RecyclerView.ViewHolder(RelativeLayout(parent.context).apply {
    val width = (getMetricsWidth(parent.context).toFloat() / 7).toInt()
    layoutParams = RelativeLayout.LayoutParams(width, width).apply {
        addRule(RelativeLayout.CENTER_IN_PARENT)
    }
    val lp = RelativeLayout.LayoutParams(80, 80).apply {
        addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
    }
    addView(ImageView(parent.context).apply {
        scaleType = ImageView.ScaleType.CENTER_INSIDE
    }, lp)
})