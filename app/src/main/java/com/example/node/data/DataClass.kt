package com.example.node.data

data class News1(
    val headline: String = "",
    var description: String = "",
    var report: String = "",
    var imageurl: String = "",
    val tags: MutableList<String> = mutableListOf(),
    val timeStamp: Long = System.currentTimeMillis()
)

data class Highlights(
    val headline: String = "",
    var report: String = "",
    var imageurl: String = "",
    val tags: MutableList<String> = mutableListOf(),
    val timeStamp: Long = System.currentTimeMillis()
)

data class NewsData1(
    val id: String,
    val description: String,
    val heading: String,
    val imageUrl: String,
    val report: String,
    val tags: List<String>? = mutableListOf(),
    val time: Long
)