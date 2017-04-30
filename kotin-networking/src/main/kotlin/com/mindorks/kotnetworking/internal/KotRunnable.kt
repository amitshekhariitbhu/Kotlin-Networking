package com.mindorks.kotnetworking.internal

import com.mindorks.kotnetworking.request.KotRequest

/**
 * Created by amitshekhar on 30/04/17.
 */
class KotRunnable(val request: KotRequest) : Runnable {

    override fun run() {
        // do some networking here and deliver response
        request.deliverSuccess()
    }

}