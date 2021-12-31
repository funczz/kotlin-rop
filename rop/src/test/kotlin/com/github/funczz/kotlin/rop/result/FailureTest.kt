package io.kotlintest.provided.com.github.funczz.kotlin.rop.result

import com.github.funczz.kotlin.rop.result.*
import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.provided.ISerializableUtil
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec

internal class FailureTest : StringSpec(), ISerializableUtil {

    init {

        "Failure: Serialization" {
            val expected = Exception("test")
            val origin = RopResult.failure<String>(error = expected)
            val actual = origin.dump().load() as Failure<*>

            actual.javaClass shouldBe origin.javaClass
            actual shouldNotBeSameInstanceAs origin
            actual.isFailure shouldBe true
            actual.error.message shouldBe expected.message
        }

        "Failure: toString" {
            val expected = "Failure(java.lang.Exception: test)"
            RopResult.failure<String>(error = Exception("test", Exception("cause")))
                .toString() shouldBe expected
        }

        "Failure: hashCode" {
            RopResult.failure<String>(error = Exception("test"))
                .hashCode() shouldBe RopResult.failure<String>(error = Exception("test")).hashCode()
            RopResult.failure<String>(error = Exception("test"))
                .hashCode() shouldBe RopResult.failure<Int>(error = Exception("test")).hashCode()
            RopResult.failure<String>(error = Exception("test"))
                .hashCode() shouldBe RopResult.failure<String>(error = Exception("TEST")).hashCode()
            RopResult.failure<String>(error = Exception("test"))
                .hashCode() shouldBe RopResult.failure<String>(
                error = RopResultException(
                    "test",
                    cause = Exception("test")
                )
            ).hashCode()
            RopResult.failure<String>(error = Exception("test"))
                .hashCode() shouldNotBe RopResult.success { "hello world." }.hashCode()
        }

        "Failure: isFailure" {
            RopResult.failure<String>(error = Exception("test")).isFailure shouldBe true
        }

        "Failure: isSuccess" {
            RopResult.failure<String>(error = Exception("test")).isSuccess shouldBe false
        }

        "Failure: equals" {
            RopResult.failure<String>(error = Exception("test"))
                .equals(RopResult.failure<String>(error = Exception("test"))) shouldBe true
            RopResult.failure<String>(error = Exception("test"))
                .equals(RopResult.failure<Int>(error = Exception("test"))) shouldBe true
            RopResult.failure<String>(error = Exception("test"))
                .equals(RopResult.success { "hello world." }) shouldBe false
            RopResult.failure<String>(error = Exception("test"))
                .equals("hello world.") shouldBe false
        }

        "Failure: getOrElse" {
            val expected = "hello world."
            val result = RopResult.failure<String>(error = Exception(expected))
            val actual = result.getOrElse { it.message ?: "" }

            actual shouldBe expected
        }

        "Failure: getOrNull" {
            val expected = null
            val result = RopResult.failure<String>(error = Exception("test"))
            val actual = result.getOrNull()

            actual shouldBe expected
        }

        "Failure: getOrThrow" {
            val expected = "RopResult: Failure.getOrThrow - An exception was thrown. cause=java.lang.Exception: test"
            val result = RopResult.failure<String>(error = Exception("test"))
            val actual = shouldThrow<RopResultException> {
                result.getOrThrow()
            }.message

            actual shouldBe expected
        }

        "Failure: fold" {
            val expected = "hello world."
            val result = RopResult.failure<String>(error = Exception(expected))
            val actual = result.fold(
                failure = { it.message ?: "" },
                success = { it }
            )

            actual shouldBe expected
        }

        "Failure: match" {
            val expected = "hello world."
            val result = RopResult.failure<String>(error = Exception(expected))
            var actual = ""
            result.match({ actual = it.message ?: "" }) {}

            actual shouldBe expected
        }

        "Failure: filter" {
            val expected = "hello world."
            val result = RopResult.failure<String>(error = Exception(expected))
            var actual = ""
            result.filter {
                it == "hello world."
            }.match(
                failure = { actual = it.message ?: "" },
                success = {}
            )

            actual shouldBe expected
        }

        "Failure: map" {
            val expected = "hello world."
            val result = RopResult.failure<String>(error = Exception(expected))
            var actual = ""
            result.map {
                it.uppercase()
            }.match(
                failure = { actual = it.message ?: "" },
                success = {}
            )

            actual shouldBe expected
        }

        "Failure: mapOrElse" {
            val expected = "hello world."
            val result = RopResult.failure<String>(error = Exception(expected))
            var actual = ""
            result.mapOrElse(
                fn = { it.uppercase() },
                or = { it.message ?: "" }
            ).match(
                failure = {},
                success = { actual = it }
            )

            actual shouldBe expected
        }

        "Failure: andThen" {
            val expected = "hello world."
            val result = RopResult.failure<String>(error = Exception(expected))
            var actual = ""
            result.andThen {
                RopResult.tee { it.uppercase() }
            }.match(
                failure = { actual = it.message ?: "" },
                success = {}
            )

            actual shouldBe expected
        }

        "Failure: orElse" {
            val expected = "hello world."
            val result = RopResult.failure<String>(error = Exception(expected))
            var actual = ""
            result.orElse {
                RopResult.tee { it.message ?: "" }
            }.match(
                failure = {},
                success = { actual = it }
            )

            actual shouldBe expected
        }

        "Failure: andThenOrElse" {
            val expected = "hello world."
            val result = RopResult.failure<String>(error = Exception(expected))
            var actual = ""
            result.andThenOrElse(
                fn = { RopResult.tee { it.uppercase() } },
                or = { RopResult.tee { it.message ?: "" } }
            ).match(
                failure = {},
                success = { actual = it }
            )

            actual shouldBe expected
        }

        "Failure, Failure -> Failure: zip" {
            val expected = "hello world."
            val result = RopResult.failure<String>(error = Exception(expected))
            var actual = ""
            result.zip {
                RopResult.tee { throw Exception("HELLO WORLD.") }
            }.match(
                failure = { actual = it.message ?: "" },
                success = {}
            )

            actual shouldBe expected
        }

        "Failure, Success -> Failure: zip" {
            val expected = "hello world."
            val result = RopResult.failure<String>(error = Exception(expected))
            var actual = ""
            result.zip {
                RopResult.tee { "HELLO WORLD." }
            }.match(
                failure = { actual = it.message ?: "" },
                success = {}
            )

            actual shouldBe expected
        }

    }

}


