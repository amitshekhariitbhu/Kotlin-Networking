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

package com.mindorks.kotnetworking.interfaces

import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.IOException
import java.lang.reflect.Type

/**
 * Created by aamir on 30/04/17.
 */
interface Parser<in F, out T> {

    @Throws(IOException::class)
    fun convert(value: F): T

    abstract class Factory {

        open fun responseBodyParser(type: Type): Parser<ResponseBody, *>? {
            return null
        }

        open fun requestBodyParser(type: Type): Parser<*, RequestBody>? {
            return null
        }

        open fun getObject(string: String, type: Type): Any? {
            return null
        }

        open fun getString(objectAny: Any): String? {
            return null
        }

        open fun getStringMap(objectAny: Any): Map<String, String>? {
            return null
        }

    }
}