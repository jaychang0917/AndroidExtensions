package com.jaychang.extensions.demo

import androidx.test.InstrumentationRegistry
import androidx.test.annotation.UiThreadTest
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
  @UiThreadTest
  @Test
  fun useAppContext() {
    // Context of the app under test.
    val appContext = InstrumentationRegistry.getContext()
    Assert.assertEquals("com.jaychang.extensions.demo", appContext.packageName)
  }
}