package com.uzabase.playtest2.core.zoom

fun interface Zoomable<K> {
    fun zoom(key: K): Any?
}