package com.uzabase.playtest2.core.assertion

interface AsLong {
    fun asLong(): Long
}

interface AsString {
    fun asString(): String
}

interface AsBoolean {
    fun asBoolean(): Boolean
}

interface AsRaw {
    fun asRaw(): Any
}

interface Assertable : AsLong, AsString, AsBoolean, AsRaw