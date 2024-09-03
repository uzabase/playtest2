package com.uzabase.playtest2.core.zoom

interface Zoomable<K> {
    fun zoom(key: K): Any?
}