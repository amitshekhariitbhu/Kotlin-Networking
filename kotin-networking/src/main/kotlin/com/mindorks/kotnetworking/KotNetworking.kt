package com.mindorks.kotnetworking

import android.content.Context
import com.mindorks.kotnetworking.common.Method
import com.mindorks.kotnetworking.requestbuidler.GetRequestBuilder
import com.mindorks.kotnetworking.requestbuidler.PostRequestBuilder

/**
 * Created by amitshekhar on 30/04/17.
 */
class KotNetworking private constructor() {


    companion object {

        fun initialize(context: Context) {

        }

        fun get(url: String): GetRequestBuilder {
            return GetRequestBuilder(url)
        }

        fun head(url: String): GetRequestBuilder {
            return GetRequestBuilder(url, Method.HEAD)
        }

        fun post(url: String): PostRequestBuilder {
            return PostRequestBuilder(url)
        }

        fun put(url: String): PostRequestBuilder {
            return PostRequestBuilder(url, Method.PUT)
        }

    }


}