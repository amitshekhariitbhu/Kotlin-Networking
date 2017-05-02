/*
 *    Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.mindorks.kotnetworking.requestbuidler

import com.mindorks.kotnetworking.common.Method
import com.mindorks.kotnetworking.common.Priority
import com.mindorks.kotnetworking.common.RequestBuilder
import com.mindorks.kotnetworking.request.KotRequest
import com.mindorks.kotnetworking.utils.ParseUtil
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

/**
 * Created by amitshekhar on 30/04/17.
 */
class GetRequestBuilder(var url: String, var method: Method = Method.GET) : RequestBuilder {

    private var priority: Priority = Priority.MEDIUM
    private var tag: Any? = null
    private val headersMap: Map<String, String> = mutableMapOf()
    private val queryParameterMap: Map<String, String> = mutableMapOf()
    private val pathParameterMap: Map<String, String> = mutableMapOf()
    private var cacheControl: CacheControl? = null
    private var executor: Executor? = null
    private var okHttpClient: OkHttpClient? = null
    private var userAgent: String? = null

    override fun setPriority(priority: Priority): RequestBuilder {
        this.priority = priority
        return this
    }

    override fun setTag(tag: Any): RequestBuilder {
        this.tag = tag
        return this
    }

    override fun addHeaders(key: String, value: String): GetRequestBuilder {
        headersMap.plus(Pair(key, value))
        return this
    }

    override fun addHeaders(headerMap: Map<String, String>): GetRequestBuilder {
        headersMap.plus(headerMap)
        return this
    }

    override fun addHeaders(objectAny: Any): RequestBuilder {
        ParseUtil.parserFactory?.getStringMap(objectAny)?.let { it -> headersMap.plus(it) }
        return this
    }

    override fun addQueryParameter(key: String, value: String): RequestBuilder {
        queryParameterMap.plus(Pair(key, value))
        return this
    }

    override fun addQueryParameter(queryParameterMap: Map<String, String>): RequestBuilder {
        queryParameterMap.plus(queryParameterMap)
        return this
    }

    override fun addQueryParameter(objectAny: Any): RequestBuilder {
        ParseUtil.parserFactory?.getStringMap(objectAny)?.let { it -> queryParameterMap.plus(it) }
        return this
    }

    override fun addPathParameter(key: String, value: String): RequestBuilder {
        pathParameterMap.plus(Pair(key, value))
        return this
    }

    override fun addPathParameter(pathParameterMap: Map<String, String>): RequestBuilder {
        this.pathParameterMap.plus(pathParameterMap)
        return this
    }

    override fun addPathParameter(objectAny: Any): RequestBuilder {
        ParseUtil.parserFactory?.getStringMap(objectAny)?.let { it -> pathParameterMap.plus(it) }
        return this
    }

    override fun doNotCacheResponse(): RequestBuilder {
        cacheControl = CacheControl.Builder().noStore().build()
        return this
    }

    override fun getResponseOnlyIfCached(): RequestBuilder {
        cacheControl = CacheControl.FORCE_CACHE
        return this
    }

    override fun getResponseOnlyFromNetwork(): RequestBuilder {
        cacheControl = CacheControl.FORCE_NETWORK
        return this
    }

    override fun setMaxAgeCacheControl(maxAge: Int, timeUnit: TimeUnit): RequestBuilder {
        cacheControl = CacheControl.Builder().maxAge(maxAge, timeUnit).build()
        return this
    }

    override fun setMaxStaleCacheControl(maxStale: Int, timeUnit: TimeUnit): RequestBuilder {
        cacheControl = CacheControl.Builder().maxStale(maxStale, timeUnit).build()
        return this
    }

    override fun setExecutor(executor: Executor): RequestBuilder {
        this.executor = executor
        return this
    }

    override fun setOkHttpClient(okHttpClient: OkHttpClient): RequestBuilder {
        this.okHttpClient = okHttpClient
        return this
    }

    override fun setUserAgent(userAgent: String): RequestBuilder {
        this.userAgent = userAgent
        return this
    }

    fun build(): KotRequest {
        return KotRequest(this)
    }

}