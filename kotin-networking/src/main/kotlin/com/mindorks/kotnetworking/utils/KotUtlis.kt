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

package com.mindorks.kotnetworking.utils

import com.mindorks.kotnetworking.common.KotConstants
import com.mindorks.kotnetworking.error.KotError
import com.mindorks.kotnetworking.request.KotRequest

/**
 * Created by aamir on 03/05/17.
 */
class KotUtlis {
    companion object {
        fun getErrorForConnection(kotError: KotError): KotError {
            kotError.errorDetail = KotConstants.CONNECTION_ERROR
            kotError.errorCode = 0
            return kotError
        }

        fun getErrorForServerResponse(kotError: KotError, kotRequest: KotRequest, code: Int): KotError {
            val parsedKotError = kotRequest.parseNetworkError(kotError)
            parsedKotError.errorDetail = KotConstants.RESPONSE_FROM_SERVER_ERROR
            parsedKotError.errorCode = code
            return parsedKotError
        }

        fun getErrorForParse(kotError: KotError): KotError {
            kotError.errorCode = 0
            kotError.errorDetail = KotConstants.PARSE_ERROR
            return kotError
        }
    }
}