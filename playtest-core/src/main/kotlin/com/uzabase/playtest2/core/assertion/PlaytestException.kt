package com.uzabase.playtest2.core.assertion

class PlaytestException(override val message: String) : AssertionError(message)

fun playtestException(message: String): Nothing = throw PlaytestException(message)
