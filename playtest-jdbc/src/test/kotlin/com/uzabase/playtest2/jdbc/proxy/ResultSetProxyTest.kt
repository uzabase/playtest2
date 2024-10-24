package com.uzabase.playtest2.jdbc.proxy

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import org.testcontainers.containers.PostgreSQLContainer
import java.sql.DriverManager
import java.util.*

class ResultSetProxyTest : FunSpec({
    val container = PostgreSQLContainer("postgres:17.0-bookworm")
        .withDatabaseName("playtest2")
        .withUsername("Jhon")
        .withPassword("P@ssw0rd")
        .withInitScript("postgresql/sql/init.sql")

    beforeTest {
        container.start()
    }

    context("Create ResultSetProxy from ResultSet") {
        test("sorted int values") {
            DriverManager.getConnection(container.jdbcUrl, container.username, container.password).use { conn ->
                conn.prepareStatement("select * from acme.i order by i").executeQuery().use(ResultSetProxy::of)
            }.shouldBeEqual(
                ResultSetProxy(
                    listOf(
                        mapOf("i" to 1),
                        mapOf("i" to 42),
                    )
                )
            )
        }

        test("sorted double value") {
            DriverManager.getConnection(container.jdbcUrl, container.username, container.password).use { conn ->
                conn.prepareStatement("select * from acme.d order by d").executeQuery().use(ResultSetProxy::of)
            }.shouldBeEqual(
                ResultSetProxy(
                    listOf(
                        mapOf("d" to 1.0.toBigDecimal()),
                        mapOf("d" to 3.3.toBigDecimal()),
                    )
                )
            )
        }

        test("sorted uuid values") {
            DriverManager.getConnection(container.jdbcUrl, container.username, container.password).use { conn ->
                conn.prepareStatement("select * from acme.uid order by uid").executeQuery().use(ResultSetProxy::of)
            }.shouldBeEqual(
                ResultSetProxy(
                    listOf(
                        mapOf("uid" to UUID.fromString("56af9408-9063-11ef-bee3-7f726de76f14")),
                    )
                )
            )
        }

        test("sorted string values") {
            DriverManager.getConnection(container.jdbcUrl, container.username, container.password).use { conn ->
                conn.prepareStatement("select * from acme.str order by str").executeQuery().use(ResultSetProxy::of)
            }.shouldBeEqual(
                ResultSetProxy(
                    listOf(
                        mapOf("str" to ""),
                        mapOf("str" to "hello"),
                    )
                )
            )
        }

        test("sorted boolean values") {
            DriverManager.getConnection(container.jdbcUrl, container.username, container.password).use { conn ->
                conn.prepareStatement("select * from acme.bool order by bool").executeQuery().use(ResultSetProxy::of)
            }.shouldBeEqual(
                ResultSetProxy(
                    listOf(
                        mapOf("bool" to false),
                        mapOf("bool" to true),
                        mapOf("bool" to null),
                    )
                )
            )
        }
    }
})
