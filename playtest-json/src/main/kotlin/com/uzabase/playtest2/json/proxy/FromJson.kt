package com.uzabase.playtest2.json.proxy

import com.jayway.jsonpath.JsonPath
import com.uzabase.playtest2.core.assertion.Proxies
import com.uzabase.playtest2.core.assertion.makeAssertableProxyFactory
import com.uzabase.playtest2.json.JsonPathContext

val FromJson = makeAssertableProxyFactory<JsonPathContext>(Proxies(
    asString = {
        JsonPath.read(it.json, it.path)
    },
    asLong = {
        JsonPath.read<Int>(it.json, it.path).toLong()
    },
    asRaw = {
        JsonPath.read<Int>(it.json, it.path).toLong()
    }
))
