package io.kotlintest.provided.com.github.funczz.kotlin.rop.option

import com.github.funczz.kotlin.rop.option.*
import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.provided.ISerializableUtil
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import java.util.*

internal class SomeTest : StringSpec(), ISerializableUtil {

    init {

        "Some: Serialization" {
            val expected = "hello world."
            val origin = RopOption.some { "hello world." }
            val actual = origin.dump().load() as RopOption<*>

            actual.javaClass shouldBe origin.javaClass
            actual shouldNotBeSameInstanceAs origin
            actual.getOrThrow() shouldBe expected
        }

        "Some: toString" {
            RopOption.some { "hello world." }.toString() shouldBe "Some(hello world.)"
            RopOption.some { 0 }.toString() shouldBe "Some(0)"
        }

        "Some: hashCode" {
            RopOption.some { "hello world." }.hashCode() shouldBe RopOption.some { "hello world." }.hashCode()
            RopOption.some { "hello world." }.hashCode() shouldNotBe RopOption.some { "HELLO WORLD." }.hashCode()
            RopOption.some { "hello world." }.hashCode() shouldNotBe RopOption.some { 0 }.hashCode()
            RopOption.some { "hello world." }.hashCode() shouldNotBe "hello world.".hashCode()
        }

        "Some: equals" {
            RopOption.some { "hello world." }.equals(RopOption.some { "hello world." }) shouldBe true
            RopOption.some { "hello world." }.equals(RopOption.some { "HELLO WORLD." }) shouldBe false
            RopOption.some { "hello world." }.equals(RopOption.some { 0 }) shouldBe false
        }

        "Some: isNone" {
            RopOption.some { "hello world." }.isNone shouldBe false
        }

        "Some: isSome" {
            RopOption.some { "hello world." }.isSome shouldBe true
        }

        "Some: getOrElse" {
            val expected = "hello world."
            val option = RopOption.some { "hello world." }
            val actual = option.getOrElse { "HELLO WORLD." }

            actual shouldBe expected
        }

        "Some: getOrNull" {
            val expected = "hello world."
            val option = RopOption.some { "hello world." }
            val actual = option.getOrNull()

            actual shouldBe expected
        }

        "Some: getOrThrow" {
            val expected = "hello world."
            val option = RopOption.some { "hello world." }
            val actual = option.getOrThrow()

            actual shouldBe expected
        }

        "Some: Optional.toRopOption" {
            val expected = RopOption.some { "hello world." }
            val optional = Optional.of("hello world.")
            val actual = optional.toRopOption()

            actual shouldBe expected
        }

        "Some: toOptional" {
            val expected = Optional.of("hello world.")
            val option = RopOption.some { "hello world." }
            val actual = option.toOptional()

            actual shouldBe expected
        }

        "Some: match" {
            val expected = "hello world."
            val option = RopOption.some { "hello world." }
            var actual = ""
            option.match { actual = it }

            actual shouldBe expected
        }

        "Some: filter" {
            val expected = true
            val option = RopOption.some { "hello world." }
            val actual = option.filter { it == "hello world." }

            actual.isSome shouldBe expected
        }

        "Some -> None: filter" {
            val expected = false
            val option = RopOption.some { "hello world." }
            val actual = option.filter { it == "HELLO WORLD." }

            actual.isSome shouldBe expected
        }

        "Some: fold" {
            val expected = "hello world."
            val option = RopOption.some { expected }
            val actual = option.fold(
                none = { "None" },
                some = { it }
            )

            actual shouldBe expected
        }

        "Some: map" {
            val expected = "HELLO WORLD."
            val option = RopOption.some { "hello world." }
            var actual = ""
            option.map {
                it.uppercase()
            }.match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "Some: mapOrElse" {
            val expected = "HELLO WORLD."
            val option = RopOption.some { "hello world." }
            var actual = ""
            option.mapOrElse(
                fn = { it.uppercase() },
                or = { "mapOrElse" }
            ).match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "Some: andThen" {
            val expected = "HELLO WORLD."
            val option = RopOption.some { "hello world." }
            var actual = ""
            option.andThen {
                RopOption.tee { it.uppercase() }
            }.match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "Some -> None: andThen" {
            val expected = "None"
            val option = RopOption.some { "hello world." }
            var actual = ""
            option.andThen {
                RopOption.tee { null }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

        "Some: orElse" {
            val expected = "hello world."
            val option = RopOption.some { expected }
            var actual = ""
            option.orElse {
                RopOption.tee { "orElse" }
            }.match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "Some: andThenOrElse" {
            val expected = "hello world."
            val option = RopOption.some { 0 }
            var actual = ""
            option.andThenOrElse(
                fn = { RopOption.tee { (0..it).joinToString { expected } } },
                or = { RopOption.tee { "andThenOrElse" } }
            ).match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "Some, None -> Some: xor" {
            val expected = "hello world."
            val option = RopOption.some { "hello world." }
            var actual = ""
            option.xor {
                RopOption.tee { null }
            }.match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "Some, Some -> None: xor" {
            val expected = "None"
            val option = RopOption.some { "hello world." }
            var actual = ""
            option.xor {
                RopOption.tee { "xor" }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

        "Some, None -> None: zip" {
            val expected = "None"
            val option = RopOption.some { "hello world." }
            var actual = ""
            option.zip {
                RopOption.tee { null }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

        "Some, Some -> Some: zip" {
            val expected = "hello world. zip"
            val option = RopOption.some { "hello world." }
            var actual = ""
            option.zip {
                RopOption.tee { "zip" }
            }.match(
                none = {},
                some = { actual = "${it.first} ${it.second}" }
            )

            actual shouldBe expected
        }

    }

}