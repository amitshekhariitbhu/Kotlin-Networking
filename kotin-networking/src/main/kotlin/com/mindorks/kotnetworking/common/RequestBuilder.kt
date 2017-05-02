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

package com.mindorks.kotnetworking.common

import okhttp3.OkHttpClient
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

/**
 * Created by amitshekhar on 30/04/17.
 */
interface RequestBuilder {

    fun setPriority(priority: Priority): RequestBuilder

    fun setTag(tag: Any): RequestBuilder

    fun addHeaders(key: String, value: String): RequestBuilder

    fun addHeaders(headerMap: Map<String, String>): RequestBuilder

    fun addHeaders(objectAny: Any): RequestBuilder

    fun addQueryParameter(key: String, value: String): RequestBuilder

    fun addQueryParameter(queryParameterMap: Map<String, String>): RequestBuilder

    fun addQueryParameter(objectAny: Any): RequestBuilder

    fun addPathParameter(key: String, value: String): RequestBuilder

    fun addPathParameter(pathParameterMap: Map<String, String>): RequestBuilder

    fun addPathParameter(objectAny: Any): RequestBuilder

    fun doNotCacheResponse(): RequestBuilder

    fun getResponseOnlyIfCached(): RequestBuilder

    fun getResponseOnlyFromNetwork(): RequestBuilder

    fun setMaxAgeCacheControl(maxAge: Int, timeUnit: TimeUnit): RequestBuilder

    fun setMaxStaleCacheControl(maxStale: Int, timeUnit: TimeUnit): RequestBuilder

    fun setExecutor(executor: Executor): RequestBuilder

    fun setOkHttpClient(okHttpClient: OkHttpClient): RequestBuilder

    fun setUserAgent(userAgent: String): RequestBuilder

}