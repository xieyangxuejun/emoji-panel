package com.foretree.support.emoji

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.foretree.support.emoji.adapter.EmojiTextAdapter
import kotlinx.android.synthetic.main.emoji_layout_child_viewpager.view.*
import net.lucode.hackware.magicindicator.ViewPagerHelper


/**
 * 子表情视图
 * Created by silen on 16/08/2018
 */
class EmojiChildLayout : LinearLayout {
    private var numRow = 7
    private var numColumn = 3
    private var iconDeleteResId = 0
    private val emojiList = arrayListOf<EmojiEntity>()
    private var onChooseEmojiCallback: OnEmojiChooseCallback? = null


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context, attr: AttributeSet?, defStyle: Int) : super(context, attr, defStyle) {
        inflate(context, R.layout.emoji_layout_child_viewpager, this)
        val array = context.obtainStyledAttributes(attr, R.styleable.EmojiChildLayout)
        val row = array.getInteger(R.styleable.EmojiChildLayout_num_row, 0)
        val column = array.getInteger(R.styleable.EmojiChildLayout_num_column, 0)
        iconDeleteResId = array.getResourceId(R.styleable.EmojiChildLayout_icon_delete, R.drawable.delete)
        if (row != 0) numRow = row
        if (column != 0) numColumn = column
        array.recycle()
    }

    /**
     * 选择必须设置
     */
    fun setOnChooseEmojiCallback(callback: OnEmojiChooseCallback) {
        this.onChooseEmojiCallback = callback
    }

    /**
     * 设置一套表情数据
     */
    fun setEmojiIcons(emojis: List<EmojiEntity>) {
        val onePageCount = (numRow * numColumn) - 1
        var pageCount = emojis.size / onePageCount
        if (emojis.size % onePageCount != 0) pageCount++
        if (pageCount <= 0) return
        this.emojiList.run {
            clear()
            addAll(emojis)
        }
        initChildViewPagerView(pageCount)
        initIndicator(pageCount)
    }

    private fun initChildViewPagerView(pageCount: Int) {
        vp_child_emoji.offscreenPageLimit = pageCount
        // get one page list num
        val pageViews = arrayListOf<View>()
        //每一页的数量
        val onePageCount = (numRow * numColumn) - 1
        for (page in 1..pageCount) {
            val start = Math.min(Math.max(onePageCount * (page - 1), 0), emojiList.size)
            val end = Math.min(Math.max(onePageCount * (page), start), emojiList.size)
            pageViews.add(getChildGridView(arrayListOf<EmojiEntity>().apply {
                addAll(emojiList.subList(start, end))
                add(EmojiEntity(Type.Drawable, "png", "delete", iconDeleteResId, ""))
            }))
        }
        vp_child_emoji.adapter = EmojiPageAdapter(pageViews)
    }

    private fun getChildGridView(list: List<EmojiEntity>): View {
        return RelativeLayout(context).apply {
            layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
            gravity = Gravity.CENTER
            addView(RecyclerView(context).apply {
                overScrollMode = View.OVER_SCROLL_NEVER
                val itemHeight = getMetricsWidth(context).toFloat() / numRow
                layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (itemHeight * numColumn).toInt())
                layoutManager = GridLayoutManager(context, numRow)
                adapter = EmojiTextAdapter(itemHeight.toInt()).apply {
                    submitList(list)
                    setOnItemClickListener { adapter, view, position ->
                        if (onChooseEmojiCallback == null) return@setOnItemClickListener
                        onChooseEmojiCallback?.run {
                            if (position == adapter.itemCount - 1) {
                                onDeleteEmoji()
                            } else {
                                onChooseEmoji(adapter.getItem(position))
                            }
                        }
                    }
                }
            })
        }
    }

    private fun initIndicator(size: Int) {
        indicator_child_emoji.navigator = ScaleCircleNavigator(context).apply {
            setCircleCount(size)
            setNormalCircleColor(Color.LTGRAY)
            setSelectedCircleColor(Color.BLACK)
            setCircleClickListener(object : ScaleCircleNavigator.OnCircleClickListener {
                override fun onClick(index: Int) {
                    vp_child_emoji?.currentItem = index
                }
            })
        }
        ViewPagerHelper.bind(indicator_child_emoji, vp_child_emoji)
    }

    interface OnEmojiChooseCallback {
        fun onChooseEmoji(entity: EmojiEntity)
        fun onDeleteEmoji()
    }
}
