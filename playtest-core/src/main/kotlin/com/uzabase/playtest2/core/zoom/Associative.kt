package com.uzabase.playtest2.core.zoom

import com.uzabase.playtest2.core.assertion.ShouldBeString

fun interface AsAssociative {
    fun asAssociative(key: String): Associative
}

class Associative private constructor(
    val map: Map<String, String>,
    val key: String
) : ShouldBeString {
    companion object {
        fun of(map: Map<String, String>, key: String) = Associative(map, key)
    }

    override fun shouldBe(expected: String): Boolean =
        map[key]?.let { it == expected } ?: false
}