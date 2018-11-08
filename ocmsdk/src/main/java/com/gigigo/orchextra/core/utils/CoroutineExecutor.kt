package com.gigigo.orchextra.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

private val uiContext: CoroutineContext = Dispatchers.Main

private val bgContext: CoroutineDispatcher by lazy {
  val numProcessors = Runtime.getRuntime().availableProcessors()
  when {
    numProcessors <= 2 -> newFixedThreadPoolContext(2, "background")
    else -> Dispatchers.Default
  }
}

class CoroutineExecutor {
  fun background(command: () -> Unit) {
    execute(
        BACKGROUND, command
    )
  }

  fun disk(command: () -> Unit) {
    execute(
        DISK, command
    )
  }

  fun network(command: () -> Unit) {
    execute(
        NETWORK, command
    )
  }

  fun ui(command: () -> Unit) {
    execute(Dispatchers.Main, command)
  }

  fun execute(context: CoroutineContext, command: () -> Unit) {
    Timber.d(Thread.currentThread().name)
    GlobalScope.launch(bgContext) {
      Timber.d("launch ${Thread.currentThread().name}")
      command.invoke()
    }
  }

  companion object {
    @JvmField
    val BACKGROUND: CoroutineContext = bgContext
    @JvmField
    val DISK: CoroutineContext = bgContext
    @JvmField
    val NETWORK: CoroutineContext = bgContext
    @JvmField
    val MAIN: CoroutineContext = uiContext
  }
}
