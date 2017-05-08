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
import com.mindorks.kotnetworking.common.Progress
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.IOException

/**
 * Created by amitshekhar on 01/05/17.
 */
class RequestProgressBody(private val requestBody: RequestBody, progressCallback: ((bytesUploaded: Long, totalBytes: Long) -> Unit)?) : RequestBody() {

    private var bufferedSink: BufferedSink? = null
    private var progressHandler: UploadProgressHandler? = null

    init {
        if (progressCallback != null) {
            this.progressHandler = UploadProgressHandler(progressCallback)
        }
    }

    override fun contentType(): MediaType = requestBody.contentType()

    @Throws(IOException::class)
    override fun contentLength(): Long = requestBody.contentLength()

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink))
        }
        requestBody.writeTo(bufferedSink)
        bufferedSink?.flush()
    }

    private fun sink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            internal var bytesWritten = 0L
            internal var contentLength = 0L

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (contentLength == 0L) {
                    contentLength = contentLength()
                }
                bytesWritten += byteCount
                progressHandler?.obtainMessage(KotConstants.UPDATE,
                        Progress(bytesWritten, contentLength))?.sendToTarget()
            }
        }
    }
}