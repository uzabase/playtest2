package com.uzabase.playtest2.http.proxy

import com.uzabase.playtest2.core.zoom.Zoomable
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
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
                .shouldBeTrue()
        }

        test("should not be equal as string") {
            fromClasspathToResponseBodyProxy("com/uzabase/playtest2/http/proxy/response-body.txt")
                .shouldBe("Hello, world!!")
                .shouldBeFalse()
        }

        test("should contain") {
            fromClasspathToResponseBodyProxy("com/uzabase/playtest2/http/proxy/response-body.txt")
                .shouldContain("world")
                .shouldBeTrue()
        }

        test("should not contain") {
            fromClasspathToResponseBodyProxy("com/uzabase/playtest2/http/proxy/response-body.txt")
                .shouldContain("world!!")
                .shouldBeFalse()
        }
    }

})
