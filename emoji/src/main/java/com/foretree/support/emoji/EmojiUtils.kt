package com.foretree.support.emoji

import android.content.res.Resources
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.widget.TextView
import java.util.regex.Pattern

/**
 * 发现一个问题.如何做了其他操作的话比如spannable和其他耗时,替换表情比较耗时
 * Created by silen on 20/08/2018
 */
object EmojiUtils {

    @JvmStatic
    fun replaceEmoji(tv: TextView): Spannable {
        return replaceEmoji(tv.resources, tv.text, tv.textSize.toInt())
    }

    @JvmStatic
    fun replaceEmoji(resources: Resources, content: CharSequence, textSize: Int): Spannable {
        return replaceEmoji(resources, content, textSize, textSize)
    }

    @JvmStatic
    fun replaceEmoji(resources: Resources, content: CharSequence, width: Int, height: Int): Spannable {
        val emojiEntities = arrayListOf<RegEntity>()
        Pattern.compile(regx).matcher(content).run {
            while (this.find()) {
                emojiEntities.add(RegEntity(this.start(), this.end(), this.group()))
            }
        }

        val ssb = SpannableStringBuilder(content)
        for (entity in emojiEntities) {
            val emojiEntity = EmojiManager.getInstance().getEmojiEntity(entity.value) ?: continue
            val drawable = EmojiManager.getInstance().getEmojiDrawable(resources, emojiEntity)
                    ?: continue
            drawable.setBounds(0, 0, width, height)
            val imageSpan = ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM)
            val ss = SpannableString(entity.value)
            ss.setSpan(imageSpan, 0, ss.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

            //插入需要将以前的字符移除
            ssb.delete(entity.start, entity.end)
            ssb.insert(entity.start, ss)
        }
        return ssb
    }
}