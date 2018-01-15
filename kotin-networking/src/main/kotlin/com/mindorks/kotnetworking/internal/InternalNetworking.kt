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

import android.net.TrafficStats
import com.mindorks.kotnetworking.common.ConnectionClassManager
import com.mindorks.kotnetworking.common.KotConstants
import com.mindorks.kotnetworking.common.Method
import com.mindorks.kotnetworking.error.KotError
import com.mindorks.kotnetworking.request.KotRequest
import com.mindorks.kotnetworking.utils.KotUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by amitshekhar on 01/05/17.
 */
class InternalNetworking {

    companion object {

        var sOkHttpClient: OkHttpClient = defaultOkHttpClient()
        var userAgent: String? = null

        private fun defaultOkHttpClient(): OkHttpClient =
                OkHttpClient().newBuilder().apply {
                    connectTimeout(60, TimeUnit.SECONDS)
                    readTimeout(60, TimeUnit.SECONDS)
                    writeTimeout(60, TimeUnit.SECONDS)
                }.build()

        @Throws(KotError::class)
        fun makeSimpleRequestAndGetResponse(kotRequest: KotRequest): Response? {

            val okHttpRequest: Request
            val okHttpResponse: Response?

            try {
                var builder: Request.Builder = Request.Builder().url(kotRequest.getFormattedUrl())
                addHeadersToRequestBuilder(builder, kotRequest)
                var requestBody: RequestBody? = null

                when (kotRequest.method) {
                    Method.GET -> {
                        builder = builder.get()
                    }
                    Method.POST -> {
                        requestBody = kotRequest.getRequestBody()
                        builder = builder.post(requestBody)
                    }
                    Method.PUT -> {
                        requestBody = kotRequest.getRequestBody()
                        builder = builder.put(requestBody)
                    }
                    Method.DELETE -> {
                        requestBody = kotRequest.getRequestBody()
                        builder = builder.delete(requestBody)
                    }
                    Method.HEAD -> {
                        builder = builder.head()
                    }
                    Method.PATCH -> {
                        requestBody = kotRequest.getRequestBody()
                        builder = builder.patch(requestBody)
                    }
                }

                kotRequest.cacheControl?.let {
                    builder.cacheControl(kotRequest.cacheControl)
                }
                okHttpRequest = builder.build()

                if (kotRequest.okHttpClient != null)
                    kotRequest.call = kotRequest.okHttpClient?.newBuilder()?.cache(sOkHttpClient.cache())?.build()?.newCall(okHttpRequest)
                else
                    kotRequest.call = sOkHttpClient.newCall(okHttpRequest)

                val startTime = System.currentTimeMillis()
                val startBytes = TrafficStats.getTotalRxBytes()
                okHttpResponse = kotRequest.call?.execute()
                val timeTaken = System.currentTimeMillis() /*endTime*/ - startTime
                if (okHttpResponse?.cacheResponse() == null) {
                    val finalBytes = TrafficStats.getTotalRxBytes()
                    val diffBytes: Long? = when {
                        finalBytes == TrafficStats.UNSUPPORTED.toLong() || startBytes == TrafficStats.UNSUPPORTED.toLong() ->
                            okHttpResponse?.body()?.contentLength()
                        else -> finalBytes - startBytes
                    }
                    ConnectionClassManager.instance?.updateBandwidth(diffBytes, timeTaken)

                    val bytesSent = when {
                        requestBody != null && requestBody.contentLength() > 0L ->
                            requestBody.contentLength()
                        else -> -1L
                    }
                    val bytesReceived = diffBytes ?: 0
                    KotUtils.sendAnalytics(kotRequest.analyticsListener, timeTaken, bytesSent, bytesReceived, false)
                } else if (kotRequest.analyticsListener != null) {
                    if (okHttpResponse.networkResponse() == null) {
                        KotUtils.sendAnalytics(kotRequest.analyticsListener, timeTaken, 0, 0, true)
                    } else {
                        val bytesSent = when{
                            requestBody != null && requestBody.contentLength() != 0L ->
                                requestBody.contentLength()
                            else -> -1L
                        }
                        KotUtils.sendAnalytics(kotRequest.analyticsListener, timeTaken, bytesSent, 0, true)
                    }
                }


            } catch (ioe: IOException) {
                throw KotError(ioe)
            }

            return okHttpResponse
        }

        @Throws(KotError::class)
        fun makeDownloadRequestAndGetResponse(kotRequest: KotRequest): Response? {
            val okHttpRequest: Request
            val okHttpResponse: Response?
            try {
                var builder: Request.Builder = Request.Builder().url(kotRequest.getFormattedUrl())
                addHeadersToRequestBuilder(builder, kotRequest)
                builder = builder.get()
                kotRequest.cacheControl?.let { builder.cacheControl(it) }
                okHttpRequest = builder.build()

                val okHttpClient = if (kotRequest.okHttpClient != null) {
                    kotRequest.okHttpClient?.newBuilder()
                            ?.cache(sOkHttpClient.cache())
                            ?.addInterceptor { chain ->
                                val response: Response = chain.proceed(chain.request())
                                response.newBuilder()
                                        .body(ResponseProgressBody(response.body(), kotRequest.getDownloadProgressListener()))
                                        .build()
                            }?.build()
                } else {
                    sOkHttpClient.newBuilder().addInterceptor { chain ->
                        val response: Response = chain.proceed(chain.request())
                        response.newBuilder()
                                .body(ResponseProgressBody(response.body(), kotRequest.getDownloadProgressListener()))
                                .build()
                    }?.build()
                }

                kotRequest.call = okHttpClient?.newCall(okHttpRequest)

                val startTime = System.currentTimeMillis()
                val startBytes = TrafficStats.getTotalRxBytes()
                okHttpResponse = kotRequest.call?.execute()
                KotUtils.saveFile(okHttpResponse, kotRequest.dirPath, kotRequest.fileName)
                val timeTaken = System.currentTimeMillis() /*endTime*/ - startTime
                if (okHttpResponse?.cacheResponse() == null) {
                    val finalBytes = TrafficStats.getTotalRxBytes()
                    val diffBytes: Long? = when(TrafficStats.UNSUPPORTED.toLong()) {
                        finalBytes, startBytes -> okHttpResponse?.body()?.contentLength()
                        else -> finalBytes - startBytes
                    }
                    ConnectionClassManager.instance?.updateBandwidth(diffBytes, timeTaken)
                    val bytesReceived = diffBytes ?: 0
                    KotUtils.sendAnalytics(kotRequest.analyticsListener, timeTaken, -1, bytesReceived, false)
                } else if (kotRequest.analyticsListener != null) {
                    KotUtils.sendAnalytics(kotRequest.analyticsListener, timeTaken, -1, 0, true)
                }

            } catch (ioe: IOException) {
                try {
                    val destinationFile = File(kotRequest.dirPath + File.separator + kotRequest.fileName)
                    if (destinationFile.exists()) {
                        destinationFile.delete()
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                throw KotError(ioe)
            }
            return okHttpResponse
        }

        @Throws(KotError::class)
        fun makeUploadRequestAndGetResponse(kotRequest: KotRequest): Response? {
            val okHttpRequest: Request
            val okHttpResponse: Response?

            try {
                var builder: Request.Builder = Request.Builder().url(kotRequest.getFormattedUrl())
                addHeadersToRequestBuilder(builder, kotRequest)
                val requestBody: RequestBody = kotRequest.getMultiPartRequestBody()
                val requestBodyLength = requestBody.contentLength()
                builder = builder.post(RequestProgressBody(requestBody,
                        kotRequest.uploadProgressListener))

                kotRequest.cacheControl?.let {
                    builder.cacheControl(kotRequest.cacheControl)
                }

                okHttpRequest = builder.build()

                kotRequest.call = kotRequest.okHttpClient?.let {
                    it.newBuilder()?.cache(sOkHttpClient.cache())?.build()?.newCall(okHttpRequest)
                } ?: sOkHttpClient.newCall(okHttpRequest)

                val startTime = System.currentTimeMillis()
                okHttpResponse = kotRequest.call?.execute()
                val timeTaken = System.currentTimeMillis() /*endTime*/ - startTime

                if (kotRequest.analyticsListener != null) {
                    okHttpResponse?.cacheResponse()?.let {
                        val byteReceived = it.body()?.contentLength()
                        KotUtils.sendAnalytics(kotRequest.analyticsListener, timeTaken,
                                requestBodyLength, byteReceived ?: 0, false)
                    } ?: okHttpResponse?.networkResponse()?.let {
                        KotUtils.sendAnalytics(kotRequest.analyticsListener, timeTaken, requestBodyLength, 0, true)
                    } ?: KotUtils.sendAnalytics(kotRequest.analyticsListener, timeTaken, 0, 0, true)
                }
            } catch (ioe: IOException) {
                throw KotError(ioe)
            }

            return okHttpResponse
        }

        private fun addHeadersToRequestBuilder(builder: Request.Builder, kotRequest: KotRequest) {
            when {
                kotRequest.userAgent != null -> builder.addHeader(KotConstants.USER_AGENT, kotRequest.userAgent)
                userAgent != null -> {
                    kotRequest.userAgent = userAgent
                    builder.addHeader(KotConstants.USER_AGENT, userAgent)
                }
            }
            val requestHeaders = kotRequest.getHeaders()
            builder.headers(requestHeaders)
            if (kotRequest.userAgent != null && !requestHeaders.names().contains(KotConstants.USER_AGENT)) {
                builder.addHeader(KotConstants.USER_AGENT, kotRequest.userAgent)
            }
        }

    }
}