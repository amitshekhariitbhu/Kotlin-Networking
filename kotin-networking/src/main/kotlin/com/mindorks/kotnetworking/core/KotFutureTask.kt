package com.mindorks.kotnetworking.core

import com.mindorks.kotnetworking.internal.KotRunnable
import java.util.concurrent.FutureTask

/**
 * Created by amitshekhar on 30/04/17.
 */
class KotFutureTask(var runnable: KotRunnable) : FutureTask<KotRunnable>(runnable, null), Comparable<KotFutureTask> {

    override fun compareTo(other: KotFutureTask): Int {
        return 0
    }

}