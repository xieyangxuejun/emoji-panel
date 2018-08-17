package com.foretree.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.util.Log
import com.foretree.support.emoji.EmojiManager
import com.foretree.support.emoji.Type
import kotlinx.android.synthetic.main.activity_main.*

val TAG = "======>"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.getInstance().run {
            loadSystem(Type.Asset, "emoji/system_cover.png")
            loadAsset(this@MainActivity, "emoji/1")
        }
        setContentView(R.layout.activity_main)
        emoji_layout.bindEditText(et_input)

        EmojiManager.getInstance().replaceEmoji(tv_text)

        //tv_text.text = SpannableStringBuilder(tv_text.text).append(getEmojiStringByUnicode(0x1F601))
        Log.d("====>", tv_text.text.toString())
    }


}
