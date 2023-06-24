package com.example.testapplication.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class MainCoroutineRule:TestWatcher() {
    val testDispatcher= StandardTestDispatcher(TestCoroutineScheduler())
    val testScope= TestScope(testDispatcher)
    override fun starting(description: Description?) {
       Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
    }
}