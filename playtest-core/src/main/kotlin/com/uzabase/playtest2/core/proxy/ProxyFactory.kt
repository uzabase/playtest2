package com.uzabase.playtest2.core.proxy

import com.uzabase.playtest2.core.assertion.Assertable

class ProxyFactory {
    companion object {
        fun ofString(s: String): Assertable =
            object : Assertable {
                override fun asLong(): Long = s.toLong()
                override fun asString(): String = s
                override fun asBoolean(): Boolean = s.toBoolean()
                override fun asRaw(): Any = s
            }

        fun ofLong(l: Long): Assertable =
            object : Assertable {
                override fun asLong(): Long = l
                override fun asString(): String = l.toString()
                override fun asBoolean(): Boolean = throw UnsupportedOperationException()
                override fun asRaw(): Any = l
            }

        fun ofBoolean(b: Boolean): Assertable =
            object : Assertable {
                override fun asLong(): Long = throw UnsupportedOperationException()
                override fun asString(): String = b.toString()
                override fun asBoolean(): Boolean = b
                override fun asRaw(): Any = b
            }
    }
}