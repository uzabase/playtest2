package com.uzabase.playtest2.core.assertion

val FromString = makeAssertableProxyFactory<String>(Proxies(
    asString = { it },
    asLong = { it.toLong() }
))

val FromLong = makeAssertableProxyFactory<Long>(Proxies(
    asString = { it.toString() },
    asLong = { it }
))