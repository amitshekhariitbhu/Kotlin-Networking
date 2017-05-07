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

import com.mindorks.kotnetworking.common.Priority
import com.mindorks.kotnetworking.common.RequestBuilder
import com.mindorks.kotnetworking.utils.ParseUtil
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

/**
 * It's an implementation of {@link com.mindorks.kotnetworking.common.RequestBuilder}
 * It contain all the common variable so no need to write it again for Every method
 * @author
 */
abstract class RequestBuilderImpl : RequestBuilder {

    var priority: Priority = Priority.MEDIUM
    var tag: Any? = null
    val headersMap: MutableMap<String, String> = mutableMapOf()
    val queryParameterMap: MutableMap<String, String> = mutableMapOf()
    val pathParameterMap: MutableMap<String, String> = mutableMapOf()
    var cacheControl: CacheControl? = null
    var executor: Executor? = null
    var okHttpClient: OkHttpClient? = null
    var userAgent: String? = null


    override fun setPriority(priority: Priority): RequestBuilder {
        this.priority = priority
        return this
    }

    override fun setTag(tag: Any): RequestBuilder {
        this.tag = tag
        return this
    }

    override fun addHeaders(key: String, value: String): RequestBuilder {
        headersMap.put(key, value)
        return this
    }

    override fun addHeaders(headerMap: MutableMap<String, String>): RequestBuilder {
        headersMap.putAll(headerMap)
        return this
    }

    override fun addHeaders(objectAny: Any): RequestBuilder {
        ParseUtil.parserFactory?.getStringMap(objectAny)?.let { it -> headersMap.putAll(it) }
        return this
    }

    override fun addQueryParameter(key: String, value: String): RequestBuilder {
        queryParameterMap.put(key, value)
        return this
    }

    override fun addQueryParameter(queryParameterMap: MutableMap<String, String>): RequestBuilder {
        this.queryParameterMap.putAll(queryParameterMap)
        return this
    }

    override fun addQueryParameter(objectAny: Any): RequestBuilder {
        ParseUtil.parserFactory?.getStringMap(objectAny)?.let { it -> queryParameterMap.putAll(it) }
        return this
    }

    override fun addPathParameter(key: String, value: String): RequestBuilder {
        pathParameterMap.put(key, value)
        return this
    }

    override fun addPathParameter(pathParameterMap: MutableMap<String, String>): RequestBuilder {
        this.pathParameterMap.putAll(pathParameterMap)
        return this
    }

    override fun addPathParameter(objectAny: Any): RequestBuilder {
        ParseUtil.parserFactory?.getStringMap(objectAny)?.let { it -> pathParameterMap.putAll(it) }
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
}