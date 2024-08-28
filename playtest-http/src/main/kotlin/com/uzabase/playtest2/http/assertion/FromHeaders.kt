package com.uzabase.playtest2.http.assertion

import com.uzabase.playtest2.core.assertion.Assertable
import com.uzabase.playtest2.core.assertion.AssertableProxy
import com.uzabase.playtest2.core.assertion.Proxies
import okhttp3.Headers

val FromHeaders = { it: Headers ->
    AssertableProxy.make<Headers>(it, Proxies(
        asString = {
            it.sortedBy { (k, _) -> k }.joinToString("\n") { (k, v) -> "$k: $v" }
        }
    ))
}