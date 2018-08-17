package com.foretree.support.emoji

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.widget.EditText
import android.widget.TextView
import java.io.File
import java.util.regex.Pattern

/**
 * Created by silen on 15/08/2018
 */
class EmojiManager {

    companion object {
        private var mInstance: EmojiManager? = null

        fun getInstance(): EmojiManager {
            if (mInstance == null) {
                synchronized(this) {
                    if (mInstance == null) {
                        mInstance = EmojiManager()
                    }
                }
            }
            return mInstance!!
        }
    }

    private val emojiMap = HashMap<String, String>()
    private var rootDir: String? = null

    /**
     * 导入zip
     */
    fun load(filePath: String, outDir: String) {
        FileUtils.unzipDir(filePath, outDir)
    }

    /**
     * 导入asset
     */
    fun loadAsset(context: Context, dirName: String) {
        emojiMap.clear()
        rootDir = dirName
        context.resources.assets.list(dirName).forEach {
            val key = "[${it.substring(0, it.lastIndexOf('.'))}]"
            emojiMap.put(key, dirName + File.separator + it)
        }
    }

    fun getEmojiDrawable(res: Resources, name: String?): Drawable? {
        val value = emojiMap[name] ?: return null
        return BitmapDrawable(res, BitmapFactory.decodeStream(res.assets.open(value)))
    }

    fun getEmojiKeys(): List<String> {
        return emojiMap.keys.toList()
    }

    /**
     * et插入emoji
     */
    fun insertEmoji(editText: EditText, name: String) {
        insertEmoji(editText, name, 0)
    }

    fun insertEmoji(editText: EditText, name: String, maxLength: Int) {
        val content = editText.toString()
        if (maxLength != 0 && content.length + name.length > maxLength) return

        val drawable = getEmojiDrawable(editText.context.resources, name) ?: return

        val textSize = editText.textSize.toInt()
        drawable.setBounds(0, 0, textSize, textSize)
        val imageSpan = ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM)
        val ss = SpannableString(name)
        ss.setSpan(imageSpan, 0, ss.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)


        val index = Math.max(editText.selectionStart, 0)
        val ssb = SpannableStringBuilder(editText.text)
        ssb.insert(index, ss)

        editText.text = ssb
        editText.setSelection(index + ss.length)
    }

    fun replaceEmoji(tv: TextView) {
        val content = tv.text.toString()

        val emojiEntities = arrayListOf<RegEntity>()
        Pattern.compile(regx).matcher(content).run {
            while (this.find()) {
                emojiEntities.add(RegEntity(this.start(), this.end(), this.group()))
            }
        }

        val ssb = SpannableStringBuilder(tv.text)
        for (entity in emojiEntities) {
            val drawable = getEmojiDrawable(tv.resources, entity.value) ?: continue
            val textSize = tv.textSize.toInt()
            drawable.setBounds(0, 0, textSize, textSize)
            val imageSpan = ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM)
            val ss = SpannableString(entity.value)
            ss.setSpan(imageSpan, 0, ss.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

            //插入需要将以前的字符移除
            ssb.delete(entity.start, entity.end)
            ssb.insert(entity.start, ss)
        }
        tv.text = ssb
    }

    fun deleteEmoji(editText: EditText) {
        if (!TextUtils.isEmpty(editText.getText())) {
            val selectionStart = editText.selectionStart// 获取光标的位置
            if (selectionStart > 0) {
                val content = editText.text.toString()
                val tempStr = content.substring(0, selectionStart)
                val start = tempStr.lastIndexOf("[")// 获取最后一个表情的位置
                val end = tempStr.lastIndexOf("]")// 获取最后一个表情的位置
                if (start != -1 && end == selectionStart - 1) {
                    val cs = tempStr.substring(start, selectionStart)
                    if (emojiMap.containsKey(cs))
                        editText.editableText.delete(start, selectionStart)
                    else
                        editText.editableText.delete(selectionStart - 1, selectionStart)
                } else {
                    editText.editableText.delete(selectionStart - 1, selectionStart)
                }
            }
        }
    }
}