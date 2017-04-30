package com.mindorks.kotnetworking.core

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

/**
 * Created by amitshekhar on 30/04/17.
 */
class MainThreadExecutor : Executor {

    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun execute(runnable: Runnable?) {
        handler.post(runnable)
    }

}