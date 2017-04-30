package com.mindorks.sample

import android.app.Application
import com.mindorks.kotnetworking.KotNetworking

/**
 * Created by amitshekhar on 30/04/17.
 */
class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        KotNetworking.initialize(this)
    }
}