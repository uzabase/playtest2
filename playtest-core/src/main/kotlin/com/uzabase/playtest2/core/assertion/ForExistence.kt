package com.uzabase.playtest2.core.assertion

fun interface ShouldBeExist {
    fun shouldBeExist(): Boolean
}

fun interface ShouldNotBeExist {
    fun shouldNotBeExist(): Boolean
}