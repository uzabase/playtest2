package com.uzabase.playtest2.core.zoom

import com.uzabase.playtest2.core.assertion.Assertable

interface Zoom<K> {
    fun zoom(key: K): Assertable
}

interface Zoomable<K> {
    fun zoom(key: K): Any
}