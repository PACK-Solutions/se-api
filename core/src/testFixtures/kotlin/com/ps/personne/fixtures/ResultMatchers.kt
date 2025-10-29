package com.ps.personne.fixtures

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.fold
import io.kotest.assertions.AssertionErrorBuilder.Companion.fail
import io.kotest.matchers.shouldBe

fun <T, E> Result<T, E>.shouldBeFailure(): E = this.fold(
    success = { v -> fail("Expected Err but was Ok($v)") },
    failure = { e -> e },
)

infix fun <T, E> Result<T, E>.shouldBeFailure(expected: E): E = this.fold(
    success = { v -> fail("Expected Err($expected) but was Ok($v)") },
    failure = { e ->
        e shouldBe expected
        e
    },
)

infix fun <T, E> Result<T, E>.shouldBeFailure(assertions: (E) -> Unit): E = this.fold(
    success = { v -> fail("Expected Err but was Ok($v)") },
    failure = { e ->
        assertions(e)
        e
    },
)

@JvmName("shouldBeFailureReified")
inline fun <reified EE> Result<*, *>.shouldBeFailureOf(): EE = this.fold(
    success = { v -> fail("Expected Err<${EE::class.simpleName}> but was Ok($v)") },
    failure = { e ->
        if (e is EE) e else fail("Expected Err of type ${EE::class.simpleName} but was ${e!!::class.simpleName}")
    },
)

fun <T, E> Result<T, E>.shouldBeSuccess(): T = this.fold(
    success = { it },
    failure = { e -> fail("Expected Ok but was Err($e)") },
)

infix fun <T, E> Result<T, E>.shouldBeSuccess(expected: T): T = this.fold(
    success = { v ->
        v shouldBe expected
        v
    },
    failure = { e -> fail("Expected Ok($expected) but was Err($e)") },
)

infix fun <T, E> Result<T, E>.shouldBeSuccess(assertions: (T) -> Unit): T = this.fold(
    success = { v ->
        assertions(v)
        v
    },
    failure = { e -> fail("Expected Ok but was Err($e)") },
)

@JvmName("shouldBeSuccessReified")
inline fun <reified TT> Result<*, *>.shouldBeSuccessOf(): TT = this.fold(
    success = { v ->
        if (v is TT) v else fail("Expected Ok<${TT::class.simpleName}> but was Ok(${v!!::class.simpleName})")
    },
    failure = { e -> fail("Expected Ok<${TT::class.simpleName}> but was Err($e)") },
)
