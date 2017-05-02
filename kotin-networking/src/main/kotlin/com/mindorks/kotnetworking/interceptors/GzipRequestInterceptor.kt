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

package com.mindorks.kotnetworking.interceptors

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import okio.BufferedSink
import okio.GzipSink
import okio.Okio
import java.io.IOException

/**
 * Created by amitshekhar on 02/05/17.
 */
class GzipRequestInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
            return chain.proceed(originalRequest)
        }

        val compressedRequest = originalRequest.newBuilder()
                .header("Content-Encoding", "gzip")
                .method(originalRequest.method(), forceContentLength(gzip(originalRequest.body())))
                .build()
        return chain.proceed(compressedRequest)
    }

    @Throws(IOException::class)
    private fun forceContentLength(requestBody: RequestBody): RequestBody {

        val buffer = Buffer()

        requestBody.writeTo(buffer)

        return object : RequestBody() {
            override fun contentType(): MediaType {
                return requestBody.contentType()
            }

            override fun contentLength(): Long {
                return buffer.size()
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                sink.write(buffer.snapshot())
            }
        }
    }


    private fun gzip(body: RequestBody): RequestBody {
        return object : RequestBody() {

            override fun contentType(): MediaType {
                return body.contentType()
            }

            override fun contentLength(): Long {
                return -1 // We don't know the compressed length in advance!
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                val gzipSink = Okio.buffer(GzipSink(sink))
                body.writeTo(gzipSink)
                gzipSink.close()
            }
        }
    }
}