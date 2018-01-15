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

package com.mindorks.kotnetworking.utils

import com.mindorks.kotnetworking.common.KotConstants
import com.mindorks.kotnetworking.core.Core
import com.mindorks.kotnetworking.error.KotError
import com.mindorks.kotnetworking.request.KotRequest
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URLConnection

/**
 * Created by aamir on 03/05/17.
 */

object KotUtils {
    fun getErrorForConnection(kotError: KotError): KotError {
        kotError.errorDetail = KotConstants.CONNECTION_ERROR
        kotError.errorCode = 0
        return kotError
    }

    fun getErrorForServerResponse(kotError: KotError, kotRequest: KotRequest, code: Int): KotError {
        val parsedKotError = kotRequest.parseNetworkError(kotError)
        parsedKotError.errorDetail = KotConstants.RESPONSE_FROM_SERVER_ERROR
        parsedKotError.errorCode = code
        return parsedKotError
    }

    fun getErrorForParse(kotError: KotError): KotError {
        kotError.errorCode = 0
        kotError.errorDetail = KotConstants.PARSE_ERROR
        return kotError
    }

    fun getMimeType(path: String): String {
        val fileNameMap = URLConnection.getFileNameMap()
        var mimeType: String? = fileNameMap.getContentTypeFor(path)
        if (mimeType == null) {
            mimeType = "application/octet-stream"
        }
        return mimeType
    }

    fun saveFile(response: Response?, dirPath: String?, fileName: String?) {
        val inputStream: InputStream? = response?.body()?.byteStream()

        val dir = File(dirPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val file = File(dir, fileName)
        val fos = FileOutputStream(file)
        inputStream.use { input ->
            fos.use { output ->
                if (output is FileOutputStream) input?.copyTo(output)
            }
        }
    }

    fun sendAnalytics(analyticsListener: ((timeTakenInMillis: Long, bytesSent: Long, bytesReceived: Long, isFromCache: Boolean) -> Unit)?,
                      timeTaken: Long, bytesSent: Long, bytesReceived: Long, isFromCache: Boolean) {
        Core.executorSupplier.forMainThreadTasks().execute {
            analyticsListener?.invoke(timeTaken, bytesSent, bytesReceived, isFromCache)
        }
    }
}