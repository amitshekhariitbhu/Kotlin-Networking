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

package com.mindorks.kotnetworking.gsonparserfactory

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.mindorks.kotnetworking.interfaces.Parser
import okhttp3.ResponseBody
import java.io.IOException

/**
 * Created by aamir on 30/04/17.
 */
class GsonResponseBodyParser<out T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Parser<ResponseBody, T> {


    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val jsonReader = gson.newJsonReader(value.charStream())
        value.use {
            return adapter.read(jsonReader)
        }
    }
}