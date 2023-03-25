package com.chpham.domain

/**
 * A generic interface for a single-use case with a parameter.
 * Implementations of this interface represent a use case in which a single parameter is required to execute the use case, and the result is returned asynchronously.
 * @param P The type of the parameter required to execute the use case.
 * @param R The type of the result returned by the use case.
 *
 * @since March 19, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
interface SingleUseCaseWithParameter<P, R> {
    /**
     * Executes the use case with the specified parameter and returns the result asynchronously.
     * @param parameter The parameter required to execute the use case.
     * @return The result of executing the use case.
     */
    suspend fun execute(parameter: P): R
}
