package com.uzabase.playtest2.http.proxy

import com.uzabase.playtest2.core.assertion.Failed
import com.uzabase.playtest2.core.assertion.Ok
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.types.shouldBeInstanceOf
import java.net.URL
import java.nio.file.Paths

class ResponseBodyProxyTest : FunSpec({
    fun fromClasspathToResponseBodyProxy(path: String) =
        javaClass.classLoader
            .getResource(path)
            .let(URL::toURI)
            .let(Paths::get)
            .let(ResponseBodyProxy::of)

    context("ResponseBodyProxy") {
        test("should be equal as string") {
            fromClasspathToResponseBodyProxy("com/uzabase/playtest2/http/proxy/response-body.txt")
                .shouldBe("Hello, world")
                .shouldBe(Ok)
        }

        test("should not be equal as string") {
            val testResult = fromClasspathToResponseBodyProxy("com/uzabase/playtest2/http/proxy/response-body.txt")
                .shouldBe("Hello, world!!")

            testResult.shouldBeInstanceOf<Failed>()
            testResult.explain().shouldMatch(
                """
                |Expected:
                |  value: Hello, world!!
                |  class: kotlin.String
                |Actual:
                |  value: [^\s]+/com/uzabase/playtest2/http/proxy/response-body.txt
                |  class: sun.nio.fs.UnixPath
                """.trimMargin()
            )
        }

        test("should contain") {
            fromClasspathToResponseBodyProxy("com/uzabase/playtest2/http/proxy/response-body.txt")
                .shouldContain("world")
                .shouldBe(Ok)
        }

        test("should not contain") {
            fromClasspathToResponseBodyProxy("com/uzabase/playtest2/http/proxy/response-body.txt")
                .shouldContain("world!!")
                .shouldBeInstanceOf<Failed>()
        }
    }

})
