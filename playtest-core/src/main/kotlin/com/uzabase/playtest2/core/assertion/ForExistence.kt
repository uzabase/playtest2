package com.uzabase.playtest2.core.assertion

fun interface ShouldBeExist {
    fun shouldBeExist(): TestResult
}

fun interface ShouldNotBeExist {
    fun shouldNotBeExist(): TestResult
}