package com.chpham.domain

/**
 * Interface for a single-use case. A single-use case is an operation that is executed only once and returns a result of type T.
 *
 * @since March 19, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
interface SingleUseCase<T> {

    /**
     * Executes the single-use case and returns a result of type T.
     * @return the result of the operation
     */
    suspend fun execute(): T
}
