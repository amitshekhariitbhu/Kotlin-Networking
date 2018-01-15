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

package com.mindorks.kotnetworking.request

import com.mindorks.kotnetworking.common.*
import com.mindorks.kotnetworking.core.Core
import com.mindorks.kotnetworking.error.KotError
import com.mindorks.kotnetworking.interfaces.Parser
import com.mindorks.kotnetworking.internal.KotRequestQueue
import com.mindorks.kotnetworking.requestbuidler.DownloadBuilder
import com.mindorks.kotnetworking.requestbuidler.GetRequestBuilder
import com.mindorks.kotnetworking.requestbuidler.MultipartRequestBuilder
import com.mindorks.kotnetworking.requestbuidler.PostRequestBuilder
import com.mindorks.kotnetworking.utils.KotUtils
import com.mindorks.kotnetworking.utils.ParseUtil
import okhttp3.*
import okio.Okio
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.lang.reflect.Type
import java.util.concurrent.Executor
import java.util.concurrent.Future


/**
 * Created by amitshekhar on 30/04/17.
 */
class KotRequest {

    //region Member Variables
    var method: Method
    var url: String
    var dirPath: String? = null
    var fileName: String? = null
    var bodyParameterMap: MutableMap<String, String>? = null
    var urlEncodedFormBodyParameterMap: MutableMap<String, String>? = null
    var headersMap: MutableMap<String, String>
    val tag: Any?
    val queryParameterMap: MutableMap<String, String>
    val pathParameterMap: MutableMap<String, String>
    val multiPartFileMap: MutableMap<String, File> = mutableMapOf()
    var jsonObjectRequestCallback: ((response: JSONObject?, error: KotError?) -> Unit)? = null
    var jsonArrayRequestCallback: ((response: JSONArray?, error: KotError?) -> Unit)? = null
    var stringRequestCallback: ((response: String?, error: KotError?) -> Unit)? = null
    var mOkHttpResponseListener: ((response: Response?, error: KotError?) -> Unit)? = null
    var mParsedResponseListener: ((response: Any?, error: KotError?) -> Unit)? = null
    var downloadCallback: ((error: KotError?) -> Unit)? = null
    var requestType: RequestType
    var responseType: ResponseType? = null
    var cacheControl: CacheControl? = null
    var executor: Executor? = null
    var okHttpClient: OkHttpClient? = null
    var userAgent: String? = null
    var call: Call? = null
    var priority: Priority
    var sequenceNumber: Int = 0
    var progress: Int = 0
    var percentageThresholdForCancelling: Int = 0
    var isCancelled = false
    var isDelivered = false
    var future: Future<*>? = null
    var uploadProgressListener: ((bytesDownloaded: Long, totalBytes: Long) -> Unit)? = null
    var analyticsListener: ((timeTakenInMillis: Long, bytesSent: Long, bytesReceived: Long, isFromCache: Boolean) -> Unit)? = null
    var mType: Type? = null


    private var downloadProgressListener: ((bytesDownloaded: Long, totalBytes: Long) -> Unit)? = null
    private var applicationJsonString: String? = null
    private var stringBody: String? = null
    private var file: File? = null
    private var bytes: ByteArray? = null
    private var customMediaType: MediaType? = null
    private var JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8")
    private var MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8")

    //endregion

    //region Constructors
    constructor(getRequestBuilder: GetRequestBuilder) {
        this.method = getRequestBuilder.method
        this.url = getRequestBuilder.url
        this.priority = getRequestBuilder.priority
        this.headersMap = getRequestBuilder.headersMap
        this.queryParameterMap = getRequestBuilder.queryParameterMap
        this.pathParameterMap = getRequestBuilder.pathParameterMap
        this.requestType = RequestType.SIMPLE
        this.cacheControl = getRequestBuilder.cacheControl
        this.executor = getRequestBuilder.executor
        this.okHttpClient = getRequestBuilder.okHttpClient
        this.userAgent = getRequestBuilder.userAgent
        this.tag = getRequestBuilder.tag
    }

    constructor(postRequestBuilder: PostRequestBuilder) {
        this.method = postRequestBuilder.method
        this.url = postRequestBuilder.url
        this.priority = postRequestBuilder.priority
        this.headersMap = postRequestBuilder.headersMap
        this.queryParameterMap = postRequestBuilder.queryParameterMap
        this.pathParameterMap = postRequestBuilder.pathParameterMap
        this.bodyParameterMap = postRequestBuilder.bodyParameterMap
        this.applicationJsonString = postRequestBuilder.applicationJsonString
        this.urlEncodedFormBodyParameterMap = postRequestBuilder.urlEncodedFormBodyParameterMap
        this.file = postRequestBuilder.file
        this.bytes = postRequestBuilder.bytes
        this.stringBody = postRequestBuilder.stringBody
        postRequestBuilder.customContentType?.let { mediaType -> this.customMediaType = MediaType.parse(mediaType) }
        this.requestType = RequestType.SIMPLE
        this.cacheControl = postRequestBuilder.cacheControl
        this.executor = postRequestBuilder.executor
        this.okHttpClient = postRequestBuilder.okHttpClient
        this.userAgent = postRequestBuilder.userAgent
        this.tag = postRequestBuilder.tag
    }


    constructor(multipartRequestBuilder: MultipartRequestBuilder) {
        this.method = multipartRequestBuilder.method
        this.url = multipartRequestBuilder.url
        this.priority = multipartRequestBuilder.priority
        this.headersMap = multipartRequestBuilder.headersMap
        this.queryParameterMap = multipartRequestBuilder.queryParameterMap
        this.pathParameterMap = multipartRequestBuilder.pathParameterMap
        multipartRequestBuilder.mCustomContentType?.let { mediaType -> this.customMediaType = MediaType.parse(mediaType) }
        this.requestType = RequestType.MULTIPART
        this.cacheControl = multipartRequestBuilder.cacheControl
        this.percentageThresholdForCancelling = multipartRequestBuilder.mPercentageThresholdForCancelling
        this.executor = multipartRequestBuilder.executor
        this.okHttpClient = multipartRequestBuilder.okHttpClient
        this.userAgent = multipartRequestBuilder.userAgent
        this.tag = multipartRequestBuilder.tag
        this.multiPartFileMap.putAll(multipartRequestBuilder.mMultiPartFileMap)
    }

    constructor(downloadBuilder: DownloadBuilder) {
        this.method = Method.GET
        this.url = downloadBuilder.url
        this.priority = downloadBuilder.priority
        this.dirPath = downloadBuilder.dirPath
        this.fileName = downloadBuilder.fileName
        this.headersMap = downloadBuilder.headersMap
        this.queryParameterMap = downloadBuilder.queryParameterMap
        this.pathParameterMap = downloadBuilder.pathParameterMap
        this.requestType = RequestType.DOWNLOAD
        this.cacheControl = downloadBuilder.cacheControl
        this.percentageThresholdForCancelling = downloadBuilder.percentageThresholdForCancelling
        this.executor = downloadBuilder.executor
        this.okHttpClient = downloadBuilder.okHttpClient
        this.userAgent = downloadBuilder.userAgent
        this.tag = downloadBuilder.tag
    }
    //endregion

    //region Listeners
    fun setUploadProgressListener(progressListener: (bytesDownloaded: Long, totalBytes: Long) -> Unit): KotRequest {
        uploadProgressListener = progressListener
        return this
    }

    fun setAnalyticsListener(analyticsListener: (timeTakenInMillis: Long, bytesSent: Long, bytesReceived: Long, isFromCache: Boolean) -> Unit): KotRequest {
        this.analyticsListener = analyticsListener
        return this
    }

    fun setDownloadProgressListener(downloadProgressListener: (bytesDownloaded: Long, totalBytes: Long) -> Unit): KotRequest {
        this.downloadProgressListener = downloadProgressListener
        return this
    }
    //endregion

    //region Getters

    fun getAsJSONObject(handler: (result: JSONObject?, error: KotError?) -> Unit) {
        responseType = ResponseType.JSON_OBJECT
        jsonObjectRequestCallback = handler
        KotRequestQueue.addRequest(this)
    }

    fun getAsString(handler: (result: String?, error: KotError?) -> Unit) {
        responseType = ResponseType.STRING
        stringRequestCallback = handler
        KotRequestQueue.addRequest(this)
    }

    fun getAsJSONArray(handler: (result: JSONArray?, error: KotError?) -> Unit) {
        responseType = ResponseType.JSON_ARRAY
        jsonArrayRequestCallback = handler
        KotRequestQueue.addRequest(this)
    }

    fun getAsOkHttpResponse(handler: (response: Response?, error: KotError?) -> Unit) {
        this.responseType = ResponseType.OK_HTTP_RESPONSE
        this.mOkHttpResponseListener = handler
        KotRequestQueue.addRequest(this)
    }

    inline fun <reified T> getAsParseResponse(noinline handler: (response: Any?, error: KotError?) -> Unit) {
        this.responseType = ResponseType.PARSED
        this.mParsedResponseListener = handler
        this.mType = T::class.java
        KotRequestQueue.addRequest(this)
    }

    fun getFormattedUrl(): String {
        var tempUrl = url

        pathParameterMap.entries.forEach { entry -> tempUrl = tempUrl.replace("{${entry.key}}", entry.value) }

        val urlBuilder: HttpUrl.Builder = HttpUrl.parse(tempUrl).newBuilder()

        queryParameterMap.entries.forEach { entry -> urlBuilder.addQueryParameter(entry.key, entry.value) }

        return urlBuilder.build().toString()

    }

    fun getHeaders(): Headers {
        val builder: Headers.Builder = Headers.Builder()
        try {
            headersMap.entries.forEach { entry -> builder.add(entry.key, entry.value) }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return builder.build()
    }

    fun getRequestBody(): RequestBody {
        when {
            applicationJsonString != null -> {
                if (customMediaType != null) {
                    return RequestBody.create(customMediaType, applicationJsonString)
                }
                return RequestBody.create(JSON_MEDIA_TYPE, applicationJsonString)
            }
            stringBody != null -> {
                if (customMediaType != null) {
                    return RequestBody.create(customMediaType, stringBody)
                }
                return RequestBody.create(MEDIA_TYPE_MARKDOWN, stringBody)
            }
            file != null -> {
                if (customMediaType != null) {
                    return RequestBody.create(customMediaType, file)
                }
                return RequestBody.create(MEDIA_TYPE_MARKDOWN, file)
            }
            bytes != null -> {
                if (customMediaType != null) {
                    return RequestBody.create(customMediaType, bytes)
                }
                return RequestBody.create(MEDIA_TYPE_MARKDOWN, bytes)
            }
            else -> {
                val builder: FormBody.Builder = FormBody.Builder()
                try {
                    bodyParameterMap?.entries?.forEach { entry -> builder.add(entry.key, entry.value) }
                    urlEncodedFormBodyParameterMap?.entries?.forEach { entry -> builder.addEncoded(entry.key, entry.value) }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                return builder.build()
            }
        }
    }


    fun getMultiPartRequestBody(): RequestBody {
        val builder = MultipartBody.Builder()
                .setType(if (customMediaType == null) MultipartBody.FORM else customMediaType)
        try {
            for ((key, value) in multiPartFileMap) {
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"$key\""),
                        RequestBody.create(null, value))
            }
            for ((key, value) in multiPartFileMap.entries) {
                val fileName = value.name
                val fileBody = RequestBody.create(MediaType.parse(KotUtils.getMimeType(fileName)),
                        value)
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"$key\"; filename=\"$fileName\""),
                        fileBody)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return builder.build()
    }

    fun getDownloadProgressListener(): ((Long, Long) -> Unit)? {
        return { bytesDownloaded: Long, totalBytes: Long ->
            if (!isCancelled) {
                downloadProgressListener?.invoke(bytesDownloaded, totalBytes)
            }
        }
    }


    fun startDownload(handler: (kotError: KotError?) -> Unit) {
        downloadCallback = handler
        KotRequestQueue.addRequest(this)
    }

    //endregion

    //region Delivery
    fun deliverError(kotError: KotError) {
        try {
            if (!isDelivered) {
                if (isCancelled) {
                    kotError.errorDetail = KotConstants.REQUEST_CANCELLED_ERROR
                    kotError.errorCode = 0
                }
                deliverErrorResponse(kotError)
            }
            isDelivered = true
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun deliverSuccessResponse(kotResponse: KotResponse<*>) {
        jsonObjectRequestCallback?.invoke(kotResponse.result as JSONObject?, null)
        jsonArrayRequestCallback?.invoke(kotResponse.result as JSONArray?, null)
        stringRequestCallback?.invoke(kotResponse.result as String?, null)
        downloadCallback?.invoke(null)
        mParsedResponseListener?.invoke(kotResponse.result, null)

        finish()
    }

    private fun deliverErrorResponse(kotError: KotError) {
        jsonObjectRequestCallback?.invoke(null, kotError)
        jsonArrayRequestCallback?.invoke(null, kotError)
        stringRequestCallback?.invoke(null, kotError)
        downloadCallback?.invoke(kotError)
        mOkHttpResponseListener?.invoke(null, kotError)
        mParsedResponseListener?.invoke(null, kotError)

    }

    fun deliverOkHttpResponse(okHttpResponse: Response) {
        try {
            isDelivered = true
            if (!isCancelled) {
                executor?.execute { mOkHttpResponseListener?.invoke(okHttpResponse, null) } ?:
                        Core.executorSupplier.forMainThreadTasks().execute { mOkHttpResponseListener?.invoke(okHttpResponse, null) }
            } else {
                val kotError = KotError()
                kotError.errorDetail = KotConstants.REQUEST_CANCELLED_ERROR
                kotError.errorCode = 0
                mOkHttpResponseListener?.invoke(null, kotError)
                finish()
            }
        } catch(ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun deliverResponse(kotResponse: KotResponse<*>) {
        try {
            isDelivered = true
            if (!isCancelled) {
                executor?.execute { deliverSuccessResponse(kotResponse) } ?:
                        Core.executorSupplier.forMainThreadTasks().execute { deliverSuccessResponse(kotResponse) }
            } else {
                val kotError = KotError()
                kotError.errorDetail = KotConstants.REQUEST_CANCELLED_ERROR
                kotError.errorCode = 0
                deliverErrorResponse(kotError)
                finish()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
    //endregion

    //region Parsers
    fun parseNetworkError(kotError: KotError): KotError {
        try {
            val errorResponse: Response? = kotError.response
            kotError.errorBody = errorResponse?.let {
                errorResponse.body()?.let {
                    errorResponse.body().source()?.let {
                        source ->
                        Okio.buffer(source).readUtf8()
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return kotError
    }

    fun parseResponse(response: Response): KotResponse<*>? {
        when (responseType) {

            ResponseType.JSON_ARRAY -> return try {
                val json = JSONArray(Okio.buffer(response.body().source()).readUtf8())
                KotResponse.success(json)
            } catch (e: Exception) {
                KotResponse.failed(KotUtils.getErrorForParse(KotError(e)))
            }

            ResponseType.STRING -> return try {
                KotResponse.success(Okio.buffer(response.body().source()).readUtf8())
            } catch (e: Exception) {
                KotResponse.failed(KotUtils.getErrorForParse(KotError(e)))
            }

            ResponseType.JSON_OBJECT -> return try {
                val json = JSONObject(Okio.buffer(response.body().source()).readUtf8())
                KotResponse.success(json)
            } catch (e: Exception) {
                KotResponse.failed(KotUtils.getErrorForParse(KotError(e)))
            }

            ResponseType.OK_HTTP_RESPONSE -> {
            }

            ResponseType.PREFETCH -> {
            }

            ResponseType.PARSED -> {
                var result: Any?
                ParseUtil.parserFactory?.let {
                    result = (ParseUtil.parserFactory as Parser.Factory)
                            .responseBodyParser(mType as Type)?.convert(response.body())
                    return KotResponse.success(result)
                }

                return KotResponse.failed(KotError("Something wrong"))
            }

            else -> {
            }
        }

        return null
    }
    //endregion

    //region Download Request helper methods

    fun updateDownloadCompletion() {
        isDelivered = true
        if (downloadCallback != null) {
            if (!isCancelled) {
                if (executor != null) {
                    executor?.execute({
                        downloadCallback?.invoke(null)
                        finish()
                    })
                } else {
                    Core.executorSupplier.forMainThreadTasks().execute({
                        downloadCallback?.invoke(null)
                        finish()
                    })
                }
            } else {
                deliverError(KotError())
                finish()
            }
        } else {
            finish()
        }
    }
    //endregion

    //region finisher

    fun cancel(forceCancel: Boolean) {
        try {
            if (forceCancel || percentageThresholdForCancelling == 0
                    || progress < percentageThresholdForCancelling) {
                isCancelled = true
                call?.cancel()
                future?.cancel(true)
                if (!isDelivered) deliverError(KotError())
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun destroy() {
        downloadProgressListener = null
        jsonObjectRequestCallback = null
        jsonArrayRequestCallback = null
        stringRequestCallback = null
        mOkHttpResponseListener = null
        mParsedResponseListener = null
        downloadCallback = null
        uploadProgressListener = null
        analyticsListener = null

    }

    fun finish() {
        destroy()
        KotRequestQueue.finish(this)
    }

    //endregion
}