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

package com.mindorks.kotnetworking

import android.content.Context
import com.mindorks.kotnetworking.common.Method
import com.mindorks.kotnetworking.internal.KotRequestQueue
import com.mindorks.kotnetworking.requestbuidler.DownloadBuilder
import com.mindorks.kotnetworking.requestbuidler.GetRequestBuilder
import com.mindorks.kotnetworking.requestbuidler.MultipartRequestBuilder
import com.mindorks.kotnetworking.requestbuidler.PostRequestBuilder

/**
 * Created by amitshekhar on 30/04/17.
 */
object KotNetworking {

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

    fun upload(url: String): MultipartRequestBuilder {
        return MultipartRequestBuilder(url, Method.POST)
    }

    fun download(url: String, dirPath: String, fileName: String): DownloadBuilder {
        return DownloadBuilder(url, dirPath, fileName)
    }

    fun cancelAll() {
        KotRequestQueue.cancelAll(false)
    }
    fun forceCancelAll() {
        KotRequestQueue.cancelAll(true)
    }

    fun cancel(tag: Any) {
        KotRequestQueue.cancelRequestWithGivenTag(tag, false)
    }
}