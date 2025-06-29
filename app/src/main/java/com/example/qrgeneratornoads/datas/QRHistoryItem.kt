package com.example.qrgeneratornoads.datas

data class QRHistoryItem(
    val id:Long= System.currentTimeMillis(),
    val text:String,
    val type:String, //"Text","URL",etc.
    val timeStamp:Long= System.currentTimeMillis()
)