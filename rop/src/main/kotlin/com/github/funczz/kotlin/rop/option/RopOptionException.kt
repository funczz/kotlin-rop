package com.github.funczz.kotlin.rop.option

class RopOptionException @JvmOverloads constructor(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    companion object {
        private const val serialVersionUID: Long = 9084568445714944612L
    }
}
