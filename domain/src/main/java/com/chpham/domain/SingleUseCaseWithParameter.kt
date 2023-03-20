package com.chpham.domain

interface SingleUseCaseWithParameter<P, R> {
    suspend fun execute(parameter: P): R
}