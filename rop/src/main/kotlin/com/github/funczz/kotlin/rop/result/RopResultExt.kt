package com.github.funczz.kotlin.rop.result

/**
 * 入れ子になっている RopResult を一段階アンラップする
 * @return <code>RopResult<RopResult<T>></code> を アンラップした <code>RopResult<T></code>
 */
fun <T : Any> RopResult<RopResult<T>>.flatten(): RopResult<T> = when (this) {
    is Failure<RopResult<T>> -> andThen { it }
    is Success<RopResult<T>> -> value
}

/**
 * Failure なら関数 failure を適用し、
 * Success なら関数 success を適用する。
 * @param failure 引数として Throwable を持ち、戻り値のない関数
 * @param success 引数として型 T を持ち、戻り値のない関数
 */
inline fun <T : Any> RopResult<T>.match(failure: (Throwable) -> Unit = {}, success: (T) -> Unit) = when (this) {
    is Failure<T> -> failure(this.error)
    is Success<T> -> success(this.value)
}

/**
 * Failure なら関数 failure を適用し、
 * Success なら関数 success を適用する。
 * @param failure 引数として Throwable を持ち、型 R を返す関数
 * @param success 引数として型 T を持ち、型 R を返す関数
 * @return R
 */
inline fun <T : Any, R : Any> RopResult<T>.fold(failure: (Throwable) -> R, success: (T) -> R): R = when (this) {
    is Failure<T> -> failure(this.error)
    is Success<T> -> success(this.value)
}

/**
 * Result を RopResult に変換する。
 * @return RopResult
 */
fun <T : Any> Result<T>.toRopResult(): RopResult<T> = this.fold(
    onFailure = { RopResult.failure(it) },
    onSuccess = { RopResult.tee { it } }
)

/**
 * RopResult を Result に変換する。
 * @return Result
 */
fun <T : Any> RopResult<T>.toResult(): Result<T> = this.fold(
    failure = { Result.failure(it) },
    success = { Result.success(it) }
)