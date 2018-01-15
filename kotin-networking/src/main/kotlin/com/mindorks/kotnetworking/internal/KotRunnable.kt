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

import com.mindorks.kotnetworking.common.KotResponse
import com.mindorks.kotnetworking.common.Priority
import com.mindorks.kotnetworking.common.RequestType
import com.mindorks.kotnetworking.common.ResponseType
import com.mindorks.kotnetworking.core.Core
import com.mindorks.kotnetworking.error.KotError
import com.mindorks.kotnetworking.request.KotRequest
import com.mindorks.kotnetworking.utils.KotUtils
import com.mindorks.kotnetworking.utils.SourceCloseUtil
import okhttp3.Response

/**
 * Created by amitshekhar on 30/04/17.
 */
class KotRunnable(val request: KotRequest) : Runnable {

    val priority: Priority = request.priority
    val sequence: Int = request.sequenceNumber

    override fun run() {
        when (request.requestType) {
            RequestType.SIMPLE -> executeSimpleRequest()
            RequestType.DOWNLOAD -> executeDownloadRequest()
            RequestType.MULTIPART -> executeUploadRequest()
        }
    }

    private fun executeSimpleRequest() {
        var okHttpResponse: Response? = null
        try {

            okHttpResponse = InternalNetworking.makeSimpleRequestAndGetResponse(request)

            if (okHttpResponse == null) {
                deliverError(request, KotUtils.getErrorForConnection(KotError()))
                return
            }

            if (request.responseType == ResponseType.OK_HTTP_RESPONSE) {
                request.deliverOkHttpResponse(okHttpResponse)
            }

            if (okHttpResponse.code() >= 400) {
                deliverError(request, KotUtils.getErrorForServerResponse(KotError(okHttpResponse),
                        request, okHttpResponse.code()))
                return
            }

            val kotResponse: KotResponse<*>? = request.parseResponse(okHttpResponse)

            kotResponse?.let {
                if (!kotResponse.isSuccess()) {
                    kotResponse.error?.let { error -> deliverError(request, error) }
                    return
                }

                kotResponse.response = okHttpResponse as Response
                request.deliverResponse(kotResponse)
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            deliverError(request, KotUtils.getErrorForConnection(KotError(ex)))
        } finally {
            SourceCloseUtil.close(okHttpResponse, request)
        }
    }


    private fun executeUploadRequest() {
        var okHttpResponse: Response? = null

        try {
            okHttpResponse = InternalNetworking.makeUploadRequestAndGetResponse(request)

            if (okHttpResponse == null) {
                deliverError(request, KotUtils.getErrorForConnection(KotError()))
                return
            }

            if (request.responseType == ResponseType.OK_HTTP_RESPONSE) {
                request.deliverOkHttpResponse(okHttpResponse)
            }

            if (okHttpResponse.code() >= 400) {
                deliverError(request, KotUtils.getErrorForServerResponse(KotError(okHttpResponse),
                        request, okHttpResponse.code()))
                return
            }

            val kotResponse: KotResponse<*>? = request.parseResponse(okHttpResponse)

            kotResponse?.let {
                if (!kotResponse.isSuccess()) {
                    kotResponse.error?.let { error -> deliverError(request, error) }
                    return
                }

                kotResponse.response = okHttpResponse as Response
                request.deliverResponse(kotResponse)
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            deliverError(request, KotUtils.getErrorForConnection(KotError(ex)))
        } finally {
            SourceCloseUtil.close(okHttpResponse, request)
        }
    }

    private fun executeDownloadRequest() {

        try {
            InternalNetworking.makeDownloadRequestAndGetResponse(request)?.let {
                if (it.code() >= 400) {
                    deliverError(request, KotUtils.getErrorForServerResponse(KotError(it), request, it.code()))
                    return
                }
                request.updateDownloadCompletion()
            } ?: deliverError(request, KotUtils.getErrorForConnection(KotError()))
        } catch (ex: Exception) {
            ex.printStackTrace()
            deliverError(request, KotUtils.getErrorForConnection(KotError(ex)))
        }
    }

    private fun deliverError(kotRequest: KotRequest, kotError: KotError) {
        Core.executorSupplier.forMainThreadTasks().execute {
            kotRequest.deliverError(kotError)
            kotRequest.finish()
        }
    }

}