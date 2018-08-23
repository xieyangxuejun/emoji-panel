package com.foretree.sample

/**
 * Created by silen on 23/08/2018
 */

data class EmojiJsonModel(
    val id: String,
    val version: Int,
    val group_name_tw: String,
    val group_name_cn: String,
    val group_name_en: String,
    val display_only: Int,
    val group_type: Int,
    val emoticons: List<Emoticon>
)

data class Emoticon(
    val chs: String,
    val cht: String,
    val gif: String,
    val png: String,
    val type: String
)