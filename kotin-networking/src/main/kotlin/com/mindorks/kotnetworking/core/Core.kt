package com.mindorks.kotnetworking.core

/**
 * Created by amitshekhar on 30/04/17.
 */
class Core private constructor() {

    val executorSupplier: ExecutorSupplier = DefaultExecutorSupplier()

    private object Holder {
        val INSTANCE = Core()
    }

    companion object {
        val instance: Core by lazy { Holder.INSTANCE }
    }

}