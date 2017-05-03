package com.mindorks.kotnetworking.requestbuidler

import com.mindorks.kotnetworking.common.Priority
import com.mindorks.kotnetworking.common.RequestBuilder
import com.mindorks.kotnetworking.utils.ParseUtil
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

/**
 * It's an implementation of {@link com.mindorks.kotnetworking.common.RequestBuilder}
 * It contain all the common variable so no need to write it again for Every method
 * @author
 */
abstract class RequestBuilderImpl : RequestBuilder {

    protected var priority: Priority = Priority.MEDIUM
    protected var tag: Any? = null
    val headersMap: Map<String, String> = mutableMapOf()
    protected val queryParameterMap: Map<String, String> = mutableMapOf()
    protected val pathParameterMap: Map<String, String> = mutableMapOf()
    protected var cacheControl: CacheControl? = null
    protected var executor: Executor? = null
    protected var okHttpClient: OkHttpClient? = null
    protected var userAgent: String? = null


    override fun setPriority(priority: Priority): RequestBuilder {
        this.priority = priority
        return this
    }

    override fun setTag(tag: Any): RequestBuilder {
        this.tag = tag
        return this
    }

    override fun addHeaders(key: String, value: String): RequestBuilder {
        headersMap.plus(Pair(key, value))
        return this
    }

    override fun addHeaders(headerMap: Map<String, String>): RequestBuilder {
        headersMap.plus(headerMap)
        return this
    }

    override fun addHeaders(objectAny: Any): RequestBuilder {
        ParseUtil.parserFactory?.getStringMap(objectAny)?.let { it -> headersMap.plus(it) }
        return this
    }

    override fun addQueryParameter(key: String, value: String): RequestBuilder {
        queryParameterMap.plus(Pair(key, value))
        return this
    }

    override fun addQueryParameter(queryParameterMap: Map<String, String>): RequestBuilder {
        queryParameterMap.plus(queryParameterMap)
        return this
    }

    override fun addQueryParameter(objectAny: Any): RequestBuilder {
        ParseUtil.parserFactory?.getStringMap(objectAny)?.let { it -> queryParameterMap.plus(it) }
        return this
    }

    override fun addPathParameter(key: String, value: String): RequestBuilder {
        pathParameterMap.plus(Pair(key, value))
        return this
    }

    override fun addPathParameter(pathParameterMap: Map<String, String>): RequestBuilder {
        this.pathParameterMap.plus(pathParameterMap)
        return this
    }

    override fun addPathParameter(objectAny: Any): RequestBuilder {
        ParseUtil.parserFactory?.getStringMap(objectAny)?.let { it -> pathParameterMap.plus(it) }
        return this
    }

    override fun doNotCacheResponse(): RequestBuilder {
        cacheControl = CacheControl.Builder().noStore().build()
        return this
    }

    override fun getResponseOnlyIfCached(): RequestBuilder {
        cacheControl = CacheControl.FORCE_CACHE
        return this
    }

    override fun getResponseOnlyFromNetwork(): RequestBuilder {
        cacheControl = CacheControl.FORCE_NETWORK
        return this
    }

    override fun setMaxAgeCacheControl(maxAge: Int, timeUnit: TimeUnit): RequestBuilder {
        cacheControl = CacheControl.Builder().maxAge(maxAge, timeUnit).build()
        return this
    }

    override fun setMaxStaleCacheControl(maxStale: Int, timeUnit: TimeUnit): RequestBuilder {
        cacheControl = CacheControl.Builder().maxStale(maxStale, timeUnit).build()
        return this
    }

    override fun setExecutor(executor: Executor): RequestBuilder {
        this.executor = executor
        return this
    }

    override fun setOkHttpClient(okHttpClient: OkHttpClient): RequestBuilder {
        this.okHttpClient = okHttpClient
        return this
    }

    override fun setUserAgent(userAgent: String): RequestBuilder {
        this.userAgent = userAgent
        return this
    }
}