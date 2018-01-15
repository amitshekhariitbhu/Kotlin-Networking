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

package com.mindorks.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.mindorks.kotnetworking.KotNetworking
import com.mindorks.kotnetworking.common.Priority
import com.mindorks.sample.model.User
import com.mindorks.sample.util.Utils
import java.io.IOException

class GetApiTestActivity : AppCompatActivity() {

    companion object {

        private val TAG: String? = "GetApiTestActivity"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_api_test)

    }

    fun getAsString(view: View) {
        KotNetworking.get(ApiEndPoint.GET_JSON_OBJECT)
                .addPathParameter("userId", "1")
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString { response, error ->
                    if (error != null) {
                        Log.d(TAG, error.toString())
                    } else {
                        Log.d(TAG, response.toString())
                    }
                }

        KotNetworking.get(ApiEndPoint.GET_JSON_OBJECT)
                .addPathParameter("userId", "1")
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsOkHttpResponse { response, error ->
                    response?.apply {
                        if (isSuccessful) {
                            try {
                                Log.d(TAG, "response : ${body().source().readUtf8()}")
                            } catch (ioe: IOException) {
                                ioe.printStackTrace()
                            }
                        }
                    }

                    error?.let {
                        Log.d(TAG, error.toString())
                    }

                }
    }

    fun getAsJSONArray(view: View) {
        KotNetworking.get(ApiEndPoint.GET_JSON_ARRAY)
                .addPathParameter("pageNumber", "0")
                .addQueryParameter("limit", "3")
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener { timeTakenInMillis, bytesSent, bytesReceived, isFromCache ->
                    println("timeTakenInMillis ---> $timeTakenInMillis")
                    println("bytesSent ---> $bytesSent")
                    println("bytesReceived ---> $bytesReceived")
                    println("isFromCache ---> $isFromCache")
                }
                .getAsJSONArray { response, error ->
                    if (error != null) {
                        Log.d(TAG, error.toString())
                    } else {
                        Log.d(TAG, response.toString())
                    }
                }

        KotNetworking.get(ApiEndPoint.GET_JSON_ARRAY)
                .addPathParameter("pageNumber", "0")
                .addQueryParameter("limit", "3")
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener { timeTakenInMillis, bytesSent, bytesReceived, isFromCache ->
                    println("timeTakenInMillis ---> $timeTakenInMillis")
                    println("bytesSent ---> $bytesSent")
                    println("bytesReceived ---> $bytesReceived")
                    println("isFromCache ---> $isFromCache")
                }
                .getAsOkHttpResponse { response, error ->
                    response?.apply {
                        if (isSuccessful) {
                            try {
                                Log.d(TAG, "response : ${body().source().readUtf8()}")
                            } catch (ioe: IOException) {
                                ioe.printStackTrace()
                            }
                        }
                    }

                    error?.let {
                        Log.d(TAG, error.toString())
                    }

                }
    }

    fun getAsJSONObject(view: View) {
        KotNetworking.get(ApiEndPoint.GET_JSON_OBJECT)
                .addPathParameter("userId", "1")
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener { timeTakenInMillis, bytesSent, bytesReceived, isFromCache ->
                    println("timeTakenInMillis ---> $timeTakenInMillis")
                    println("bytesSent ---> $bytesSent")
                    println("bytesReceived ---> $bytesReceived")
                    println("isFromCache ---> $isFromCache")
                }
                .getAsJSONObject { response, error ->
                    if (error != null) {
                        Log.d(TAG, error.toString())
                    } else {
                        Log.d(TAG, response.toString())
                    }
                }

        KotNetworking.get(ApiEndPoint.GET_JSON_OBJECT)
                .addPathParameter("userId", "1")
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener { timeTakenInMillis, bytesSent, bytesReceived, isFromCache ->
                    println("timeTakenInMillis ---> $timeTakenInMillis")
                    println("bytesSent ---> $bytesSent")
                    println("bytesReceived ---> $bytesReceived")
                    println("isFromCache ---> $isFromCache")
                }
                .getAsParseResponse<User> { response, error ->
                    println("Parsed Response $response")
                }

        KotNetworking.get(ApiEndPoint.GET_JSON_OBJECT)
                .addPathParameter("userId", "1")
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener { timeTakenInMillis, bytesSent, bytesReceived, isFromCache ->
                    println("timeTakenInMillis ---> $timeTakenInMillis")
                    println("bytesSent ---> $bytesSent")
                    println("bytesReceived ---> $bytesReceived")
                    println("isFromCache ---> $isFromCache")
                }
                .getAsOkHttpResponse { response, error ->
                    response?.apply {
                        if (isSuccessful) {
                            try {
                                Log.d(TAG, "response : ${body().source().readUtf8()}")
                            } catch (ioe: IOException) {
                                ioe.printStackTrace()
                            }
                        }
                    }

                    error?.let {
                        Log.d(TAG, error.toString())
                    }

                }
    }

    fun downloadImageFile(view: View) {
        val imageUrl: String = "http://i.imgur.com/AtbX9iX.png"
        val dirPath: String = Utils.getRootDirPath(applicationContext)
        val fileName: String = "Img_1.jpg"
        KotNetworking.download(imageUrl, dirPath, fileName)
                .setPriority(Priority.HIGH)
                .build()
                .setAnalyticsListener { timeTakenInMillis, bytesSent, bytesReceived, isFromCache ->
                    println("timeTakenInMillis ---> $timeTakenInMillis")
                    println("bytesSent ---> $bytesSent")
                    println("bytesReceived ---> $bytesReceived")
                    println("isFromCache ---> $isFromCache")
                }
                .startDownload { error ->
                    if (error != null) {
                        Log.d(TAG, error.toString())
                    } else {
                        Log.d(TAG, "completed")
                    }
                }


        val cancelTag: String = "HollyMolly"

        KotNetworking.download(imageUrl, dirPath, fileName)
                .setPriority(Priority.HIGH)
                .setTag(cancelTag)
                .build()
                .setAnalyticsListener { timeTakenInMillis, bytesSent, bytesReceived, isFromCache ->
                    println("timeTakenInMillis ---> $timeTakenInMillis")
                    println("bytesSent ---> $bytesSent")
                    println("bytesReceived ---> $bytesReceived")
                    println("isFromCache ---> $isFromCache")
                }
                .setDownloadProgressListener { bytesDownloaded, totalBytes ->
                    println("bytesDownloaded ---> $bytesDownloaded")
                    println("totalBytes ---> $totalBytes")
                }
                .getAsOkHttpResponse { response, error ->
                    response?.apply {
                        if (isSuccessful) {
                            try {
                                Log.d(TAG, "response : ${body().source().readUtf8()}")
                            } catch (ioe: IOException) {
                                ioe.printStackTrace()
                            }
                        }
                    }

                    error?.let {
                        Log.d(TAG, error.toString())
                    }

                }

        KotNetworking.cancel(cancelTag)
    }

    fun sendAndCancelAll(view: View) {
        val imageUrl: String = "http://i.imgur.com/AtbX9iX.png"
        val dirPath: String = Utils.getRootDirPath(applicationContext)
        val fileName: String = "Img_1.jpg"

        KotNetworking.get(ApiEndPoint.GET_JSON_OBJECT)
                .addPathParameter("userId", "1")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener { timeTakenInMillis, bytesSent, bytesReceived, isFromCache ->
                    println("timeTakenInMillis ---> $timeTakenInMillis")
                    println("bytesSent ---> $bytesSent")
                    println("bytesReceived ---> $bytesReceived")
                    println("isFromCache ---> $isFromCache")
                }
                .getAsOkHttpResponse { response, error ->
                    response?.apply {
                        if (isSuccessful) {
                            try {
                                Log.d(TAG, "response : ${body().source().readUtf8()}")
                            } catch (ioe: IOException) {
                                ioe.printStackTrace()
                            }
                        }
                    }

                    error?.let {
                        Log.d(TAG, error.toString())
                    }

                }

        KotNetworking.download(imageUrl, dirPath, fileName)
                .setPriority(Priority.HIGH)
                .build()
                .setAnalyticsListener { timeTakenInMillis, bytesSent, bytesReceived, isFromCache ->
                    println("timeTakenInMillis ---> $timeTakenInMillis")
                    println("bytesSent ---> $bytesSent")
                    println("bytesReceived ---> $bytesReceived")
                    println("isFromCache ---> $isFromCache")
                }
                .setDownloadProgressListener { bytesDownloaded, totalBytes ->
                    println("bytesDownloaded ---> $bytesDownloaded")
                    println("totalBytes ---> $totalBytes")
                }
                .getAsOkHttpResponse { response, error ->
                    response?.apply {
                        if (isSuccessful) {
                            try {
                                Log.d(TAG, "response : ${body().source().readUtf8()}")
                            } catch (ioe: IOException) {
                                ioe.printStackTrace()
                            }
                        }
                    }

                    error?.let {
                        Log.d(TAG, error.toString())
                    }

                }

        KotNetworking.cancelAll()
    }

}
