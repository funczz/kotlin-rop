package com.github.funczz.kotlin.rop.option

import java.util.*

/**
 * 入れ子になっている RopOption を一段階アンラップする
 * @return <code>RopOption<RopOption<T>></code> を アンラップした <code>RopOption<T></code>
 */
fun <T : Any> RopOption<RopOption<T>>.flatten(): RopOption<T> = when (this) {
    is None<RopOption<T>> -> RopOption.none()
    is Some<RopOption<T>> -> value
}

/**
 * None なら関数 none を適用し、
 * Some なら関数 some を適用する。
 * @param none 引数を持たず、戻り値のない関数
 * @param some 引数として型 T を持ち、戻り値のない関数
 */
inline fun <T : Any> RopOption<T>.match(none: () -> Unit = {}, some: (T) -> Unit) = when (this) {
    is None<T> -> none()
    is Some<T> -> some(value)
}

/**
 * None なら関数 none を適用し、
 * Some なら関数 some を適用する。
 * @param none 引数を持たず、型 R を返す関数
 * @param some 引数として型 T を持ち、型 R を返す関数
 * @return R
 */
inline fun <T : Any, R : Any> RopOption<T>.fold(none: () -> R, some: (T) -> R): R = when (this) {
    is None<T> -> none()
    is Some<T> -> some(value)
}

/**
 * Optional を RopOption に変換する。
 * @return RopOption
 */
fun <T : Any> Optional<T>.toRopOption(): RopOption<T> = RopOption.tee {
    if (isPresent) get() else null
}

/**
 * RopOption を Optional に変換する。
 * @return Optional
 */
fun <T : Any> RopOption<T>.toOptional(): Optional<T> {
    return fold(
        none = { Optional.empty() },
        some = { Optional.of(it) }
    )
}