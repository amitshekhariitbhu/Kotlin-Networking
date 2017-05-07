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
import com.google.gson.reflect.TypeToken
import com.mindorks.kotnetworking.interfaces.Parser
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.lang.reflect.Type
import java.util.*

/**
 * Created by aamir on 30/04/17.
 */
class GsonParserFactory(private val gson: Gson = Gson()) : Parser.Factory() {

    override fun responseBodyParser(type: Type): Parser<ResponseBody, *>? {
        val typeAdapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
        return GsonResponseBodyParser(gson, typeAdapter)
    }

    override fun requestBodyParser(type: Type): Parser<*, RequestBody>? {
        val typeAdapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
        return GsonRequestBodyParser(gson, typeAdapter)
    }

    override fun getObject(string: String, type: Type): Any? {
        try {
            return gson.fromJson(string, type)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }

    override fun getString(objectAny: Any): String? {
        return gson.toJson(objectAny)
    }

    override fun getStringMap(objectAny: Any): HashMap<String, String>? {
        try {
            val type = object : TypeToken<HashMap<String, String>>() {}.type
            return gson.fromJson<HashMap<String, String>>(gson.toJson(objectAny), type)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return HashMap()
    }
}