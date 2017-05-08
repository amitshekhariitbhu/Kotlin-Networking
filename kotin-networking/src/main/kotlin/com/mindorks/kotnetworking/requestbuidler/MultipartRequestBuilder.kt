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
import com.mindorks.kotnetworking.request.KotRequest
import com.mindorks.kotnetworking.utils.ParseUtil
import java.io.File

/**
 * @author vinayagasundar
 */
class MultipartRequestBuilder(var url: String, var method: Method = Method.POST)
    : RequestBuilderImpl() {

    var mMultiPartParameterMap : MutableMap<String, String> = mutableMapOf()
    var mPercentageThresholdForCancelling : Int = 0
    var mCustomContentType: String? = null
    var mMultiPartFileMap: MutableMap<String, File> = mutableMapOf()



    fun addMultiPartParameter(key: String, value: String): MultipartRequestBuilder {
        mMultiPartParameterMap.put(key, value)
        return this
    }

    fun addMultiPartParameter(objectAny: Any): MultipartRequestBuilder {
        ParseUtil.parserFactory?.getStringMap(objectAny)?.let {
            stringMap -> this.mMultiPartParameterMap.putAll(stringMap)
        }
        return this
    }

    fun addMultiPartParameter(params : MutableMap<String, String>): MultipartRequestBuilder {
        mMultiPartParameterMap.putAll(params)
        return this
    }


    fun addMultiPartFile(key: String, file: File): MultipartRequestBuilder {
        mMultiPartFileMap.put(key, file)
        return this
    }

    fun addMultiPartFile(params: MutableMap<String, File>): MultipartRequestBuilder {
        mMultiPartFileMap.putAll(params)
        return this
    }


    fun setContentType(contentType: String): MultipartRequestBuilder {
        this.mCustomContentType = contentType
        return this
    }

    fun setPercentageThresholdForCancelling(threshold: Int): MultipartRequestBuilder {
        mPercentageThresholdForCancelling = threshold
        return this
    }


    override fun build(): KotRequest {
        return KotRequest(this)
    }
}