package com.nyc.schools.network

import kotlinx.coroutines.CoroutineDispatcher

class CoroutinesDispatcherProvider(
    val main: CoroutineDispatcher,
    val computation: CoroutineDispatcher,
    val io: CoroutineDispatcher
)