package com.uzabase.playtest2.core.assertion

class PlaytestAssertionError(override val message: String) : AssertionError(message)

fun playtestError(message: String): Nothing = throw PlaytestAssertionError(message)
