package com.mindorks.sample.util

import android.content.Context
import android.os.Environment
import android.support.v4.content.ContextCompat

/**
 * Created by aamir on 23/06/17.
 */
class Utils {

    companion object {
        fun getRootDirPath(context: Context): String {
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                val file = ContextCompat.getExternalFilesDirs(context.applicationContext, null)[0]
                return file.absolutePath
            } else {
                return context.applicationContext.filesDir.absolutePath
            }
        }
    }

}