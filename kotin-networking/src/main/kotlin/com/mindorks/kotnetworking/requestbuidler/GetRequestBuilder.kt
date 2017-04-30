package com.mindorks.kotnetworking.requestbuidler

import com.mindorks.kotnetworking.common.Method
import com.mindorks.kotnetworking.common.RequestBuilder
import com.mindorks.kotnetworking.request.KotRequest

/**
 * Created by amitshekhar on 30/04/17.
 */
class GetRequestBuilder(var url: String, var method: Method = Method.GET) : RequestBuilder {

    val headersMap: Map<String, String> = mutableMapOf()

    override fun addHeaders(key: String, value: String): GetRequestBuilder {
        headersMap.plus(Pair(key, value))
        return this
    }

    override fun addHeaders(headerMap: Map<String, String>): GetRequestBuilder {
        headersMap.plus(headerMap)
        return this
    }

    fun build(): KotRequest {
        return KotRequest(this)
    }

}