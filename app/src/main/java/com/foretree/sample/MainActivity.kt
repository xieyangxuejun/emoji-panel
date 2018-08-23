package com.foretree.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.foretree.support.emoji.EmojiEntity
import com.foretree.support.emoji.EmojiManager
import com.foretree.support.emoji.Type
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

val TAG = "======>"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.getInstance().run {
            //loadSystem(Type.Asset, "emoji/system_cover.png")
            //loadAsset(this@MainActivity, "emoji/1")

            val dirName = "emoji/com.digizen.star.default/"
            val jsonString = readAssetFile(dirName + "emoji.json", Charset.defaultCharset())
            val emojiJsonModel = JsonParserUtil.parse(jsonString, EmojiJsonModel::class.java, null)
            val childMap = linkedMapOf<String, EmojiEntity>()
            for (emoticon in emojiJsonModel.emoticons) {
                childMap[emoticon.chs] = EmojiEntity(Type.Asset, "png", emoticon.chs, dirName + emoticon.png, dirName)
            }
            putAll(dirName, childMap)
        }
        setContentView(R.layout.activity_main)
        emoji_layout.bindEditText(et_input)

        EmojiManager.getInstance().replaceEmoji(tv_text)

        //tv_text.text = SpannableStringBuilder(tv_text.text).append(getEmojiStringByUnicode(0x1F601))
        Log.d("====>", tv_text.text.toString())
    }


    fun readAssetFile(fileName: String, encoding: Charset): String {
        var resultString = ""
        var `is`: InputStream? = null
        try {
            `is` = assets.open(fileName)
            val buffer = ByteArray(`is`!!.available())

            `is`!!.read(buffer)
            resultString = String(buffer, encoding)
        } catch (e1: Exception) {
            e1.printStackTrace()

            try {
                if (`is` != null) {
                    `is`.close()
                }
            } catch (e2: IOException) {
                e2.printStackTrace()
            }

        } finally {
            try {
                if (`is` != null) {
                    `is`.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return resultString
    }
}
