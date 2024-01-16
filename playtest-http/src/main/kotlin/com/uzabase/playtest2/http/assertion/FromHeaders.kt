package com.uzabase.playtest2.http.assertion

import com.uzabase.playtest2.core.assertion.Proxies
import com.uzabase.playtest2.core.assertion.makeAssertableProxyFactory
import okhttp3.Headers

val FromHeaders = makeAssertableProxyFactory<Headers>(Proxies(
    asString = {
        it.joinToString("\n") { (k, v) -> "$k: $v" }
    }
))