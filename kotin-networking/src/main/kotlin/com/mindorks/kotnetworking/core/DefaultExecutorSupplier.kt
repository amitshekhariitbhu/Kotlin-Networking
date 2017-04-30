package com.mindorks.kotnetworking.core

import android.os.Process
import java.util.concurrent.Executor
import java.util.concurrent.ThreadFactory

/**
 * Created by amitshekhar on 30/04/17.
 */
class DefaultExecutorSupplier : ExecutorSupplier {

    companion object {
        private val DEFAULT_MAX_NUM_THREADS: Int = 2 * Runtime.getRuntime().availableProcessors() + 1
    }

    private val backgroundPriorityThreadFactory: ThreadFactory = PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND)
    private val mNetworkExecutor: KotExecutor = KotExecutor(DEFAULT_MAX_NUM_THREADS, backgroundPriorityThreadFactory)
    private val mImmediateNetworkExecutor: KotExecutor = KotExecutor(2, backgroundPriorityThreadFactory)
    private val mMainThreadExecutor: Executor = MainThreadExecutor()

    override fun forNetworkTasks(): KotExecutor {
        return mNetworkExecutor
    }

    override fun forImmediateNetworkTasks(): KotExecutor {
        return mImmediateNetworkExecutor
    }

    override fun forMainThreadTasks(): Executor {
        return mMainThreadExecutor
    }

}