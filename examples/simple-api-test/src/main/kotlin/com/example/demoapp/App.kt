package com.example.demoapp

import io.undertow.Handlers
import io.undertow.Undertow
import io.undertow.util.HttpString
import okhttp3.OkHttpClient
import okhttp3.Request

class App {
    companion object {

        private val client = OkHttpClient().newBuilder().build()

        private val server: Undertow = Undertow.builder()
            .addHttpListener(8080, "localhost")
            .setHandler(Handlers.path().addExactPath("/ping") { exchange ->
                client.newCall(Request.Builder().url("http://localhost:3000/ping").build()).execute()
                exchange.responseHeaders.put(HttpString.tryFromString("Content-Type"), "application/json")
                exchange.responseSender.send(
                    """
                    {
                        "message": "pong"
                    }
                """.trimIndent()
                )
            }.addExactPath("/values") { exchange ->
                exchange.responseHeaders.put(HttpString.tryFromString("Content-Type"), "application/json")
                exchange.responseSender.send(
                    """
                    {
                        "boolValue": {
                            "trueValue": true,
                            "falseValue": false
                        }
                    }
                """.trimIndent()
                )
            })
            .build()

        fun startServer() {
            server.start()
        }

        fun stopServer() {
            server.stop()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            startServer()
        }
    }
}

fun main() {
    App.startServer()
}