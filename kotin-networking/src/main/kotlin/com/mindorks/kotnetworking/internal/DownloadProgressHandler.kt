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

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.mindorks.kotnetworking.common.KotConstants
import com.mindorks.kotnetworking.common.Progress

/**
 * Created by amitshekhar on 01/05/17.
 */
class DownloadProgressHandler(private val progressCallback: ((bytesDownloaded: Long, totalBytes: Long) -> Unit)) : Handler(Looper.getMainLooper()) {

    override fun handleMessage(msg: Message?) {
        when (msg?.what) {
            KotConstants.UPDATE -> {
                val progress: Progress = msg.obj as Progress
                progressCallback(progress.currentBytes, progress.totalBytes)
            }
            else -> super.handleMessage(msg)
        }
    }

}