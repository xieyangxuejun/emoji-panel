package com.foretree.support.emoji

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

/**
 * Created by silen on 16/08/2018
 */
class EmojiPageAdapter(val emojiViews: List<View>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = emojiViews.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(emojiViews[position])
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return emojiViews[position].apply {
            container.addView(this)
        }
    }
}