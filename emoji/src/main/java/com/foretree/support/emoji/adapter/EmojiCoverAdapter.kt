package com.foretree.support.emoji.adapter

import android.annotation.TargetApi
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.view.ViewGroup
import com.foretree.support.emoji.CoverEntity
import com.foretree.support.emoji.R
import kotlinx.android.synthetic.main.emoji_item_text.view.*

/**
 * Created by silen on 17/08/2018
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
class EmojiCoverAdapter : BaseListAdapter<CoverEntity, BaseListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseListViewHolder =
            BaseListViewHolder(parent, R.layout.emoji_item_text)

    override fun onBindViewHolder(holder: BaseListViewHolder, position: Int, item: CoverEntity?) {
        item?.run {
            holder.itemView.run {
                setPadding(20, 20, 20, 20)
                setBackgroundColor(if (item.checked) Color.LTGRAY else Color.WHITE)
                //暂时从asset加载
                tv_emoji_icon.background = BitmapDrawable(resources, BitmapFactory.decodeStream(resources.assets.open(item.res)))
            }
        }
    }

    fun update(position: Int) {
        for (index in 0..(itemCount - 1)) {
            getItem(index).checked = false
        }
        getItem(position).checked = true
        notifyDataSetChanged()
    }
}