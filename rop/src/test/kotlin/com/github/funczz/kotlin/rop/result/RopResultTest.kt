package io.kotlintest.provided.com.github.funczz.kotlin.rop.result

import com.github.funczz.kotlin.rop.result.*
import io.kotlintest.provided.ISerializableUtil
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

internal class RopResultTest : StringSpec(), ISerializableUtil {

    init {

        "RopResult.failure" {
            val expected = true
            val actual = RopResult.failure<String>(Exception("test"))

            actual.isFailure shouldBe expected
        }

        "RopResult.success" {
            val expected = true
            val actual = RopResult.success { "hello world." }

            actual.isSuccess shouldBe expected
        }

        "RopResult.tee -> Failure" {
            val expected = true
            val actual = RopResult.tee<String> { throw Exception("test") }

            actual.isFailure shouldBe expected
        }

        "RopResult.tee -> Success" {
            val expected = true
            val actual = RopResult.tee { "hello world." }

            actual.isSuccess shouldBe expected
        }

        "Success(Success): flatten" {
            val expected = "hello world."
            val result = RopResult.success { RopResult.success { expected } }
            val actual = result.flatten()

            actual.getOrThrow() shouldBe expected
        }

        "Success(Failure): flatten" {
            val result: RopResult<RopResult<String>> =
                RopResult.success { RopResult.tee<String> { throw Exception("test") } }
            val actual: RopResult<String> = result.flatten()

            actual.isFailure shouldBe true
        }

        "Failure: flatten" {
            val result: RopResult<RopResult<String>> = RopResult.failure<RopResult<String>>(error = Exception("test"))
            val actual: RopResult<String> = result.flatten()

            actual.isFailure shouldBe true
        }

        "Failure: toResult" {
            val expected = "hello world."
            val result = RopResult.failure<String>(error = Exception(expected))
            val actual = result
                .toResult()
                .exceptionOrNull()!!
                .message

            actual shouldBe expected
        }

        "Failure: Result.toRopResult" {
            val expected = "hello world."
            val result = Result.failure<String>(Exception(expected))
            val actual = result
                .toRopResult()
                .fold(
                    failure = { it.message ?: "" },
                    success = { it }
                )

            actual shouldBe expected
        }

        "Success: toResult" {
            val expected = "hello world."
            val result = RopResult.success { expected }
            val actual = result
                .toResult()
                .getOrThrow()

            actual shouldBe expected
        }

        "Success: Result.toRopResult" {
            val expected = "hello world."
            val result = Result.success(expected)
            val actual = result
                .toRopResult()
                .getOrThrow()

            actual shouldBe expected
        }
    }

}