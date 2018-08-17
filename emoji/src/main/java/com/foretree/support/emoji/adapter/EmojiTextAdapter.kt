package com.foretree.support.emoji.adapter

import android.os.Build
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import android.widget.TextView
import com.foretree.support.emoji.EmojiEntity
import com.foretree.support.emoji.EmojiManager
import com.foretree.support.emoji.R
import com.foretree.support.emoji.Type


/**
 * 单图适配器
 * Created by silen on 16/08/2018
 */
class EmojiTextAdapter(private val itemHeight: Int) : BaseListAdapter<EmojiEntity, EmojiTextVH>() {

    override fun onBindViewHolder(holder: EmojiTextVH, position: Int, item: EmojiEntity?) {
        holder.itemView.run {
            layoutParams.height = itemHeight
            layoutParams.width = itemHeight
            findViewById<TextView>(R.id.tv_emoji_icon).run {
                if ("delete" == item?.name) {
                    setBackgroundResource(item.resource as Int)
                } else {
                    item?.run {
                        when (type) {
                            Type.System -> text = name
                            Type.Drawable -> setBackgroundResource(resource as Int)
                            Type.File, Type.Asset -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    background = EmojiManager.getInstance().getEmojiDrawable(resources, item)
                                } else {
                                    setBackgroundDrawable(EmojiManager.getInstance().getEmojiDrawable(resources, item))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiTextVH = EmojiTextVH(parent)
}