package com.github.funczz.kotlin.rop.result

open class RopResultException @JvmOverloads constructor(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    companion object {
        private const val serialVersionUID: Long = 4157144687361944787L
    }
}
