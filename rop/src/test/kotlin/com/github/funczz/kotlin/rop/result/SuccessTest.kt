package io.kotlintest.provided.com.github.funczz.kotlin.rop.result

import com.github.funczz.kotlin.rop.result.RopResult
import com.github.funczz.kotlin.rop.result.Success
import com.github.funczz.kotlin.rop.result.fold
import com.github.funczz.kotlin.rop.result.match
import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.provided.ISerializableUtil
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec

internal class SuccessTest : StringSpec(), ISerializableUtil {

    init {

        "Success: Serialization" {
            val expected = "hello world."
            val origin = RopResult.success { expected }
            val actual = origin.dump().load() as Success<*>

            actual.javaClass shouldBe origin.javaClass
            actual shouldNotBeSameInstanceAs origin
            actual.isSuccess shouldBe true
            actual.getOrThrow() shouldBe expected
        }

        "Success: toString" {
            val expected = "Success(hello world.)"
            RopResult.success { "hello world." }.toString() shouldBe expected
        }

        "Success: hashCode" {
            RopResult.success { "hello world." }
                .hashCode() shouldBe RopResult.success { "hello world." }.hashCode()
            RopResult.success { "hello world." }
                .hashCode() shouldNotBe RopResult.success { "HELLO WORLD." }.hashCode()
            RopResult.success { "hello world." }
                .hashCode() shouldNotBe RopResult.failure<String>(error = Exception("test")).hashCode()
        }

        "Success: equals" {
            RopResult.success { "hello world." }
                .equals(RopResult.success { "hello world." }) shouldBe true
            RopResult.success { "hello world." }
                .equals(RopResult.success { "HELLO WORLD." }) shouldBe false
            RopResult.success { "hello world." }
                .equals(RopResult.failure<String>(error = Exception("test"))) shouldBe false
        }

        "Success: isFailure" {
            RopResult.success { "hello world." }.isFailure shouldBe false
        }

        "Success: isSuccess" {
            RopResult.success { "hello world." }.isSuccess shouldBe true
        }

        "Success: getOrElse" {
            val expected = "hello world."
            val result = RopResult.success { expected }
            val actual = result.getOrElse { "" }

            actual shouldBe expected
        }

        "Success: getOrNull" {
            val expected = "hello world."
            val result = RopResult.success { expected }
            val actual = result.getOrNull()

            actual shouldBe expected
        }

        "Success: getOrThrow" {
            val expected = "hello world."
            val result = RopResult.success { expected }
            val actual = result.getOrThrow()

            actual shouldBe expected
        }

        "Success: fold" {
            val expected = "hello world."
            val result = RopResult.success { expected }
            val actual = result.fold(
                failure = { it.message ?: "" },
                success = { it }
            )

            actual shouldBe expected
        }

        "Success: match" {
            val expected = "hello world."
            val result = RopResult.success { expected }
            var actual = ""
            result.match { actual = expected }

            actual shouldBe expected
        }

        "Success -> Failure: filter" {
            val expected = "RopResult: Success.filter - Does not match filter"
            val result = RopResult.success { "hello world." }
            var actual = ""
            result.filter {
                false
            }.match(
                failure = { actual = it.message ?: "" },
                success = {}
            )

            actual shouldBe expected
        }

        "Success -> Success: filter" {
            val expected = "hello world."
            val result = RopResult.success { expected }
            var actual = ""
            result.filter {
                true
            }.match(
                failure = {},
                success = { actual = it }
            )

            actual shouldBe expected
        }

        "Success: map" {
            val expected = "hello world."
            val result = RopResult.success { expected.uppercase() }
            var actual = ""
            result.map {
                it.lowercase()
            }.match(
                failure = {},
                success = { actual = it }
            )

            actual shouldBe expected
        }

        "Success: mapOrElse" {
            val expected = "hello world."
            val result = RopResult.success { expected.uppercase() }
            var actual = ""
            result.mapOrElse(
                fn = { it.lowercase() },
                or = { expected.uppercase() }
            ).match(
                failure = {},
                success = { actual = it }
            )

            actual shouldBe expected
        }

        "Success: andThen" {
            val expected = "hello world."
            val result = RopResult.success { expected.uppercase() }
            var actual = ""
            result.andThen {
                RopResult.tee { it.lowercase() }
            }.match(
                failure = {},
                success = { actual = it }
            )

            actual shouldBe expected
        }

        "Success: orElse" {
            val expected = "hello world."
            val result = RopResult.success { expected }
            var actual = ""
            result.orElse {
                RopResult.tee { expected.uppercase() }
            }.match(
                failure = {},
                success = { actual = it }
            )

            actual shouldBe expected
        }

        "Success: andThenOrElse" {
            val expected = "hello world."
            val result = RopResult.success { expected.uppercase() }
            var actual = ""
            result.andThenOrElse(
                fn = { RopResult.tee { it.lowercase() } },
                or = { RopResult.tee { expected.uppercase() } }
            ).match(
                failure = {},
                success = { actual = it }
            )

            actual shouldBe expected
        }

        "Success, Failure -> Failure: zip" {
            val expected = "hello world."
            val result = RopResult.success { expected }
            var actual = ""
            result.zip {
                RopResult.tee { throw Exception(it) }
            }.match(
                failure = { actual = it.message ?: "" },
                success = {}
            )

            actual shouldBe expected
        }

        "Success, Success -> Success: zip" {
            val v = "hello world."
            val expected = "(1:hello world., 2:hello world.)"
            val result = RopResult.success { "1:%s".format(v) }
            var actual = ""
            result.zip {
                RopResult.tee { "2:%s".format(v) }
            }.match(
                failure = {},
                success = { actual = it.toString() }
            )

            actual shouldBe expected
        }

    }

}


