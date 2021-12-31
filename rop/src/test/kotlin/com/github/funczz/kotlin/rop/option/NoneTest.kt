package io.kotlintest.provided.com.github.funczz.kotlin.rop.option

import com.github.funczz.kotlin.rop.option.*
import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.provided.ISerializableUtil
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import java.util.*

internal class NoneTest : StringSpec(), ISerializableUtil {

    init {

        "None: Serialization" {
            val expected = true
            val origin = RopOption.none<String>()
            val actual = origin.dump().load() as RopOption<*>

            actual.javaClass shouldBe origin.javaClass
            actual shouldNotBeSameInstanceAs origin
            actual.isNone shouldBe expected
        }

        "None: toString" {
            RopOption.none<String>().toString() shouldBe "None"
        }

        "None: hashCode" {
            RopOption.none<String>().hashCode() shouldBe RopOption.none<String>().hashCode()
            RopOption.none<String>().hashCode() shouldBe RopOption.none<Int>().hashCode()
            RopOption.none<String>().hashCode() shouldNotBe RopOption.some { "hello world." }.hashCode()
            RopOption.none<String>().hashCode() shouldNotBe "hello world.".hashCode()
        }

        "None: equals" {
            RopOption.none<String>().equals(RopOption.none<String>()) shouldBe true
            RopOption.none<String>().equals(RopOption.none<Int>()) shouldBe true
            RopOption.none<String>().equals(0) shouldBe false
        }

        "None: isNone" {
            RopOption.none<String>().isNone shouldBe true
        }

        "None: isSome" {
            RopOption.none<String>().isSome shouldBe false
        }

        "None: getOrElse" {
            val expected = "hello world."
            val option = RopOption.none<String>()
            val actual = option.getOrElse { "hello world." }

            actual shouldBe expected
        }

        "None: getOrNull" {
            val expected = null
            val option = RopOption.none<String>()
            val actual = option.getOrNull()

            actual shouldBe expected
        }

        "None: getOrThrow" {
            val expected = "RopOption: None.getOrThrow - Value does not exist"
            val option = RopOption.none<String>()
            val actual = shouldThrow<RopOptionException> {
                option.getOrThrow()
            }.message

            actual shouldBe expected
        }

        "None: Optional.toRopOption" {
            val expected = RopOption.none<String>()
            val optional = Optional.empty<String>()
            val actual = optional.toRopOption()

            actual shouldBe expected
        }

        "None: toOptional" {
            val expected = Optional.empty<String>()
            val option = RopOption.none<String>()
            val actual = option.toOptional()

            actual shouldBe expected
        }

        "None: fold" {
            val expected = "None"
            val option = RopOption.none<String>()
            val actual = option.fold(
                none = { expected },
                some = { it }
            )

            actual shouldBe expected
        }

        "None: match" {
            val expected = "None"
            val option = RopOption.none<String>()
            var actual = ""
            option.match({ actual = "None" }) {}

            actual shouldBe expected
        }

        "None: filter" {
            val expected = true
            val option = RopOption.none<String>()
            val actual = option.filter { it == "hello world." }

            actual.isNone shouldBe expected
        }

        "None: map" {
            val expected = "None"
            val option = RopOption.none<String>()
            var actual = ""
            option.map {
                it.uppercase()
            }.match(
                none = { actual = "None" },
                some = {}
            )

            actual shouldBe expected
        }

        "None: mapOrElse" {
            val expected = "mapOrElse"
            val option = RopOption.none<String>()
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

        "None: andThen" {
            val expected = "None"
            val option = RopOption.none<String>()
            var actual = ""
            option.andThen {
                RopOption.tee { it.uppercase() }
            }.match(
                none = { actual = "None" },
                some = {}
            )

            actual shouldBe expected
        }

        "None: orElse" {
            val expected = "orElse"
            val option = RopOption.none<String>()
            var actual = ""
            option.orElse {
                RopOption.tee { expected }
            }.match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "None: andThenOrElse" {
            val expected = "andThenOrElse"
            val option = RopOption.none<Int>()
            var actual = ""
            option.andThenOrElse(
                fn = { RopOption.tee { "%s".format(it) } },
                or = { RopOption.tee { expected } }
            ).match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "None, None -> None: xor" {
            val expected = "xor"
            val option = RopOption.none<String>()
            var actual = ""
            option.xor {
                RopOption.tee { null }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

        "None, Some -> Some: xor" {
            val expected = "xor"
            val option = RopOption.none<String>()
            var actual = ""
            option.xor {
                RopOption.tee { expected }
            }.match(
                none = {},
                some = { actual = it }
            )

            actual shouldBe expected
        }

        "None, None -> None: zip" {
            val expected = "zip"
            val option = RopOption.none<String>()
            var actual = ""
            option.zip {
                RopOption.tee { null }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

        "None, Some -> None: zip" {
            val expected = "zip"
            val option = RopOption.none<String>()
            var actual = ""
            option.zip {
                RopOption.tee { expected }
            }.match(
                none = { actual = expected },
                some = {}
            )

            actual shouldBe expected
        }

    }

}