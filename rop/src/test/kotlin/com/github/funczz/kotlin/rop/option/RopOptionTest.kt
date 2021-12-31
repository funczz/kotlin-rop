package io.kotlintest.provided.com.github.funczz.kotlin.rop.option

import com.github.funczz.kotlin.rop.option.RopOption
import com.github.funczz.kotlin.rop.option.flatten
import io.kotlintest.provided.ISerializableUtil
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

internal class RopOptionTest : StringSpec(), ISerializableUtil {

    init {

        "RopOption.none" {
            val expected = true
            val actual = RopOption.none<String>()

            actual.isNone shouldBe expected
        }

        "RopOption.some" {
            val expected = true
            val actual = RopOption.some { "hello world." }

            actual.isSome shouldBe expected
        }

        "RopOption.tee -> None" {
            val expected = true
            val actual = RopOption.tee { null }

            actual.isNone shouldBe expected
        }

        "RopOption.tee -> Some" {
            val expected = true
            val actual = RopOption.tee { "hello world." }

            actual.isSome shouldBe expected
        }

        "flatten" {
            val expected = "hello world."
            val option = RopOption.some { RopOption.some { expected } }
            val actual = option.flatten()

            actual.getOrThrow() shouldBe expected
        }

    }

}