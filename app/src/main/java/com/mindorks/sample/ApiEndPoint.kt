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

/**
 * Created by aamir on 05/05/17.
 */
class ApiEndPoint {

    companion object {
        private val BASE_URL = "https://fierce-cove-29863.herokuapp.com"
        val GET_JSON_ARRAY = BASE_URL + "/getAllUsers/{pageNumber}"
        val GET_JSON_OBJECT = BASE_URL + "/getAnUserDetail/{userId}"
        val CHECK_FOR_HEADER = BASE_URL + "/checkForHeader"
        val POST_CREATE_AN_USER = BASE_URL + "/createAnUser"
        val UPLOAD_IMAGE = BASE_URL + "/uploadImage"
    }

}