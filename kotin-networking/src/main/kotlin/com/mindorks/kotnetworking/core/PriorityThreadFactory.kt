package com.mindorks.kotnetworking.core

import android.os.Process
import java.util.concurrent.ThreadFactory

/**
 * Created by amitshekhar on 30/04/17.
 */
class PriorityThreadFactory(val threadPriority: Int) : ThreadFactory {

    override fun newThread(runnable: Runnable?): Thread {
        val wrapperRunnable = Runnable {
            try {
                Process.setThreadPriority(threadPriority)
            } catch(ignored: Throwable) {

            }
            runnable?.run()
        }
        return Thread(wrapperRunnable)
    }

}