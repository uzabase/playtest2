package com.uzabase.playtest2.http.assertion

import com.jayway.jsonpath.JsonPath
import com.uzabase.playtest2.core.assertion.Proxies
import com.uzabase.playtest2.core.assertion.makeAssertableProxyFactory
import com.uzabase.playtest2.http.JsonContainer

val FromJson = makeAssertableProxyFactory<JsonContainer>(Proxies(
    asString = {
        JsonPath.read(it.json, it.path)
    },
    asLong = {
        JsonPath.read(it.json, it.path)
    },
    asRaw = {
        JsonPath.read(it.json, it.path)
    }
))
