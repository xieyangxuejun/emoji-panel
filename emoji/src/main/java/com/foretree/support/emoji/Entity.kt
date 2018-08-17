package com.foretree.support.emoji

/**
 * Created by silen on 17/08/2018
 */
data class RegEntity(
        val start: Int,
        val end: Int,
        val value: String
)

data class EmojiEntity(
        val type: Type,
        val format: String,
        val name: String,
        val resource: Any,
        val key: String         //方便表情包查找
)

//封面
data class CoverEntity(
        var checked: Boolean,
        val msg: String,
        val res: String,
        val type: Type = Type.Asset
)