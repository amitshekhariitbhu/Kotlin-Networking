package com.mindorks.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.mindorks.kotnetworking.KotNetworking

class MainActivity : AppCompatActivity() {

    companion object {

        private val TAG: String? = "MainActivity"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        KotNetworking.get("www.example.com")
                .addHeaders("key", "value")
                .build()
                .getResponse { result, error ->
                    if (error == 0L) {
                        Log.d(TAG, result.toString())
                    } else {
                        Log.d(TAG, error.toString())
                    }
                }
    }

}
