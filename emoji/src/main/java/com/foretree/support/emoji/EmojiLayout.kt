package com.foretree.support.emoji

import android.content.Context
import android.graphics.Color
import android.support.v4.view.ViewPager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.foretree.support.emoji.adapter.BaseListAdapter
import com.foretree.support.emoji.adapter.BaseListViewHolder
import com.foretree.support.emoji.adapter.EmojiCoverAdapter
import kotlinx.android.synthetic.main.emoji_layout_viewpager.view.*

/**
 * Created by silen on 15/08/2018
 */
class EmojiLayout : RelativeLayout {
    private lateinit var emojiPageAdapter: EmojiPageAdapter
    private lateinit var emojiCoverAdapter: EmojiCoverAdapter

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context, attr: AttributeSet?, defStyle: Int) : super(context, attr, defStyle) {
        initView(context, attr)
    }

    private fun initView(context: Context, attr: AttributeSet?) {
        inflate(context, R.layout.emoji_layout_viewpager, this)

        val array = context.obtainStyledAttributes(attr, R.styleable.EmojiLayout)
        val backgroundColor = array.getColor(R.styleable.EmojiLayout_android_background, Color.WHITE)
        var layoutHeight = 0
        var layoutWidth = 0
        try {
            layoutHeight = array.getDimensionPixelSize(R.styleable.EmojiLayout_android_layout_height, -1)
            layoutHeight -= (paddingTop + paddingBottom)
            layoutWidth = array.getDimensionPixelSize(R.styleable.EmojiLayout_android_layout_width, -1)
            layoutHeight -= (paddingLeft + paddingEnd)
        } catch (e: UnsupportedOperationException) {
            e.printStackTrace()
        }

        array.recycle()

        emojiPageAdapter = EmojiPageAdapter(arrayListOf<View>().apply {
            EmojiManager.getInstance().getEmojiPackageMap().forEach {
                //添加每一个view
                add(EmojiChildLayout(getContext(), layoutWidth, layoutHeight, backgroundColor).apply {
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    setEmojiIcons(it.value.values.toList())
                })
            }
        })

        vp_emoji.adapter = emojiPageAdapter
        vp_emoji.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                emojiCoverAdapter.update(position)
            }
        })

        //封面
        emojiCoverAdapter = EmojiCoverAdapter().apply {
            submitList(EmojiManager.getInstance().getEmojiCoverList())
            setOnItemClickListener({ _: BaseListAdapter<CoverEntity, BaseListViewHolder>, _: View, position: Int ->
                vp_emoji.setCurrentItem(position)
            })
        }
        rv_icons.run {
            layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = emojiCoverAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL).apply {
                setDrawable(context.resources.getDrawable(R.drawable.divider_cover))
            })
        }
    }

    /**
     * 绑定选择监听
     */
    fun bindEditText(editText: EditText) {
        emojiPageAdapter.emojiViews.forEach {
            if (it is EmojiChildLayout) {
                it.setOnChooseEmojiCallback(object : EmojiChildLayout.OnEmojiChooseCallback {
                    override fun onChooseEmoji(entity: EmojiEntity) {
                        EmojiManager.getInstance().insertEmoji(editText, entity)
                    }

                    override fun onDeleteEmoji() {
                        EmojiManager.getInstance().deleteEmoji(editText)
                    }
                })
            }
        }
    }
}