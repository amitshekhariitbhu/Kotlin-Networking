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
import com.mindorks.kotnetworking.common.RequestBuilder
import com.mindorks.kotnetworking.request.KotRequest

/**
 * Created by amitshekhar on 30/04/17.
 */
class PostRequestBuilder(var url: String, var method: Method = Method.POST) : RequestBuilder {

    val headersMap: Map<String, String> = mutableMapOf()

    override fun addHeaders(key: String, value: String): PostRequestBuilder {
        headersMap.plus(Pair(key, value))
        return this
    }

    override fun addHeaders(headerMap: Map<String, String>): PostRequestBuilder {
        headersMap.plus(headerMap)
        return this
    }

    fun build(): KotRequest {
        return KotRequest(this)
    }

}