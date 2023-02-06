package com.nyc.schools

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nyc.schools.network.CoroutinesDispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import org.junit.Rule
import org.junit.rules.TestRule

open class BaseTestClass {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    lateinit var coroutines: CoroutinesDispatcherProvider

    init {
        MockKAnnotations.init(this)
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.v(any(), any()) } returns 0
    }
}