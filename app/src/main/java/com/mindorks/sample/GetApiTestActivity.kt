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
import com.mindorks.sample.util.Utils

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
    }

    fun getAsJSONArray(view: View) {
        KotNetworking.get(ApiEndPoint.GET_JSON_ARRAY)
                .addPathParameter("pageNumber", "0")
                .addQueryParameter("limit", "3")
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray { response, error ->
                    if (error != null) {
                        Log.d(TAG, error.toString())
                    } else {
                        Log.d(TAG, response.toString())
                    }
                }
    }

    fun getAsJSONObject(view: View) {
        KotNetworking.get(ApiEndPoint.GET_JSON_OBJECT)
                .addPathParameter("userId", "1")
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject { response, error ->
                    if (error != null) {
                        Log.d(TAG, error.toString())
                    } else {
                        Log.d(TAG, response.toString())
                    }
                }
    }

    fun downloadImageFile(view: View) {
        val imageUrl: String = "http://i.imgur.com/m6K1DCQ.jpg"
        val dirPath: String = Utils.getRootDirPath(applicationContext)
        val fileName: String = "Img_" + System.currentTimeMillis() + ".jpg"
        KotNetworking.download(imageUrl, dirPath, fileName)
                .setPriority(Priority.HIGH)
                .build()
                .startDownload { error ->
                    if (error != null) {
                        Log.d(TAG, error.toString())
                    } else {
                        Log.d(TAG, "completed")
                    }
                }
    }

}
