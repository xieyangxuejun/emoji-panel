package com.foretree.support.emoji

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import java.io.File
import java.util.regex.Pattern

/**
 * 表情管理类
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

    //每套表情包的key
    private val emojiPackageMap = hashMapOf<String, LinkedHashMap<String, EmojiEntity>>()
    //封面
    private val emojiCoverList = arrayListOf<CoverEntity>()

    /**
     * 导入zip
     */
    fun loadZip(filePath: String, outDir: String) {
        FileUtils.unzipDir(filePath, outDir)
    }

    /**
     * 导入asset, 封面也是在此文件夹下, cover.png
     */
    fun loadAsset(context: Context, dirName: String) {
        val childMap = linkedMapOf<String, EmojiEntity>()
        context.resources.assets.list(dirName).forEach {
            val fileNmae = it.substring(0, it.lastIndexOf('.'))
            val resource = dirName + File.separator + it
            if ("emoji_cover" == fileNmae) {
                emojiCoverList.add(CoverEntity(false, fileNmae, resource))
            } else {
                val key = "[$fileNmae]"
                childMap[key] = EmojiEntity(Type.Asset, "png", key, resource, dirName)
            }
        }
        putAll(dirName, childMap)
    }

    private fun putAll(key: String, childMap: LinkedHashMap<String, EmojiEntity>) {
        val getMap = emojiPackageMap.get(key)
        if (getMap == null) {
            emojiPackageMap[key] = childMap
        } else {
            getMap.putAll(childMap)
        }
    }

    fun loadSystem(type: Type, coverPath: String) {
        loadSystem("system", type, coverPath, *EmojiData.DEFAULT)
    }

    fun loadSystem(parentKey: String, type: Type, coverPath: String, vararg intArray: Int) {
        val childMap = linkedMapOf<String, EmojiEntity>()
        intArray.forEach {
            val key = getEmojiStringByUnicode(it)
            childMap[key] = EmojiEntity(Type.System, "", key, "", parentKey)
        }
        putAll(parentKey, childMap)
        emojiCoverList.add(CoverEntity(true, "默认", coverPath, type))
    }

    /**
     * 获取bitmap drawable
     */
    fun getEmojiDrawable(res: Resources, entity: EmojiEntity): Drawable? {
        when (entity.type) {
            Type.Asset -> return BitmapDrawable(res, BitmapFactory.decodeStream(res.assets.open(entity.resource.toString())))
            Type.File -> return BitmapDrawable(res, entity.resource.toString())
            Type.Drawable -> return res.getDrawable(entity.resource as Int)
            else -> return null
        }
    }

    /**
     * et插入emoji
     */
    fun insertEmoji(editText: EditText, entity: EmojiEntity) {
        insertEmoji(editText, entity, 0)
    }

    private fun getEmojiStringByUnicode(unicode: Int): String {
        return String(Character.toChars(unicode))
    }

    fun insertEmoji(editText: EditText, entity: EmojiEntity, maxLength: Int) {
        val content = editText.toString()
        if (maxLength != 0 && content.length + entity.key.length > maxLength) return

        val drawable = getEmojiDrawable(editText.context.resources, entity)

        val ss = SpannableString(entity.name)
        if (drawable != null) {
            val height = getTextHeight(editText)
            drawable.setBounds(0, 0, height, height)
            val imageSpan = ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM)
            ss.setSpan(imageSpan, 0, ss.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

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
            val emojiEntity = getEmojiEntity(entity.value) ?: continue
            val drawable = getEmojiDrawable(tv.resources, emojiEntity) ?: continue
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
        if (!TextUtils.isEmpty(editText.text)) {
            val selectionStart = editText.selectionStart// 获取光标的位置
            if (selectionStart > 0) {
                val content = editText.text.toString()
                val tempStr = content.substring(0, selectionStart)
                val start = tempStr.lastIndexOf("[")// 获取最后一个表情的位置
                val end = tempStr.lastIndexOf("]")// 获取最后一个表情的位置
                if (start != -1 && end == selectionStart - 1) {
                    val cs = tempStr.substring(start, selectionStart)
                    if (containsEmojiName(cs))
                        editText.editableText.delete(start, selectionStart)
                    else
                        editText.editableText.delete(selectionStart - 1, selectionStart)
                } else {
                    //editText.editableText.delete(selectionStart - 1, selectionStart)
                    editText.dispatchKeyEvent(KeyEvent(0,0,0,
                            KeyEvent.KEYCODE_DEL,
                            0,0,0,0,
                            KeyEvent.KEYCODE_ENDCALL))
                }
            }
        }
    }

    /**
     * 获取文字的高度
     */
    private fun getTextHeight(et: EditText): Int {
        val p = Paint()
        p.textSize = et.textSize
        return p.fontMetrics.let {
            Math.floor((it.bottom - it.top).toDouble()).toInt()
        }
    }

    /**
     * get emoji entity
     */
    private fun getEmojiEntity(name: String): EmojiEntity? {
        emojiPackageMap.forEach {
            if (it.value.containsKey(name)) {
                return it.value[name]
            }
        }
        return null
    }

    /**
     * find emoji key
     */
    private fun containsEmojiName(name: String): Boolean {
        emojiPackageMap.forEach {
            if (it.value.containsKey(name)) {
                return true
            }
        }
        return false
    }

    /**
     * 表情全部数据
     */
    fun getEmojiPackageMap(): HashMap<String, LinkedHashMap<String, EmojiEntity>> {
        return emojiPackageMap
    }

    /**
     * 封面
     */
    fun getEmojiCoverList(): List<CoverEntity> {
        return emojiCoverList
    }
}