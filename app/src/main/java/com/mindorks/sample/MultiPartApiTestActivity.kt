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

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.mindorks.kotnetworking.KotNetworking
import com.mindorks.kotnetworking.common.Priority
import com.mindorks.sample.util.FilePickUtils
import java.io.File


class MultiPartApiTestActivity : AppCompatActivity() {

    companion object {
        private val TAG = "MultiPartApiAct"
    }

    private val PICK_IMAGE_CODE = 200
    private val REQUEST_STORAGE_PERMISSION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_part_api_test)

        val selectImage = findViewById(R.id.select_image_button)
        selectImage.setOnClickListener {
            if (hasReadPermission())
                pickImage()
            else
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUEST_STORAGE_PERMISSION)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            val originalUri = data?.data
            val takeFlags = data?.flags?.and((Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION))
            // Check for the freshest data.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                contentResolver.takePersistableUriPermission(originalUri, takeFlags!!)
            }

            Log.i(TAG, "URI " + originalUri)

            val filePath = FilePickUtils.getSmartFilePath(this, originalUri!!)

            Log.i(TAG, "URI " + filePath)

            multipartRequest(filePath)
        }
    }


    private fun multipartRequest(filePath: String?) {
        KotNetworking.upload(ApiEndPoint.UPLOAD_IMAGE)
                .addMultiPartFile("image", File(filePath))
                .setTag(this).setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener { timeTakenInMillis, bytesSent, bytesReceived, isFromCache ->
                    println("timeTakenInMillis ---> $timeTakenInMillis")
                    println("bytesSent ---> $bytesSent")
                    println("bytesReceived ---> $bytesReceived")
                    println("isFromCache ---> $isFromCache")
                }
                .setUploadProgressListener { bytesDownloaded, totalBytes ->
                    Log.i(TAG, "setUploadProgressListener : " +
                            "Bytes Upload $bytesDownloaded/$totalBytes ")
                }
                .getAsJSONObject { result, error ->
                    if (error != null) {
                        Log.d(TAG, error.toString())
                    } else {
                        Log.d(TAG, result.toString())
                    }
                }
    }


    /**
     * Select a Image from device
     */
    private fun pickImage() {
        val intent = Intent();
        intent.type = "image/*"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.action = Intent.ACTION_OPEN_DOCUMENT
        } else {
            intent.action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(intent, PICK_IMAGE_CODE)
    }

    /**
     * Check app has Write Permission or not
     */
    private fun hasReadPermission(): Boolean {
        return ActivityCompat
                .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED
    }



}
