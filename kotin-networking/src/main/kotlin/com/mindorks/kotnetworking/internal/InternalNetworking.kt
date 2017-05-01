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

package com.mindorks.kotnetworking.internal

import com.mindorks.kotnetworking.error.KotError
import okhttp3.OkHttpClient
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * Created by amitshekhar on 01/05/17.
 */
class InternalNetworking private constructor() {

    companion object {

        var okHttpClient: OkHttpClient = defaultOkHttpClient()

        fun defaultOkHttpClient(): OkHttpClient = OkHttpClient()
                .newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

        @Throws(KotError::class)
        fun makeSimpleRequestAndGetResponse(): Response {
            TODO("Add Logic")
        }

        @Throws(KotError::class)
        fun makeDownloadRequestAndGetResponse(): Response {
            TODO("Add Logic")
        }

        @Throws(KotError::class)
        fun makeUploadRequestAndGetResponse(): Response {
            TODO("Add Logic")
        }

    }
}