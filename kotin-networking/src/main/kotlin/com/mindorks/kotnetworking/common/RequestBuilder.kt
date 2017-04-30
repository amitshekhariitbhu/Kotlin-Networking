package com.mindorks.kotnetworking.common

/**
 * Created by amitshekhar on 30/04/17.
 */
interface RequestBuilder {

    fun addHeaders(key: String, value: String): RequestBuilder

    fun addHeaders(headerMap: Map<String, String>): RequestBuilder

}