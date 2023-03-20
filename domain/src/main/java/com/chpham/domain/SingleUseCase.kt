package com.chpham.domain

interface SingleUseCase<T> {
    suspend fun execute(): T
}