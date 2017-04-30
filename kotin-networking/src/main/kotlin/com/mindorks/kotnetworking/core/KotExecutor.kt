package com.mindorks.kotnetworking.core

import com.mindorks.kotnetworking.internal.KotRunnable
import java.util.concurrent.*

/**
 * Created by amitshekhar on 30/04/17.
 */
class KotExecutor(maxNumThreads: Int, threadFactory: ThreadFactory) :
        ThreadPoolExecutor(maxNumThreads, maxNumThreads, 0, TimeUnit.MILLISECONDS,
                PriorityBlockingQueue<Runnable>(), threadFactory) {

    override fun submit(task: Runnable?): Future<*> {
        val futureTask = KotFutureTask(task as KotRunnable)
        execute(futureTask)
        return futureTask
    }

}