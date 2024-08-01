package com.rosf73.garcani.localdata

private var totalCount = 0

data class Speech(
    val number: Int = totalCount++,
    val msg: String,
)

fun String.toSpeech() = Speech(msg = this)