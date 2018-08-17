package com.foretree.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.foretree.support.emoji.EmojiManager
import kotlinx.android.synthetic.main.activity_main.*

val TAG = "======>"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.getInstance().loadAsset(this, "emoji/1")
        setContentView(R.layout.activity_main)
        emoji_layout.bindEditText(et_input)

        EmojiManager.getInstance().replaceEmoji(tv_text)
        Log.d("====>", tv_text.text.toString())
    }
}
