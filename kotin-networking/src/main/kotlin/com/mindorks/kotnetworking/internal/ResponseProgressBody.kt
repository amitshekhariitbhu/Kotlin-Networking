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

import com.mindorks.kotnetworking.common.KotConstants
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * Created by amitshekhar on 01/05/17.
 */
class ResponseProgressBody(private val responseBody: ResponseBody, progressCallback: ((bytesDownloaded: Long, totalBytes: Long) -> Unit)?) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null
    private var progressHandler: DownloadProgressHandler? = null

    init {
        if (progressCallback != null) {
            this.progressHandler = DownloadProgressHandler(progressCallback)
        }
    }


    override fun contentLength(): Long = responseBody.contentLength()

    override fun contentType(): MediaType = responseBody.contentType()

    override fun source(): BufferedSource {
        return bufferedSource ?: Okio.buffer(source(responseBody.source()))
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {

            internal var totalBytesRead: Long = 0

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                progressHandler?.obtainMessage(KotConstants.UPDATE, responseBody.contentLength())?.sendToTarget()
                return bytesRead
            }
        }
    }

}