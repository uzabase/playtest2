package com.uzabase.playtest2.core.config

import com.uzabase.playtest2.core.proxy.ProxyFactory
import com.uzabase.playtest2.core.proxy.AssertableProxyFunctions
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import java.net.URI
import java.net.URL
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

data object MyMod1ModuleKey : ModuleKey

data class MyMod1Configuration(
    val endpoint: URL
) : ModuleConfiguration

data class MyMod2ModuleKey(val name: String) : ModuleKey

data class MyMod2Configuration(
    val endpoint: URL
) : ModuleConfiguration

data class Dog(val name: String, val age: Int)

val FromDog = { it: Dog ->
    ProxyFactory.make<Dog, Unit>(
        it,
        AssertableProxyFunctions(
            asString = { "My dog name is ${it.name}, age is ${it.age}!!" },
        )
    )
}

fun myMod1(endpoint: URL): ConfigurationEntry =
    ConfigurationEntry(MyMod1ModuleKey, MyMod1Configuration(endpoint))

fun myMod2(name: String, endpoint: URL): ConfigurationEntry =
    ConfigurationEntry(MyMod2ModuleKey(name), MyMod2Configuration(endpoint))

class ConfigurationTest : FunSpec({
    beforeSpec {
        Configuration.playtest2 {
            myMod1(URI("http://localhost:8080").toURL()) +
                    myMod2("awesomeApi1", URI("http://localhost:3000").toURL()) +
                    myMod2("awesomeApi2", URI("http://localhost:3001").toURL())
        }
    }

    test("Configuration builder should build configurations") {
        val configsProp = Configuration.Companion::class.declaredMemberProperties
            .find { it.name == "configs" }!!

        configsProp.isAccessible = true
        configsProp.get(Configuration) shouldBe mapOf(
            MyMod1ModuleKey to MyMod1Configuration(URI("http://localhost:8080").toURL()),
            MyMod2ModuleKey("awesomeApi1") to MyMod2Configuration(URI("http://localhost:3000").toURL()),
            MyMod2ModuleKey("awesomeApi2") to MyMod2Configuration(URI("http://localhost:3001").toURL())
        )
    }

    test("Configuration should return module configuration") {
        forAll(
            row(MyMod1ModuleKey, MyMod1Configuration(URI("http://localhost:8080").toURL())),
            row(MyMod2ModuleKey("awesomeApi1"), MyMod2Configuration(URI("http://localhost:3000").toURL())),
            row(MyMod2ModuleKey("awesomeApi2"), MyMod2Configuration(URI("http://localhost:3001").toURL()))
        ) { key, config ->
            Configuration[key] shouldBe config
        }
    }
})
