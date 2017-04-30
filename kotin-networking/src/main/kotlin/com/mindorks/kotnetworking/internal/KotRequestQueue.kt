package com.mindorks.kotnetworking.internal

import com.mindorks.kotnetworking.core.Core
import com.mindorks.kotnetworking.request.KotRequest

/**
 * Created by amitshekhar on 30/04/17.
 */
class KotRequestQueue private constructor() {

    private object Holder {
        val INSTANCE = KotRequestQueue()
    }

    companion object {
        val instance: KotRequestQueue by lazy { Holder.INSTANCE }
    }

    fun addRequest(request: KotRequest) {
        var future = Core.instance
                .executorSupplier
                .forNetworkTasks()
                .submit(KotRunnable(request))
    }

}