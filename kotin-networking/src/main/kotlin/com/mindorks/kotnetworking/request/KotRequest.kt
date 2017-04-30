package com.mindorks.kotnetworking.request

import com.mindorks.kotnetworking.common.Method
import com.mindorks.kotnetworking.core.Core
import com.mindorks.kotnetworking.internal.KotRequestQueue
import com.mindorks.kotnetworking.requestbuidler.GetRequestBuilder
import com.mindorks.kotnetworking.requestbuidler.PostRequestBuilder


/**
 * Created by amitshekhar on 30/04/17.
 */
class KotRequest {

    var method: Method
    var url: String
    var headersMap: Map<String, String>
    var responseCallback: ((result: Long, error: Long) -> Unit)? = null

    constructor(getRequestBuilder: GetRequestBuilder) {
        this.method = getRequestBuilder.method
        this.url = getRequestBuilder.url
        this.headersMap = getRequestBuilder.headersMap
    }

    constructor(postRequestBuilder: PostRequestBuilder) {
        this.method = postRequestBuilder.method
        this.url = postRequestBuilder.url
        this.headersMap = postRequestBuilder.headersMap
    }

    fun getResponse(handler: (result: Long, error: Long) -> Unit) {
        responseCallback = handler
        KotRequestQueue.instance.addRequest(this)
    }

    fun deliverSuccess() {
        Core.instance
                .executorSupplier
                .forMainThreadTasks()
                .execute {
                    responseCallback?.invoke(10, 0)
                }
    }

}