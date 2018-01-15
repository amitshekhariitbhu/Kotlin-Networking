package com.mindorks.kotnetworking.utils

import com.mindorks.kotnetworking.common.ResponseType
import com.mindorks.kotnetworking.request.KotRequest
import okhttp3.Response

/**
 * Created by aamir on 06/05/17.
 */

object SourceCloseUtil {

    fun close(response: Response?, kotRequest: KotRequest) {

        if (kotRequest.responseType !== ResponseType.OK_HTTP_RESPONSE && response?.body() != null &&
                response.body()?.source() != null) {
            try {
                response.body().source().close()
            } catch (ignore: Exception) {

            }

        }
    }
}