package com.mindorks.kotnetworking.core

import java.util.concurrent.Executor

/**
 * Created by amitshekhar on 30/04/17.
 */
interface ExecutorSupplier {

    fun forNetworkTasks(): KotExecutor
    fun forImmediateNetworkTasks(): KotExecutor
    fun forMainThreadTasks(): Executor

}