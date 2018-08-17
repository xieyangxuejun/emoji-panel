package com.foretree.support.emoji

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.foretree.support.emoji.adapter.EmojiImageAdapter
import kotlinx.android.synthetic.main.emoji_layout_viewpager.view.*

/**
 * Created by silen on 15/08/2018
 */
class EmojiLayout : LinearLayout {
    private lateinit var emojiPageAdapter: EmojiPageAdapter

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context, attr: AttributeSet?, defStyle: Int) : super(context, attr, defStyle) {
        initView(context, attr)
    }

    private fun initView(context: Context, attr: AttributeSet?) {
        inflate(context, R.layout.emoji_layout_viewpager, this)
        emojiPageAdapter = EmojiPageAdapter(arrayListOf<View>().apply {
            add(EmojiChildLayout(getContext()).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                setEmojiIcons(EmojiManager.getInstance().getEmojiKeys())
            })
        })
        vp_emoji.adapter = emojiPageAdapter
        rv_icons.run {
            layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = EmojiImageAdapter().apply {
                submitList(arrayListOf<String>().apply {
                    add("delete")
                })
            }
        }
    }

    /**
     * 绑定选择监听
     */
    fun bindEditText(editText: EditText) {
        emojiPageAdapter.emojiViews.forEach {
            if (it is EmojiChildLayout) {
                it.setOnChooseEmojiCallback(object : EmojiChildLayout.OnEmojiChooseCallback {
                    override fun onChooseEmoji(name: String) {
                        EmojiManager.getInstance().insertEmoji(editText, name)
                    }

                    override fun onDeleteEmoji() {
                        EmojiManager.getInstance().deleteEmoji(editText)
                    }
                })
            }
        }
    }
}